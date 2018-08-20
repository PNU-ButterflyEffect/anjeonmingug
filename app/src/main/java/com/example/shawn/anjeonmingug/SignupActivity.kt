package com.example.shawn.anjeonmingug

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue.TIMESTAMP
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.Exclude
import com.google.firebase.internal.FirebaseAppHelper.getUid
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId





class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate---------------------------------------")
        setContentView(R.layout.activity_signup)

        // 회원가입 들어오면 세션을 죽임
        FirebaseAuth.getInstance().signOut()
        //로그인 세션을 체크하는 부분
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            //세션
            var user = firebaseAuth.currentUser
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
                this.playerId = ""
                this.date = TIMESTAMP
            }
        }

        var myRef = database.getReference()

        val key = FirebaseAuth.getInstance().currentUser!!.uid
        val post =User(key!!, name, email, pass, playerId)
        myRef.child("Users").child(key!!).setValue(post)
    }

    fun createEmailId() {
        val userEmail = editText_email.text.toString()
        val userPass = editText_password.text.toString()
        val userName : String = editText_name.text.toString()

        val refreshedToken = FirebaseInstanceId.getInstance().token

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPass)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                this.writeNewUser(userName, userEmail, userPass, refreshedToken!!)
                Toast.makeText(this, "회원가입에 성공하였습니다.", Toast.LENGTH_LONG).show()
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