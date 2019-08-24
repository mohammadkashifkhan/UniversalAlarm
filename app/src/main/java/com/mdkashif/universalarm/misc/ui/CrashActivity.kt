package com.mdkashif.universalarm.misc.ui

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.mdkashif.universalarm.R

import kotlinx.android.synthetic.main.activity_crash.*

class CrashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash)
        if(intent.getStringExtra("error").isNotEmpty())
            tvErrorLog.text=intent.getStringExtra("error")
    }
}
