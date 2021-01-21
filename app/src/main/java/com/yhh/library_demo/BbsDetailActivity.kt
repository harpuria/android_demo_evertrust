package com.yhh.library_demo

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class BbsDetailActivity : AppCompatActivity() {

    lateinit var bbsDetailTitleText:TextView
    lateinit var bbsDetailContentText:TextView
    lateinit var bbsDetailBackBtn:Button
    lateinit var bbsDetailModifyBtn:Button
    lateinit var bbsDetailDeleteBtn:Button

    val helper = SqliteHelper(this@BbsDetailActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bbs_detail)

        initView()

        bbsDetailTitleText.text = intent.getStringExtra("title")
        bbsDetailContentText.text = intent.getStringExtra("content")

        // 조회 수 증가
        val bbsNo = intent.getLongExtra("bbs_no", 0)
        val currentVisitCount = intent.getIntExtra("visit_count", 0).plus(1)
        helper.updateVisitCount(bbsNo, currentVisitCount)

        bbsDetailBackBtn.setOnClickListener {
            finish()
        }

        bbsDetailModifyBtn.setOnClickListener {
            val intent = Intent(this@BbsDetailActivity, BbsFormActivity::class.java)
            intent.putExtra("isModify", true)
            intent.putExtra("bbs_no", bbsNo)
            intent.putExtra("title", bbsDetailTitleText.text)
            intent.putExtra("content", bbsDetailContentText.text)
            startActivity(intent)
        }

        bbsDetailDeleteBtn.setOnClickListener {
            showDeleteDialog(bbsNo)
        }
    }

    fun initView(){
        bbsDetailTitleText = findViewById(R.id.bbsDetailTitleText)
        bbsDetailContentText = findViewById(R.id.bbsDetailContentText)
        bbsDetailBackBtn = findViewById(R.id.bbsDetailBackBtn)
        bbsDetailModifyBtn = findViewById(R.id.bbsDetailModifyBtn)
        bbsDetailDeleteBtn = findViewById(R.id.bbsDetailDeleteBtn)
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.close()
    }

    fun showDeleteDialog(bbsNo:Long){
        val msgBuilder = AlertDialog.Builder(this@BbsDetailActivity)
                .setTitle("게시물 삭제")
                .setMessage("게시물을 삭제하시겠습니까?")
                .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    if(helper.deleteBbs(bbsNo)){
                        Toast.makeText(this@BbsDetailActivity, "게시글 삭제가 완료되었습니다", Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        Toast.makeText(this@BbsDetailActivity, "게시글 삭제 실패", Toast.LENGTH_LONG).show()
                    }
                })
                .setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which ->

                })
        val msgDlg = msgBuilder.create()
        msgDlg.show()
    }
}