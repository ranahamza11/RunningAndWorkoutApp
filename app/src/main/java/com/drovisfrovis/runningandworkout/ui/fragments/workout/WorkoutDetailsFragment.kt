package com.drovisfrovis.runningandworkout.ui.fragments.workout

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.adapter.WorkoutDetailsAdapter
import com.drovisfrovis.runningandworkout.db.WorkoutDao
import com.drovisfrovis.runningandworkout.db.entities.Exercise
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_workout_details.*
import kotlinx.coroutines.launch
import java.io.Serializable
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class WorkoutDetailsFragment : Fragment(R.layout.fragment_workout_details) {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val key = arguments?.getInt("planKey") ?: -1

        ivBack.setOnClickListener {
            navHostFragment.findNavController().navigate(R.id.action_workoutDetailsFragment_to_plansFragment)
        }

        if(key != -1) {
            lifecycleScope.launch {
                val planWithExercises = workoutDao.getExercisesOfPreBuildPlans(key)
                val exercises: MutableList<Exercise> =  planWithExercises.first().exercises.toMutableList()

                tvPlanName.text = "Day ${planWithExercises.first().prePlan.prePlanId}"
                val size = exercises.size

                var tempExercise: Exercise

                if(exercises[exercises.lastIndex].exerciseId != 6) {
                    tempExercise = workoutDao.getExerciseById(6)
                    exercises.add(tempExercise)
                }

                for(i in 1 until size) {
                    if(i == 1 && exercises[i].exerciseId == 6) {
                        tempExercise = workoutDao.getExerciseById(exercises[0].exerciseId)
                    }
                    else {
                        tempExercise = workoutDao.getExerciseById(exercises[i].exerciseId)
                    }

                    exercises.add(tempExercise)
                }

                tempExercise = workoutDao.getExerciseById(6)
                exercises.add(tempExercise)
                tempExercise = workoutDao.getExerciseById(27)
                exercises.add(tempExercise)

                tvExDurationAndCount.text = "${planWithExercises.first().prePlan.totalDuration / 1000L / 60} MIN - ${exercises.size} EXERCISES"

                listView.isClickable = true
                listView.adapter = WorkoutDetailsAdapter(requireContext(), exercises)

                btnGo.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("exercises", exercises as Serializable)
                    bundle.putInt("planKey", planWithExercises.first().prePlan.prePlanId)
                    navHostFragment.findNavController().navigate(R.id.action_workoutDetailsFragment_to_startingWorkoutScreenFragment, bundle)
                }

                listView.setOnItemClickListener { _, _, position, _ ->
                    val exerciseInfoDialog = ExerciseInfoDialogFragment(exercises[position])
                    exerciseInfoDialog.show(requireActivity().supportFragmentManager, "bottomSheetFragment")
                }

            }
        }




    }
}