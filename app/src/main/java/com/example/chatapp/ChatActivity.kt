package com.example.chatapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    var receiverRoom:String? = null
    var senderRoom:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //val intent = Intent()
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")//intent in UserAdapter when clicked on itemView tells ReceiverUid
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid//senderRoom is a Unique Node under Chats Node having receiverUid+senderUid
        receiverRoom  = senderUid + receiverUid

        supportActionBar?.title = name

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)//call MsgAdapter class by passing context&msgList

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //logic for adding data to recyclerView
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {//adds node chat,senderRoom(sender&receiverId)node,
                //messages node
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear() //each time when new msg entered,msgList gets cleared,&again snapshot.children
                    //gets parsed through by element postSnapshot &again Data(Msgs) are stored 1by1 into "message"
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)//gets Msg from snapshot.children
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        //addind message to database
        sendButton.setOnClickListener{
            val message = messageBox.text.toString()
            val messageObject = Message(message,senderUid)

            //push() creates unique node everytime
            //Unique Node means SENDERRoom i.e, receiverUid+senderUid: DavidUid+AliUid
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener{
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }//'addOnSuccessListener' creates unique node RECEOVERRoom i.e, senderUid+receiverUid: AliUid+DavidUid
            //'setValue(messageObject)' sets message & Sender
            messageBox.setText(""); //makes messageBox empty right after we click SEND BUTTON
        }
    }
}