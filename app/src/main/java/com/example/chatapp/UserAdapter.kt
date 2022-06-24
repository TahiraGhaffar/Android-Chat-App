package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val context : Context,val userList:ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
//A ViewGroup is a special view that can contain other views (called children.)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {//each time when new
    //user added into recyclerView,this function called
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)//'view' now contains Newly ADDED user into recyclerView displayed in 'userLayout'

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position] //takes addedUser's position into 'currentUser'
        holder.textName.text = currentUser.name
//onClicking on 'itemView' in recyclerview, it takes from 'UserAdapter' to 'ChatActivity' class
        //here e.g: ALI logged in & clicked on David itemView
        holder.itemView.setOnClickListener{
            val intent = Intent(context,ChatActivity::class.java)

            intent.putExtra("name",currentUser.name)
            //intent.putExtra("uid",FirebaseAuth.getInstance().currentUser?.uid)
            intent.putExtra("uid",currentUser.uid)

            context.startActivity(intent)//this starts Activity after shifting from 'UserAdapter' to 'ChatActivity'
        }
    }

    override fun getItemCount(): Int {
        return userList.size//shows list of users in RecyclerView of Users
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
       val textName = itemView.findViewById<TextView>(R.id.txt_name) //stores Name of User on clicked itemView
        // into 'textName'
    }

}