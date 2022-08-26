package com.drovisfrovis.runningandworkout.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.db.entities.PlanDetails
import com.drovisfrovis.runningandworkout.db.entities.PreBuildPlans
import kotlinx.android.synthetic.main.item_plans.view.*
import java.util.zip.Inflater

class PlanAdapter(private val contxt: Context, private val list: List<PreBuildPlans>, private val completedPlans: List<PlanDetails>) : ArrayAdapter<PreBuildPlans>(contxt, R.layout.item_plans, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View = LayoutInflater.from(contxt).inflate(R.layout.item_plans, parent, false)
        if(position % 7 == 0 || position == 0) {
            val count = (position/7) + 1
            view.cl_week_count.visibility = View.VISIBLE
            view.tvWeekCount.text = "Week $count"
        }
        else {
            view.cl_week_count.visibility = View.GONE
        }

        if(list[position].totalDuration == 0L) {
            view.ivCheckBox.setImageResource(R.drawable.ic_rest)
        }
        else {
            view.ivCheckBox.setImageResource(R.drawable.ic_check_complete)
        }
        view.tvPlanNumber.text = "Day ${list[position].prePlanId}"

        for(i in completedPlans.indices) {
            if(list[position].prePlanId == completedPlans[i].prePlanId)
                view.ivCheckBox.setColorFilter(ContextCompat.getColor(context, R.color.green))
        }

        return view
    }
}
