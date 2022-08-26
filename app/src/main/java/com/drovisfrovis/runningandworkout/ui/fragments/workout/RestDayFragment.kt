package com.drovisfrovis.runningandworkout.ui.fragments.workout

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.drovisfrovis.runningandworkout.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_rest_day.*

class RestDayFragment: Fragment(R.layout.fragment_rest_day) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivBack.setOnClickListener {
            navHostFragment.findNavController().navigate(R.id.action_restDayFragment_to_plansFragment)
        }

        btnFinished.setOnClickListener {
            navHostFragment.findNavController().navigate(R.id.action_restDayFragment_to_plansFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

}