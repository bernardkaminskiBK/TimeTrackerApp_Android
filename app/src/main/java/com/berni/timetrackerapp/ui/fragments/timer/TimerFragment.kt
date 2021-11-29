package com.berni.timetrackerapp.ui.fragments.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    private lateinit var timerViewModel: TimerViewModel

    private var _mBinding: FragmentTimerBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        timerViewModel =
            ViewModelProvider(this).get(TimerViewModel::class.java)

        timerViewModel.text.observe(viewLifecycleOwner, Observer {
            mBinding.tvTimer.text = it
        })

        _mBinding = FragmentTimerBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }
}