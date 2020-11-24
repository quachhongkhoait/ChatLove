package com.cj.chatlove_app.ui.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.cj.chatlove_app.R
import com.cj.chatlove_app.ui.fragment.chat.ChatFragment
import com.cj.chatlove_app.ui.fragment.home.HomeFragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val fm: FragmentManager = supportFragmentManager

    private lateinit var mDB: FirebaseDatabase
    private var user: FirebaseUser? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUp()
//        mDB = Firebase.database
//        user = Firebase.auth.currentUser
//        user?.let {
//            // Name, email address, and profile photo Url
//            val name = user!!.displayName
//            val email = user!!.email
//            val photoUrl = user!!.photoUrl
//
//            // Check if user's email is verified
//            val emailVerified = user!!.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            val uid = user!!.uid
//            Log.d("nnn", "onCreate: $name $email ${photoUrl.toString()} $emailVerified $uid")
//            Toast.makeText(applicationContext, "${photoUrl.toString()} $emailVerified", Toast.LENGTH_LONG).show()
//        }

//        mTVSendMail.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            val emailAddress = "cjkhoa1@gmail.com"
//
//            Firebase.auth.sendPasswordResetEmail(emailAddress)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(applicationContext, "Email sent", Toast.LENGTH_LONG).show()
//                        Log.d("nnn", "Email sent.")
//                    }
//                }
        // Write a message to the database
//            val myRef = mDB.getReference("users")
//            myRef.child(user!!.uid).setValue(user?.providerData?.get(0))
//        }
    }

    private fun setUp() {
        val mPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        mPagerAdapter.addData()
        mViewPager.adapter = mPagerAdapter
        mViewPager.offscreenPageLimit = 4
        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mBottomNav!!.getMenu().getItem(position).setChecked(true)
                selectToolbar(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        mBottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_chat -> {
                    selectToolbar(0)
                    mViewPager.currentItem = 0
                    true
                }
                R.id.menu_home -> {
                    selectToolbar(1)
                    mViewPager.currentItem = 1
                    true
                }
                R.id.menu_notif -> {
                    selectToolbar(2)
                    mViewPager.currentItem = 2
                    true
                }
                R.id.menu_profile -> {
                    selectToolbar(3)
                    mViewPager.currentItem = 3
                    true
                }
                else -> false
            }
        }
        selectToolbar(0)
    }

    private fun selectToolbar(i: Int) {
        when (i) {
            0 -> mToolBar.setTitle("Chat")
            1 -> mToolBar.setTitle("Home")
            2 -> mToolBar.setTitle("Notification")
            3 -> mToolBar.setTitle("My Profile")
        }

    }
}