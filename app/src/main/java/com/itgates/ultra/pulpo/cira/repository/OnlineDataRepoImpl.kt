package com.itgates.ultra.pulpo.cira.repository

import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedActualVisitModel
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedActualVisitsListModel
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedNewPlanModel
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*
import com.itgates.ultra.pulpo.cira.network.retrofit.DataRetrofitApi
import com.itgates.ultra.pulpo.cira.network.retrofit.FilesDataRetrofitApi
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject


class OnlineDataRepoImpl @Inject constructor(
    private val dataRetrofitApi: DataRetrofitApi,
    private val filesDataRetrofitApi: FilesDataRetrofitApi
): OnlineDataRepo {

    suspend fun getFile(fileName: String): ResponseBody {
        return filesDataRetrofitApi.getFileAsync(fileName).await()
    }

    override suspend fun authenticationAction(
        headers: Map<String, String>,
        username: String,
        password: String
    ): Response<LoginPharmaResponse> {
        return dataRetrofitApi.pharmaAuthenticateUserAsync(headers, username, password).await()
    }

    override suspend fun fetchUserData(headers: Map<String, String>): UserPharmaResponse {
        return dataRetrofitApi.pharmaUserDataAsync(headers).await()
    }

    override suspend fun fetchMasterData(headers: Map<String, String>): MasterDataPharmaResponse {
        return dataRetrofitApi.masterDataAsync(headers).await()
    }

    override suspend fun fetchAccountsAndDoctorsDetailsData(
        headers: Map<String, String>
    ): AccountsAndDoctorsDetailsPharmaResponse {
        return dataRetrofitApi.accountsAndDoctorsDetailsAsync(headers).await()
    }

    override suspend fun fetchPresentationsAndSlidesDetailsData(
        headers: Map<String, String>,
        lineId: String
    ): PresentationsAndSlidesDetailsPharmaResponse {
        return dataRetrofitApi.presentationsAndSlidesDetailsAsync(headers, lineId).await()
    }

    override suspend fun fetchPlannedVisitsData(
        headers: Map<String, String>
    ): PlannedVisitsPharmaResponse {
        return dataRetrofitApi.plannedVisitsDataAsync(headers).await()
    }

    override suspend fun uploadActualVisitsData(
        headers: Map<String, String>,
        uploadedListObj: UploadedActualVisitsListModel
    ): ActualVisitPharmaResponse {
        return dataRetrofitApi.uploadActualVisitAsync(headers, uploadedListObj).await()
//        return try{
//            dataRetrofitApi.uploadActualVisitAsync(headers, list).await()
//        }catch (ex:Exception){
//            ActualVisitPharmaResponse(listOf(), "", "")
//        }
    }

    override suspend fun uploadNewPlansData(
        headers: Map<String, String>,
        list: List<UploadedNewPlanModel>
    ): NewPlanPharmaResponse {
        println("Uploaded List : $list")
        return dataRetrofitApi.uploadNewPlanAsync(headers, list).await()
//        return try{
//            dataRetrofitApi.uploadActualVisitAsync(headers, list).await()
//        }catch (ex:Exception){
//            ActualVisitPharmaResponse(listOf(), "", "")
//        }
    }
}