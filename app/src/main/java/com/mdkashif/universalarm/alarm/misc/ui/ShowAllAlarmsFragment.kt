package com.mdkashif.universalarm.alarm.misc.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.misc.AlarmsListAdapter
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.persistence.RoomRepository
import com.mdkashif.universalarm.utils.Utils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_show_all_alarms.*
import javax.inject.Inject

class ShowAllAlarmsFragment : BaseFragment(), AlarmsListAdapter.GetTotalAlarmCountInterface {

    @Inject
    lateinit var roomRepository: RoomRepository

    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_all_alarms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pair = roomRepository.fetchAllAlarmsAsync()
        setRVAdapter(pair)
    }

    private fun setRVAdapter(pair: Pair<MutableList<TimingsModel>?, MutableList<LocationsModel>?>) {
        mLinearLayoutManager = LinearLayoutManager(mActivity)
        rvAlarms.layoutManager = mLinearLayoutManager
        Utils.setRVSlideInLeftAnimation(rvAlarms)
        val adapter = AlarmsListAdapter(this, pair.first!!, pair.second!!, "ShowAll", mActivity, mLinearLayoutManager, disposable)
        rvAlarms.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun fetchTotalAlarmCount() {
        // do nothing
    }
}
