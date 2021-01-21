package com.yhh.library_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button

// 웹뷰 관련 참조하기 좋은 블로그
// https://blog.yena.io/studynote/2020/05/13/Android-WebView.html

// 웹뷰를 사용하기 위해서는 매니페스트 파일에서 아래의 권한 추가가 선행되어야 한다.
// <uses-permission android:name="android.permission.INTERNET"/>

// 웹뷰에서 URL 호출 시 ERR_CLEARTEXT_NOT_PERMITTED 가 출력되는 경우
// 매니페스트 파일에 application 요소 안에 아래의 내용을 추가해야 한다.
// android:usesCleartextTraffic="true"

class WebViewActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var webViewBackBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        initView()

        val url = intent.getStringExtra("url")
        webView.webViewClient = WebViewClient()

        // WebSettings 는 웹뷰에서 사용하는 다양한 설정(자바스크립트 허용여부, 메타태그, 새 창, 캐싱 등)을 할 수 있다
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true // JavaScript 허용하기

        webView.loadUrl(url!!.trim())

        webViewBackBtn.setOnClickListener {
            finish()
        }
    }

    fun initView(){
        webView = findViewById(R.id.webView)
        webViewBackBtn = findViewById(R.id.webViewBackBtn)
    }
}