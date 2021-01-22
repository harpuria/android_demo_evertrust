package com.yhh.library_demo

import android.content.DialogInterface
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.yhh.library_demo.data.User
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    lateinit var loginIdEditText: EditText
    lateinit var loginPasswordEditText: EditText
    lateinit var loginSigninBtn: Button
    lateinit var loginSignUpBtn: Button

    // SQlhelper 객체 생성
    val helper = SqliteHelper(this@LoginActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()


        loginSigninBtn.setOnClickListener {
            val id = loginIdEditText.text.toString()
            val password = loginPasswordEditText.text.toString()
            val user = User(0, id, password, "")

            if(id.isEmpty() || password.isEmpty()){
                Toast.makeText(this@LoginActivity, "모든 항목을 입력해 주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(helper.isExistUser(user)){
                Toast.makeText(this@LoginActivity, "존재하지 않는 아이디 입니다", Toast.LENGTH_LONG).show()
            }else{
                if(helper.isCheckPassword(user)){
                    Toast.makeText(this@LoginActivity, "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show()
                }
                else{
                    val intent = Intent(this@LoginActivity, MainPagerActivity::class.java)
                    Singleton.curUser = helper.selectOneUser(id)
                    startActivity(intent)
                }
            }

        }

        loginSignUpBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    fun initView(){
        loginIdEditText = findViewById(R.id.loginIdEditText)
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText)
        loginSigninBtn = findViewById(R.id.loginSigninBtn)
        loginSignUpBtn = findViewById(R.id.loginSignUpBtn)
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.close()
    }

    override fun onBackPressed() {
        val msgBuilder = AlertDialog.Builder(this@LoginActivity)
                .setTitle("앱 종료")
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("예", DialogInterface.OnClickListener { dialogInterface, i ->
                    finishAffinity()
                })
                .setNegativeButton("아니오", DialogInterface.OnClickListener { dialogInterface, i ->
                    return@OnClickListener
                })

        val msgDlg = msgBuilder.create()
        msgDlg.show()
    }
}