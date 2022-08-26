package com.drovisfrovis.runningandworkout.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.db.WorkoutDao
import com.drovisfrovis.runningandworkout.db.entities.Exercise
import com.drovisfrovis.runningandworkout.db.entities.PreBuildPlans
import com.drovisfrovis.runningandworkout.db.relations.PreBuildPlansExerciseCrossRef
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_COUNTDOWN_TIME
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_FIRST_TIME_TOGGLE
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_NAME
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_TRAINING_REST
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setup.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class SetupFragment: Fragment(R.layout.fragment_setup) {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true

    @Inject
    @Named("workoutDao")
    lateinit var workoutDao: WorkoutDao

    lateinit var exercises: List<Exercise>
    lateinit var preBuildPlanExerciseRelation: List<PreBuildPlansExerciseCrossRef>
    lateinit var preBuildPlans: List<PreBuildPlans>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress_circular.visibility = View.INVISIBLE
        if(!isFirstAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(R.id.action_setupFragment_to_runFragment, savedInstanceState, navOptions)
        }
        tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if(success) {
                lifecycleScope.launch {
                    progress_circular.visibility = View.VISIBLE
                    addData()
                    exercises.forEach{ workoutDao.insertExercise(it) }
                    preBuildPlans.forEach { workoutDao.insertPreBuildPlans(it) }
                    preBuildPlanExerciseRelation.forEach { workoutDao.insertPreBuildPlansExerciseCrossRef(it) }
                    navHostFragment.findNavController().navigate(R.id.action_setupFragment_to_runFragment)
                }

            } else {
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun addData() {
        exercises = listOf(
            Exercise("STRAIGHT-ARM PLANK", 20000L, 0,
                "Start in the push-up position, but keep your arms straight. This exercise strengthens your abdomen and back muscles."),
            Exercise("STANDING BICYCLE CRUNCHES", 0L, 10,
                "Stand with your feet shoulder width apart. Bring your knee to your opposite elbow. \n" +
                        "\n" +
                        "Return to the start position and repeat with the other side."),
            Exercise("HIGH STEPPING", 30000L, 0,
                "Run in place while pulling your knees as high as possible with each step.\n" +
                        "\n" +
                        "Keep your upper body upright during this exercise."),
            Exercise("JUMPING JACKS", 30000L, 0,
                "Start with your feet together and your arms by your sides, then jump up with your feet apart and your hands overhead.\n" +
                        "\n" +
                        "Return to the start position then do the next rep. This exercise provides a full-body workout and works all your large muscle groups."),
            Exercise("SKIPPING WITHOUT ROPE", 20000L, 0,
                "Place your arms at your sides and pretend to hold a skipping rope handle in each hand.\n" +
                        "\n" +
                        "Jump and alternately land on the balls of your feet, rotating your writs at the same time as if you were spinning a rope."),
            Exercise("MOUNTAIN CLIMBER", 30000L, 0,
                "Start in the push-up position. Bend your right knee towards your chest and keep your left leg straight, then quickly switch from one leg to other.\n" +
                        "\n" +
                        "This exercise strengthens multiple muscle groups."),
            Exercise("TOY SOLDIERS", 0L, 12,
                "Start with your feet shoulder width apart. Keep your legs and arms straight.\n" +
                        "\n" +
                        "Kick your left leg up to touch your right hand to your toes. Repeat with the other side."),
            Exercise("BACKWARD LUNGE", 0L, 5,
                "Stand with your feet shoulder width apart and your hands on your hips.\n" +
                        "\n" +
                        "Step a big step backward with your right leg and lower your body until your left thigh is parallel to the floor. Return and repeat with the other side."),
            Exercise("KNEE TO ELBOW CRUNCHES", 0L, 5,
                "Stand with your feet shoulder width apart and your hands behind your ears.\n" +
                        "\n" +
                        "Raise your right knee, and use your left elbow to touch your right knee. Change sides and repeat."),
            Exercise("IN & OUTS", 20000L, 0,
                "Lay prone on the ground with arms supporting your body. Hop forward, bring your knees toward your chest, then hop back to the starting position. Repeat the exercise."),
            Exercise("SQUAT REACH UPS", 20000L, 0,
                "Stand with your feet a little wider than shoulder width apart, elbows bent, palms facing each other and fingers pointed toward the ceiling.\n" +
                        "\n" +
                        "Lower your body until your thighs are parallel to the floor. When you come up, turn your body to the left, straighten your right leg, lift your right heel and stretch and reach your right arm to the left.\n" +
                        "\n" +
                        "Return to the start position, and repeat with the other side."),
            Exercise("BUTT BRIDGE", 0L, 15, "Lie on your back with knees bent and feet flat on the floor. Put your arms flat at your sides.\n" +
                    "\n" +
                    "Then lift your butt up and down."),
            Exercise("GLUTE KICK BACK", 0L, 5,"Start on all fours with your knees under your butt and your hands directly under your shoulders.\n" +
                    "\n" +
                    "Then kick one leg back until it is parallel with the ground. Switch to the other side after several sets."),
            Exercise("FROG PRESS", 0L, 10,"Lie on your back with your legs lifted, knees bent and heels squeezed together. Put your arms flat at your sides.\n" +
                    "\n" +
                    "Straighten your legs and bring them back to the starting position. Repeat the exercise."),
            Exercise("STRAIGHT LEG RAISE LEFT", 20000L, 0,"Lie flat down on your back with your left leg straight on the ground, and your right leg bent with your right foot flat on the ground.\n" +
                    "\n" +
                    "Tighten your left leg and lift it straight up until your thighs are parallel, then slowly lower it down."),
            Exercise("STRAIGHT LEG RAISE RIGHT", 20000L, 0,"Lie flat down on your back with your right leg straight on the ground, and your left leg bent with your left foot flat on the ground.\n" +
                    "\n" +
                    "Tighten your right leg and lift it straight up until your thighs are parallel, then slowly lower it down."),
            Exercise("FLUTTER KICKS", 20000L, 0,"Lie on your back with your arms at your sides. Lift your legs and keep them as straight as you can.\n" +
                    "\n" +
                    "Then quickly raise your right leg up, and simultaneously lower your left leg. Switch legs and repeat."),
            Exercise("SQUATS", 0L, 10,"Stand with your feet shoulder width apart and your arms stretched forward, then lower your body until your thighs are parallel with the floor.\n" +
                    "\n" +
                    "Your knees should be extended in the same direction as your toes. Return to the start position and do the next rep.\n" +
                    "\n" +
                    "This works the thighs, hips buttocks, quads, hamstrings and lower body."),
            Exercise("REVERSE CRUNCHES", 0L, 10,"Lie on your back with your knees up at a 90-degree angle and your hands behind your head.\n" +
                    "\n" +
                    "Lift your upper body and thighs, and then stretch out. Repeat the exercise."),
            Exercise("STEP-UP ONTO CHAIR", 0L, 5,"Stand in front of a chair. Then step up on the chair and step back down.\n" +
                    "\n" +
                    "The exercise works to strengthen the legs and buttocks."),
            Exercise("TRICEPS DIPS", 0, 10,"For the start position, sit on the chair. Then move your hip off the chair with your hands holding the edge of the chair.\n" +
                    "\n" +
                    "Slowly bend and stretch your arms to make your body go up and down. This is a great exercise for the triceps."),
            Exercise("PLANK", 20000L, 0,"Lie on the floor with your toes and forearms on the ground. Keep your body straight and hold this position as long as you can.\n" +
                    "\n" +
                    "This exercise strengthens the abdomen, back and shoulders."),
            Exercise("BICYCLE CRUNCHES", 0L, 5,"Lie on the floor with your hands behind your ears. Raise your knees and close your right elbow toward your left knee, then close your left elbow towards your right knee. Repeat the exercise."),
            Exercise("LUNGES", 0L, 9,"Stand with your feet shoulder width apart and your hands on your hips.\n" +
                    "\n" +
                    "Take a step forward with your right leg and lower your body until your right thigh is parallel with the floor. Then return and switch to the other leg. This exercise strengthens the quadriceps, gluteus maximus and hamstrings. \n"),
            Exercise("INCLINE PUSH-UPS", 0L, 10,"Start in the regular push-up position but with your hands elevated on a chair or bench.\n" +
                    "\n" +
                    "Then push your body up down using your arm strength.\n" +
                    "\n" +
                    "Remember to keep your body straight."),
            Exercise("RUSSIAN TWIST", 0L, 10,"Sit on the floor with your knees bent, feet lifted a little bit and back tilted backwards.\n" +
                    "\n" +
                    "Then hold your hands together and twist from side to side."),
            Exercise("COBRA STRETCH", 30000L, 0,"Lie down your stomach and bend your elbows with your hands beneath your shoulders.\n" +
                    "\n" +
                    "Then push your chest up off the ground as far as possible. Hold this position for seconds."),
            Exercise("BURPEES", 0L, 10,"Stand with your feet shoulder width apart, then put your hands on the ground and kick your feet backward. Do a quick push-up and then jump up."),
            Exercise("ABDOMINAL CRUNCHES", 0L, 10,"Lie on your back with your knees bent and your arms stretched forward. \n" +
                    "\n" +
                    "Then lift your upper body off the floor. Hold for a few seconds and slowly return.\n" +
                    "\n" +
                    "It primarily works the rectus abdominis muscle and the obliques."),
            Exercise("LEG RAISES", 0L, 10,"Lie down on your back, and put your hands beneath your hips for support.\n" +
                    "\n" +
                    "Then lift your legs up until they form a right angle with the floor.\n" +
                    "\n" +
                    "Slowly bring your legs back down and repeat the exercise."),
            Exercise("SCISSORS", 0L, 20,"Lie on your back with arms at sides, palms down and legs extended.\n" +
                    "\n" +
                    "Lift your legs and cross the right thigh over the left, then reverse the exercise.\n" +
                    "\n" +
                    "Make sure your back is always flat.\n" +
                    "\n" +
                    "It’s a great exercise for the obliques and rectus abdominis."),
            Exercise("INCHWORMS", 0L, 10,"Start with your feet shoulder width apart.\n" +
                    "\n" +
                    "Bend your body and walk your hands in front of you as far as you can, then walk your hands back. Repeat the exercise.\n"),
            Exercise("HEEL TOUCH", 0L, 5,"Lie on the ground with your legs bent and your arms by your sides.\n" +
                    "\n" +
                    "Slightly lift your upper body off the floor and make your hands alternately reach your heels."),
            Exercise("RECLINED OBLIQUE TWIST", 0L, 5,"Lie on your back with your legs extended and your elbows directly under your shoulders.\n" +
                    "\n" +
                    "Lift your left leg up at a 45-degree angle while reaching your right arm over to your left side.\n" +
                    "\n" +
                    "Then slowly go back to the starting position. Repeat several times, and then switch to the other side."),
            Exercise("PLANK JACKS", 20000L, 0,"Start in the straight arm plank position with your hands under your shoulders and feet together.\n" +
                    "\n" +
                    "Hop your feet apart and land on your toes, then hop back. Repeat the exercise."),
            Exercise("V-UP", 0L, 10,"Lie on your back with your arms and legs extended and your legs squeezed together.\n" +
                    "\n" +
                    "Raise your upper body and legs, use your arms to touch your toes, then go back to the start position and repeat the exercise."),
            Exercise("CRUNCHES WITH LEGS RAISED", 0L, 10,"Lie on your back with your legs straight up towards the ceiling and your hands behind your ears.\n" +
                    "\n" +
                    "Lift your shoulders up off the floor, then slowly go back to the starting position. Repeat the exercise."),
            Exercise("SQUAT PULSES", 20000L, 0,"Stand with your feet shoulder width apart. Bend your arms in front of you to keep balance.\n" +
                    "\n" +
                    "Lower your body until your thighs are parallel with the floor Come half way up instead of coming all the way up, then squat down again. Repeat."),
            Exercise("LATERAL PLANK WALK", 20000L, 0,"Start in a straight arm plank position with your hands underneath your shoulders.\n" +
                    "\n" +
                    "Walk your hands and feet to the left at the same time. Take a few steps to the left, then a few steps to the right.  Repeat the exercise."),
            Exercise("ROUNDHOUSE SQUAT KICKS", 0L, 5,"Stand with feet shoulder width apart. Put your arms in front of your chest to keep your balance.\n" +
                    "\n" +
                    "Then do a squat, and when you come up, kick your left leg to the side. Repeat the exercise and then switch to the right leg.\n" +
                    "\n" +
                    "Inhale when you go down, and exhale when you go up. Your knees should be extended in line with your toes.\n" +
                    "\n" +
                    "It’s a great exercise for the quadriceps and gluteus."),
            Exercise("PUSH-UPS", 0L, 20,"Lay prone on the ground with arms supporting your body.\n" +
                    "\n" +
                    "Keep your body straight while raising and lowering your body with your arms.\n" +
                    "\n" +
                    "This exercise works the chest, shoulders, triceps, back and legs."),
            Exercise("LONG ARM CRUNCHES", 0L, 16,"Lie on your back with knees bent and feet flat on the floor. Put your arms straight over the top of your head.\n" +
                    "\n" +
                    "Lift your upper body off the floor, then slowly go back to the start position. The exercise increases abdominal endurance."),
            Exercise("DECLINE PUSH-UPS", 0L, 10,"Start on all fours with your knees under your butt and your hands under your shoulders.\n" +
                    "\n" +
                    "Then elevate your feet on a chair or bench, and push your body up and down mainly using your arm strength.\n" +
                    "\n" +
                    "Remember to keep your body straight.")
        )

        preBuildPlans  = listOf(
            PreBuildPlans(540000L),
            PreBuildPlans(540000L),
            PreBuildPlans(540000L),
            PreBuildPlans(0L),
            PreBuildPlans(540000L),
            PreBuildPlans(540000L),
            PreBuildPlans(600000L),
            PreBuildPlans(0L),
            PreBuildPlans(660000L),
            PreBuildPlans(720000L),
            PreBuildPlans(660000L),
            PreBuildPlans(0L),
            PreBuildPlans(660000L),
            PreBuildPlans(720000L),
            PreBuildPlans(720000L),
            PreBuildPlans(0L),
            PreBuildPlans(840000L),
            PreBuildPlans(780000L),
            PreBuildPlans(780000L),
            PreBuildPlans(0L),
            PreBuildPlans(780000L),
            PreBuildPlans(840000L),
            PreBuildPlans(900000L),
            PreBuildPlans(0L),
            PreBuildPlans(840000L),
            PreBuildPlans(900000L),
            PreBuildPlans(840000L),
            PreBuildPlans(0L),
            PreBuildPlans(840000L),
            PreBuildPlans(900000L),
        )

        preBuildPlanExerciseRelation = listOf(
            PreBuildPlansExerciseCrossRef(1, 6),
            PreBuildPlansExerciseCrossRef(1, 18),
            PreBuildPlansExerciseCrossRef(1, 2),
            PreBuildPlansExerciseCrossRef(1, 41),
            PreBuildPlansExerciseCrossRef(1, 19),

            PreBuildPlansExerciseCrossRef(2, 6),
            PreBuildPlansExerciseCrossRef(2, 21),
            PreBuildPlansExerciseCrossRef(2, 11),
            PreBuildPlansExerciseCrossRef(2, 42),
            PreBuildPlansExerciseCrossRef(2, 23),

            PreBuildPlansExerciseCrossRef(3, 6),
            PreBuildPlansExerciseCrossRef(3, 11),
            PreBuildPlansExerciseCrossRef(3, 29),
            PreBuildPlansExerciseCrossRef(3, 33),
            PreBuildPlansExerciseCrossRef(3, 17),

            PreBuildPlansExerciseCrossRef(5, 40),
            PreBuildPlansExerciseCrossRef(5, 24),
            PreBuildPlansExerciseCrossRef(5, 41),
            PreBuildPlansExerciseCrossRef(5, 19),
            PreBuildPlansExerciseCrossRef(5, 33),

            PreBuildPlansExerciseCrossRef(6, 40),
            PreBuildPlansExerciseCrossRef(6, 21),
            PreBuildPlansExerciseCrossRef(6, 2),
            PreBuildPlansExerciseCrossRef(6, 38),
            PreBuildPlansExerciseCrossRef(6, 23),

            PreBuildPlansExerciseCrossRef(7, 40),
            PreBuildPlansExerciseCrossRef(7, 21),
            PreBuildPlansExerciseCrossRef(7, 24),
            PreBuildPlansExerciseCrossRef(7, 42),
            PreBuildPlansExerciseCrossRef(7, 23),

            PreBuildPlansExerciseCrossRef(9, 6),
            PreBuildPlansExerciseCrossRef(9, 18),
            PreBuildPlansExerciseCrossRef(9, 31),
            PreBuildPlansExerciseCrossRef(9, 41),
            PreBuildPlansExerciseCrossRef(9, 30),
            PreBuildPlansExerciseCrossRef(9, 33),

            PreBuildPlansExerciseCrossRef(10, 6),
            PreBuildPlansExerciseCrossRef(10, 24),
            PreBuildPlansExerciseCrossRef(10, 31),
            PreBuildPlansExerciseCrossRef(10, 42),
            PreBuildPlansExerciseCrossRef(10, 29),
            PreBuildPlansExerciseCrossRef(10, 23),

            PreBuildPlansExerciseCrossRef(11, 6),
            PreBuildPlansExerciseCrossRef(11, 18),
            PreBuildPlansExerciseCrossRef(11, 39),
            PreBuildPlansExerciseCrossRef(11, 41),
            PreBuildPlansExerciseCrossRef(11, 23),
            PreBuildPlansExerciseCrossRef(11, 33),

            PreBuildPlansExerciseCrossRef(13, 40),
            PreBuildPlansExerciseCrossRef(13, 21),
            PreBuildPlansExerciseCrossRef(13, 2),
            PreBuildPlansExerciseCrossRef(13, 39),
            PreBuildPlansExerciseCrossRef(13, 42),
            PreBuildPlansExerciseCrossRef(13, 29),

            PreBuildPlansExerciseCrossRef(14, 40),
            PreBuildPlansExerciseCrossRef(14, 11),
            PreBuildPlansExerciseCrossRef(14, 20),
            PreBuildPlansExerciseCrossRef(14, 30),
            PreBuildPlansExerciseCrossRef(14, 12),
            PreBuildPlansExerciseCrossRef(14, 23),

            PreBuildPlansExerciseCrossRef(15, 40),
            PreBuildPlansExerciseCrossRef(15, 20),
            PreBuildPlansExerciseCrossRef(15, 41),
            PreBuildPlansExerciseCrossRef(15, 30),
            PreBuildPlansExerciseCrossRef(15, 33),
            PreBuildPlansExerciseCrossRef(15, 36),

            PreBuildPlansExerciseCrossRef(17, 6),
            PreBuildPlansExerciseCrossRef(17, 24),
            PreBuildPlansExerciseCrossRef(17, 31),
            PreBuildPlansExerciseCrossRef(17, 29),
            PreBuildPlansExerciseCrossRef(17, 19),
            PreBuildPlansExerciseCrossRef(17, 37),
            PreBuildPlansExerciseCrossRef(17, 36),

            PreBuildPlansExerciseCrossRef(18, 6),
            PreBuildPlansExerciseCrossRef(18, 18),
            PreBuildPlansExerciseCrossRef(18, 32),
            PreBuildPlansExerciseCrossRef(18, 41),
            PreBuildPlansExerciseCrossRef(18, 19),
            PreBuildPlansExerciseCrossRef(18, 33),
            PreBuildPlansExerciseCrossRef(18, 17),

            PreBuildPlansExerciseCrossRef(19, 6),
            PreBuildPlansExerciseCrossRef(19, 2),
            PreBuildPlansExerciseCrossRef(19, 39),
            PreBuildPlansExerciseCrossRef(19, 38),
            PreBuildPlansExerciseCrossRef(19, 29),
            PreBuildPlansExerciseCrossRef(19, 37),
            PreBuildPlansExerciseCrossRef(19, 36),

            PreBuildPlansExerciseCrossRef(21, 40),
            PreBuildPlansExerciseCrossRef(21, 21),
            PreBuildPlansExerciseCrossRef(21, 32),
            PreBuildPlansExerciseCrossRef(21, 42),
            PreBuildPlansExerciseCrossRef(21, 12),
            PreBuildPlansExerciseCrossRef(21, 17),
            PreBuildPlansExerciseCrossRef(21, 36),

            PreBuildPlansExerciseCrossRef(22, 40),
            PreBuildPlansExerciseCrossRef(22, 18),
            PreBuildPlansExerciseCrossRef(22, 21),
            PreBuildPlansExerciseCrossRef(22, 31),
            PreBuildPlansExerciseCrossRef(22, 30),
            PreBuildPlansExerciseCrossRef(22, 33),
            PreBuildPlansExerciseCrossRef(22, 17),

            PreBuildPlansExerciseCrossRef(23, 40),
            PreBuildPlansExerciseCrossRef(23, 24),
            PreBuildPlansExerciseCrossRef(23, 31),
            PreBuildPlansExerciseCrossRef(23, 29),
            PreBuildPlansExerciseCrossRef(23, 23),
            PreBuildPlansExerciseCrossRef(23, 19),
            PreBuildPlansExerciseCrossRef(23, 37),

            PreBuildPlansExerciseCrossRef(25, 6),
            PreBuildPlansExerciseCrossRef(25, 21),
            PreBuildPlansExerciseCrossRef(25, 2),
            PreBuildPlansExerciseCrossRef(25, 42),
            PreBuildPlansExerciseCrossRef(25, 17),
            PreBuildPlansExerciseCrossRef(25, 37),
            PreBuildPlansExerciseCrossRef(25, 36),

            PreBuildPlansExerciseCrossRef(26, 6),
            PreBuildPlansExerciseCrossRef(26, 24),
            PreBuildPlansExerciseCrossRef(26, 11),
            PreBuildPlansExerciseCrossRef(26, 20),
            PreBuildPlansExerciseCrossRef(26, 41),
            PreBuildPlansExerciseCrossRef(26, 42),
            PreBuildPlansExerciseCrossRef(26, 36),

            PreBuildPlansExerciseCrossRef(27, 6),
            PreBuildPlansExerciseCrossRef(27, 18),
            PreBuildPlansExerciseCrossRef(27, 31),
            PreBuildPlansExerciseCrossRef(27, 32),
            PreBuildPlansExerciseCrossRef(27, 19),
            PreBuildPlansExerciseCrossRef(27, 33),
            PreBuildPlansExerciseCrossRef(27, 37),

            PreBuildPlansExerciseCrossRef(29, 40),
            PreBuildPlansExerciseCrossRef(29, 18),
            PreBuildPlansExerciseCrossRef(29, 21),
            PreBuildPlansExerciseCrossRef(29, 11),
            PreBuildPlansExerciseCrossRef(29, 38),
            PreBuildPlansExerciseCrossRef(29, 42),
            PreBuildPlansExerciseCrossRef(29, 17),

            PreBuildPlansExerciseCrossRef(30, 40),
            PreBuildPlansExerciseCrossRef(30, 24),
            PreBuildPlansExerciseCrossRef(30, 11),
            PreBuildPlansExerciseCrossRef(30, 39),
            PreBuildPlansExerciseCrossRef(30, 41),
            PreBuildPlansExerciseCrossRef(30, 29),
            PreBuildPlansExerciseCrossRef(30, 33),
        )

    }

    private fun writePersonalDataToSharedPref() : Boolean {
        val name = etName.text.toString()
        val weight = etWeight.text.toString()

        if(name.isEmpty() || weight.isEmpty()) {
            return false
        }

        sharedPreferences.edit().putString(KEY_NAME, name).putFloat(KEY_WEIGHT, weight.toFloat()).putBoolean(
            KEY_FIRST_TIME_TOGGLE, false).putLong(KEY_COUNTDOWN_TIME, 15000L).putLong(
            KEY_TRAINING_REST, 30000L) .apply()

        val toolBarText = "Let's go, $name!"
        requireActivity().tvToolbarTitle.text = toolBarText
        return true

    }
}