package com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesColumns.AccountTypeColumns

@Entity(tableName = TablesNames.AccountTypeTable)
data class AccountType(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_account_type_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = AccountTypeColumns.SORT) val sorting: Int,
    @ColumnInfo(name = AccountTypeColumns.SHIFT_ID) val shiftId: Int
): IdAndNameObj(id, embedded)