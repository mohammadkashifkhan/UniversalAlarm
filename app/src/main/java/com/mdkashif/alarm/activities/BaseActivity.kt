package com.mdkashif.alarm.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.animation.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.hendraanggrian.widget.ExpandableRecyclerView
import com.mdkashif.alarm.R
import com.mdkashif.alarm.alarm.battery.BatteryReceiver
import com.mdkashif.alarm.alarm.prayer.geocoder.GetCurrentLocation
import com.mdkashif.alarm.alarm.prayer.geocoder.GetLocationAddress
import com.mdkashif.alarm.custom.CustomProgressDialog
import com.mdkashif.alarm.utils.AppDatabase

open class BaseActivity : AppCompatActivity() {
    private var progressDialog: CustomProgressDialog? = null
    private var parentLayout: View? = null
    private val batteryReceiver = BatteryReceiver.getInstance()

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

    fun showSnackbar(message: String) {
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

    fun setRVSlideInLeftAnimation(view: ExpandableRecyclerView){
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

    override fun onDestroy() {
        AppDatabase.destroyInstance()
        super.onDestroy()
    }

    companion object {
        var city: String? = null
        var country: String? = null
    }

}
