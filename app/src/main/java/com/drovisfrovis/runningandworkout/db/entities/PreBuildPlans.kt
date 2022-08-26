package com.drovisfrovis.runningandworkout.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class PreBuildPlans (
        var totalDuration: Long = 0L
        ) : Serializable {
        @PrimaryKey(autoGenerate = true)
        var prePlanId: Int = 0
}

