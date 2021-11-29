package com.berni.timetrackerapp.ui.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.FragmentAddBinding
import com.berni.timetrackerapp.databinding.FragmentSettingsBinding
import com.berni.timetrackerapp.ui.fragments.add.AddViewModel

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    private var _mBinding: FragmentSettingsBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        settingsViewModel.text.observe(viewLifecycleOwner, Observer {
            mBinding.tvSettings.text = it
        })

        _mBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}