package com.drovisfrovis.runningandworkout.db

import androidx.room.*
import com.drovisfrovis.runningandworkout.db.entities.Exercise
import com.drovisfrovis.runningandworkout.db.entities.PlanDetails
import com.drovisfrovis.runningandworkout.db.entities.PreBuildPlans
import com.drovisfrovis.runningandworkout.db.relations.*

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreBuildPlans(preBuildPlans: PreBuildPlans)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanDetails(planDetails: PlanDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreBuildPlansExerciseCrossRef(preBuildPlansExerciseCrossRef: PreBuildPlansExerciseCrossRef)

    @Query("DELETE FROM PlanDetails WHERE planDetailId=:planDetId")
    suspend fun deletePlanDetailById(planDetId: Int)

    @Query("SELECT * FROM PreBuildPlans")
    suspend fun getAllPreBuildPlans() : List<PreBuildPlans>

    @Query("SELECT * FROM EXERCISE WHERE exerciseId=:id")
    suspend fun getExerciseById(id: Int): Exercise

    @Transaction
    @Query("SELECT * FROM planDetails ORDER BY timeStamp DESC")
    suspend fun getDetailsOfAllPlans(): List<PlanDetails>

    @Transaction
    @Query("SELECT * FROM preBuildPlans WHERE prePlanId =:prePlanId")
    suspend fun getExercisesOfPreBuildPlans(prePlanId: Int) : List<PreBuildPlanWithExercises>



}