package com.drovisfrovis.runningandworkout.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Exercise(
    var name: String,
    var totalDuration: Long = 0L,
    var reps: Int = 0,
    var description: String
) :Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exerciseId", index = true)
    var exerciseId: Int = 0
}