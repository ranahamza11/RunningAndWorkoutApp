package com.drovisfrovis.runningandworkout.adapter

import android.content.Context
import android.content.DialogInterface
import android.view.*
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.manager.Lifecycle
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.db.WorkoutDao
import com.drovisfrovis.runningandworkout.db.entities.PlanDetails
import com.drovisfrovis.runningandworkout.misc.WorkoutUtility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.item_history.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.zip.Inflater
import javax.inject.Inject
import javax.inject.Named


class HistoryAdapter(private val contxt: Context, private val list: MutableList<PlanDetails>, val workoutDao: WorkoutDao) : ArrayAdapter<PlanDetails>(contxt, R.layout.item_history, list) {

    private var emptyListener: (()-> Unit)? = null

    fun setEmptyListListener(listener: () -> Unit) {
        emptyListener = listener
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View = LayoutInflater.from(contxt).inflate(R.layout.item_history, parent, false)
        view.tvPlanName.text = "Day ${list[position].prePlanId}"
        view.tvDuration.text = WorkoutUtility.getFormattedCountDownTime(list[position].completeDuration, true)
        view.tvCalories.text = "${list[position].caloriesBurned}KCAL"
        view.tvDateAndTime.text = list[position].dateAndTimeStamp

        view.ivOptions.setOnClickListener {
            val dialog = getDialog(arrayOf("Delete"), position)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val wmlp = dialog.window?.attributes
            wmlp?.let {
                it.gravity = Gravity.TOP + Gravity.START
                it.x = 100
                it.y = 100
            }
            dialog.show()
        }


        return view
    }

    fun getDialog(items: Array<String>, pos: Int) = MaterialAlertDialogBuilder(contxt)
        .setIcon(R.drawable.ic_delete)
        .setItems(items, DialogInterface.OnClickListener { dialog, item ->
            if(item == 0) {
                val id = list[pos].planDetailId
                list.removeAt(pos)
                notifyDataSetChanged()
                CoroutineScope(Dispatchers.Main).launch {
                    workoutDao.deletePlanDetailById(id)
                    emptyListener?.let { yes -> yes() }
                }

            }
        } )
        .create()
}
