package com.mdkashif.universalarm.activities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.preference.RingtonePreference
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.base.BaseActivity
import com.mdkashif.universalarm.utils.AppConstants
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder
import com.pkmmte.view.CircularImageView
import jp.wasabeef.blurry.Blurry

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.settings)
        supportFragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment()).commit()
    }

    class SettingsFragment : PreferenceFragmentCompat(), androidx.preference.Preference.OnPreferenceClickListener {
        lateinit var toggleTheme: androidx.preference.Preference
        lateinit var toggleVibrate: androidx.preference.Preference
        private val googlePlayUrl = "http://play.google.com/store/apps/details?id="
        private lateinit var mActivity: SettingsActivity
        private lateinit var mIntent: Intent

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.fragment_settings, rootKey)

            findPreference<androidx.preference.Preference>(getString(R.string.prefKeyTitleAbout)).onPreferenceClickListener = this
            findPreference<androidx.preference.Preference>(getString(R.string.prefKeyTitleRate)).onPreferenceClickListener = this
            findPreference<androidx.preference.Preference>(getString(R.string.prefKeyTitleShare)).onPreferenceClickListener = this
            findPreference<androidx.preference.Preference>(getString(R.string.prefKeyTitleFaq)).onPreferenceClickListener = this
            findPreference<androidx.preference.Preference>(getString(R.string.prefKeyTitlePP)).onPreferenceClickListener = this
            findPreference<androidx.preference.Preference>(getString(R.string.prefKeyTitleTNC)).onPreferenceClickListener = this
            findPreference<androidx.preference.Preference>(getString(R.string.prefKeyTitleSendFeedback)).onPreferenceClickListener = this
            findPreference<androidx.preference.Preference>(getString(R.string.prefKeyTitleSnooze)).onPreferenceClickListener = this

            toggleTheme = findPreference<androidx.preference.Preference>(getString(R.string.prefKeyTitleTheme))
            toggleTheme.onPreferenceClickListener = this
            toggleTheme.summary = SharedPrefHolder.getInstance(mActivity).theme

            bindPreferenceSummaryToValue(findPreference<androidx.preference.Preference>(getString(R.string.prefKeyTitleRingtone)))

            toggleVibrate = findPreference<androidx.preference.Preference>(getString(R.string.prefKeyTitleVibrate))
            toggleVibrate.onPreferenceChangeListener = object : androidx.preference.Preference.OnPreferenceChangeListener {
                override fun onPreferenceChange(preference: androidx.preference.Preference, o: Any): Boolean {
                    SharedPrefHolder.getInstance(mActivity).vibrateStatus = java.lang.Boolean.valueOf(o.toString())
                    return true
                }
            }
        }

        private fun showAboutDevDialog() {
            val dialogAboutDev = MaterialDialog(mActivity).show {
                cancelable(true)
                customView(R.layout.layout_about_dev, scrollable = true)
            }

            val view = dialogAboutDev.getCustomView()

            val avatar: CircularImageView = view.findViewById(R.id.avatar)
            val bitmap: Bitmap
            val ivDevBlurryImage: ImageView = view.findViewById(R.id.ivDevBlurryImage)
            val ivLinkedIn: ImageView = view.findViewById(R.id.linkedin)
            val ivGmail: ImageView = view.findViewById(R.id.gmail)
            val ivGooglePlay: ImageView = view.findViewById(R.id.googleplay)
            val ivStackOverFlow: ImageView = view.findViewById(R.id.stackOverFlow)
            val ivGithub: ImageView = view.findViewById(R.id.github)
            val tvMadeWithLove: TextView = view.findViewById(R.id.madeWithLove)
            val tvOpenLicenses: TextView = view.findViewById(R.id.opensource)
            val tvVersion: TextView = view.findViewById(R.id.tvVersion)

            val spannable = SpannableString(getString(R.string.summary_about))
            spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.red)), 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tvMadeWithLove.setText(spannable, TextView.BufferType.SPANNABLE)

            if (avatar.drawable != null) {
                bitmap = (avatar.drawable as BitmapDrawable).bitmap
                Blurry.with(activity)
                        .radius(5)
                        .sampling(2)
                        .color(Color.argb(100, 0, 0, 0))
                        .async()
                        .animate(1000).from(bitmap).into(ivDevBlurryImage)
            }

            ivLinkedIn.setOnClickListener {
                mIntent = Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://add/%@" + "mdkashif2093"))
                val packageManager = activity!!.packageManager
                val list = packageManager.queryIntentActivities(mIntent, PackageManager.MATCH_DEFAULT_ONLY)
                if (list.isEmpty()) {
                    mIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=" + "mdkashif2093"))
                }
                mActivity.executeIntent(mIntent, false)
            }

            ivGmail.setOnClickListener {
                val addresses = "mohammadkshf2093@gmail.com"
                val uri = Uri.parse("mailto:$addresses").buildUpon().build()
                mIntent = Intent(Intent.ACTION_SENDTO, uri)
                mIntent.putExtra(Intent.EXTRA_SUBJECT, "Query!")
                mActivity.executeIntent(mIntent, false)
            }

            ivGooglePlay.setOnClickListener {
                try {
                    mActivity.executeIntent(Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Mohammad+Kashif+Khan")), false)
                } catch (anfe: android.content.ActivityNotFoundException) {
                    mActivity.executeIntent(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Mohammad+Kashif+Khan")), false)
                }
            }

            ivStackOverFlow.setOnClickListener { mActivity.executeIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://stackoverflow.com/users/5518744/kashif-k")), false) }

            ivGithub.setOnClickListener { mActivity.executeIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mohammadkashifkhan")), false) }

            tvOpenLicenses.setOnClickListener {
                MaterialDialog(mActivity).show {
                    cancelable(true)
                    title(R.string.openSourceLicenses)
                    customView(R.layout.layout_open_source_licenses, scrollable = true)
                }
            }

            try {
                val pInfo = activity!!.packageManager.getPackageInfo(activity!!.packageName, 0)
                tvVersion.text = pInfo.versionName
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun sendFeedback(context: Context) {
            var body: String? = null
            try {
                body = context.packageManager.getPackageInfo(context.packageName, 0).versionName
                body = ("\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                        Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                        "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER)
            } catch (e: PackageManager.NameNotFoundException) {
            }

            val addresses = "mohammadkshf2093@gmail.com"
            val uri = Uri.parse("mailto:$addresses").buildUpon().build()
            mIntent = Intent(Intent.ACTION_SENDTO, uri)
            mIntent.putExtra(Intent.EXTRA_SUBJECT, "Query from Universal Alarm app")
            mIntent.putExtra(Intent.EXTRA_TEXT, body)
            mActivity.executeIntent(mIntent, false)
        }

        override fun onPreferenceClick(preference: androidx.preference.Preference?): Boolean {
            when (preference!!.title) {
                getString(R.string.prefKeyTitleSnooze) -> {
                    MaterialDialog(mActivity).show {
                        title(R.string.snoozeTitle)
                        listItemsSingleChoice(R.array.snoozeTimings) { dialog, index, text ->
                            SharedPrefHolder.getInstance(mActivity).snoozeTimeArrayPosition = index
                        }
                    }
                }

                getString(R.string.prefKeyTitleAbout) ->
                    showAboutDevDialog()

                getString(R.string.prefKeyTitleRate) -> {
                    val uri = Uri.parse("market://details?id=" + activity!!.packageName)
                    mIntent = Intent(Intent.ACTION_VIEW, uri)
                    mIntent.addFlags((Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK))
                    try {
                        mActivity.executeIntent(mIntent, false)
                    } catch (e: ActivityNotFoundException) {
                        mActivity.executeIntent(Intent(Intent.ACTION_VIEW,
                                Uri.parse(googlePlayUrl + activity!!.packageName)), false)
                    }
                }

                getString(R.string.prefKeyTitleShare) -> {
                    try {
                        mIntent = Intent(Intent.ACTION_SEND)
                        mIntent.type = "text/plain"
                        mIntent.putExtra(Intent.EXTRA_SUBJECT, "Universal Alarm")
                        var sAux = "\nLet me recommend you this application\n\n"
                        sAux = sAux + googlePlayUrl + activity!!.packageName
                        mIntent.putExtra(Intent.EXTRA_TEXT, sAux)
                        mActivity.executeIntent(Intent.createChooser(mIntent, "Share with"), false)
                    } catch (e: Exception) { //e.toString();
                    }
                }

                getString(R.string.prefKeyTitleFaq) -> {
                    mIntent = Intent(activity, WebviewActivity::class.java)
                    mIntent.putExtra("endpoint", AppConstants.FAQ)
                    mActivity.executeIntent(mIntent, false)
                }

                getString(R.string.prefKeyTitlePP) -> {
                    mIntent = Intent(activity, WebviewActivity::class.java)
                    mIntent.putExtra("endpoint", AppConstants.PP)
                    mActivity.executeIntent(mIntent, false)
                }

                getString(R.string.prefKeyTitleTNC) -> {
                    mIntent = Intent(activity, WebviewActivity::class.java)
                    mIntent.putExtra("endpoint", AppConstants.TNC)
                    mActivity.executeIntent(mIntent, false)
                }

                getString(R.string.prefKeyTitleSendFeedback) ->
                    sendFeedback(activity!!)

                getString(R.string.prefKeyTitleTheme) -> {
                    MaterialDialog(mActivity).show {
                        title(R.string.dialogChooseThemeBtTitle)
                        listItemsSingleChoice(R.array.themes) { dialog, index, text ->
//                            ThemeHelper.switchTheme(context, mActivity, resources.getStringArray(R.array.themes)[index])
                            toggleTheme.summary = resources.getStringArray(R.array.themes)[index]
                            dialog.dismiss()
//                            val theme = R.style.settingsDarkTheme
//                            mActivity.setTheme(theme)
//                            mActivity.recreate()
                        }
                        positiveButton(R.string.dialogChooseThemeBtText)
                    }
                }
            }
            return true
        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            mActivity = context as SettingsActivity
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private fun bindPreferenceSummaryToValue(preference: androidx.preference.Preference) {
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.context)
                            .getString(preference.key, ""))
        }

        private val sBindPreferenceSummaryToValueListener = object : androidx.preference.Preference.OnPreferenceChangeListener {
            override fun onPreferenceChange(preference: androidx.preference.Preference, newValue: Any): Boolean {
                val stringValue = newValue.toString()
                if (preference is RingtonePreference) {
                    if (TextUtils.isEmpty(stringValue)) {
                        preference.setSummary(R.string.pref_ringtone_silent)
                    } else {
                        val ringtone = RingtoneManager.getRingtone(
                                preference.getContext(), Uri.parse(stringValue))
                        SharedPrefHolder.getInstance(preference.getContext()).ringtoneUri = Uri.parse(stringValue).toString()

                        if (ringtone == null)
                            preference.setSummary(R.string.prefSummaryRingtone)
                        else {
                            val name = ringtone.getTitle(preference.getContext())
                            preference.setSummary(name)
                        }
                    }
                }
                return true
            }
        }
    }
}
