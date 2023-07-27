package com.itgates.ultra.pulpo.cira.network.models.requestModels

import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomGiveawayModule

data class GiveawayInfoModel(
    val giveaway_id: Long,
    val units: Int
) {
    constructor(roomGiveawayModule: RoomGiveawayModule): this(
        roomGiveawayModule.giveawayId,roomGiveawayModule.samples
    )
}