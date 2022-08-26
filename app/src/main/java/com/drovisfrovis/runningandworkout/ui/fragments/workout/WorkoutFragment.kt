package com.drovisfrovis.runningandworkout.ui.fragments.workout

import android.content.DialogInterface
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
import com.drovisfrovis.runningandworkout.misc.WorkoutUtility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_workout.*
import java.io.Serializable

class WorkoutFragment : Fragment(R.layout.fragment_workout) {

    var isTimerRunning: Boolean = false
    private var startingCountDownTime: Long = 0L
    lateinit var countDownTimer: CountDownTimer
    private var totalTimeForAllExercise: Long = 0   //total time to complete all exercises by user
    private var pauseTime = 0L
    var timeWhenExerciseStart = 0L
    var remainingTime = 0L
    lateinit var exercises: List<Exercise>
    var planKey = 0
    private var totalExercises = 0

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
                pauseTimer()
                pauseTime = System.currentTimeMillis()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callBack)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exercises = arguments?.getSerializable("exercises") as List<Exercise>
        planKey = arguments?.getInt("planKey") ?: -1
        totalExercises = arguments?.getInt("totalExercises") ?: 1
        totalTimeForAllExercise = arguments?.getLong("totalTime") ?: 0L
        timeWhenExerciseStart = System.currentTimeMillis()

        ivBack.setOnClickListener {
            val dialog = getDialog()
            dialog.show()
            pauseTimer()
            pauseTime = System.currentTimeMillis()
        }

        pbTotalWorkoutsLength.max = totalExercises
        pbTotalWorkoutsLength.progress = totalExercises - (exercises.size-1)

        if(exercises.isNotEmpty()) {
            tvExName.text = exercises[0].name
            Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), WorkoutUtility.getExerciseById(exercises[0].exerciseId))).into(gifName)

            ivNext.setOnClickListener {
                navigateToNextScreen(exercises.toMutableList(), planKey, timeWhenExerciseStart)
            }

            if(exercises[0].totalDuration != 0L) {  //timer
                ivPause.setOnClickListener {
                    pauseTimer()
                    pauseTime = System.currentTimeMillis()
                    val exerciseInfoDialog = ExerciseInfoDialogFragment(exercises[0]).apply { setOnDismissListener { resumeExercise() } }
                    exerciseInfoDialog.isCancelable = false
                    exerciseInfoDialog.show(requireActivity().supportFragmentManager, "bottomSheetFragment")
                }

                startingCountDownTime = exercises[0].totalDuration
                remainingTime = startingCountDownTime
                pbCurWorkoutLength.max = (startingCountDownTime / 1000).toInt()

                startTimer()

            }
            else {  //Reps
                pbCurWorkoutLength.visibility = View.INVISIBLE
                ivPause.setImageResource(R.drawable.ic_check_complete)
                tvTotalTime.visibility = View.INVISIBLE
                tvCountdownTimer.text = "X${exercises[0].reps}"

                ivPause.setOnClickListener {
                    navigateToNextScreen(exercises.toMutableList(), planKey, timeWhenExerciseStart)
                }

            }

            tvExName.text = exercises[0].name
            Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), WorkoutUtility.getExerciseById(exercises[0].exerciseId))).into(gifName)

        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(remainingTime, 1000){
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                pbCurWorkoutLength.progress = ((startingCountDownTime - remainingTime)/1000L).toInt()
                isTimerRunning = true
                if(remainingTime > 60000L)
                    tvCountdownTimer.text = "${WorkoutUtility.getFormattedCountDownTime(remainingTime, true)}\""
                else
                    tvCountdownTimer.text = "${WorkoutUtility.getFormattedCountDownTime(remainingTime, false)}\""
            }

            override fun onFinish() {
                navigateToNextScreen(exercises.toMutableList(), planKey, timeWhenExerciseStart)
            }
        }.start()
    }

    fun pauseTimer() {
        isTimerRunning = false
        countDownTimer.cancel()
    }

    fun navigateToNextScreen(exercises: MutableList<Exercise>, planKey: Int, timeWhenExerciseStart: Long) {
        val exerciseTime = System.currentTimeMillis() - timeWhenExerciseStart
        totalTimeForAllExercise += exerciseTime
        if(isTimerRunning) {
            countDownTimer.cancel()
            isTimerRunning = false
        }
        exercises.removeAt(0)
        exercises.toList()
        val bundle = Bundle()
        bundle.putSerializable("exercises", exercises as Serializable)
        bundle.putInt("planKey", planKey)
        bundle.putInt("totalExercises", totalExercises)
        bundle.putLong("totalTime", totalTimeForAllExercise)
        if(exercises.isEmpty()) {
            navHostFragment.findNavController().navigate(R.id.action_workoutFragment_to_resultFragment, bundle)
        }
        else
            navHostFragment.findNavController().navigate(R.id.action_workoutFragment_to_restFragment, bundle)
    }

    fun resumeExercise() {
        pauseTime = System.currentTimeMillis() - pauseTime
        timeWhenExerciseStart += pauseTime
        startTimer()
    }

    fun getDialog() = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        .setTitle("Quit the Exercise?")
        .setMessage("Are you sure to quit the exercise")
        .setIcon(R.drawable.ic_cancel)
        .setCancelable(false)
        .setPositiveButton("Yes") { _, _ ->
            findNavController().navigate(R.id.action_workoutFragment_to_plansFragment)
        }
        .setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.cancel()
            resumeExercise()
        }
        .create()

}