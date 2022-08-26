package com.drovisfrovis.runningandworkout.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_COUNTDOWN_TIME
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_NAME
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_TRAINING_REST
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment: Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFieldsFromSharedPref()

        btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPref()

            if(success) {
                Snackbar.make(view, "Saved Changes!", Snackbar.LENGTH_LONG).show()
            }
            else {
                Snackbar.make(view, "All fields are required", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun loadFieldsFromSharedPref() {
        val name = sharedPreferences.getString(KEY_NAME, "")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT, 80f)
        val countdownTime = sharedPreferences.getLong(KEY_COUNTDOWN_TIME, 10000L) / 1000
        val trainingRest = sharedPreferences.getLong(KEY_TRAINING_REST, 10000L) / 1000

        etName.setText(name)
        etWeight.setText(weight.toString())
        etCountDownTime.setText(countdownTime.toString())
        etTrainingRest.setText(trainingRest.toString())

    }

    private fun applyChangesToSharedPref(): Boolean {
        val name = etName.text.toString()
        val weight = etWeight.text.toString()
        val countdownTime = etCountDownTime.text.toString()
        val trainingRest = etTrainingRest.text.toString()
        if(name.isEmpty() || weight.isEmpty() || countdownTime.isEmpty() || trainingRest.isEmpty()) {
            return false
        }

        sharedPreferences.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putLong(KEY_COUNTDOWN_TIME, (countdownTime.toLong() * 1000))
            .putLong(KEY_TRAINING_REST, (trainingRest.toLong() * 1000))
            .apply()

        val toolbarText = "Let's Go $name"
        requireActivity().tvToolbarTitle.text = toolbarText

        return true
    }

}