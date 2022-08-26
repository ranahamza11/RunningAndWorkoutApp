package com.drovisfrovis.runningandworkout.db.relations

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.drovisfrovis.runningandworkout.db.entities.Exercise
import com.drovisfrovis.runningandworkout.db.entities.PreBuildPlans
import java.io.Serializable

@Entity(primaryKeys = ["prePlanId", "exerciseId"])
data class PreBuildPlansExerciseCrossRef (
    var prePlanId: Int,
    @ColumnInfo(name = "exerciseId", index = true)
    var exerciseId: Int

) : Serializable