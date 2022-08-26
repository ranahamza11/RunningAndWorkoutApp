package com.drovisfrovis.runningandworkout.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.drovisfrovis.runningandworkout.db.entities.Exercise
import com.drovisfrovis.runningandworkout.db.entities.PreBuildPlans
import java.io.Serializable

data class PreBuildPlanWithExercises(
    @Embedded var prePlan: PreBuildPlans,
    @Relation(
        parentColumn = "prePlanId",
        entityColumn = "exerciseId",
        associateBy = Junction(PreBuildPlansExerciseCrossRef::class)
    )
    var exercises: List<Exercise>
) : Serializable