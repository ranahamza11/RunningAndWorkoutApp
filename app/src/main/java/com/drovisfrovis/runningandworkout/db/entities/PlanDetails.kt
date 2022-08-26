package com.drovisfrovis.runningandworkout.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class PlanDetails(
    var completeDuration: Long = 0L,
    var timeStamp: Long = 0L,
    var dateAndTimeStamp: String,
    var caloriesBurned: Float,
    var prePlanId: Int = 0
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var planDetailId: Int = 0
}