package com.drovisfrovis.runningandworkout.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.drovisfrovis.runningandworkout.db.entities.PlanDetails
import com.drovisfrovis.runningandworkout.db.entities.PreBuildPlans


class PreBuildPlansAndPlanDetails(
    @Embedded var preBuild: PreBuildPlans,
    @Relation(parentColumn = "prePlanId", entityColumn = "prePlanId")
    var planDetails: PlanDetails
)


