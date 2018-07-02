package com.mdkashif.alarm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mdkashif.alarm.R;
import com.mdkashif.alarm.base.BaseActivity;

public class SplashActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,AppIntroActivity.class);
                startActivity(intent);
                finish();
//                overridePendingTransition(R.anim.right_enter,R.anim.slide_out);
            }
        },2000);
    }
}
