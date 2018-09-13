package com.example.shawn.anjeonmingug

import android.icu.text.SimpleDateFormat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_review_write.*
import java.util.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class review_write : AppCompatActivity() {

    var database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_write)

        var writingPage = getIntent()
        //var count = writingPage.getIntExtra("sizeoflist", 0)
        var streetAddressFull = writingPage.getStringExtra("fulladdress")
        var countDB = FirebaseDatabase.getInstance().getReference("building_reviewDB/" + com.example.shawn.anjeonmingug.streetAddressFull + "/count")
        var thesize:Int = 0
        val reviewCountListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var review_count = dataSnapshot.value
                try {
                    thesize = review_count.toString().toInt()
                } catch (e: NumberFormatException) {
                    Log.d("thesize", "NumberFormatException")
                    thesize = 0
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        countDB.addListenerForSingleValueEvent(reviewCountListener)
        var userName = writingPage.getStringExtra("userName")

        review_submit.setOnClickListener(){
            val now = System.currentTimeMillis()
            val date = Date(now)
            val sdf = SimpleDateFormat("yyyy-MM-dd : hh-mm-ss")
            val getTime = sdf.format(date)

            val rawReviewData = userName+"\\split\\"+review_text.text.toString()+"\\split\\"+getTime

            //DB에 추가하기
            database.getReference("building_reviewDB/"+streetAddressFull+"/review"+ thesize.toString()).setValue(rawReviewData)

            database.getReference("building_reviewDB/"+streetAddressFull+"/count").setValue((thesize+1).toString())


            Toast.makeText(this, "리뷰를 등록하였습니다.", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
