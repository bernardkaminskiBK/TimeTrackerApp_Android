package com.berni.timetrackerapp.ui.fragments.statistics

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.application.TimeTrackerApplication
import com.berni.timetrackerapp.databinding.FragmentStatisticsBinding
import com.berni.timetrackerapp.model.database.viewmodel.DatabaseViewModel
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerViewModelFactory
import com.berni.timetrackerapp.utils.Converter.convertSecondsToHours
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private lateinit var statisticsViewModel: StatisticsViewModel

    private var _mBinding: FragmentStatisticsBinding? = null
    private val mBinding get() = _mBinding!!

    private val database: DatabaseViewModel by viewModels {
        TimeTrackerViewModelFactory(
            requireActivity().application,
            (requireActivity().application as TimeTrackerApplication).repository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _mBinding = FragmentStatisticsBinding.bind(view)

        statisticsViewModel = ViewModelProvider(this)[StatisticsViewModel::class.java]

        setBarChart()
    }

    private fun setBarChart() {
        val barChart = mBinding.barChart

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        val barDataSet = BarDataSet(entries, getString(R.string.bar_chart_legend_title))

        database.getTotalTimeRecords.observe(viewLifecycleOwner) { recordTotalTimeList ->

            for((xIndex, recordsTotalSum) in recordTotalTimeList.withIndex()) {
                labels.add(recordsTotalSum.name)
                entries.add(BarEntry(recordsTotalSum.totalTime.convertSecondsToHours(), xIndex))
            }
            val data = BarData(labels, barDataSet)

            barChart.data = data // set the data and list of lables into chart
        }

        barChart.setDescription("")  // set the description

        // xAxis
        barChart.xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.setDrawAxisLine(false)
        barChart.xAxis.textSize = 12f

        // left axis
        barChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        barChart.axisLeft.axisLineColor = ContextCompat.getColor(requireContext(), R.color.primaryColor)
//        barChart.axisLeft.gridColor = resources.getColor(R.color.white)
        barChart.axisLeft.textSize = 14f
        barChart.legend.textColor = resources.getColor(R.color.white)

        // right axis
        barChart.axisRight.textColor = ContextCompat.getColor(requireContext(), R.color.primaryDarkColor)
        barChart.axisRight.axisLineColor = ContextCompat.getColor(requireContext(), R.color.primaryColor)
        barChart.axisRight.gridColor = ContextCompat.getColor(requireContext(), R.color.primaryColor)

        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.primaryColor)
        barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        barDataSet.valueTextSize = 16f

        barChart.animateY(2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}