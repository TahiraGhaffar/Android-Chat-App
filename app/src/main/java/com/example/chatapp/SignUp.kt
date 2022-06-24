package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import android.content.Intent
import android.nfc.Tag
import android.util.Log
import android.util.Log.INFO
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.logging.Level.INFO

class SignUp : AppCompatActivity() {
    private lateinit var edtName: AppCompatEditText
    private lateinit var edtEmail: AppCompatEditText
    private lateinit var edtPassword: AppCompatEditText
    private lateinit var btnSignUp: AppCompatButton

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener{
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            signUp(name, email,password)
        }
    }

    private fun signUp(name:String, email:String, password:String){
        //logic of creatng user
        mAuth.createUserWithEmailAndPassword( email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //code for jumping to home
                        addUserToDatabase(name,email, mAuth.currentUser?.uid!!) //!! means it is null safe
                    val intent = Intent(this@SignUp,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                      Log.i("Error",
                          Toast.makeText(this@SignUp,"Some error occurred",Toast.LENGTH_SHORT).show()
                              .toString()
                      )
                  // Log.i(Tag,Toast.makeText(this@SignUp,"Some error occurred",Toast.LENGTH_SHORT).show())
                }
            }
    }

    private  fun addUserToDatabase(name: String,email: String,uid:String){
         mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }



}