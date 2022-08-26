package com.drovisfrovis.runningandworkout.ui.fragments.workout

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.db.entities.Exercise
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_COUNTDOWN_TIME
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_TRAINING_REST
import com.drovisfrovis.runningandworkout.misc.WorkoutUtility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_rest.*
import java.io.Serializable
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class RestFragment : Fragment(R.layout.fragment_rest) {

    var isTimerRunning: Boolean = false

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    var startingCountDownTime = 20000L

    lateinit var countDownTimer: CountDownTimer
    var totalTimeForAllExercise = 0L
    var totalExercises = 0

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
                val dialog = getDialog()
                dialog.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callBack)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exercises = arguments?.getSerializable("exercises") as List<Exercise>
        val planKey = arguments?.getInt("planKey") ?: -1
        totalExercises = arguments?.getInt("totalExercises") ?: 1
        totalTimeForAllExercise = arguments?.getLong("totalTime") ?: 0L
        val timeWhenExerciseStart = System.currentTimeMillis()

        btnSkip.setOnClickListener {
            navigateToNextScreen(exercises, planKey, timeWhenExerciseStart)
        }

        tvNextExName.text = exercises[0].name
        Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), WorkoutUtility.getExerciseById(exercises[0].exerciseId))).into(gifName)

        tvRemainingEx.text = "Next ${totalExercises - (exercises.size-1)}/$totalExercises"

        if(exercises[0].totalDuration != 0L) {
            tvExDuration.text = WorkoutUtility.getFormattedCountDownTime(exercises[0].totalDuration, true)
        }
        else {
            tvExDuration.text = "X${exercises[0].reps}"
        }

        startingCountDownTime = sharedPreferences.getLong(KEY_TRAINING_REST, 10000L)

        var remainingTime = startingCountDownTime
        pbRest.max = (startingCountDownTime / 1000).toInt()

        countDownTimer = object : CountDownTimer(remainingTime, 1000){
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                pbRest.progress = ((startingCountDownTime - remainingTime)/1000L).toInt()
                isTimerRunning = true
                if(remainingTime > 60000L)
                    tvRestCountDown.text = WorkoutUtility.getFormattedCountDownTime(remainingTime, true)
                else
                    tvRestCountDown.text = WorkoutUtility.getFormattedCountDownTime(remainingTime, false)
            }

            override fun onFinish() {
                navigateToNextScreen(exercises, planKey, timeWhenExerciseStart)
            }

        }.start()
    }

    fun navigateToNextScreen(exercises: List<Exercise>, planKey: Int, timeWhenExerciseStart: Long) {
        val exerciseTime = System.currentTimeMillis() - timeWhenExerciseStart
        totalTimeForAllExercise += exerciseTime
        if(isTimerRunning) {
            countDownTimer.cancel()
            isTimerRunning = false
        }
        val bundle = Bundle()
        bundle.putSerializable("exercises", exercises as Serializable)
        bundle.putInt("planKey", planKey)
        bundle.putInt("totalExercises", totalExercises)
        bundle.putLong("totalTime", totalTimeForAllExercise)
        navHostFragment.findNavController().navigate(R.id.action_restFragment_to_workoutFragment, bundle)
    }

    fun getDialog() = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        .setTitle("Quit the Exercise?")
        .setMessage("Are you sure to quit the exercise")
        .setIcon(R.drawable.ic_cancel)
        .setPositiveButton("Yes") { _, _ ->
            findNavController().navigate(R.id.action_resultFragment_to_plansFragment)
        }
        .setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.cancel()
        }
        .create()
}