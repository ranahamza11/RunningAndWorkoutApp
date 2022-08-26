package com.drovisfrovis.runningandworkout.ui.fragments.workout

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.db.WorkoutDao
import com.drovisfrovis.runningandworkout.db.entities.Exercise
import com.drovisfrovis.runningandworkout.db.entities.PlanDetails
import com.drovisfrovis.runningandworkout.misc.Constants
import com.drovisfrovis.runningandworkout.misc.WorkoutUtility
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.coroutines.launch
import java.nio.channels.CancelledKeyException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.min
import kotlin.math.round

@AndroidEntryPoint
class ResultFragment: Fragment(R.layout.fragment_result) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    @Named("workoutDao")
    lateinit var workoutDao: WorkoutDao

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_resultFragment_to_historyFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callBack)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exercises = arguments?.getSerializable("exercises") as List<Exercise>
        val planKey = arguments?.getInt("planKey") ?: -1
        val totalExercises = arguments?.getInt("totalExercises") ?: 1
        val totalTimeForAllExercise = arguments?.getLong("totalTime") ?: 0L

        val minutes: Float = totalTimeForAllExercise / 1000F / 60F
        val weight = sharedPreferences.getFloat(Constants.KEY_WEIGHT, 80F)

        val calories = round(((minutes * (7.0 * 3.5 * weight)) /200F) * 100)/100
        tvCalories.text = "$calories"
        tvDuration.text = WorkoutUtility.getFormattedCountDownTime(totalTimeForAllExercise, true)
        tvExercises.text = totalExercises.toString()

        tvPlanName.text = "Day $planKey"
        val sdf = SimpleDateFormat("MMM d h:mma", Locale.getDefault())
        val dateAndTimeStamp = sdf.format(Calendar.getInstance().time)
        val planDetails = PlanDetails(totalTimeForAllExercise, System.currentTimeMillis(), dateAndTimeStamp, calories.toFloat(), planKey)

        lifecycleScope.launch {
            workoutDao.insertPlanDetails(planDetails)
            Snackbar.make(requireView(), "Workout Successful", Snackbar.LENGTH_SHORT).show()

            btnFinished.setOnClickListener {
                findNavController().navigate(R.id.action_resultFragment_to_historyFragment)
            }
        }
    }
}