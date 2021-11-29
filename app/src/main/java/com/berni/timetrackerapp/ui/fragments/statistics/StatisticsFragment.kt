package com.berni.timetrackerapp.ui.fragments.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.FragmentSettingsBinding
import com.berni.timetrackerapp.databinding.FragmentStatisticsBinding
import com.berni.timetrackerapp.ui.fragments.settings.SettingsViewModel

class StatisticsFragment : Fragment() {

    private lateinit var statisticsViewModel: StatisticsViewModel

    private var _mBinding: FragmentStatisticsBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        statisticsViewModel =
            ViewModelProvider(this).get(StatisticsViewModel::class.java)

        statisticsViewModel.text.observe(viewLifecycleOwner, Observer {
            mBinding.tvStatistics.text = it
        })

        _mBinding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}