package com.example.shawn.anjeonmingug

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        button_changeEmail.setOnClickListener {
            var editTextNewId = EditText(this)
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setView(editTextNewId)
            alertDialog.setTitle("아이디 변경")
            alertDialog.setMessage("변경하고 싶은 아이디를 입력해주세요.")
            alertDialog.setPositiveButton("확인",{dialogInterface, i ->
                changeId(editTextNewId.text.toString())
            })
            alertDialog.setNegativeButton("취소",{dialogInterface, i ->  })
            alertDialog.show()
        }

        button_checkEmail.setOnClickListener {
            sendEmailVerification()
        }

        button_changePassword.setOnClickListener {
            var editTextNewPassword = EditText(this)
            editTextNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("패스워드 변경")
            alertDialog.setMessage("변경하고 싶은 패스워드를 입력하세요")
            alertDialog.setView(editTextNewPassword)
            alertDialog.setPositiveButton("변경",{dialogInterface, i -> changePassword(editTextNewPassword.text.toString()) })
            alertDialog.setNegativeButton("취소",{dialogInterface, i -> dialogInterface.dismiss() })
            alertDialog.show()
        }
        //로그아웃
        button_logout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
    fun changePassword(password:String){
        FirebaseAuth.getInstance().currentUser!!.updatePassword(password).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                Toast.makeText(this,"비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
    fun sendEmailVerification(){
        if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
            Toast.makeText(this, "이메일 인증이 완료되었습니다.", Toast.LENGTH_LONG).show()
            return
        }
        FirebaseAuth.getInstance().currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "확인 메일을 보냈습니다.", Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
    fun changeId(email:String){
        FirebaseAuth.getInstance().currentUser!!.updateEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this,"이메일 변경이 완료되었습니다.", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this,task.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
    }
}
