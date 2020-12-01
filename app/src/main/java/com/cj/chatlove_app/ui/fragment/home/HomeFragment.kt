package com.cj.chatlove_app.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cj.chatlove_app.R
import com.cj.chatlove_app.itf.OnItemClick
import com.cj.chatlove_app.model.User
import com.cj.chatlove_app.ui.message.MessageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    var mListUser: MutableList<User> = mutableListOf()
    lateinit var mHomeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getData()
    }

    private fun getData() {
        var firebaseUser = FirebaseAuth.getInstance().currentUser
        var reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mListUser.clear()
                for (postSnapshot in snapshot.children) {
                    var user = postSnapshot.getValue(User::class.java)!!
                    if (!user.uid.equals(firebaseUser?.uid)) {
                        mListUser.add(user)
                    }
                }
                mHomeAdapter.notifyDataSetChanged()
                mRecyclerViewHome.visibility = View.VISIBLE
                mProgressBarLoading.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("nnn", "onCancelled: " + error.message)
                Toast.makeText(
                    activity, "DatabaseError",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun init() {
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerViewHome.layoutManager = linearLayoutManager
        mHomeAdapter = HomeAdapter(mListUser, object : OnItemClick {
            override fun onItem(position: Int) {
                var intent = Intent(activity, MessageActivity::class.java)
                intent.putExtra("uid", mListUser[position].uid)
                startActivity(intent)
//                EventBus.getDefault().post(OnChangeViewPager(mListUser[position].uid))
//                Toast.makeText(
//                    activity,
//                    mListUser[position].uid,
//                    Toast.LENGTH_LONG
//                ).show()
            }

        })
        mRecyclerViewHome.adapter = mHomeAdapter
    }
}