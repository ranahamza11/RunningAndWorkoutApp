package com.drovisfrovis.runningandworkout.ui.fragments.workout

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.os.TokenWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.db.entities.Exercise
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_COUNTDOWN_TIME
import com.drovisfrovis.runningandworkout.misc.WorkoutUtility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_starting_workout_screen.*
import java.io.Serializable
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class StartingWorkoutScreenFragment : Fragment(R.layout.fragment_starting_workout_screen) {

    var isTimerRunning: Boolean = false

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    var startingCountDownTime = 15000L

    lateinit var countDownTimer: CountDownTimer

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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exercises: List<Exercise> = arguments?.getSerializable("exercises") as List<Exercise>
        val planKey: Int = arguments?.getInt("planKey") ?: -1

        ivBack.setOnClickListener {
            if(isTimerRunning) {
                countDownTimer.cancel()
                val dialog = getDialog()
                dialog.show()
            }
        }

        tvExName.text = exercises[0].name
        Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), WorkoutUtility.getExerciseById(exercises[0].exerciseId))).into(gifName)

        startingCountDownTime = sharedPreferences.getLong(KEY_COUNTDOWN_TIME, 10000L)
        var remainingTime = startingCountDownTime
        pbCountDownTime.max = (startingCountDownTime / 1000).toInt()

        ivNext.setOnClickListener {
            navigateToNextScreen(exercises, planKey)
        }

        countDownTimer = object : CountDownTimer(remainingTime, 1000){
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                pbCountDownTime.progress = (remainingTime/1000L).toInt()
                isTimerRunning = true
                if(remainingTime > 60000L)
                    tvCountdown.text = WorkoutUtility.getFormattedCountDownTime(remainingTime, true)
                else
                    tvCountdown.text = WorkoutUtility.getFormattedCountDownTime(remainingTime, false)
            }

            override fun onFinish() {
                navigateToNextScreen(exercises, planKey)
            }

        }.start()


    }

    fun navigateToNextScreen(exercises: List<Exercise>, planKey: Int) {
        val bundle = Bundle()
        bundle.putSerializable("exercises", exercises as Serializable)
        bundle.putInt("planKey", planKey)
        bundle.putInt("totalExercises", exercises.size)
        bundle.putLong("totalTime", 0L)
        navHostFragment.findNavController().navigate(R.id.action_startingWorkoutScreenFragment_to_workoutFragment, bundle)
    }

    fun getDialog() = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        .setTitle("Quit the Exercise?")
        .setMessage("Are you sure to quit the exercise")
        .setIcon(R.drawable.ic_cancel)
        .setCancelable(false)
        .setPositiveButton("Yes") { _, _ ->
            findNavController().navigate(R.id.action_startingWorkoutScreenFragment_to_plansFragment)
            childFragmentManager.popBackStack()
        }
        .setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.cancel()
        }
        .create()

    override fun onStop() {
        super.onStop()
        if(isTimerRunning) {
            isTimerRunning = false
            countDownTimer.cancel()
        }
    }
}