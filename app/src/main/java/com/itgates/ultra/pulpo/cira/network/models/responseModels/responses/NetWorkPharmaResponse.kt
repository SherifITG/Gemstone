package com.itgates.ultra.pulpo.cira.network.models.responseModels.responses

import com.google.gson.annotations.SerializedName
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*

data class LoginPharmaResponse(
//    @SerializedName("Data") val Data: UserTokenData,
    val access_token: String,
    @SerializedName("message") val Status_Message: String,
    val Status: Int
)

data class UserPharmaResponse(
    @SerializedName("user") val user: UserDetailsData,
    val Status_Message: String,
    val Status: Int
)

data class MasterDataPharmaResponse(
    @SerializedName("data") var data: OnlineMasterData,
    val Status_Message: String?,
    val Status: Int?
)

data class AccountsAndDoctorsDetailsPharmaResponse(
    @SerializedName("data") val Data: OnlineAccountsAndDoctorsData,
    val Status_Message: String?,
    val Status: Int?
)

data class PresentationsAndSlidesDetailsPharmaResponse(
    @SerializedName("Data") val Data: OnlinePresentationsAndSlidesData,
    val Status_Message: String,
    val Status: Int
)

data class PlannedVisitsPharmaResponse(
    @SerializedName("data") val Data: ArrayList<OnlinePlannedVisitData>,
    val Status_Message: String?,
    val Status: Int?
)

data class ActualVisitPharmaResponse(
    @SerializedName("data") val Data: List<ActualVisitDTO>,
    val Status_Message: String?,
    val Status: Int?
)

data class NewPlanPharmaResponse(
    @SerializedName("Data") val Data: List<NewPlanDTO>,
    val Status_Message: String,
    val Status: Int
)