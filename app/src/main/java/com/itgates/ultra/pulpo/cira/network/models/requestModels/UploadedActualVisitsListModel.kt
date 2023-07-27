package com.itgates.ultra.pulpo.cira.network.models.requestModels

import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.ActualVisit
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.ShiftEnum
import kotlin.streams.toList

data class UploadedActualVisitsListModel(
    val uploadedActualVisitsListModel: List<UploadedActualVisitModel>
)