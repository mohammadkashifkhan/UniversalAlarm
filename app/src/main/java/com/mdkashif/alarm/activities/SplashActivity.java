package com.mdkashif.alarm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;
import com.mdkashif.alarm.R;
import com.mdkashif.alarm.activities.base.BaseActivity;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BaseActivity{
    private int splashTimeOut = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this,new Crashlytics());
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,AppIntroActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        },splashTimeOut);
    }
}
