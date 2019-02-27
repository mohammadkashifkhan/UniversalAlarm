package com.mdkashif.universalarm.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.animation.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.snackbar.Snackbar
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmListAdapter
import com.mdkashif.universalarm.alarm.prayer.geocoder.GetCurrentLocation
import com.mdkashif.universalarm.alarm.prayer.geocoder.GetLocationAddress
import com.mdkashif.universalarm.alarm.prayer.job.PrayerDataFetchScheduler
import com.mdkashif.universalarm.custom.SwipeToDeleteCallback
import com.mdkashif.universalarm.utils.AppConstants
import com.mdkashif.universalarm.utils.persistence.AppDatabase
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder


open class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: MaterialDialog
    private var parentLayout: View? = null

    //    @Inject
    lateinit var appDatabase: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this) // Dagger

        super.onCreate(savedInstanceState)
        parentLayout = findViewById(android.R.id.content)

        val location = GetCurrentLocation.instance.findLocation(applicationContext)
        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            GetLocationAddress.getAddressFromLocation(latitude, longitude,
                    applicationContext, GeocodeHandler(applicationContext))
        }
        PrayerDataFetchScheduler.scheduleJob(applicationContext)
        appDatabase = AppDatabase.getAppDatabase(applicationContext)
    }

    val isOnline: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm != null) {
                if (Build.VERSION.SDK_INT < 23) {
                    val ni = cm.activeNetworkInfo

                    if (ni != null) {
                        return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
                    }
                } else {
                    val n = cm.activeNetwork

                    if (n != null) {
                        val nc = cm.getNetworkCapabilities(n)

                        return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    }
                }
            }
            return false
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

    fun replaceFragment(fragment: Fragment, tag: String, isAddToBackStack: Boolean) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
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

    fun enableSwipeToDeleteAndUndo(mAdapter: AlarmListAdapter, mRecyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                val item = mAdapter.data[position]

                mAdapter.removeItem(position)

                val snackBar = Snackbar
                        .make(parentLayout!!.findViewById(android.R.id.content),
                                "Alarm removed", Snackbar.LENGTH_LONG)
                snackBar.setAction("UNDO") {
                    mAdapter.restoreItem(item, position)
                    mRecyclerView.scrollToPosition(position)
                }
                snackBar.setActionTextColor(resources.getColor(R.color.gray))
                snackBar.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

    private class GeocodeHandler(var context: Context) : Handler() {
        override fun handleMessage(message: Message) {
            when (message.what) {
                1 -> {
                    val bundle = message.data
                    SharedPrefHolder.getInstance(context).city = bundle.getString("city")
                    SharedPrefHolder.getInstance(context).country = bundle.getString("country")
                }
            }
        }
    }

    fun returnDbInstance(): AppDatabase {
        return appDatabase
    }

    fun detectThemeAuto(): String {
        var autoTheme = ""
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> autoTheme = AppConstants.themeLight

            Configuration.UI_MODE_NIGHT_YES -> autoTheme = AppConstants.themeDark

            Configuration.UI_MODE_NIGHT_UNDEFINED -> autoTheme = "undefined"
        }
        return autoTheme
    }

}
