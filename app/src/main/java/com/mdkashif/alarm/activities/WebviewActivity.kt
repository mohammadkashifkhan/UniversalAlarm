package com.mdkashif.alarm.activities

import android.os.Bundle
import android.webkit.WebSettings
import com.mdkashif.alarm.R
import com.mdkashif.alarm.custom.AlarmWebViewClient
import kotlinx.android.synthetic.main.activity_webview.*

class WebviewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        loadUrlToWebView(intent.getStringExtra("endpoint"))
    }

    private fun loadUrlToWebView(url: String) {
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.settings.setAppCacheEnabled(true)
        webView.settings.javaScriptEnabled=true
        webView.loadUrl(url)
        webView.webViewClient = AlarmWebViewClient(this)
    }

}
