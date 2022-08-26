package com.drovisfrovis.runningandworkout.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.drovisfrovis.runningandworkout.db.entities.Exercise
import com.drovisfrovis.runningandworkout.db.entities.PlanDetails
import com.drovisfrovis.runningandworkout.db.entities.PreBuildPlans
import com.drovisfrovis.runningandworkout.db.relations.PreBuildPlansExerciseCrossRef

@Database(
    entities = [
        Exercise::class,
        PlanDetails::class,
        PreBuildPlans::class,
        PreBuildPlansExerciseCrossRef::class
    ], version = 3, exportSchema = false
)
abstract class WorkoutDatabase : RoomDatabase(){
    abstract fun getWorkoutDao(): WorkoutDao
}