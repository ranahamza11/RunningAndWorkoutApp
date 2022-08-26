package com.drovisfrovis.runningandworkout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drovisfrovis.runningandworkout.db.WorkoutDao
import com.drovisfrovis.runningandworkout.db.entities.PreBuildPlans
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    @Named("workoutDao") val workoutDao: WorkoutDao
): ViewModel() {

    lateinit var allPreBuildPlans: List<PreBuildPlans>

    fun getPreBuildPlans() {
        viewModelScope.launch {
            allPreBuildPlans = workoutDao.getAllPreBuildPlans()
        }
    }
}