package com.berni.timetrackerapp.ui.fragments.statistics

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.application.TimeTrackerApplication
import com.berni.timetrackerapp.databinding.FragmentStatisticsBinding
import com.berni.timetrackerapp.model.database.viewmodel.DatabaseViewModel
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerViewModelFactory
import com.berni.timetrackerapp.model.entities.RecordDateTime
import com.berni.timetrackerapp.model.entities.RecordTotalTime
import com.berni.timetrackerapp.utils.Converter.convertSecondsToDateTime
import com.berni.timetrackerapp.utils.Converter.convertSecondsToHours
import com.berni.timetrackerapp.utils.Converter.roundTwoDecimalPlaces
import com.berni.timetrackerapp.utils.Formatter.dateToStringFormat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private lateinit var statisticsViewModel: StatisticsViewModel

    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart
    private lateinit var pieChart: PieChart

    private lateinit var barChartData: ArrayList<RecordTotalTime>
    private lateinit var lineChartData: ArrayList<RecordDateTime>
    private lateinit var pieChartData: ArrayList<PieEntry>

    private lateinit var dropDownNameFilter: AutoCompleteTextView
    private lateinit var dropDownDateFilter: AutoCompleteTextView

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

        dropDownNameFilter = mBinding.actvLineChartName
        dropDownDateFilter = mBinding.actvBarChartTotalHours

        statisticsViewModel = ViewModelProvider(this)[StatisticsViewModel::class.java]

        barChartData = ArrayList()
        lineChartData = ArrayList()
        pieChartData = ArrayList()

        setPieChartData()
        observePieChartTitle()
        observeDropDownNameFilterData()
        observeBartChartData()

        setHasOptionsMenu(true)
    }

    private fun observeBartChartData() {
        statisticsViewModel.recordDate.observe(viewLifecycleOwner) { date ->
            dropDownDateFilter.setText(date!!, false)
            setBarChartData(date)

            mBinding.chartStatistics.tvBarChartTitle.text = getString(
                R.string.barChartLabelText,
                date.dateToStringFormat()
            )

            observeLineChartData(date)
        }
    }

    private fun observeLineChartData(date: String) {
        statisticsViewModel.recordName.observe(viewLifecycleOwner) { name ->
            dropDownNameFilter.setText(name!!, false)
            setLineChartData(name, date)

            mBinding.chartStatistics.tvLineChartTitle.text = getString(
                R.string.lineChartLabelText,
                name,
                date.dateToStringFormat()
            )
        }
    }

    private fun observeDropDownNameFilterData() {
        database.allRecordNames.observe(viewLifecycleOwner) {
            val arrayNameAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, it)
            dropDownNameFilter.setAdapter(arrayNameAdapter)
            dropDownNameFilter.setOnItemClickListener { parent, view, position, id ->
                statisticsViewModel.saveNameOfRecord(arrayNameAdapter.getItem(position)!!)
            }
        }
    }

    private fun observePieChartTitle() {
        database.getAllDate.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val firstDateRecord = it[0]
                val lastDateRecord = it[it.size - 1]

                mBinding.chartStatistics.tvPieChartTitle.text = getString(
                    R.string.pieChartLabelText,
                    firstDateRecord.dateToStringFormat(),
                    lastDateRecord.dateToStringFormat()
                )
            } else {
                statisticsViewModel.saveDateOfRecord("Date")
                statisticsViewModel.saveNameOfRecord("Name")
            }

            observeDropDownDateFilterData(it)
        }
    }

    private fun observeDropDownDateFilterData(it: List<String>) {
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, it)
        dropDownDateFilter.setAdapter(arrayAdapter)
        dropDownDateFilter.setOnItemClickListener { parent, view, position, id ->
            statisticsViewModel.saveDateOfRecord(arrayAdapter.getItem(position)!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.statistics_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_jump_to_records -> {
                findNavController().navigate(StatisticsFragmentDirections.actionNavStatisticToNavRecords())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setPieChartData() {
        database.getTotalTimeRecords.observe(viewLifecycleOwner) { recordTotalHoursTimeList ->
            pieChartData.clear()
            for (recordTotalHours in recordTotalHoursTimeList) {
                pieChartData.add(
                    PieEntry(
                        recordTotalHours.totalTime.convertSecondsToHours(),
                        recordTotalHours.name
                    )
                )
            }
            setPieChart()
        }
    }

    private fun setBarChartData(month: String) {
        database.getAllRecordsByMonth(month)
            .observe(viewLifecycleOwner) { recordsByMonth ->
                barChartData.clear()
                for (recordByMonth in recordsByMonth) {
                    barChartData.add(recordByMonth)
                }
                setBarChart()
            }
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

    private fun setPieChart() {
        pieChart = mBinding.chartStatistics.pieChart
        initPieChart()

        pieChart.setUsePercentValues(true)

        val colors: ArrayList<Int> = ArrayList()
        for (i in pieChartData.indices) {
            if (i % 2 == 0) {
                colors.add(ContextCompat.getColor(requireContext(), R.color.primaryColor))
            } else {
                colors.add(ContextCompat.getColor(requireContext(), R.color.darkSeaBlue))
            }
        }

        val dataSet = PieDataSet(pieChartData, "")
        val data = PieData(dataSet)

        // In Percentage
//        data.setValueFormatter(PercentFormatter())
        data.setValueFormatter(object : ValueFormatter() {
            override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
                return "${value.roundTwoDecimalPlaces()}%"
            }
        })

        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart.data = data

        data.setValueTextSize(12f)
        data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        //create hole in center
        pieChart.holeRadius = 48f
        pieChart.transparentCircleRadius = 51f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(ContextCompat.getColor(requireContext(), R.color.primaryDarkColor))

        //add text in center
        pieChart.setDrawCenterText(false);
//        pieChart.centerText = "Mobile OS Market share"

        pieChart.invalidate()
    }

    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""

        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)

        //adding padding
        pieChart.setExtraOffsets(20f, 0f, 20f, 20f)
        pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(true)

        pieChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        pieChart.legend.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        pieChart.legend.isWordWrapEnabled = true
    }

    private fun setBarChart() {
        barChart = mBinding.chartStatistics.barChart
        initBarChart()

        val entries = ArrayList<BarEntry>()

        for (i in barChartData.indices) {
            val record = barChartData[i]
            entries.add(BarEntry(i.toFloat(), record.totalTime.convertSecondsToHours()))
        }

        val barDataSet = BarDataSet(entries, getString(R.string.bar_chart_legend_title))
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.primaryColor)
        barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        barDataSet.valueTextSize = 9f
        barDataSet.valueFormatter = xAxisBarChartLabelFormatter

        val data = BarData(barDataSet)
        barChart.data = data // set the data and list of labels into chart

        barChart.postInvalidate()
    }

    private val xAxisBarChartLabelFormatter = object : ValueFormatter() {
        override fun getBarLabel(barEntry: BarEntry?): String {
            val label = ((barEntry?.y!!.toFloat()) * 3600).toLong()
            return label.convertSecondsToDateTime()
        }
    }

    private fun initBarChart() {
        //hide grid lines
        barChart.axisLeft.setDrawGridLines(true)
        val xAxis: XAxis = barChart.xAxis

        //left axis
        barChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        barChart.axisLeft.axisLineColor = ContextCompat.getColor(requireContext(), R.color.primaryColor)
        barChart.axisLeft.gridColor = ContextCompat.getColor(requireContext(), R.color.primaryColor)
        barChart.axisLeft.textSize = 14f
        barChart.legend.textColor = ContextCompat.getColor(requireContext(), R.color.white)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = true
        barChart.legend.textSize = 10f

        //remove description label
        barChart.description.isEnabled = false

        //add animation
        barChart.animateY(2000)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.valueFormatter = BarChartAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = 0f
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.textSize = 11f
    }

    private fun setLineChart() {
        lineChart = mBinding.chartStatistics.lineChart
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

    inner class BarChartAxisFormatter : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < barChartData.size) {
                barChartData[index].name
            } else {
                ""
            }
        }
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

