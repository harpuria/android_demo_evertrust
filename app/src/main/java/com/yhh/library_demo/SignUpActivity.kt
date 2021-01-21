package com.yhh.library_demo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.github.drjacky.imagepicker.ImagePicker
import com.yhh.library_demo.data.User
import java.io.File

// 이미지 피커
// ㄴ https://github.com/Drjacky/ImagePicker?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=8208

class SignUpActivity : AppCompatActivity() {

    lateinit var signUpIdEditText: EditText
    lateinit var signUpPasswordEditText: EditText
    lateinit var signUpChkPasswordEditText: EditText
    lateinit var signUpBackBtn:Button
    lateinit var signUpBtn:Button
    lateinit var profileImageView:ImageView
    var profileFilePath: String = ""

    // SQlhelper 객체 생성
    val helper = SqliteHelper(this@SignUpActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initView()

        // 이미지 피커
        profileImageView.setOnClickListener {
            ImagePicker.with(this)
                    .galleryOnly()
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .cropOval()				//Allow dimmed layer to have a circle inside
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
        }

        signUpBackBtn.setOnClickListener {
            finish()
        }

        signUpBtn.setOnClickListener {
            var userNo:Long? = 0
            val id = signUpIdEditText.text.toString()
            val password = signUpPasswordEditText.text.toString()
            val chkPassword = signUpChkPasswordEditText.text.toString()

            if(id.isEmpty() || password.isEmpty() || chkPassword.isEmpty()){
                Toast.makeText(this@SignUpActivity, "모든 항목을 입력해 주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!password.equals(chkPassword)){
                Toast.makeText(this@SignUpActivity, "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!helper.selectUser().isEmpty()){
                userNo = helper.selectUser().last().userNo?.plus(1)
            }
            else{
                helper.insertUser(User(0, id, password, profileFilePath))
                Toast.makeText(this@SignUpActivity, "가입이 완료되었습니다", Toast.LENGTH_LONG).show()
                finish()
            }

            var user = User(userNo, id, password, profileFilePath)
            if(!helper.isExistUser(user)){
                Toast.makeText(this@SignUpActivity, "이미 존재하는 아이디 입니다", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else{
                helper.insertUser(user)
                Toast.makeText(this@SignUpActivity, "가입이 완료되었습니다", Toast.LENGTH_LONG).show()
                finish()
            }

            // image file -> internal storage save test
            val filename = "myfile"
            val fileContents = "hello world"
            applicationContext.openFileOutput(filename, MODE_PRIVATE).use {
                it.write(fileContents.toByteArray())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            val fileUrl = data?.data
            profileImageView.setImageURI(fileUrl)
            profileFilePath = ImagePicker.getFilePath(data)!!
        }
    }

    fun initView(){
        signUpIdEditText = findViewById(R.id.signUpIdEditText)
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditText)
        signUpChkPasswordEditText = findViewById(R.id.signUpChkPasswordEditText)
        signUpBackBtn = findViewById(R.id.signUpBackBtn)
        signUpBtn = findViewById(R.id.signUpBtn)
        profileImageView = findViewById(R.id.profileImageView)
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.close()
    }
}