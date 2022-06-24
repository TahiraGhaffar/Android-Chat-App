package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context:Context, val messagaList:ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1;
    val ITEM_SENT = 2;
//2ndly this method called,this creates viewHolder according to msg SENT or RECEIVED
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            //inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return ReceiveViewHolder(view)
        }
        else{
            //inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }
    }
 //ViewHolder is separate for each Text Sent or Rceived
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messagaList[position]

        if(holder.javaClass == SentViewHolder::class.java){
            //do stuff for sent view holder
            val viewHolder = holder as SentViewHolder //typecasting
            holder.sentMessage.text = currentMessage.message //adds msg into holder that contains sent msg
        }
        else{
            //do stuff for receive view holder
            val viewHolder = holder as ReceiveViewHolder//typecast
            holder.receiveMessage.text = currentMessage.message//adds msg into holder that contains received msg
        }
    }

    override fun getItemViewType(position: Int): Int {// 1stly this method called, this tells,if msg sent OR received
        //return super.getItemViewType(position)
        val currentMessage = messagaList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVE
        }
    }


    override fun getItemCount(): Int {
        return messagaList.size
    }

    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){//ViewHolder is a
         val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)
    }

    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)//initializing 'receiveMessage' only
    }//here,
}