package com.drovisfrovis.runningandworkout.adapter

import android.content.Context
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.db.entities.Exercise
import com.drovisfrovis.runningandworkout.misc.WorkoutUtility
import kotlinx.android.synthetic.main.item_workout_details.view.*


class WorkoutDetailsAdapter(private val contxt: Context, private val list: List<Exercise>) : ArrayAdapter<Exercise>(contxt, R.layout.item_plans, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(contxt).inflate(R.layout.item_workout_details, parent, false)

        view.tvExerciseName.text = list[position].name
        var durationOrReps = 0

        if(list[position].totalDuration == 0L){
            view.tvExDuration.text = "x${list[position].reps}"
        }
        else {
            view.tvExDuration.text = "00:${list[position].totalDuration / 1000}"
        }

        Glide.with(contxt).load(ContextCompat.getDrawable(contxt, WorkoutUtility.getExerciseById(list[position].exerciseId))) .into(view.gifName)
        return view
    }
}