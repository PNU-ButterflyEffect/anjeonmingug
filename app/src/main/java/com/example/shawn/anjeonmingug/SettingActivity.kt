package com.example.shawn.anjeonmingug

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.view_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        textView_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            //startActivity(Intent(this, MainActivity::class.java))
        }
        textView_noti.setOnClickListener {
            includeSetting.setVisibility(View.VISIBLE)
            textView_logout.setEnabled(false)
        }
        textView_alarmclose.setOnClickListener {
            includeSetting.setVisibility(View.INVISIBLE)
            textView_logout.setEnabled(true)
        }
    }
}
