package com.drovisfrovis.runningandworkout.ui.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drovisfrovis.runningandworkout.db.entities.Run
import com.drovisfrovis.runningandworkout.misc.SortType
import com.drovisfrovis.runningandworkout.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    val runsSortedByDate = mainRepository.getAllRunsSortedByDate()
    val runsSortedByDistance = mainRepository.getAllRunSortedByDistance()
    val runsSortedByTimeInMillis = mainRepository.getAllRunSortedByTimeInMillis()
    val runsSortedByCaloriesBurned = mainRepository.getAllRunsSortedByCaloriesBurned()
    val runsSortedByAvgSpeed = mainRepository.getAllRunSortedByAvgSpeed()

    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortType.DATE

    init {
        runs.addSource(runsSortedByDate, Observer { result ->
            if(sortType == SortType.DATE) {
                result?.let { runs.value = result }

            }
        })

        runs.addSource(runsSortedByAvgSpeed, Observer { result ->
            if(sortType == SortType.AVG_SPEED) {
                result?.let { runs.value = result }

            }
        })

        runs.addSource(runsSortedByCaloriesBurned, Observer { result ->
            if(sortType == SortType.CALORIES_BURNED) {
                result?.let { runs.value = result }

            }
        })

        runs.addSource(runsSortedByDistance, Observer { result ->
            if(sortType == SortType.DISTANCE) {
                result?.let { runs.value = result }

            }
        })

        runs.addSource(runsSortedByTimeInMillis, Observer { result ->
            if(sortType == SortType.RUNNING_TIME) {
                result?.let { runs.value = result }

            }
        })
    }

    fun sortRuns(sortType: SortType) = when(sortType) {
            SortType.DATE -> runsSortedByDate.value?.let { runs.value = it }
            SortType.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
            SortType.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
            SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
            SortType.CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runs.value = it }
        }.also {
            this.sortType = sortType
    }

    fun insertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}