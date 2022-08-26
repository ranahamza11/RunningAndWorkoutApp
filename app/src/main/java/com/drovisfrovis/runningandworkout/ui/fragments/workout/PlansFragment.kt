package com.drovisfrovis.runningandworkout.ui.fragments.workout

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.adapter.PlanAdapter
import com.drovisfrovis.runningandworkout.db.WorkoutDao
import com.drovisfrovis.runningandworkout.db.entities.PreBuildPlans
import com.drovisfrovis.runningandworkout.ui.viewmodels.WorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_plans.*
import kotlinx.android.synthetic.main.item_plans.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class PlansFragment : Fragment(R.layout.fragment_plans){

    private val viewModel : WorkoutViewModel by viewModels()

    @Inject
    @Named("workoutDao")
    lateinit var workoutDao: WorkoutDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var preBuildPlans: List<PreBuildPlans>
        listView.isClickable = true

        btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_plansFragment_to_historyFragment)
        }

        lifecycleScope.launch {
            preBuildPlans = workoutDao.getAllPreBuildPlans()
            val completedPlans = workoutDao.getDetailsOfAllPlans()

            if(completedPlans.isEmpty())
                btnHistory.visibility = View.GONE
            else
                btnHistory.visibility = View.VISIBLE

            listView.adapter = PlanAdapter(requireContext(), preBuildPlans, completedPlans)

            listView.setOnItemClickListener { _, _, position, _ ->
                if(preBuildPlans[position].totalDuration == 0L) {
                    navHostFragment.findNavController().navigate(R.id.action_plansFragment_to_restDayFragment)
                }
                else{
                    val bundle = Bundle()
                    bundle.putInt("planKey", preBuildPlans[position].prePlanId)
                    navHostFragment.findNavController().navigate(R.id.action_plansFragment_to_workoutDetailsFragment, bundle)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }
}
