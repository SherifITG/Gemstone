package com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames

@Entity(tableName = TablesNames.DivisionTable)
data class Division(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_division_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = "line_id") val lineId: Long,
    @ColumnInfo(name = "type_id") val typeId: Long,
    @ColumnInfo(name = "not_manager") val notManager: Int,
    @ColumnInfo(name = "url") val url: String,
): IdAndNameObj(id, embedded)