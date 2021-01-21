package com.yhh.library_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.yhh.library_demo.data.Bbs
import java.text.SimpleDateFormat
import java.util.*

class BbsFormActivity : AppCompatActivity() {
    lateinit var bbsTitleEdit: EditText
    lateinit var bbsContentEdit: EditText
    lateinit var bbsCancelBtn: Button
    lateinit var bbsWriteBtn: Button

    val helper = SqliteHelper(this@BbsFormActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bbs_form)

        initView()

        val isModify = intent.getBooleanExtra("isModify", false)
        if(isModify){
            bbsTitleEdit.setText(intent.getStringExtra("title"))
            bbsContentEdit.setText(intent.getStringExtra("content"))
        }

        bbsCancelBtn.setOnClickListener {
            finish()
        }

        bbsWriteBtn.setOnClickListener {
            var bbsNo:Long? = 0
            val title = bbsTitleEdit.text.toString()
            val content = bbsContentEdit.text.toString()

            val currentDateTime = Calendar.getInstance().time
            var dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)

            if(!isModify){
                // 새 게시물
                if(!helper.selectBbs().isEmpty()){
                    bbsNo = helper.selectBbs().last().bbsNo?.plus(1)
                    helper.insertBbs(Bbs(bbsNo, title, content, Singleton.curUser.id, Singleton.curUser.password, 0, dateFormat))
                    Toast.makeText(this@BbsFormActivity, "게시글 작성 완료", Toast.LENGTH_LONG).show()
                    finish()
                }
                else {
                    helper.insertBbs(Bbs(bbsNo, title, content, Singleton.curUser.id, Singleton.curUser.password, 0, dateFormat))
                    Toast.makeText(this@BbsFormActivity, "게시글 작성 완료", Toast.LENGTH_LONG).show()
                    finish()
                }
            }else{
                bbsNo = intent.getLongExtra("bbs_no", 0)
                // 게시물 수정
                helper.updateBbs(Bbs(bbsNo, title, content, Singleton.curUser.id, Singleton.curUser.password, 0, dateFormat))
                Toast.makeText(this@BbsFormActivity, "게시글 수정 완료", Toast.LENGTH_LONG).show()
                finish()
            }

        }
    }

    fun initView(){
        bbsTitleEdit = findViewById(R.id.bbsTitleEdit)
        bbsContentEdit = findViewById(R.id.bbsContentEdit)
        bbsCancelBtn = findViewById(R.id.bbsCancelBtn)
        bbsWriteBtn = findViewById(R.id.bbsWriteBtn)
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.close()
    }
}