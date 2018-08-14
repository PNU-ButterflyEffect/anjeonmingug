package com.example.shawn.anjeonmingug

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import com.onesignal.OneSignal
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue.TIMESTAMP
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.Exclude

class SignupActivity : AppCompatActivity() {
    var i = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate---------------------------------------")
        setContentView(R.layout.activity_signup)
        //로그인 세션을 체크하는 부분
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            //세션
            var user = firebaseAuth.currentUser
            if(this.i > 0)
                if (user != null) {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
        }
        button_signup.setOnClickListener {
            println("onCreate")
            createEmailId()
        }
    }
    var userId : String? = null;
    var authStateListener: FirebaseAuth.AuthStateListener? = null

    var database = FirebaseDatabase.getInstance()

    // not return
    fun getPlayerId(){
        OneSignal.idsAvailable { userId, registrationId ->
            Log.d("debug", "User:$userId")
            this.userId = userId;
            if (registrationId != null)
                Log.d("debug", "registrationId:$registrationId")
        }
    }

    private fun writeNewUser(name: String, email: String, pass: String, playerId : String) {

        class User {
            var key : String
            var name: String
            var email: String
            var pass : String
            var playerId : String
            var date : Any


            constructor(key:String, name: String, email: String, pass: String, playerId : String) {
                this.key = key
                this.name = name
                this.email = email
                this.pass = pass
                this.playerId = playerId
                this.date = TIMESTAMP
            }

            @Exclude
            fun toMap(): Map<String, Any> {
                val result = HashMap<String, Any>()
                result.put("key", this.key)
                result.put("name", this.name)
                result.put("email", this.email)
                result.put("pass", this.pass)
                result.put("playerId", this.playerId)
                result.put("date", this.date)

                return result
            }
        }

        var myRef = database.getReference()
        val key = myRef.child("users").push().getKey()
        val post =User(key!!, name, email, pass, playerId)
        myRef.child("message").push().setValue(post)

    }

    fun createEmailId() {
        val userEmail = editText_email.text.toString()
        val userPass = editText_password.text.toString()
        val userName : String = editText_name.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPass)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                getPlayerId()
                this.writeNewUser(userName, userEmail, userPass, this.userId!!)
                Toast.makeText(this, "회원가입에 성공하였습니다.", Toast.LENGTH_LONG).show()
                this.i += 1;
                println(this.i )
            }else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener!!)

    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener!!)
    }
}