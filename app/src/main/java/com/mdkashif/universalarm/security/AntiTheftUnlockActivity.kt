package com.mdkashif.universalarm.security

import android.os.Bundle
import android.os.PersistableBundle
import com.afollestad.materialdialogs.MaterialDialog
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.persistence.AppPreferences

class AntiTheftUnlockActivity : AppLockActivity() {
    var theftAlarmStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        if(intent!=null)
            theftAlarmStatus = intent.getBooleanExtra("param", false)
    }

    override fun showForgotDialog() {
        MaterialDialog(this).show {
            title(R.string.activity_dialog_title)
            message(R.string.activity_dialog_content)
            positiveButton(R.string.activity_dialog_accept) { }
            negativeButton(R.string.activity_dialog_decline) { }
        }
    }

    override fun onPinFailure(attempts: Int) {

    }

    override fun onPinSuccess(attempts: Int) {
        AppPreferences.theftAlarmStatus = theftAlarmStatus

        finish()
    }
}
