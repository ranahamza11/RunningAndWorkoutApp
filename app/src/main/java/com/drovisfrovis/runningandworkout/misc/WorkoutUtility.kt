package com.drovisfrovis.runningandworkout.misc

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.drovisfrovis.runningandworkout.R

object WorkoutUtility {

    fun getExerciseById(id: Int): Int {
        return when(id) {
            1 -> R.drawable.straight_arm_plank
            2 -> R.drawable.standing_bicycle_crunches
            3 -> R.drawable.high_stepping
            4 -> R.drawable.jumping_jacks
            5 -> R.drawable.skipping_without_rope
            6 -> R.drawable.mountain_climber
            7 -> R.drawable.toy_soldiers
            8 -> R.drawable.backward_lunge
            9 -> R.drawable.knee_to_elbow_crunches
            10 -> R.drawable.in_and_outs
            11 -> R.drawable.squat_reach_ups
            12 -> R.drawable.butt_bridge
            13 -> R.drawable.glute_kick_back
            14 -> R.drawable.frog_press
            15 -> R.drawable.straight_leg_raise_left
            16 -> R.drawable.straight_leg_raise_right
            17 -> R.drawable.flutter_kicks
            18 -> R.drawable.squats
            19 -> R.drawable.reverse_crunches
            20 -> R.drawable.step_up_onto_chair
            21 -> R.drawable.triceps_dips
            22 -> R.drawable.plank
            23 -> R.drawable.bicycle_crunches
            24 -> R.drawable.lunges
            25 -> R.drawable.incline_push_ups
            26 -> R.drawable.russian_twist
            27 -> R.drawable.cobra_stretch
            28 -> R.drawable.burpees
            29 -> R.drawable.abdominal_crunches
            30 -> R.drawable.leg_raises
            31 -> R.drawable.scissors
            32 -> R.drawable.inchworms
            33 -> R.drawable.heel_touch
            34 -> R.drawable.reclined_oblique_twist
            35 -> R.drawable.plank_jacks
            36 -> R.drawable.v_ups
            37 -> R.drawable.crunches_with_leg_raised
            38 -> R.drawable.squat_pulses
            39 -> R.drawable.lateral_plank_walk
            40 -> R.drawable.roundhouse_squat_kicks
            41 -> R.drawable.push_ups
            42 -> R.drawable.long_arm_crunches
            43 -> R.drawable.decline_push_ups
            else -> R.drawable.abdominal_crunches
        }

    }

    fun getFormattedCountDownTime(ms: Long, isMinutesInclude: Boolean): String {
        val minutes = ms / 1000 / 60
        val seconds = (ms / 1000) % 60

        val text: String
        if(isMinutesInclude) {
            text = "${if(minutes < 10) "0" else ""}$minutes:${if(seconds < 10) "0" else ""}$seconds"

        }
        else {
            text = "${if(seconds < 10) "0" else ""}$seconds"
        }
        return text
    }
}