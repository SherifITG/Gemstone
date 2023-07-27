package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.PlannedVisit

data class RelationalPlannedVisit(
    @Embedded val plannedVisit: PlannedVisit,
    val divName: String,
    val notDivManager: Int,
    val lineId: Long,
    val brickName: String?,
    val brickId: Long,
    val accTypeName: String,
    val shiftId: Int,
    val accName: String,
    val firstLL: String,
    val firstLG: String,
    val docName: String
)