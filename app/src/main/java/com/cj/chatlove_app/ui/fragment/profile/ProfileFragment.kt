package com.cj.chatlove_app.ui.fragment.profile

import android.app.Activity.RESULT_OK
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cj.chatlove_app.R
import com.cj.chatlove_app.model.User
import com.cj.chatlove_app.ui.register.RegisterActivity
import com.cj.chatlove_app.ui.splash.SplashActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlin.collections.HashMap


class ProfileFragment : Fragment() {

    lateinit var mStorageRef: StorageReference
    lateinit var mReference: DatabaseReference
    lateinit var mFUser: FirebaseUser
    lateinit var imageUri: Uri
    lateinit var mUploadTask: UploadTask

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var a = 45
        Log.d("nnn", "onCreateView: " + Integer.toBinaryString(a))
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFUser = FirebaseAuth.getInstance().currentUser!!
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads")
        getData()
        onClick()
    }

    private fun getData() {
        mReference =FirebaseDatabase.getInstance().getReference("Users").child(mFUser.uid)
        mReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.getValue(User::class.java)
                if (!user?.avatar.equals("default")){
                    Glide.with(activity!!).load(user?.avatar).into(mCIVAvatar)
                }else{
                    mCIVAvatar.setImageResource(R.drawable.chatlove)
                }
                mTVName.setText(user?.displayName)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun onClick() {

        mCIVAvatar.setOnClickListener {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickPhoto.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)// bật quyền truy cập nội dung
            startActivityForResult(pickPhoto, 1)
        }
        mRL6.setOnClickListener {
            var intent = Intent(activity, RegisterActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            activity!!.finish()
            FirebaseAuth.getInstance().signOut()
        }
        mSWTheme.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
//                restartApp()
//                EventBus.getDefault().post(OnChangeViewPager(""))
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.d("nnn", "onActivityResult: " + data!!.data)
                var uri = data.data!!
                var imageTitle = "${System.currentTimeMillis()}" + "." + getFileExtention(uri)
                Log.d("nnn", "onClick: " + getFileExtention(uri))
                var storage = Firebase.storage
                var storageRef = storage.getReference("images")
                var imagesRef = storageRef.child(mFUser.uid).child("avatar")
                    .child(imageTitle)
                var uploadTask = imagesRef.putFile(uri)
//                uploadTask.addOnFailureListener {
//                    Log.d("nnn", "addOnFailureListener: " + it.message)
//                }.addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        var imageURL = it.result.toString()
//                        Log.d("nnn", "addOnCompleteListener:  OK")
//                        Toast.makeText(activity, "Upload OK", Toast.LENGTH_LONG).show()
//                        mReference =
//                            FirebaseDatabase.getInstance().getReference("users").child(mFUser.uid)
//                        var map = HashMap<String, Any>()
////                        map.put("Avatar", "Images/${mFUser.uid}/Avatar/$imageTitle")
//                        map.put("Avatar", "$imageURL")
//                        mReference.updateChildren(map)
//                    }
//                }
                uploadTask.continueWithTask(object :
                    Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                    override fun then(p0: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                        if (!p0.isSuccessful) {
                            throw p0.exception!!
                        }
                        return imagesRef.downloadUrl
                    }

                }).addOnCompleteListener(object : OnCompleteListener<Uri> {
                    override fun onComplete(p0: Task<Uri>) {
                        if (p0.isSuccessful) {
                            var imageURL = p0.result.toString()
                            Log.d("nnn", "addOnCompleteListener:  OK")
                            Toast.makeText(activity, "Upload OK", Toast.LENGTH_LONG).show()
                            mReference =
                                FirebaseDatabase.getInstance().getReference("Users")
                                    .child(mFUser.uid)
                            var map = HashMap<String, Any>()
//                        map.put("Avatar", "Images/${mFUser.uid}/Avatar/$imageTitle")
                            map.put("avatar", "$imageURL")
                            mReference.updateChildren(map)
                        } else {
                            Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        }
    }

    private fun getFileExtention(parse: Uri): String? {
        var contentResolver = activity!!.contentResolver
        var mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(parse))
    }

    private fun restartApp() {
        val intent = Intent(activity, SplashActivity::class.java)
        val mPendingIntentId: Int = 5
        val mPendingIntent = PendingIntent.getActivity(
            activity,
            mPendingIntentId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val mgr = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr[AlarmManager.RTC, System.currentTimeMillis() + 100] = mPendingIntent
        System.exit(0)
    }
}
