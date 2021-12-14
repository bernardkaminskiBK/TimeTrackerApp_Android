package com.berni.timetrackerapp.ui.fragments.records

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.application.TimeTrackerApplication
import com.berni.timetrackerapp.databinding.*
import com.berni.timetrackerapp.model.database.FilterOrder
import com.berni.timetrackerapp.model.database.viewmodel.DatabaseViewModel
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerViewModelFactory
import com.berni.timetrackerapp.model.entities.Record
import com.berni.timetrackerapp.ui.adapters.FilterAdapter
import com.berni.timetrackerapp.ui.adapters.RecordAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val SHOW_ALL_RECORDS = "All Records"

class RecordsFragment : Fragment(R.layout.fragment_records) {

    private lateinit var mRecordsAdapter: RecordAdapter
    private lateinit var mFilterAdapter: FilterAdapter

    private lateinit var addRecordDialog: BottomSheetDialog
    private lateinit var filterDialog: Dialog

    private lateinit var binding: BottomSheetAddDialogBinding

    private var _mBinding: FragmentRecordsBinding? = null
    private val mBinding get() = _mBinding!!

    private val database: DatabaseViewModel by viewModels {
        TimeTrackerViewModelFactory(
            requireActivity().application,
            (requireActivity().application as TimeTrackerApplication).repository
        )
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _mBinding = FragmentRecordsBinding.bind(view)

//        for (i in TestData.randomTestDataToDB(30)) {
//            database.insert(i)
//        }

        mBinding.apply {
            fabAdd.setOnClickListener { addRecordDialog() }
            mRecordsAdapter = RecordAdapter(this@RecordsFragment)
            rvRecordsList.apply {
                adapter = mRecordsAdapter
                setHasFixedSize(true)
            }
            setSwipedDelete()
            setHideFabWhenScroll(fabAdd, rvRecordsList)
        }

        database.records.observe(viewLifecycleOwner) {
            it.let {
                mRecordsAdapter.submitList(it)
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.records_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_records -> {
                filterListDialog()
                return true
            }
            R.id.action_delete_all_records -> {
                deleteAllRecordsDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setSwipedDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val record =
                    mRecordsAdapter.currentList[viewHolder.adapterPosition]

                database.delete(record)
                undoSwipedDelete(record)
            }
        }).attachToRecyclerView(mBinding.rvRecordsList)
    }

    private fun setHideFabWhenScroll(fab: FloatingActionButton, rv: RecyclerView) {
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) {
                    fab.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show()
                }

                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun undoSwipedDelete(record: Record) {
        Snackbar.make(requireView(), getString(R.string.deleted), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undo)) {
                database.insert(record)
            }
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.gray))
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.darkSeaBlue))
            .show()
    }

    private fun addRecordDialog() {
        addRecordDialog = BottomSheetDialog(requireContext())
        binding = BottomSheetAddDialogBinding.inflate(layoutInflater)
        binding.apply {
            tvStopwatch.setOnClickListener {
                timePickerDialog(binding.tvStopwatch)
            }
            btnSave.setOnClickListener {
                validateInput(tiNameOfProgress.text.toString())
            }
            btnCancel.setOnClickListener {
                addRecordDialog.dismiss()
            }
        }
        addRecordDialog.setContentView(binding.root)
        addRecordDialog.show()
    }

    private fun timePickerDialog(textView: TextView) {
        val cal = Calendar.getInstance()
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hour: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                val formattedDate = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(cal.time)
                textView.text = getString(R.string.timePickerInput, formattedDate)
            }
        TimePickerDialog(
            requireContext(), timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true
        ).show()
    }

    private fun validateInput(input: String) {
        if (input.isNotEmpty()) {
            val name = binding.tiNameOfProgress.text.toString()
            val time = binding.tvStopwatch.text.toString()
            database.insert(Record(0, System.currentTimeMillis(), name, time))
            addRecordDialog.dismiss()
            Toast.makeText(requireContext(), getString(R.string.success_save), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(requireContext(), getString(R.string.add_name), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun filterSelection(filterSelection: String) {
        filterDialog.dismiss()

        if (filterSelection == SHOW_ALL_RECORDS) {
            database.onFilterOrderSelected(FilterOrder.SHOW_ALL)
        } else {
            database.onQuerySelected(filterSelection)
            database.onFilterOrderSelected(FilterOrder.BY_NAME)
        }
    }

    fun editRecord(record: Record) {
        val editRecordDialog = Dialog(requireContext())
        val binding = EditCustomDialogBinding.inflate(layoutInflater)
        editRecordDialog.setContentView(binding.root)

        binding.apply {
            tvDate.text = record.createdDateFormatted
            etEditName.setText(record.name)
            tvTime.text = record.time

            binding.tvTime.setOnClickListener {
                timePickerDialog(binding.tvTime)
            }

            binding.btnEdit.setOnClickListener {
                database.update(
                    Record(
                        record.id,
                        record.date,
                        etEditName.text.toString(),
                        tvTime.text.toString()
                    )
                )
                editRecordDialog.dismiss()
            }

            binding.btnCancel.setOnClickListener {
                editRecordDialog.dismiss()
            }

        }
        editRecordDialog.show()
    }

    private fun filterListDialog() {
        filterDialog = Dialog(this.requireContext())
        val binding = FilterDialogBinding.inflate(layoutInflater)
        filterDialog.setContentView(binding.root)

        binding.apply {
            rvFilterList.layoutManager = LinearLayoutManager(requireContext())

            database.allRecordNames.observe(viewLifecycleOwner) {
                val recordNames: ArrayList<String> = ArrayList()
                recordNames.add(0, SHOW_ALL_RECORDS)
                recordNames.addAll(it)

                mFilterAdapter = FilterAdapter(this@RecordsFragment)
                rvFilterList.adapter = mFilterAdapter
                mFilterAdapter.show(recordNames)
            }
            filterDialog.show()
        }
    }

    private fun deleteAllRecordsDialog() {
        val deleteAllRecordsDialog = Dialog(requireContext())
        val binding = DeleteAllRecordsDialogBinding.inflate(layoutInflater)
        deleteAllRecordsDialog.setContentView(binding.root)

        binding.btnYes.setOnClickListener {
            database.deleteAllRecords()
            deleteAllRecordsDialog.dismiss()
        }
        binding.btnNo.setOnClickListener {
            deleteAllRecordsDialog.dismiss()
        }
        deleteAllRecordsDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}