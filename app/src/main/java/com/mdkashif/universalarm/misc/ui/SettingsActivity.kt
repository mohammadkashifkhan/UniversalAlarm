package com.mdkashif.universalarm.misc.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.net.Uri
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
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.base.BaseActivity
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.utils.AppConstants
import com.mdkashif.universalarm.utils.Utils
import com.pkmmte.view.CircularImageView
import jp.wasabeef.blurry.Blurry
import org.koin.core.KoinComponent
import org.koin.core.inject

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.settings)
        supportFragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment()).commit()
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener, KoinComponent {
        private val appPreferences: AppPreferences by inject()

        private lateinit var toggleVibrate: Preference
        private val googlePlayUrl = "http://play.google.com/store/apps/details?id="
        private lateinit var mActivity: SettingsActivity
        private lateinit var mIntent: Intent

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.fragment_settings, rootKey)

            findPreference<Preference>(getString(R.string.prefKeyTitleAbout))!!.onPreferenceClickListener = this
            findPreference<Preference>(getString(R.string.prefKeyTitleRate))!!.onPreferenceClickListener = this
            findPreference<Preference>(getString(R.string.prefKeyTitleShare))!!.onPreferenceClickListener = this
            findPreference<Preference>(getString(R.string.prefKeyTitleFaq))!!.onPreferenceClickListener = this
            findPreference<Preference>(getString(R.string.prefKeyTitlePP))!!.onPreferenceClickListener = this
            findPreference<Preference>(getString(R.string.prefKeyTitleTNC))!!.onPreferenceClickListener = this
            findPreference<Preference>(getString(R.string.prefKeyTitleSendFeedback))!!.onPreferenceClickListener = this
            findPreference<Preference>(getString(R.string.prefKeyTitleSnooze))!!.onPreferenceClickListener = this
            findPreference<Preference>(getString(R.string.prefKeyTitleLocationPrecision))!!.onPreferenceClickListener = this
            findPreference<Preference>(getString(R.string.prefKeyTitleRingtone))!!.onPreferenceClickListener = this

            findPreference<Preference>(getString(R.string.prefKeyTitleSnooze))!!.summary = context!!.resources.getStringArray(R.array.snoozeTimings)[appPreferences.snoozeTimeArrayPosition]
            findPreference<Preference>(getString(R.string.prefKeyTitleLocationPrecision))!!.summary = "within ${context!!.resources.getStringArray(R.array.locationPrecision)[appPreferences.locationPrecisionArrayPosition]}"

            bindPreferenceSummaryToValue(findPreference(getString(R.string.prefKeyTitleRingtone))!!)

            toggleVibrate = findPreference(getString(R.string.prefKeyTitleVibrate))!!
            toggleVibrate.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, o ->
                appPreferences.vibrateStatus = java.lang.Boolean.valueOf(o.toString())
                true
            }
        }

        private fun showAboutDevDialog() {
            val dialogAboutDev = MaterialDialog(mActivity).show {
                cancelable(true)
                customView(R.layout.layout_about_dev)
            }

            val view = dialogAboutDev.getCustomView()

            val avatar: CircularImageView = view.findViewById(R.id.avatar)
            val bitmap: Bitmap
            val ivDevBlurryImage: ImageView = view.findViewById(R.id.ivDevBlurryImage)
            val ivLinkedIn: ImageView = view.findViewById(R.id.ivLinkedIn)
            val ivGmail: ImageView = view.findViewById(R.id.ivGmail)
            val ivGooglePlay: ImageView = view.findViewById(R.id.ivGooglePlay)
            val ivStackOverFlow: ImageView = view.findViewById(R.id.ivStackOverFlow)
            val ivGithub: ImageView = view.findViewById(R.id.ivGithub)
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
                tvVersion.text = "Version: ${pInfo.versionName}"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onPreferenceClick(preference: Preference?): Boolean {
            when (preference!!.title) {
                getString(R.string.prefKeyTitleSnooze) -> {
                    MaterialDialog(mActivity).show {
                        title(R.string.snoozeTitle)
                        listItemsSingleChoice(R.array.snoozeTimings) { dialog, index, text ->
                            appPreferences.snoozeTimeArrayPosition = index
                            findPreference<Preference>(getString(R.string.prefKeyTitleSnooze))!!.summary = context.resources.getStringArray(R.array.snoozeTimings)[appPreferences.snoozeTimeArrayPosition]
                        }
                    }
                }

                getString(R.string.prefKeyTitleLocationPrecision) -> {
                    MaterialDialog(mActivity).show {
                        title(R.string.locationPrecisionTitle)
                        listItemsSingleChoice(R.array.locationPrecision) { dialog, index, text ->
                            appPreferences.locationPrecisionArrayPosition = index
                            findPreference<Preference>(getString(R.string.prefKeyTitleLocationPrecision))!!.summary = "within ${context.resources.getStringArray(R.array.locationPrecision)[appPreferences.locationPrecisionArrayPosition]}"
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
                    Utils.sendFeedback(mActivity)
            }
            return true
        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            mActivity = context as SettingsActivity
        }

        private fun bindPreferenceSummaryToValue(preference: Preference) {
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    androidx.preference.PreferenceManager
                            .getDefaultSharedPreferences(preference.context)
                            .getString(preference.key, ""))
        }

        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val stringValue = newValue.toString()
            if (preference is RingtonePreference) {
                if (TextUtils.isEmpty(stringValue)) {
                    preference.setSummary(R.string.pref_ringtone_silent)
                } else {
                    val ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue))
                    appPreferences.ringtoneUri = Uri.parse(stringValue).toString()

                    if (ringtone == null)
                        preference.setSummary(R.string.prefSummaryRingtone)
                    else {
                        val name = ringtone.getTitle(preference.getContext())
                        preference.setSummary(name)
                    }
                }
            }
            true
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
}
