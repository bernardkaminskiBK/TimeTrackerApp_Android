package com.berni.timetrackerapp.ui.fragments.overview

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.application.TimeTrackerApplication
import com.berni.timetrackerapp.databinding.FragmentOverviewDetailBinding
import com.berni.timetrackerapp.model.database.viewmodel.DatabaseViewModel
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerViewModelFactory
import com.berni.timetrackerapp.model.entities.OverviewDetailLastWeek
import com.berni.timetrackerapp.model.entities.Record
import com.berni.timetrackerapp.utils.Converter.convertSecondsToDateTime
import com.berni.timetrackerapp.utils.Converter.convertSecondsToHours
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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

    private val recordArgs: OverviewDetailFragmentArgs by navArgs()

    private lateinit var lineChartData: ArrayList<OverviewDetailLastWeek>
    private lateinit var lineChart: LineChart

    private lateinit var dropDownYearFilter: AutoCompleteTextView
    private lateinit var dropDownMonthFilter: AutoCompleteTextView

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
            duration = 500L
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _mBinding = FragmentOverviewDetailBinding.bind(view)

        lineChartData = ArrayList()

        mBinding.scrollViewContent.tvOverviewTitle.text = recordArgs.recordDetails.name

        dropDownYearFilter = mBinding.scrollViewContent.actvYears
        dropDownMonthFilter = mBinding.scrollViewContent.actvMonths

        mBinding.scrollViewContent.ivUnsplashGallery.setOnClickListener {
            val action = OverviewDetailFragmentDirections
                .actionNavOverviewDetailToNavGallery(recordArgs.recordDetails.copy())
            findNavController().navigate(action)
        }

        initDataToView()
    }

    private fun initDataToView() {

        Glide.with(mBinding.scrollViewContent.ivOverviewDetail)
            .load(recordArgs.recordDetails.imgUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(mBinding.scrollViewContent.ivOverviewDetail)

        database.getLastAddedRecordMonthYearByName(recordArgs.recordDetails.name)
            .observe(viewLifecycleOwner) {

                dropDownMonthFilter.setText(it.month, false)
                dropDownYearFilter.setText(it.year, false)

                setYearFilter()
                setMonthFilter(it.year)
                setDataToTableView(it.month, it.year)
                setLineChartData(recordArgs.recordDetails.name, it.year)
            }
    }

    private fun setYearFilter() {
        database.allYearsByName(recordArgs.recordDetails.name).observe(viewLifecycleOwner) { allYears ->
            val arrayYearsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, allYears)
            dropDownYearFilter.setAdapter(arrayYearsAdapter)
            dropDownYearFilter.setOnItemClickListener { parent, view, position, id ->
                val chosenYear = arrayYearsAdapter.getItem(position)!!

                setMonthFilter(chosenYear)
            }
        }
    }

    private fun setMonthFilter(chosenYear: String) {
        database.getMonthsByNameByYear(recordArgs.recordDetails.name, chosenYear)
            .observe(viewLifecycleOwner) { monthsByNameByYear ->
                val arrayMonthsAdapter =
                    ArrayAdapter(requireContext(), R.layout.dropdown_item, monthsByNameByYear)
                dropDownMonthFilter.setAdapter(arrayMonthsAdapter)
                dropDownMonthFilter.setOnItemClickListener { parent, view, position, id ->
                    val chosenMonth = arrayMonthsAdapter.getItem(position)!!

                    setDataToTableView(chosenMonth, chosenYear)
                }
            }
    }

    private fun setDataToTableView(chosenMonth: String, chosenYear: String) {
        database.getRecordTotalTimeByNameByDate(
            recordArgs.recordDetails.name,
            "${chosenMonth}/${chosenYear}"
        ).observe(viewLifecycleOwner) { totalTime ->
            mBinding.scrollViewContent.tvTotalTime.text =
                totalTime.totalTime.convertSecondsToDateTime()

            database.getRecordTotalDaysByNameByDate(
                recordArgs.recordDetails.name,
                "${chosenMonth}/${chosenYear}"
            ).observe(viewLifecycleOwner) { totalDays ->
                mBinding.scrollViewContent.tvTotalDays.text = totalDays.totalDays.toString()
                mBinding.scrollViewContent.tvAverage.text =
                    totalTime.totalTime.div(totalDays.totalDays).convertSecondsToDateTime()
            }
        }

        database.getMostRecentRecordByName(recordArgs.recordDetails.name)
            .observe(viewLifecycleOwner) {
                mBinding.scrollViewContent.tvMostRecent.text = it.mostRecentRecord
            }
    }

    private fun setLineChartData(name: String, year: String) {
        database.getLastSevenRecordsByNameByYear(name, year)
            .observe(viewLifecycleOwner) { lastWeekRecordList ->
                lineChartData.clear()
                if (lastWeekRecordList.size > 1) {
                    lastWeekRecordList.forEach { lineChartData.add(it) }
                    lineChartData.sortBy { it.dayMonth }
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
        lineDataSet.valueTextSize = 10f
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
        lineChart.axisLeft.axisLineColor =
            ContextCompat.getColor(requireContext(), R.color.primaryColor)

        //remove legend Legend.LegendOrientation.HORIZONTAL
        lineChart.legend.isEnabled = true
        lineChart.legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
        lineChart.legend.textSize = 11f
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
        xAxis.labelRotationAngle = 0f
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        xAxis.textSize = 12f
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

    inner class LineChartAxisFormatter : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < lineChartData.size) {
                lineChartData[index].dayMonth
            } else {
                ""
            }
        }
    }

}