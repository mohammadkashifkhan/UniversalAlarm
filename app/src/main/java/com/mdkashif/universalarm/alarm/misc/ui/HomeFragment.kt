package com.mdkashif.universalarm.alarm.misc.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.SettingsActivity
import com.mdkashif.universalarm.alarm.battery.ui.SetBatteryLevelFragment
import com.mdkashif.universalarm.alarm.location.ui.SetLocationFragment
import com.mdkashif.universalarm.alarm.misc.AlarmsListAdapter
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.alarm.prayer.ui.SetPrayerTimeFragment
import com.mdkashif.universalarm.alarm.time.ui.SetTimeFragment
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.persistence.RoomRepository
import com.mdkashif.universalarm.utils.Utils
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeFragment : BaseFragment(), View.OnClickListener, AlarmsListAdapter.GetTotalAlarmCountInterface, CoroutineScope {

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject
    lateinit var roomRepository: RoomRepository

    @Inject
    lateinit var appPreferences: AppPreferences

    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var rootView: View
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        rootView.fabTime.setOnClickListener(this)
        rootView.fabBattery.setOnClickListener(this)
        rootView.fabLocation.setOnClickListener(this)
        rootView.fabSalat.setOnClickListener(this)
        rootView.ivSettings.setOnClickListener(this)
        rootView.tvSeeAll.setOnClickListener(this)

        return rootView
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.fabTime -> {
                rootView.menu.close(true)
                mActivity.replaceFragment(SetTimeFragment(), SetTimeFragment::class.java.simpleName, true)
            }
            R.id.fabBattery -> {
                rootView.menu.close(true)
                mActivity.replaceFragment(SetBatteryLevelFragment(), SetBatteryLevelFragment::class.java.simpleName, true)
            }
            R.id.fabLocation -> {
                rootView.menu.close(true)
                if (ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Utils.showSnackBar("You have not granted the Location Permissions, please do so by going into the app permissions", rootView)
                    return
                }
                if (Utils.isOnline(mActivity))
                    mActivity.replaceFragment(SetLocationFragment(), SetLocationFragment::class.java.simpleName, true)
                else
                    Utils.showSnackBar("Experiencing Network Problems, Please check your Internet or try again later", rootView)
            }
            R.id.fabSalat -> {
                rootView.menu.close(true)
                if (ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Utils.showSnackBar("You have not granted the Location Permissions, please do so by going into the app permissions", rootView)
                    return
                }
                if (appPreferences.islamicDate != "")
                    mActivity.replaceFragment(SetPrayerTimeFragment(), SetPrayerTimeFragment::class.java.simpleName, true)
                else
                    Utils.showSnackBar("Fetching the latest Prayer timings, Please try again later", rootView)
            }
            R.id.ivSettings -> {
                mActivity.executeIntent(Intent(context, SettingsActivity::class.java), false)
            }
            R.id.tvSeeAll -> {
                mActivity.replaceFragment(ShowAllAlarmsFragment(), ShowAllAlarmsFragment::class.java.simpleName, true)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch {
            val pair = roomRepository.fetchAllAlarmsAsync(true)
            setRVAdapter(pair)
        }
    }

    private fun setRVAdapter(pair: Pair<MutableList<TimingsModel>?, MutableList<LocationsModel>?>) {
        mLinearLayoutManager = LinearLayoutManager(mActivity)
        rvAlarms.layoutManager = mLinearLayoutManager
        Utils.setRVSlideInLeftAnimation(rvAlarms)
        var adapter: AlarmsListAdapter
        launch {
            adapter = AlarmsListAdapter(this@HomeFragment, pair.first!!, pair.second!!, "Home", mActivity, mLinearLayoutManager, disposable)
            rvAlarms.adapter = adapter
        }
        showUiElementsAccordingly()
    }

    override fun fetchTotalAlarmCount() {
        showUiElementsAccordingly()
    }

    private fun showUiElementsAccordingly() {
        launch {
            val completePair = roomRepository.fetchAllAlarmsAsync(true)
            if ((completePair.first!!.size + completePair.second!!.size <= 3) && appPreferences.hbl == 0f)
                rootView.tvSeeAll.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        disposable.clear()
    }
}
