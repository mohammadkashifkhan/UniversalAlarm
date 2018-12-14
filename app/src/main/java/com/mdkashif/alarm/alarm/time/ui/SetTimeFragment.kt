package com.mdkashif.alarm.alarm.time.ui

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.ContainerActivity
import kotlinx.android.synthetic.main.fragment_add_time.view.*
import java.util.*


class SetTimeFragment : Fragment(), View.OnClickListener{


    private lateinit var mActivity: ContainerActivity
    private lateinit var rootView:View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_add_time, container, false)

        return rootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mActivity = context as ContainerActivity
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.fbAddNote->{
                rootView.fbAddNote.visibility=View.GONE
                rootView.etNote.visibility=View.VISIBLE
            }

            R.id.tvTime->{
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                val minute = mcurrentTime.get(Calendar.MINUTE)
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(mActivity, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute -> rootView.tvTime.text = selectedHour.toString() + ":" + selectedMinute }, hour, minute, false)
                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }
        }
    }

}
