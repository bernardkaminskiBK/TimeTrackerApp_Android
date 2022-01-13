package com.berni.timetrackerapp.ui.fragments.overview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.FragmentRecordsBinding
import com.berni.timetrackerapp.databinding.FragmentSettingsBinding
import com.berni.timetrackerapp.databinding.OverviewFragmentBinding
import com.berni.timetrackerapp.model.entities.Record
import com.berni.timetrackerapp.ui.adapters.OverviewAdapter
import com.berni.timetrackerapp.ui.fragments.settings.SettingsViewModel

class OverviewFragment : Fragment(R.layout.overview_fragment) {

    private lateinit var overviewViewModel: OverviewViewModel

    private var _mBinding: OverviewFragmentBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _mBinding = OverviewFragmentBinding.bind(view)
        overviewViewModel = ViewModelProvider(this)[OverviewViewModel::class.java]

        mBinding.rvOverview.layoutManager = GridLayoutManager(requireActivity(), 2)
        val overviewAdapter = OverviewAdapter(this)
        mBinding.rvOverview.adapter = overviewAdapter
        overviewAdapter.recordsList(listOfRecords())
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

    fun listOfRecords() : List<Record> {
        val list = mutableListOf<Record>()
        list.add(Record(id = 1, 5000000L, "Duolingo", 0))
        list.add(Record(id = 2, 3265000L, "Duolingo", 0))
        list.add(Record(id = 3, 3265000L, "Work", 0))
        list.add(Record(id = 4, 250000L, "Work", 0))
        list.add(Record(id = 5, 5000000L, "Cooking", 0))
        list.add(Record(id = 6, 250000L, "Cooking", 0))
        list.add(Record(id = 7, 3265000L, "Cooking", 0))
        return list
    }

}