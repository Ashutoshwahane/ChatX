package xyz.cybernerd404.chatx

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import xyz.cybernerd404.chatx.databinding.RecieveItemLayoutBinding
import xyz.cybernerd404.chatx.databinding.SendItemLayoutBinding
import xyz.cybernerd404.chatx.databinding.UserItemLayoutBinding


class MessageAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<Message> = arrayListOf()
    val ITEM_RECIEVE = 1
    val ITEM_SEND = 2

    fun setMessage(response: List<Message>) {
        this.list = response
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = list[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId, true)){
            ITEM_SEND
        }else{
            ITEM_RECIEVE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            return RecieveViewHolder(
                RecieveItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }else{
            return SendViewHolder(
                SendItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = list[position].message
        if (holder.javaClass == SendViewHolder::class.java){
            val viewHolder = holder as SendViewHolder
            holder.binding.sendMessageTv.text = currentMessage
        }else{
            val viewHolder = holder as RecieveViewHolder
            holder.binding.recieveMessageTv.text = currentMessage
        }


    }

    class SendViewHolder(val binding: SendItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)
    class RecieveViewHolder(val binding: RecieveItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}
