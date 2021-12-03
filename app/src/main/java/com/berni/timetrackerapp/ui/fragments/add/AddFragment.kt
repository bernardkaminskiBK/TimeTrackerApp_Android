package com.berni.timetrackerapp.ui.fragments.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.application.TimeTrackerApplication
import com.berni.timetrackerapp.databinding.FragmentAddBinding
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerDBViewModel
import com.berni.timetrackerapp.model.database.viewmodel.TimeTrackerViewModelFactory
import com.berni.timetrackerapp.ui.adapters.TimeTrackerAdapter
import com.google.android.material.snackbar.Snackbar

class AddFragment : Fragment() {

    private lateinit var addViewModel: AddViewModel
    private lateinit var mTimeTrackerAdapter: TimeTrackerAdapter

    private var _mBinding: FragmentAddBinding? = null
    private val mBinding get() = _mBinding!!

    private val mTimeTrackerDBViewModel: TimeTrackerDBViewModel by viewModels {
        TimeTrackerViewModelFactory((requireActivity().application as TimeTrackerApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addViewModel =
            ViewModelProvider(this).get(AddViewModel::class.java)

        _mBinding = FragmentAddBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTimeTrackerDBViewModel.allProgressList.observe(viewLifecycleOwner) {
            it.let {
                mBinding.apply {
                    mTimeTrackerAdapter = TimeTrackerAdapter(this@AddFragment)
                    rvTimeTrackerProgressList.adapter = mTimeTrackerAdapter

                    if (it.isNotEmpty()) {
                        rvTimeTrackerProgressList.visibility = View.VISIBLE
                        tvNoDataAvailable.visibility = View.GONE
                        mTimeTrackerAdapter.show(it)
                    } else {
                        rvTimeTrackerProgressList.visibility = View.GONE
                        tvNoDataAvailable.visibility = View.VISIBLE
                    }

                    onSwipedDelete()
                }
            }
            mTimeTrackerAdapter.submitList(it)
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

                Snackbar.make(requireView(), "Time tracker progress deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                       mTimeTrackerDBViewModel.insert(progress)
                    }
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.gray))
                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.darkSeaBlue))
                    .show()
            }
        }).attachToRecyclerView(mBinding.rvTimeTrackerProgressList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

}