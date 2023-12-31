package com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames

@Entity(tableName = TablesNames.IdAndNameTable, primaryKeys = ["id", "table_identifier"])
data class IdAndNameEntity(
    @ColumnInfo(name = "id") override val id: Long,
    @Embedded(prefix = "embedded_entity_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = "table_identifier") val tableId: IdAndNameTablesNamesEnum,
    @ColumnInfo(name = "line_id") val lineId: Long,
): IdAndNameObj(id, embedded)