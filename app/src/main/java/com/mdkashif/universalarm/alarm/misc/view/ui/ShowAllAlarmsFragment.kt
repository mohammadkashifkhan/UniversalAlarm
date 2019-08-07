package com.mdkashif.universalarm.alarm.misc.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.alarm.misc.view.adapter.AlarmsListAdapter
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.persistence.RoomRepository
import com.mdkashif.universalarm.utils.Utils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_show_all_alarms.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.CoroutineContext

class ShowAllAlarmsFragment : BaseFragment(), AlarmsListAdapter.GetTotalAlarmCountInterface, CoroutineScope, KoinComponent {

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val roomRepository: RoomRepository by inject()

    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_all_alarms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch {
            val pair = roomRepository.fetchAllAlarmsAsync()
            setRVAdapter(pair)
        }
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
        job.cancel()
        disposable.clear()
    }

    override fun fetchTotalAlarmCount() {
        // do nothing
    }
}
