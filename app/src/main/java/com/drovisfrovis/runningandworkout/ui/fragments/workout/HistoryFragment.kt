package com.drovisfrovis.runningandworkout.ui.fragments.workout

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.adapter.HistoryAdapter
import com.drovisfrovis.runningandworkout.db.WorkoutDao
import com.drovisfrovis.runningandworkout.db.entities.PlanDetails
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.item_history.*
import kotlinx.android.synthetic.main.item_history.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HistoryFragment: Fragment(R.layout.fragment_history) {

    @Inject
    @Named("workoutDao")
    lateinit var workoutDao: WorkoutDao
    lateinit var planDetails: MutableList<PlanDetails>
    lateinit var historyAdapter: HistoryAdapter

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
        val callBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_historyFragment_to_plansFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_plansFragment)
        }

        lifecycleScope.launch {
            planDetails = workoutDao.getDetailsOfAllPlans().toMutableList()
            historyAdapter = HistoryAdapter(requireContext(), planDetails, workoutDao)
            historyAdapter.setEmptyListListener {
                setEmpty()
            }
            listView.adapter = historyAdapter

        }
    }

    fun setEmpty() {
        if(planDetails.isEmpty())
            ivEmpty.visibility = View.VISIBLE
        else
            ivEmpty.visibility = View.INVISIBLE
    }



}