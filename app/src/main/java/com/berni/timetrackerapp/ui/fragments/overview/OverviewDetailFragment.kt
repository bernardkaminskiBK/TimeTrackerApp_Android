package com.berni.timetrackerapp.ui.fragments.overview

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.application.TimeTrackerApplication
import com.berni.timetrackerapp.databinding.FragmentOverviewDetailBinding
import com.berni.timetrackerapp.model.database.viewmodel.DatabaseViewModel
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerViewModelFactory
import com.berni.timetrackerapp.model.entities.RecordDateTime
import com.berni.timetrackerapp.utils.Converter.convertSecondsToDateTime
import com.berni.timetrackerapp.utils.Converter.convertSecondsToHours
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.transition.MaterialContainerTransform

class OverviewDetailFragment : Fragment(R.layout.fragment_overview_detail) {

    private lateinit var lineChartData: ArrayList<RecordDateTime>
    private lateinit var lineChart: LineChart

    private var _mBinding: FragmentOverviewDetailBinding? = null
    private val mBinding get() = _mBinding!!

    private val database: DatabaseViewModel by viewModels {
        TimeTrackerViewModelFactory(
            requireActivity().application,
            (requireActivity().application as TimeTrackerApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment_content_main
            duration = 300L
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _mBinding = FragmentOverviewDetailBinding.bind(view)

        lineChartData = ArrayList()

        setLineChartData("Duolingo","01/2021")
    }

    private fun setLineChartData(name: String, date: String) {
        database.getRecordsByNameByDateSumTimeWhereIsSameDate(name, date)
            .observe(viewLifecycleOwner) { recordDateTimeList ->
                lineChartData.clear()
                if (recordDateTimeList.size > 1) {
                    for (recordByMonth in recordDateTimeList) {
                        lineChartData.add(recordByMonth)
                    }
                    setLineChart()
                } else {
                    lineChartData.clear()
                    setLineChart()
                }
            }
    }

    private fun setLineChart() {
        lineChart = mBinding.scrollViewContent.lineChart
        initLineChart()

        //now draw bar chart with dynamic data
        val entries: ArrayList<Entry> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in lineChartData.indices) {
            val totalHour = lineChartData[i]
            entries.add(Entry(i.toFloat(), totalHour.time.convertSecondsToHours()))
        }

        val lineDataSet = LineDataSet(entries, getString(R.string.line_chart_legend_title))
        lineDataSet.valueTextSize = 8f
        lineDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        lineDataSet.valueFormatter = xAxisLineChartLabelFormatter

        val data = LineData(lineDataSet)
        lineChart.data = data

        lineChart.invalidate()
    }

    private val xAxisLineChartLabelFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val label = (value * 3600).toLong()
            return label.convertSecondsToDateTime()
        }
    }

    private fun initLineChart() {
        //hide grid lines
        lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        lineChart.axisRight.isEnabled = false

        // axis left
        lineChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        lineChart.axisLeft.textSize = 14f
        lineChart.axisLeft.axisLineColor = ContextCompat.getColor(requireContext(), R.color.primaryColor)

        //remove legend Legend.LegendOrientation.HORIZONTAL
        lineChart.legend.isEnabled = true
        lineChart.legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
        lineChart.legend.textSize = 10f
        lineChart.legend.textColor = ContextCompat.getColor(requireContext(), R.color.white)

        //remove description label
        lineChart.description.isEnabled = false

        //add animation
        lineChart.animateX(1000, Easing.EaseInSine)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = LineChartAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -20.0f
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        xAxis.textSize = 11f
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

    inner class LineChartAxisFormatter : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < lineChartData.size) {
                lineChartData[index].day
            } else {
                ""
            }
        }
    }

}