package com.mdkashif.alarm.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mdkashif.alarm.R
import com.mdkashif.alarm.alarm.AlarmSoundService
import kotlinx.android.synthetic.main.activity_show_alarm.*

class ShowBuzzingAlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_alarm)
        rippleBackground.startRippleAnimation()

        cancel.setOnClickListener{
            this.stopService(Intent(this, AlarmSoundService::class.java))
        }
    }
}
