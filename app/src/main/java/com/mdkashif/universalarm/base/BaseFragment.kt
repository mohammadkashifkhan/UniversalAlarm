package com.mdkashif.universalarm.base


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mdkashif.universalarm.activities.ContainerActivity

open class BaseFragment : Fragment() {
    lateinit var mActivity: ContainerActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //        this.activity.setTheme(ThemeHelper.active(context))
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mActivity = context as ContainerActivity
    }
}
