package com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesColumns.AccountColumns
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesColumns.DoctorColumns

@Entity(
    tableName = TablesNames.DoctorTable,
    primaryKeys = [DoctorColumns.ID, DoctorColumns.ACCOUNT_TYPE_ID, DoctorColumns.ACCOUNT_ID]
)
data class Doctor(
    @ColumnInfo(DoctorColumns.ID) override val id: Long,
    @Embedded(prefix = "embedded_doctor_") override val embedded: EmbeddedEntity,
    @ColumnInfo(DoctorColumns.LINE_ID) val lineId: Long,
    @ColumnInfo(DoctorColumns.ACCOUNT_ID) val accountId: Long,
    @ColumnInfo(DoctorColumns.ACCOUNT_TYPE_ID) val accountTypeId: Int,
    @ColumnInfo(DoctorColumns.ACTIVE_DATE) val activeDate: String,
    @ColumnInfo(DoctorColumns.INACTIVE_DATE) val inactiveDate: String,
    @ColumnInfo(DoctorColumns.SPECIALIZATION_ID) val specializationId: Long,
    @ColumnInfo(DoctorColumns.CLASS_ID) val classId: Long,
    @ColumnInfo(DoctorColumns.GENDER) val gender: String
): IdAndNameObj(id, embedded)
