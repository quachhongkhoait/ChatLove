package com.cj.chatlove_app.ui.message

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cj.chatlove_app.R
import com.cj.chatlove_app.itf.OnItemClickMessage
import com.cj.chatlove_app.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_message.*


class MessageActivity : AppCompatActivity() {

    var mListMessage: MutableList<Message> = mutableListOf()
    lateinit var mLinearLayoutManager: LinearLayoutManager
    lateinit var mMessageAdapter: MessageAdapter
    var uidReceiver = ""
    var fuser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var urlImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        uidReceiver = intent.getStringExtra("uid")!!
        fuser = FirebaseAuth.getInstance().currentUser
        init()
        onClick()
        readMessage(fuser!!.uid, uidReceiver)
    }

    private fun readMessage(myuid: String, uidReceiver: String) {
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mListMessage.clear()
                for (snap in snapshot.children) {
                    var message = snap.getValue(Message::class.java)
                    if (message!!.receiver.equals(myuid) && message.sender.equals(uidReceiver) ||
                        message!!.receiver.equals(uidReceiver) && message.sender.equals(myuid)
                    ) {
                        mListMessage.add(message)
                    }
                }
                mLinearLayoutManager.scrollToPositionWithOffset(mListMessage.size - 1, 0)
                mMessageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("nnn", "onCancelled: Message activity " + error.message)
            }

        })
    }

    private fun onClick() {
        mIVBack.setOnClickListener {
            mRltLImage.visibility = View.GONE
        }
        mBtnDetail.setOnClickListener {
            var text = mEdtDetail.text.toString().trim()
            mEdtDetail.text = null
            if (!text.isEmpty()) {
                sendMessage(
                    Message(
                        fuser!!.uid,
                        uidReceiver,
                        System.currentTimeMillis(),
                        "text",
                        text
                    )
                )
            }
        }
        mImgBack.setOnClickListener {
            finish()
        }
        mImgDetail.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1) {
            if (resultCode === RESULT_OK) {
                Log.d("nnn", "onActivityResult: " + data!!.data)
                urlImage = getRealPathFromURI(data?.data)!!
                Log.d("nnn", "onActivityResult: " + urlImage)
            } else if (resultCode === RESULT_CANCELED) {
                Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendMessage(message: Message) {
        reference = FirebaseDatabase.getInstance().getReference()
        reference?.child("Chats")?.push()?.setValue(message)

        var mesageRef = FirebaseDatabase.getInstance().getReference("ChatRoom")
            .child(fuser!!.uid)
            .child(uidReceiver)
        mesageRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()){
                    mesageRef.child("uidReceiver").setValue(uidReceiver)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun init() {
        mLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mLinearLayoutManager.stackFromEnd = true
        mRecyclerViewDetail.layoutManager = mLinearLayoutManager

        mMessageAdapter = MessageAdapter(this, mListMessage, object : OnItemClickMessage {
            override fun onAvatar() {
                Toast.makeText(
                    applicationContext, "...",
                    Toast.LENGTH_SHORT
                ).show();
            }

            override fun onImage(img: String) {
                mRltLImage.visibility = View.VISIBLE
                Glide.with(applicationContext).load(img)
                    .into(mIVMessage)
            }
        })
        mRecyclerViewDetail.adapter = mMessageAdapter
    }

    fun getRealPathFromURI(contentUri: Uri?): String? {
        var path: String? = null
        val proj = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor: Cursor? =
            getContentResolver().query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            path = cursor.getString(column_index)
        }
        cursor.close()
        return path
    }
}