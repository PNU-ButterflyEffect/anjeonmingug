package com.example.shawn.anjeonmingug

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.view_setting.*

class SettingActivity : AppCompatActivity() {
    var database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        var textUserName = findViewById<TextView>(R.id.textView_userName)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userInfo =database.getReference("Users/" + currentUser!!.uid + "/name")
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userName= dataSnapshot.value
                textUserName.text = userName.toString() + " 님"

            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        userInfo.addListenerForSingleValueEvent(userListener)


        textView_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
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
