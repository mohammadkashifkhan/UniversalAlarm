package com.mdkashif.alarm.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.mdkashif.alarm.R;
import com.mdkashif.alarm.activities.home.HomeActivity;
import com.mdkashif.alarm.base.BaseActivity;
import com.mdkashif.alarm.persistence.sharedprefs.SharedPrefHolder;

public class AppIntroActivity extends BaseActivity {

    private Button btnGotIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SharedPrefHolder.getInstance(this).isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_app_intro);

        btnGotIt = findViewById(R.id.btn_got_it);

        changeStatusBarColor();

        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
    }

    private void launchHomeScreen() {
        SharedPrefHolder.getInstance(this).setFirstTimeLaunch(false);

        startActivity(new Intent(AppIntroActivity.this, HomeActivity.class));
        finish();
//        overridePendingTransition(R.anim.right_enter,R.anim.slide_out);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

}

