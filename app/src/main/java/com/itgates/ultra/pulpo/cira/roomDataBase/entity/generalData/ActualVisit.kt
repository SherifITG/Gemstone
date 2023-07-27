package com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomMultipleListsModule
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames

@Entity(tableName = TablesNames.ActualVisitTable)
data class ActualVisit(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "online_id") var onlineId: Long,
    @ColumnInfo(name = "division_id") var divisionId: Long,
    @ColumnInfo(name = "brick_id") var brickId: Long,
    @ColumnInfo(name = "account_type_id") var accountTypeId: Int,
    @ColumnInfo(name = "account_id") var accountId: Long,
    @ColumnInfo(name = "account_doctor_id") var accountDoctorId: Long,
    @ColumnInfo(name = "no_of_doctors") val noOfDoctors: Int,
    @ColumnInfo(name = "planned_visit_id") var plannedVisitId: Long,
    @ColumnInfo(name = "multiplicity") val multiplicity: MultiplicityEnum,
    @ColumnInfo(name = "start_date") val startDate: String,
    @ColumnInfo(name = "start_time") var startTime: String,
    @ColumnInfo(name = "end_date") val endDate: String,
    @ColumnInfo(name = "end_time") var endTime: String,
    @ColumnInfo(name = "shift") val shift: ShiftEnum,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "line_id") var lineId: Long,
    @ColumnInfo(name = "ll_start") val llStart: Double,
    @ColumnInfo(name = "lg_start") val lgStart: Double,
    @ColumnInfo(name = "ll_end") var llEnd: Double,
    @ColumnInfo(name = "lg_end") var lgEnd: Double,
    @ColumnInfo(name = "visit_duration") var visitDuration: String,
    @ColumnInfo(name = "visit_deviation") var visitDeviation: Long,
    @ColumnInfo(name = "is_synced") var isSynced: Boolean,
    @ColumnInfo(name = "sync_date") var syncDate: String,
    @ColumnInfo(name = "sync_time") var syncTime: String,
    @ColumnInfo(name = "multiple_lists_info") val multipleListsInfo: RoomMultipleListsModule
) {
    constructor(
        divId: Long, brickId: Long, accountTypeId: Int, accountId: Long, accountDoctorId: Long, noOfDoctor: Int,
        plannedVisitId: Long, multiplicity: MultiplicityEnum, startDate: String, startTime: String,
        endDate: String, endTime: String, shift: ShiftEnum, userId: Long, lineId: Long,
        llStart: Double, lgStart: Double, llEnd: Double, lgEnd: Double, vDuration: String,
        vDeviation: Long, isSynced: Boolean, syncDate: String, syncTime: String,
        multipleListsInfo: RoomMultipleListsModule
    ) : this(
        0L, 0L, divId, brickId, accountTypeId, accountId, accountDoctorId, noOfDoctor, plannedVisitId,
        multiplicity, startDate, startTime, endDate, endTime, shift, userId, lineId, llStart,
        lgStart, llEnd, lgEnd, vDuration, vDeviation, isSynced, syncDate, syncTime,
        multipleListsInfo
    )
}