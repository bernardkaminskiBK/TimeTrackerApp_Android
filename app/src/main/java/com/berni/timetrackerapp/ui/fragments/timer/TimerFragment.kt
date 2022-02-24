package com.berni.timetrackerapp.ui.fragments.timer

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.application.TimeTrackerApplication
import com.berni.timetrackerapp.databinding.BottomSheetSaveDialogBinding
import com.berni.timetrackerapp.databinding.FragmentTimerBinding
import com.berni.timetrackerapp.model.database.viewmodel.DatabaseViewModel
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimerFragment : Fragment(R.layout.fragment_timer) {

    private lateinit var timerViewModel: TimerViewModel
    private lateinit var binding: BottomSheetSaveDialogBinding
    private lateinit var saveRecordDialog: BottomSheetDialog

    private var _mBinding: FragmentTimerBinding? = null
    private val mBinding get() = _mBinding!!

    private val database: DatabaseViewModel by viewModels {
        TimeTrackerViewModelFactory(
            requireActivity().application,
            (requireActivity().application as TimeTrackerApplication).repository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _mBinding = FragmentTimerBinding.bind(view)
        timerViewModel = ViewModelProvider(this)[TimerViewModel::class.java]

        timerViewModel.actualTime.observe(viewLifecycleOwner, {
            mBinding.tvStopwatch.text = it
        })

        observePlayPauseButton()

        mBinding.llStartStop.setOnClickListener { startStopTimer() }
        mBinding.llReset.setOnClickListener { resetTimer() }
        setHasOptionsMenu(true)
    }

    private fun observePlayPauseButton() {
        timerViewModel.timerStarted.observe(viewLifecycleOwner, { isTimerStarted ->
            if (isTimerStarted) {
                mBinding.ivStartStop
                    .setImageResource(R.drawable.ic_baseline_pause_white_70)
            } else {
                mBinding.ivStartStop
                    .setImageResource(R.drawable.ic_baseline_play_arrow_white_70)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.timer_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save_record -> {
                stopTimer()
                saveRecordToDB()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveRecordToDB() {
        saveRecordDialog = BottomSheetDialog(requireContext())
        binding = BottomSheetSaveDialogBinding.inflate(layoutInflater)
        binding.apply {
            tvTimerResult.text = mBinding.tvStopwatch.text
            btnSave.setOnClickListener {
                validateInput(tiNameOfRecord.text.toString())
            }
            btnCancel.setOnClickListener {
                saveRecordDialog.dismiss()
            }
            saveRecordDialog.setContentView(root)
            saveRecordDialog.show()
        }
    }

    private fun validateInput(input: String) {
        if (input.isNotEmpty()) {
            lifecycleScope.launchWhenResumed {

                val name = binding.tiNameOfRecord.text.toString()
                val time = binding.tvTimerResult.text.toString()
                val imgUrl = timerViewModel.fetchImageUrlBySearchQuery(input)

                database.saveDataIntoDatabase(imgUrl, name, time)

            }
            saveRecordDialog.dismiss()
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
            .setImageResource(R.drawable.ic_baseline_pause_white_70)
    }

    private fun stopTimer() {
        timerViewModel.stopTimer()
        mBinding.ivStartStop
            .setImageResource(R.drawable.ic_baseline_play_arrow_white_70)
    }

    private fun resetTimer() {
        mBinding.ivReset.rotation = 0f
        mBinding.ivReset.animate().apply {
            duration = 250
            rotation(-360f)
        }

        timerViewModel.resetTimer()
        mBinding.ivStartStop
            .setImageResource(R.drawable.ic_baseline_play_arrow_white_70)
        mBinding.tvStopwatch.text = getString(R.string.initialStateOfTimer)
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        timerViewModel.stopTimer()
//    }
}