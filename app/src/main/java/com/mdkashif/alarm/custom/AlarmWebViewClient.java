package com.mdkashif.alarm.custom;

import android.graphics.Bitmap;
import android.webkit.WebView;

import com.mdkashif.alarm.activities.BaseActivity;


public class AlarmWebViewClient extends android.webkit.WebViewClient {

    private boolean isErrorRecevied = false;
    boolean isWebviewLoadedCompletely=false;
    BaseActivity baseActivity;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        isWebviewLoadedCompletely=true;
        return true;
    }

    public AlarmWebViewClient(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        isErrorRecevied=true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        baseActivity.showLoader();
    }

    public void onPageFinished(final WebView view,  String url) {

        if (isWebviewLoadedCompletely && !isErrorRecevied) {
            baseActivity.hideLoader();
        }
        else {
            baseActivity.hideLoader();
            baseActivity.showToast("Sorry for the Inconvenience, Please try again later");
        }

    }

}
