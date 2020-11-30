package com.cj.chatlove_app.ui.fragment.profile

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.cj.chatlove_app.R
import com.cj.chatlove_app.eventbus.OnChangeViewPager
import com.cj.chatlove_app.ui.register.RegisterActivity
import com.cj.chatlove_app.ui.splash.SplashActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import org.greenrobot.eventbus.EventBus


class ProfileFragment : Fragment() {

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