package com.mdkashif.universalarm.security

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity
import com.mdkashif.universalarm.R

class AntiTheftUnlockActivity : AppLockActivity() {

    override fun showForgotDialog() {
        MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .title(R.string.activity_dialog_title)
                .content(R.string.activity_dialog_content)
                .positiveText(R.string.activity_dialog_accept)
                .onPositive { dialog, which ->

                }
                .negativeText(R.string.activity_dialog_decline)
                .onNegative { dialog, which ->

                }.build().show()
    }

    override fun onPinFailure(attempts: Int) {

    }

    override fun onPinSuccess(attempts: Int) {

    }
}
