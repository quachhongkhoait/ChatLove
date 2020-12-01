package com.cj.chatlove_app.ui.fragment.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cj.chatlove_app.R
import com.cj.chatlove_app.itf.OnItemClick
import com.cj.chatlove_app.model.Message
import com.cj.chatlove_app.model.UidRoom
import com.cj.chatlove_app.model.User
import com.cj.chatlove_app.ui.fragment.home.HomeAdapter
import com.cj.chatlove_app.ui.message.MessageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {

    var mListUser: MutableList<User> = mutableListOf()
    var mListUid: MutableList<UidRoom> = mutableListOf()
    lateinit var mChatAdapter: ChatAdapter
    lateinit var mFUser: FirebaseUser
    lateinit var mReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFUser = FirebaseAuth.getInstance().currentUser!!
        init()
        getData()
    }

    private fun getData() {
        mReference = FirebaseDatabase.getInstance().getReference("ChatRoom").child(mFUser.uid)
        mReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mListUid.clear()
                for (snap in snapshot.children) {
                    var idroom = snap.getValue(UidRoom::class.java)
                    mListUid.add(idroom!!)
                }
                readChats()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun readChats() {
        mReference = FirebaseDatabase.getInstance().getReference("Users")
        mReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mListUser.clear()
                for (snap in snapshot.children) {
                    var user = snap.getValue(User::class.java)
                    for (mUid in mListUid) {
                        if (user!!.uid.equals(mUid.uidReceiver)) {
                            mListUser.add(user)
                        }
                    }
                }
                mChatAdapter.notifyDataSetChanged()
                mRecyclerViewChat.visibility = View.VISIBLE
                mProgressBarLoading.visibility = View.GONE

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("nnn", "onCancelled: " + error)
            }

        })


//        mListUid.clear()
//        mReference = FirebaseDatabase.getInstance().getReference("Chats")
//        mReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (postSnapshot in snapshot.children) {
//                    var message = postSnapshot.getValue(Message::class.java)
//                    if (message?.sender.equals(mFUser.uid)) {
//                        mListUid.add(message!!.receiver)
//                    }
//                    if (message?.receiver.equals(mFUser.uid)) {
//                        mListUid.add(message!!.receiver)
//                    }
//                }
//                for (uid in mListUid) {
//                    Log.d("nnn", "onDataChange: " + uid)
//                }
//                readChats()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("nnn", "onCancelled: " + error.message)
//                Toast.makeText(
//                    activity, "DatabaseError",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
//        ///
//
//        mReference = FirebaseDatabase.getInstance().getReference("users")
//        mReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                mListUser.clear()
//                for (postSnapshot in snapshot.children) {
//                    var user = postSnapshot.getValue(User::class.java)
//                    for (it in mListUid) {
//                        Log.d("nnn", "$user!!.uid onDataChange: $it")
//                        if (user!!.uid.equals(it)) {
//                            if (mListUser.size != 0) {
//                                for (i in mListUser) {
//                                    if (!user.uid.equals(i.uid)) {
//                                        mListUser.add(i)
//                                    }
//                                }
//                            } else {
//                                mListUser.add(user!!)
//                            }
//                        }
//                    }
//                }
//                mChatAdapter.notifyDataSetChanged()
//                mRecyclerViewChat.visibility = View.VISIBLE
//                mProgressBarLoading.visibility = View.GONE
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("nnn", "onCancelled: " + error.message)
//                Toast.makeText(
//                    activity, "DatabaseError",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
    }

    private fun init() {
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerViewChat.layoutManager = linearLayoutManager
        mChatAdapter = ChatAdapter(mListUser, object : OnItemClick {
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
        mRecyclerViewChat.adapter = mChatAdapter
    }
}