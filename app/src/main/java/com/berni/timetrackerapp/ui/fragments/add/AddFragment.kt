package com.berni.timetrackerapp.ui.fragments.add

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.application.TimeTrackerApplication
import com.berni.timetrackerapp.databinding.BottomSheetAddDialogBinding
import com.berni.timetrackerapp.databinding.FragmentAddBinding
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerDBViewModel
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerViewModelFactory
import com.berni.timetrackerapp.model.entities.Progress
import com.berni.timetrackerapp.ui.adapters.TimeTrackerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var mTimeTrackerAdapter: TimeTrackerAdapter

    private lateinit var addDialogBinding: BottomSheetAddDialogBinding
    private lateinit var dialog: BottomSheetDialog

    private var _mBinding: FragmentAddBinding? = null
    private val mBinding get() = _mBinding!!

    private val mTimeTrackerDBViewModel: TimeTrackerDBViewModel by viewModels {
        TimeTrackerViewModelFactory((requireActivity().application as TimeTrackerApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _mBinding = FragmentAddBinding.bind(view)

        mBinding.apply {
            fabAdd.setOnClickListener { addProgressDialog() }
            mTimeTrackerAdapter = TimeTrackerAdapter(this@AddFragment)
            rvTimeTrackerProgressList.apply {
                adapter = mTimeTrackerAdapter
                setHasFixedSize(true)
            }
            onSwipedDelete()
        }

        mTimeTrackerDBViewModel.allProgressList.observe(viewLifecycleOwner) {
            it.let {
                mTimeTrackerAdapter.submitList(it)
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}