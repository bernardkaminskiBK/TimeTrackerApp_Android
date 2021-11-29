package com.berni.timetrackerapp.ui.fragments.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.FragmentAddBinding
import com.berni.timetrackerapp.databinding.FragmentTimerBinding
import com.berni.timetrackerapp.ui.fragments.timer.TimerViewModel

class AddFragment : Fragment() {

    private lateinit var addViewModel: AddViewModel

    private var _mBinding: FragmentAddBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addViewModel =
            ViewModelProvider(this).get(AddViewModel::class.java)

        addViewModel.text.observe(viewLifecycleOwner, Observer {
            mBinding.tvAdd.text = it
        })

        _mBinding = FragmentAddBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}