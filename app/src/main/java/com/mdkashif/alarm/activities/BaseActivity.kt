package com.mdkashif.alarm.activities

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
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
import com.google.android.material.snackbar.Snackbar
import com.mdkashif.alarm.R
import com.mdkashif.alarm.alarm.miscellaneous.AlarmListAdapter
import com.mdkashif.alarm.alarm.prayer.geocoder.GetCurrentLocation
import com.mdkashif.alarm.alarm.prayer.geocoder.GetLocationAddress
import com.mdkashif.alarm.custom.CustomProgressDialog
import com.mdkashif.alarm.custom.SwipeToDeleteCallback
import com.mdkashif.alarm.utils.AppConstants
import com.mdkashif.alarm.utils.db.AppDatabase


open class BaseActivity : AppCompatActivity() {
    private var progressDialog: CustomProgressDialog? = null
    private var parentLayout: View? = null
//    private val batteryReceiver = BatteryReceiver.getInstance()
    lateinit var appDatabase: AppDatabase

    val isOnline: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting && cm.activeNetworkInfo.isAvailable && cm.activeNetworkInfo.isConnected
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentLayout = findViewById(android.R.id.content)
        val location = GetCurrentLocation.getInstance().findLocation(applicationContext)
        progressDialog = CustomProgressDialog(this@BaseActivity)
        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            GetLocationAddress.getAddressFromLocation(latitude, longitude,
                    applicationContext, GeocoderHandler())
        }

        appDatabase = AppDatabase.getAppDatabase(applicationContext)
    }

    fun showLoader() {
        progressDialog!!.show()
    }

    fun hideLoader() {
        progressDialog!!.dismiss()
    }

    fun showToast(message: String) {
        Toast.makeText(this@BaseActivity, message + "", Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(message: String) {
        Snackbar.make(parentLayout!!.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG).show()
    }

    fun isBlank(strValue: String?): Boolean {
        return strValue == null || strValue == ""
    }

    fun replaceFragment(fragment: Fragment, tag: String, isAddToBackStack: Boolean) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.setCustomAnimations(R.animator.slide_in,
                R.animator.slide_out)
        ft.replace(R.id.flContainer, fragment, tag)
        if (isAddToBackStack) {
            ft.addToBackStack(tag)
        }
        ft.commit()
    }

    fun setRVSlideInLeftAnimation(view: RecyclerView){
        val set = AnimationSet(true)
        var animation: Animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 100
        set.addAnimation(animation)
        animation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        animation.duration=800
        set.addAnimation(animation)

        val controller = LayoutAnimationController(set, 0.5f)
        view.layoutAnimation = controller
    }

    fun enableSwipeToDeleteAndUndo(mAdapter : AlarmListAdapter, mRecyclerView: RecyclerView) {
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

    private class GeocoderHandler : Handler() {
        override fun handleMessage(message: Message) {
            when (message.what) {
                1 -> {
                    val bundle = message.data
                    city = bundle.getString("city")
                    country = bundle.getString("country")
                }
                else -> {
                    city = ""
                    country = ""
                }
            }
        }
    }

    fun returnDbInstance():AppDatabase{
        return appDatabase
    }

    fun detectThemeAuto(): String{
        var autoTheme = ""
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO-> autoTheme= AppConstants.themeLight

            Configuration.UI_MODE_NIGHT_YES-> autoTheme= AppConstants.themeDark

            Configuration.UI_MODE_NIGHT_UNDEFINED-> autoTheme= "undefined"
        }
        return autoTheme
    }

    override fun onDestroy() {
        AppDatabase.destroyInstance()
        super.onDestroy()
    }

    companion object {
        var city: String? = null
        var country: String? = null
    }

}
