package com.mdkashif.universalarm.misc.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.afollestad.materialdialogs.MaterialDialog
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.utils.AppConstants
import org.koin.android.ext.android.inject


class AntiTheftUnlockActivity : AppLockActivity() {
    private val appPreferences: AppPreferences by inject()

    private var theftAlarmStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        if (intent != null)
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
        appPreferences.theftAlarmStatus = theftAlarmStatus

        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            AppConstants.REQUEST_CODE_ENABLE_THEFT_ALARM -> appPreferences.theftPinEnabled = true
        }
    }
}
