package com.itgates.ultra.pulpo.cira.network.models.requestModels

import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomProductModule

data class ProductInfoModel(
    val product_id: Long,
    val samples: Int,
    val notes: String,
    val market_feedback: String,
    val followup: String,
    val feedback_id: Long,
) {
    constructor(roomProductModule: RoomProductModule): this(
        roomProductModule.productId,roomProductModule.samples, roomProductModule.comment,
        roomProductModule.markedFeedback, roomProductModule.followUp, roomProductModule.feedbackId
    )
}