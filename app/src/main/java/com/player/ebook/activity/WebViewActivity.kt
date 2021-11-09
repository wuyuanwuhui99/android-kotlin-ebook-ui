package com.player.ebook.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient

import androidx.appcompat.app.AppCompatActivity

import com.player.ebook.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview)
        //获得控件
        val extraData = intent.getStringExtra("url")
        // 传参
        val webView = findViewById<View>(R.id.wv_webview) as WebView
        //访问网页
        webView.loadUrl(extraData)
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //使用WebView加载显示url
                view.loadUrl(url)
                //返回true
                return true
            }
        }
    }
}