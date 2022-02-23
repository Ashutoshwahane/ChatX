package xyz.cybernerd404.chatx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import xyz.cybernerd404.chatx.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.apply {
            registerBtn.setOnClickListener {
                val email = emailEt.text.toString()
                val password = passwordEt.text.toString()
                val name = nameEt.text.toString()
                register(email, password, name)
            }
        }
    }

    private fun register(email: String, password: String, name: String ) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    debug("user created successfully")
                    val user = mAuth.currentUser
                    addUserToDatabase(name, email, mAuth.currentUser!!.uid)
                    Intent(this, MainActivity::class.java).apply {
                        startActivity(this)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    debug("user register failed ${task.exception}")
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mRef = FirebaseDatabase.getInstance().reference
        mRef.child("user").child(uid).setValue(User(name, email, uid))
    }
}