package com.example.shawn.anjeonmingug

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException





class MainActivity : AppCompatActivity() {

    fun getKeyHash(context: Context): String? {
        val manager = context.getPackageManager()
        val info = manager?.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES)
        /*val manager = context.packageManager
        val packageInfo = manager?.getPackageInfo(context, PackageManager.GET_SIGNATURES) ?: return null*/

        for (signature in info!!.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                Log.w(TAG, "Unable to get MessageDigest. signature=$signature", e)
            }

        }
        return null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        println("///////     " + getKeyHash(this))
        loginvirtual.setOnClickListener{
            var homeActivity = Intent(this, HomeActivity::class.java)
            startActivity(homeActivity)
        }
        button_getstarted.setOnClickListener {
            var SignupActivity = Intent(this, SignupActivity::class.java)
            startActivity(SignupActivity)

        }

        textView_login.bringToFront()
        textView_google.bringToFront()
    }
    fun createEmailId(){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(editText_email.text.toString(),editText_password.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "회원가입에 성공하였습니다.", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this,task.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
