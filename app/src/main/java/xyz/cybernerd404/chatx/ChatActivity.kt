package xyz.cybernerd404.chatx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.TtsSpan
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import xyz.cybernerd404.chatx.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var adapter: MessageAdapter
    private lateinit var binding: ActivityChatBinding
    private lateinit var mRef: DatabaseReference
    private lateinit var messageList: MutableList<Message>

    private var senderRoom: String? = null
    private var recieverRoom: String? = null
    private var senderData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent.extras
        messageList = arrayListOf()
        val name = intent?.getString("name")
        val recieverUid = intent?.getString("uid")
        var senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mRef = FirebaseDatabase.getInstance().reference

        senderRoom = recieverUid + senderUid
        recieverRoom = senderUid + recieverUid


        if (senderUid != null) {
            senderData = senderUid
            debug("sender : $senderData")
        }
        adapter = MessageAdapter(this)
        binding.apply {
            chatRv.adapter = adapter
            chatRv.layoutManager = LinearLayoutManager(this@ChatActivity)
            sendIv.setOnClickListener {
                val message = chatEt.text.toString()

                val messageObj = Message(message, senderData)
                debug("messageObj : $senderData")

                mRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObj).addOnSuccessListener {
                        mRef.child("chats").child(recieverRoom!!).child("messages").push()
                            .setValue(messageObj)
                    }
                chatEt.setText("")
            }
        }

        mRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    snapshot.children.forEach {
                        val message = it.getValue(Message::class.java)
                        messageList.add(message!!)

                    }
                    adapter.setMessage(messageList)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


    }
}