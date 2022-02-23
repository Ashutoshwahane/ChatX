package xyz.cybernerd404.chatx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import xyz.cybernerd404.chatx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var adapter: UserAdapter
    private lateinit var userList: MutableList<User>
    private lateinit var mRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter(this)
        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference
        userList = arrayListOf()

        binding.apply {
            userRv.adapter = adapter
            userRv.layoutManager = LinearLayoutManager(this@MainActivity)

            logoutBtn.setOnClickListener {
                mAuth.signOut()
                Intent(this@MainActivity, LoginActivity::class.java).apply {
                    finish()
                    startActivity(this)
                }
            }
        }

        mRef.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                snapshot.children.forEach {
                    val currentUser = it.getValue(User::class.java)
                    if (mAuth.currentUser?.uid != currentUser?.uid) userList.add(currentUser!!)

                    adapter.setUser(userList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })



    }
}