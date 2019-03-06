package com.mdkashif.universalarm.utils

import android.graphics.Bitmap
import android.webkit.WebView
import com.mdkashif.universalarm.base.BaseActivity

class AlarmWebViewClient(private val baseActivity: BaseActivity) : android.webkit.WebViewClient() {
    private var isErrorReceived = false
    private var isWebViewLoadedCompletely = false

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        isWebViewLoadedCompletely = true
        return true
    }

    override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
        isErrorReceived = true
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        baseActivity.showLoader()
    }

    override fun onPageFinished(view: WebView, url: String) {
        if (isWebViewLoadedCompletely && !isErrorReceived) {
            baseActivity.hideLoader()
        } else {
            baseActivity.hideLoader()
            baseActivity.showToast("Sorry for the Inconvenience, Please try again later")
        }
    }
}
