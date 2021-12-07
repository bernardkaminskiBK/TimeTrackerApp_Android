package com.berni.timetrackerapp.ui.fragments.add

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.application.TimeTrackerApplication
import com.berni.timetrackerapp.databinding.BottomSheetAddDialogBinding
import com.berni.timetrackerapp.databinding.DeleteCustomDialogBinding
import com.berni.timetrackerapp.databinding.DialogCustomListBinding
import com.berni.timetrackerapp.databinding.FragmentAddBinding
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerDBViewModel
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerViewModelFactory
import com.berni.timetrackerapp.model.entities.Progress
import com.berni.timetrackerapp.ui.adapters.CustomListItemAdapter
import com.berni.timetrackerapp.ui.adapters.TimeTrackerAdapter
import com.berni.timetrackerapp.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

private const val ALL_ITEMS = "All Items"

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var mTimeTrackerAdapter: TimeTrackerAdapter
    private lateinit var mCustomListAdapter: CustomListItemAdapter

    private lateinit var addDialogBinding: BottomSheetAddDialogBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var mCustomListDialog: Dialog

    private var _mBinding: FragmentAddBinding? = null
    private val mBinding get() = _mBinding!!

    private val mTimeTrackerDBViewModel: TimeTrackerDBViewModel by viewModels {
        TimeTrackerViewModelFactory((requireActivity().application as TimeTrackerApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _mBinding = FragmentAddBinding.bind(view)

//        for(i in TestData.randomTestDataToDB()) {
//            mTimeTrackerDBViewModel.insert(i)
//        }

        mBinding.apply {
            fabAdd.setOnClickListener { addProgressDialog() }
            mTimeTrackerAdapter = TimeTrackerAdapter(this@AddFragment)
            rvTimeTrackerProgressList.apply {
                adapter = mTimeTrackerAdapter
                setHasFixedSize(true)
            }
            onSwipedDelete()
            Utils.hideFabWhenScroll(fabAdd, rvTimeTrackerProgressList)
        }

        mTimeTrackerDBViewModel.allProgressList.observe(viewLifecycleOwner) {
            it.let {
                mTimeTrackerAdapter.submitList(it)
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_progress_time -> {
                filterListDialog()
                return true
            }
            R.id.action_delete_all_progress_time -> {
                customDeleteDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onSwipedDelete() {
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
                val progress =
                    mTimeTrackerAdapter.currentList[viewHolder.adapterPosition]

                mTimeTrackerDBViewModel.delete(progress)
                undo(progress)
            }
        }).attachToRecyclerView(mBinding.rvTimeTrackerProgressList)
    }

    private fun undo(progress: Progress) {
        Snackbar.make(requireView(), "Time tracker progress deleted", Snackbar.LENGTH_LONG)
            .setAction("UNDO") {
                mTimeTrackerDBViewModel.insert(progress)
            }
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.gray))
            .setActionTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.darkSeaBlue
                )
            )
            .show()
    }

    private fun addProgressDialog() {
        dialog = BottomSheetDialog(requireContext())
        addDialogBinding = BottomSheetAddDialogBinding.inflate(layoutInflater)
        addDialogBinding.apply {
            tvStopwatch.setOnClickListener {
                timePickerDialog()
            }
            btnSave.setOnClickListener {
                validateInput(tiNameOfProgress.text.toString())
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.setContentView(addDialogBinding.root)
        dialog.show()
    }

    private fun timePickerDialog() {
        val cal = Calendar.getInstance()
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hour: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                addDialogBinding.tvStopwatch.text =
                    "${SimpleDateFormat("HH:mm").format(cal.time)}:00"
            }
        TimePickerDialog(
            requireContext(), timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true
        ).show()
    }

    private fun validateInput(input: String) {
        if (input.isNotEmpty()) {
            val name = addDialogBinding.tiNameOfProgress.text.toString()
            val time = addDialogBinding.tvStopwatch.text.toString()
            mTimeTrackerDBViewModel.insert(Progress(0, System.currentTimeMillis(), name, time))
            dialog.dismiss()
            Toast.makeText(requireContext(), getString(R.string.success_save), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(requireContext(), getString(R.string.add_name), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun filterSelection(filterSelection: String) {
        mCustomListDialog.dismiss()

        if (filterSelection == ALL_ITEMS) {
            mTimeTrackerDBViewModel.allProgressList.observe(viewLifecycleOwner) {
                it.let {
                    mTimeTrackerAdapter.submitList(it)
                }
            }
        } else {
            mTimeTrackerDBViewModel.getFilteredProgressList(filterSelection)
                .observe(viewLifecycleOwner) {
                    it.let {
                        mTimeTrackerAdapter.submitList(it)
                    }
                }
        }
    }

    fun updateProgressItem(progress: Progress) {

    }

    private fun filterListDialog() {
        mCustomListDialog = Dialog(this.requireContext())
        val binding: DialogCustomListBinding =
            DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)

        binding.apply {
            rvList.layoutManager = LinearLayoutManager(requireContext())

            mTimeTrackerDBViewModel.allProgressNames.observe(viewLifecycleOwner) {
                val listItemHashSet: HashSet<String> = HashSet()
                listItemHashSet.addAll(it)

                val listItem: ArrayList<String> = ArrayList()
                listItem.add(0, ALL_ITEMS)
                listItem.addAll(listItemHashSet)

                mCustomListAdapter = CustomListItemAdapter(this@AddFragment)
                rvList.adapter = mCustomListAdapter
                mCustomListAdapter.show(listItem)
            }
            mCustomListDialog.show()
        }
    }

    private fun customDeleteDialog() {
        val customDialog = Dialog(requireContext())
        val mDialogBinding: DeleteCustomDialogBinding =
            DeleteCustomDialogBinding.inflate(layoutInflater)
        customDialog.setContentView(mDialogBinding.root)
        mDialogBinding.tvYes.setOnClickListener {
            mTimeTrackerDBViewModel.deleteAllProgressRecords()
            customDialog.dismiss()
        }
        mDialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}