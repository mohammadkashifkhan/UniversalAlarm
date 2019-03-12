package com.mdkashif.universalarm.base

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.snackbar.Snackbar
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.persistence.AppDatabase


open class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: MaterialDialog
    private var parentLayout: View? = null

    //    @Inject
    lateinit var appDatabase: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this) // Dagger

        super.onCreate(savedInstanceState)
        parentLayout = findViewById(android.R.id.content)
        appDatabase = AppDatabase.getAppDatabase(applicationContext)
    }

    fun showLoader() {
        progressDialog = MaterialDialog(this).show {
            cancelable(false)
            customView(R.layout.layout_dialog_custom_progress)
        }
    }

    fun hideLoader() {
        progressDialog.dismiss()
    }

    fun showToast(message: String) {
        Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(message: String) {
        Snackbar.make(parentLayout!!.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG).show()
    }

    fun executeIntent(intent: Intent, doFinish: Boolean) {
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        if (doFinish)
            finish()
    }

    fun replaceFragment(fragment: Fragment, tag: String, isAddToBackStack: Boolean, param: Int = 0, dao: TimingsModel? = null) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        if (param != 0) { // for buzzingAlarmFragment
            val bundle = Bundle()
            bundle.putString("requestCode", param.toString())
            fragment.arguments = bundle
        }
        if (dao != null) { // for sending data to setTimeFragment
            val bundle = Bundle()
            bundle.putParcelable("editableData", dao)
            fragment.arguments = bundle
        }
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in,
                android.R.animator.fade_out)
        ft.replace(R.id.flContainer, fragment, tag)
        if (isAddToBackStack) {
            ft.addToBackStack(tag)
        }
        ft.commit()
    }

    fun setRVSlideInLeftAnimation(view: RecyclerView) {
        val set = AnimationSet(true)
        var animation: Animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 100
        set.addAnimation(animation)
        animation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        animation.duration = 800
        set.addAnimation(animation)

        val controller = LayoutAnimationController(set, 0.5f)
        view.layoutAnimation = controller
    }

    fun sendFeedback() {
        var body: String? = null
        try {
            body = packageManager.getPackageInfo(packageName, 0).versionName
            body = ("\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER)
        } catch (e: PackageManager.NameNotFoundException) {
        }

        val addresses = "mohammadkshf2093@gmail.com"
        val uri = Uri.parse("mailto:$addresses").buildUpon().build()
        val mIntent = Intent(Intent.ACTION_SENDTO, uri)
        mIntent.putExtra(Intent.EXTRA_SUBJECT, "Query from Universal Alarm app")
        mIntent.putExtra(Intent.EXTRA_TEXT, body)
        executeIntent(mIntent, false)
    }

    fun returnDbInstance(): AppDatabase {
        return appDatabase
    }
}
