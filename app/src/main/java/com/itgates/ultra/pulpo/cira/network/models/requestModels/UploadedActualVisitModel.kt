package com.itgates.ultra.pulpo.cira.network.models.requestModels

import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.ActualVisit
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.ShiftEnum
import kotlin.streams.toList

data class UploadedActualVisitModel(
    val id: Long,
    val offline_id: Long,
    val div_id: Long,
    val brick_id: String,
    val acc_type_id: Int,
    val account_id: Long,
    val account_dr_id: Long,
    val no_of_doctors: Int,
    val visit_type_id: Int,
    val date: String,
    val time: String,
    val shift: Int,
    val plan_id: Long,
    val line_id: Long,
    val insertion_date: String,
    val insertion_time: String,
    val visit_duration: String,
    val visit_deviation: Long,
    val ll: String,//-end
    val lg: String,
    val ll_start: String,
    val lg_start: String,
    var product_info: List<ProductInfoModel>,
    var member_info: List<MemberInfoModel>,
    var giveaway_info: List<GiveawayInfoModel>
) {
    constructor(actual: ActualVisit): this (
        actual.onlineId, actual.id, actual.divisionId,
        if (actual.brickId == -1L) "All" else actual.brickId.toString(),
        actual.accountTypeId, actual.accountId, actual.accountDoctorId, actual.noOfDoctors,
        if (actual.multiplicity == MultiplicityEnum.DOUBLE_VISIT) 1 else 0,
        actual.startDate, actual.startTime,
        when (actual.shift) {
            ShiftEnum.AM_SHIFT -> ShiftEnum.AM_SHIFT.index.toInt()
            ShiftEnum.PM_SHIFT -> ShiftEnum.PM_SHIFT.index.toInt()
            ShiftEnum.OTHER -> ShiftEnum.OTHER.index.toInt()
        },
        actual.plannedVisitId, actual.lineId, actual.endDate, actual.endTime, actual.visitDuration,
        actual.visitDeviation, actual.llEnd.toString(),
        actual.lgEnd.toString(), actual.llStart.toString(), actual.lgStart.toString(),
        actual.multipleListsInfo.products.stream().map {
            ProductInfoModel(it)
        }.toList(),
        actual.multipleListsInfo.managers.stream().map {
            MemberInfoModel(it)
        }.toList(),
        actual.multipleListsInfo.giveaways.stream().map {
            GiveawayInfoModel(it)
        }.toList()
    )
}