package com.mdkashif.universalarm.security

import com.afollestad.materialdialogs.MaterialDialog
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity
import com.mdkashif.universalarm.R

class AntiTheftUnlockActivity : AppLockActivity() {

    override fun showForgotDialog() {
        MaterialDialog(this).show {
                title(R.string.activity_dialog_title)
                message(R.string.activity_dialog_content)
                positiveButton(R.string.activity_dialog_accept) {  }
                negativeButton(R.string.activity_dialog_decline) {  }
        }
    }

    override fun onPinFailure(attempts: Int) {

    }

    override fun onPinSuccess(attempts: Int) {

    }
}
