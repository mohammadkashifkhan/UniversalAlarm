package com.mdkashif.universalarm.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.persistence.AppDatabase


open class BaseActivity : AppCompatActivity() {

    private var parentLayout: View? = null

    //    @Inject
    lateinit var appDatabase: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this) // Dagger

        super.onCreate(savedInstanceState)
        parentLayout = findViewById(android.R.id.content)
        appDatabase = AppDatabase.getAppDatabase(applicationContext)
    }

    fun executeIntent(intent: Intent, doFinish: Boolean, param: Boolean = false, type: String = "") {
        if (type == "AntiTheftFirstTimeEnable") {
            intent.putExtra("param", param)
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK)
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        if (doFinish)
            finish()
    }

    fun replaceFragment(fragment: Fragment, tag: String, isAddToBackStack: Boolean, param: Int = 0, timingDao: TimingsModel? = null, locationDao: LocationsModel? = null) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        if (param != 0) { // for buzzingAlarmFragment
            val bundle = Bundle()
            bundle.putString("requestCode", param.toString())
            fragment.arguments = bundle
        }
        if (timingDao != null) { // for sending data to setTimeFragment
            val bundle = Bundle()
            bundle.putParcelable("editableData", timingDao)
            fragment.arguments = bundle
        }
        if (locationDao != null) { // for sending data to setTimeFragment
            val bundle = Bundle()
            bundle.putParcelable("editableData", locationDao)
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

    fun returnDbInstance(): AppDatabase {
        return appDatabase
    }
}
