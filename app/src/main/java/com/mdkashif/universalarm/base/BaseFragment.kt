package com.mdkashif.universalarm.base


import android.content.Context
import androidx.fragment.app.Fragment
import com.mdkashif.universalarm.activities.ContainerActivity

open class BaseFragment : Fragment() {
    lateinit var mActivity: ContainerActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mActivity = context as ContainerActivity
//        mActivity.delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}
