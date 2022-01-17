package com.berni.timetrackerapp.ui.fragments.overview

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.FragmentOverviewDetailBinding
import com.google.android.material.transition.MaterialContainerTransform

class OverviewDetailFragment : Fragment(R.layout.fragment_overview_detail) {

    private var _mBinding: FragmentOverviewDetailBinding? = null
    private val mBinding get() = _mBinding!!

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
    }

}