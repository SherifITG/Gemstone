package com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum

@Entity(tableName = TablesNames.PlannedVisitTable)
data class PlannedVisit(
    @PrimaryKey val id: Long,
    @ColumnInfo("line_id") val lineId: Long,
    @ColumnInfo("div_id") val divisionId: Long,
    @ColumnInfo("brick_id") val brickId: Long,
    @ColumnInfo("acc_type_id") val accountTypeId: Long,
    @ColumnInfo("account_id") val accountId: Long,
    @ColumnInfo("account_doctor_id") val accountDoctorId: Long,
    @ColumnInfo("speciality_id") val specialityId: Long,
    @ColumnInfo("acc_class") val accountClass: Long,
    @ColumnInfo("doc_class") val doctorClass: Long,
    @ColumnInfo("visit_type") val visitType: MultiplicityEnum,
    @ColumnInfo("visit_date") val visitDate: String,
    @ColumnInfo("visit_time") val visitTime: String,
    @ColumnInfo("is_done") val isDone: Boolean,
    @ColumnInfo("shift") val shift: Int,
    @ColumnInfo("user_id") val userId: Int, // todo
)
