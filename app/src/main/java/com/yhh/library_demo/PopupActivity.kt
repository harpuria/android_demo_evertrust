package com.yhh.library_demo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// 액티비티를 팝업처럼 사용하기
// 해당 액티비티의 테마를 매니페스트에서 변경
// ex) <activity android:name=".PopupActivity" android:theme="@android:style/Theme.Dialog"/>

// AppCompatActivity 대신 Activity 를 상속받는다
class PopupActivity : Activity() {

    lateinit var detailName:TextView
    lateinit var detailAddr:TextView
    lateinit var detailTel:TextView
    lateinit var detailClosedate:TextView
    lateinit var closeBtn:Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_popup)

        initView()

        detailName.text = intent.getStringExtra("name")
        detailAddr.text = intent.getStringExtra("addr")
        detailTel.text = intent.getStringExtra("tel")
        detailClosedate.text = intent.getStringExtra("closeDate")

        closeBtn.setOnClickListener {
            finish()
        }
    }

    fun initView(){
        detailName = findViewById(R.id.detailName)
        detailAddr = findViewById(R.id.detailAddr)
        detailTel = findViewById(R.id.detailTel)
        detailClosedate = findViewById(R.id.detailCloseDate)
        closeBtn = findViewById(R.id.closeBtn)
    }

    // 터치 이벤트
    // 팝업 밖(ACTION_OUTSIDE) 터치해도 팝업이 안닫히게게
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_OUTSIDE){
            return false
        }
        return true
    }

    // 백 버튼 이벤트
    // 빈 값 반환시 백버튼으로 액티비티가 닫히는 것을 막을 수 있음
    override fun onBackPressed() {
        return
    }
}