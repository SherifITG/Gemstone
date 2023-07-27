package com.itgates.ultra.pulpo.cira.network.retrofit

import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedActualVisitModel
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedActualVisitsListModel
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedNewPlanModel
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface DataRetrofitApi {

    @POST("login")
    fun pharmaAuthenticateUserAsync(
        @HeaderMap headers: Map<String, String>,
        @Query("name") username: String,
        @Query("password") password: String
    ): Deferred<Response<LoginPharmaResponse>>

    @GET("user")
    fun pharmaUserDataAsync(
        @HeaderMap headers: Map<String, String>
    ): Deferred<UserPharmaResponse>

    @GET("master-data")
    fun masterDataAsync(
        @HeaderMap headers: Map<String, String>
    ): Deferred<MasterDataPharmaResponse>

    @GET("accounts-doctors")
    fun accountsAndDoctorsDetailsAsync(
        @HeaderMap headers: Map<String, String>
    ): Deferred<AccountsAndDoctorsDetailsPharmaResponse>

    @GET("index.php")
    fun presentationsAndSlidesDetailsAsync(
        @HeaderMap headers: Map<String, String>,
        @Query("teamId") lineId: String
    ): Deferred<PresentationsAndSlidesDetailsPharmaResponse>

    @GET("plan-visits")
    fun plannedVisitsDataAsync(
        @HeaderMap headers: Map<String, String>
    ): Deferred<PlannedVisitsPharmaResponse>

    @POST("actual-visit")
    fun uploadActualVisitAsync(
        @HeaderMap headers: Map<String, String>,
        @Body uploadedListObj: UploadedActualVisitsListModel
    ): Deferred<ActualVisitPharmaResponse>

    @POST("_planned.php")
    fun uploadNewPlanAsync(
        @HeaderMap headers: Map<String, String>,
        @Body List: List<UploadedNewPlanModel>
    ): Deferred<NewPlanPharmaResponse>

}