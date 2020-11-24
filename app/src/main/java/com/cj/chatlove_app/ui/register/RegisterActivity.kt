package com.cj.chatlove_app.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.cj.chatlove_app.ui.main.MainActivity
import com.cj.chatlove_app.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    val TAG = "nnn"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // Initialize Firebase Auth
        auth = Firebase.auth
        onClick()

    }

    private fun onClick() {
        mBtnRegister.setOnClickListener {
            var email = mEdtEmail.text.toString()
            var pass = mEdtPassword.text.toString()
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        val uid = user!!.uid
                        val myRef = Firebase.database.getReference("users").child(uid)
                        var hashMap = HashMap<String, String>()
                        hashMap.put("uid", uid)
                        hashMap.put("displayName", "uid")
                        hashMap.put("email", user.email + "")
                        hashMap.put("phoneNumber", "0")
                        myRef.setValue(hashMap)
                            .addOnCompleteListener(object : OnCompleteListener<Void> {
                                override fun onComplete(p0: Task<Void>) {
                                    if (p0.isSuccessful) {
                                        val intent =
                                            Intent(applicationContext, MainActivity::class.java)
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        startActivity(intent)
                                        finish()
                                    }
                                }

                            })
//                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "You can't register woth this email and password",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }

                    // ...
                }
        }
        mBtnLogin.setOnClickListener {
            var email = mEdtEmail.text.toString()
            var password = mEdtPassword.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                        // ...
                    }

                    // ...
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
//        Log.d(TAG, "updateUI: ${user?.uid}")
//        Toast.makeText(applicationContext, "" + user?.email, Toast.LENGTH_LONG).show()
        if (user != null){
//            var mDB = Firebase.database
//            val myRef = mDB.getReference("users")
//            myRef.child(user!!.uid).setValue(user?.providerData?.get(0))
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
}