package com.berni.timetrackerapp.ui.fragments.timer

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.application.TimeTrackerApplication
import com.berni.timetrackerapp.databinding.BottomSheetSaveDialogBinding
import com.berni.timetrackerapp.databinding.FragmentTimerBinding
import com.berni.timetrackerapp.model.entities.Progress
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerDBViewModel
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog

class TimerFragment : Fragment(R.layout.fragment_timer) {

    private lateinit var timerViewModel: TimerViewModel
    private lateinit var saveDialogBinding: BottomSheetSaveDialogBinding
    private lateinit var dialog: BottomSheetDialog

    private var _mBinding: FragmentTimerBinding? = null
    private val mBinding get() = _mBinding!!

    private val mTimeTrackerDBViewModel: TimeTrackerDBViewModel by viewModels {
        TimeTrackerViewModelFactory((requireActivity().application as TimeTrackerApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _mBinding = FragmentTimerBinding.bind(view)
        timerViewModel = ViewModelProvider(this)[TimerViewModel::class.java]

        timerViewModel.actualTime.observe(viewLifecycleOwner, Observer {
            mBinding.tvStopwatch.text = it
        })

        mBinding.llStartStop.setOnClickListener { startStopTimer() }
        mBinding.llReset.setOnClickListener { resetTimer() }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.timer_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save_activity_time -> {
                stopTimer()
                saveProgressToDB()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveProgressToDB() {
        dialog = BottomSheetDialog(requireContext())
        saveDialogBinding = BottomSheetSaveDialogBinding.inflate(layoutInflater)
        saveDialogBinding.apply {
            tvTimerResult.text = mBinding.tvStopwatch.text
            btnSave.setOnClickListener {
                validateInput(tiNameOfProgress.text.toString())
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setContentView(root)
            dialog.show()
        }
    }

    private fun validateInput(input: String) {
        if (input.isNotEmpty()) {
            val name = saveDialogBinding.tiNameOfProgress.text.toString()
            val time = saveDialogBinding.tvTimerResult.text.toString()
            mTimeTrackerDBViewModel.insert(Progress(0, System.currentTimeMillis(), name, time))
            dialog.dismiss()
            Toast.makeText(requireContext(), getString(R.string.success_save), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(requireContext(), getString(R.string.add_name), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun startStopTimer() {
        if (timerViewModel.timerStarted.value!!) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        timerViewModel.startTimer()
        mBinding.ivStartStop
            .setImageResource(R.drawable.ic_baseline_pause_white_88)
    }

    private fun stopTimer() {
        timerViewModel.stopTimer()
        mBinding.ivStartStop
            .setImageResource(R.drawable.ic_baseline_play_arrow_white_88)
    }

    private fun resetTimer() {
        mBinding.ivReset.rotation = 0f
        mBinding.ivReset.animate().apply {
            duration = 250
            rotation(-360f)
        }

        timerViewModel.resetTimer()
        mBinding.ivStartStop
            .setImageResource(R.drawable.ic_baseline_play_arrow_white_88)
        mBinding.tvStopwatch.text = getString(R.string.initialStatTimer)
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerViewModel.stopTimer()
    }

}