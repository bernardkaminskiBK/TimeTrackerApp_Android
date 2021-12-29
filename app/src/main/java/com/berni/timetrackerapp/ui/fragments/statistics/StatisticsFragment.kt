package com.berni.timetrackerapp.ui.fragments.statistics

import android.os.Bundle
import android.util.Log
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
import com.berni.timetrackerapp.model.entities.RecordTotalTime
import com.berni.timetrackerapp.utils.Converter.convertSecondsToHours
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private lateinit var statisticsViewModel: StatisticsViewModel

    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart

    private lateinit var totalHours: ArrayList<RecordTotalTime>

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

        totalHours = ArrayList()

        database.getTotalTimeRecords.observe(viewLifecycleOwner) { recordTotalHoursTimeList ->
            for (recordTotalHours in recordTotalHoursTimeList) {
                totalHours.add(recordTotalHours)
//                labels.add(recordsTotalSum.name)
//                entries.add(
//                    BarEntry(
//                        recordsTotalSum.totalTime.convertSecondsToHours(),
//                        xIndex.toFloat()
//                    )
//                )
            }
            setBarChart()
            setLineChart()
        }
    }

    private fun setBarChart() {
        barChart = mBinding.chartStatistics.barChart
        initBarChart()

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        for (i in totalHours.indices) {
            val record = totalHours[i]
            entries.add(BarEntry(i.toFloat(), record.totalTime.convertSecondsToHours()))
        }

        val barDataSet = BarDataSet(entries, getString(R.string.bar_chart_legend_title))
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.primaryColor)
        barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        barDataSet.valueTextSize = 16f

        val data = BarData(barDataSet)
        barChart.data = data // set the data and list of lables into chart

        barChart.postInvalidate()


//        // xAxis
//        barChart.xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
//        barChart.xAxis.setDrawGridLines(false)
//        barChart.xAxis.setDrawGridLines(false)
//        barChart.xAxis.setDrawAxisLine(false)
//        barChart.xAxis.textSize = 12f
//
//        // left axis
//        barChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
//        barChart.axisLeft.axisLineColor =
//            ContextCompat.getColor(requireContext(), R.color.primaryColor)
////        barChart.axisLeft.gridColor = resources.getColor(R.color.white)
//        barChart.axisLeft.textSize = 14f
//        barChart.legend.textColor = resources.getColor(R.color.white)
//
//        // right axis
//        barChart.axisRight.textColor =
//            ContextCompat.getColor(requireContext(), R.color.primaryDarkColor)
//        barChart.axisRight.axisLineColor =
//            ContextCompat.getColor(requireContext(), R.color.primaryColor)
//        barChart.axisRight.gridColor =
//            ContextCompat.getColor(requireContext(), R.color.primaryColor)
//
//        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
//        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.primaryColor)
//        barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
//        barDataSet.valueTextSize = 16f
//
//        barChart.animateY(2000)
//
//        barChart.invalidate()
    }

    private fun initBarChart() {

        //        hide grid lines
        barChart.axisLeft.setDrawGridLines(true)
        val xAxis: XAxis = barChart.xAxis

        // left axis
        barChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        barChart.axisLeft.axisLineColor =
            ContextCompat.getColor(requireContext(), R.color.primaryColor)
        barChart.axisLeft.gridColor = ContextCompat.getColor(requireContext(), R.color.primaryColor)
        barChart.axisLeft.textSize = 14f
        barChart.legend.textColor = ContextCompat.getColor(requireContext(), R.color.white)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = true
        barChart.legend.textSize = 12f

        //remove description label
        barChart.description.isEnabled = false

        //add animation
        barChart.animateY(2000)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.valueFormatter = TotalHoursAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = 0f
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.setDrawAxisLine(false)
        xAxis.textSize = 11f
    }

    private fun setLineChart() {
        lineChart = mBinding.chartStatistics.lineChart
        initLineChart()

        //now draw bar chart with dynamic data
        val entries: ArrayList<Entry> = ArrayList()
//
//        scoreList = getScoreList()

        //you can replace this data object with  your custom object
        for (i in totalHours.indices) {
            val totalHour = totalHours[i]
            entries.add(Entry(i.toFloat(), totalHour.totalTime.convertSecondsToHours()))
        }

        val lineDataSet = LineDataSet(entries, "")

        val data = LineData(lineDataSet)
        lineChart.data = data

        lineChart.invalidate()

    }

    private fun initLineChart() {
        //        hide grid lines
        lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        lineChart.axisRight.isEnabled = false

        //remove legend
        lineChart.legend.isEnabled = false

        //remove description label
        lineChart.description.isEnabled = false

        //add animation
        lineChart.animateX(1000, Easing.EaseInSine)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = TotalHoursAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

    inner class TotalHoursAxisFormatter : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.e("statistics", "getAxisLabel: index $index")
            return if (index < totalHours.size) {
                totalHours[index].name
            } else {
                ""
            }
        }
    }

}

