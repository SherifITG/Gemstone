package com.itgates.ultra.pulpo.cira.repository

import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedActualVisitModel
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedActualVisitsListModel
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedNewPlanModel
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*
import retrofit2.Response

interface OnlineDataRepo {

    suspend fun authenticationAction(
        headers: Map<String, String>,
        username: String,
        password: String
    ): Response<LoginPharmaResponse>

    suspend fun fetchUserData(headers: Map<String, String>): UserPharmaResponse

    suspend fun fetchMasterData(headers: Map<String, String>): MasterDataPharmaResponse

    suspend fun fetchAccountsAndDoctorsDetailsData(
        headers: Map<String, String>
    ): AccountsAndDoctorsDetailsPharmaResponse

    suspend fun fetchPresentationsAndSlidesDetailsData(
        headers: Map<String, String>,
        lineId: String
    ): PresentationsAndSlidesDetailsPharmaResponse

    suspend fun fetchPlannedVisitsData(
        headers: Map<String, String>
    ): PlannedVisitsPharmaResponse

    suspend fun uploadActualVisitsData(
        headers: Map<String, String>,
        uploadedListObj: UploadedActualVisitsListModel
    ): ActualVisitPharmaResponse

    suspend fun uploadNewPlansData(
        headers: Map<String, String>,
        list: List<UploadedNewPlanModel>
    ): NewPlanPharmaResponse
}