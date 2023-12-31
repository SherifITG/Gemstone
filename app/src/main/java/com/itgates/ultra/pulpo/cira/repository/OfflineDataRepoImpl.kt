package com.itgates.ultra.pulpo.cira.repository

import android.util.Log
import com.itgates.ultra.pulpo.cira.CoroutineManager
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.ActualVisitDTO
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.OfflineRecordDTO
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*
import com.itgates.ultra.pulpo.cira.roomDataBase.daos.*
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Presentation
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Slide
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.*
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.SettingEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.*
import com.itgates.ultra.pulpo.cira.utilities.GlobalFormats
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.streams.toList


class OfflineDataRepoImpl @Inject constructor(
    private val accountTypeDao: AccountTypeDao,
    private val brickDao: BrickDao,
    private val divisionDao: DivisionDao,
    private val settingDao: SettingDao,
    private val idAndNameDao: IdAndNameDao,

    private val accountDao: AccountDao,
    private val doctorDao: DoctorDao,
    private val presentationDao: PresentationDao,
    private val slideDao: SlideDao,
    private val plannedVisitDao: PlannedVisitDao,
    private val actualVisitDao: ActualVisitDao,

    private val offlineLogDao: OfflineLogDao,
    private val offlineLocDao: OfflineLocDao,

    private val newPlanDao: NewPlanDao,
): OfflineDataRepo {

    override suspend fun loadActualSettings(): List<Setting> {
        return settingDao.loadAll(
            listOf(
                SettingEnum.METERS_TO_ACCEPT_DEVIATION.text,
                SettingEnum.FIELD_NO_OF_DOCTORS.text,
                SettingEnum.NO_OF_PRODUCT.text,
                SettingEnum.NO_OF_GIVEAWAYS.text,
                SettingEnum.IS_REQUIRED_PRODUCT_COMMENT.text,
                SettingEnum.IS_REQUIRED_PRODUCT_FEEDBACK.text,
                SettingEnum.IS_REQUIRED_PRODUCT_FOLLOW_UP.text,
                SettingEnum.IS_REQUIRED_PRODUCT_M_FEEDBACK.text,
            )
        )
    }

    override suspend fun loadActualAccountTypes(divIds: List<Long>, brickIds: List<Long>): List<AccountType> {
        return if (brickIds.first() != -1L) {
            accountTypeDao.loadActualAccountTypes(divIds, brickIds)
        }
        else {
            accountTypeDao.loadActualAccountTypesWithoutBrick(divIds)
        }
    }

    override suspend fun loadAllAccountTypes(): List<AccountType> {
        return accountTypeDao.loadAllAccountTypes()
    }

    override suspend fun loadClassesData(
        divIds: List<Long>,
        brickIds: List<Long>,
        accTypeIds: List<Int>
    ): List<IdAndNameEntity> {
        return idAndNameDao.loadClassesByIdList(
            IdAndNameTablesNamesEnum.PRODUCT,
            divIds,
            brickIds,
            accTypeIds
        )
    }

    override suspend fun loadUserDivisions(): List<Division> {
        return divisionDao.loadAll()
    }

    override suspend fun loadActualBricks(divIds: List<Long>): List<Brick> {
        return brickDao.loadActualBricks(divIds)
    }

    override suspend fun loadIdAndNameTablesByTAblesListForActualActivity(lineId: Long): List<IdAndNameEntity> {
        return idAndNameDao.loadByLineAndByTablesList(
            listOf(
                IdAndNameTablesNamesEnum.PRODUCT,
                IdAndNameTablesNamesEnum.FEEDBACK,
                IdAndNameTablesNamesEnum.GIVEAWAY,
                IdAndNameTablesNamesEnum.MANAGER
            ),
            lineId
        )
    }

    override suspend fun loadSlidesByPresentationId(presentationId: Long): List<Slide> {
        return slideDao.loadByPresentationId(presentationId)
    }

    override suspend fun loadOfficeWorkTypes(): List<IdAndNameEntity> {
        return idAndNameDao.loadAllByTablesList(
            listOf(
                IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE,
            )
        )
    }

    override suspend fun loadProducts(): List<IdAndNameEntity> {
        return idAndNameDao.loadAllByTablesList(listOf(IdAndNameTablesNamesEnum.PRODUCT))
    }

    override suspend fun loadActualAccounts(
        lineId: Long,
        divId: Long,
        brickId: Long,
        accTypeId: Int
    ): List<Account> {
        return if (brickId != -1L)
            accountDao.loadActualAccounts(lineId, divId, brickId, accTypeId)
        else
            accountDao.loadActualAccountsWithoutBrick(lineId, divId, accTypeId)
    }

    override suspend fun loadActualDoctors(lineId: Long, accountId: Long, accTypeId: Int): List<Doctor> {
        return doctorDao.loadActualDoctors(lineId, accountId, accTypeId)
    }

    override suspend fun loadPresentations(): List<Presentation> {
        return presentationDao.loadPresentations()
    }

    override suspend fun loadUnSyncedActualVisitsData(): List<ActualVisit> {
        return actualVisitDao.loadUnSyncedRecords(false)
    }

    override suspend fun loadUnSyncedActualNewPlansData(): List<NewPlanEntity> {
        return newPlanDao.loadUnSyncedRecords(false)
    }

    override suspend fun loadRelationalPlannedVisitsData(): List<RelationalPlannedVisit> {
        return plannedVisitDao.loadRelationalPlannedVisits()
    }

    override suspend fun loadRelationalNewPlansData(): List<RelationalNewPlan> {
        return newPlanDao.loadRelationalNewPlans()
    }

    override suspend fun loadTodayRelationalPlannedVisitsData(): List<RelationalPlannedVisit> {
        val today = GlobalFormats.getDashedDate(Locale.getDefault(), Date())
        return plannedVisitDao.loadTodayRelationalPlannedVisits(today)
    }

    override suspend fun loadRelationalPlannedOfficeWorksData(): List<RelationalPlannedOfficeWork> {
        return plannedVisitDao.loadRelationalPlannedOfficeWorks(IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE)
    }

    override suspend fun markPlannedVisitAsDone(doneId: Long): Boolean {
        plannedVisitDao.markAsDone(true, doneId)
        return true
    }

    override suspend fun loadRelationalActualVisitsData(): List<RelationalActualVisit> {
        return actualVisitDao.loadRelationalActualVisits()
    }

    override suspend fun loadRelationalOfficeWorkReportsData(): List<RelationalOfficeWorkReport> {
        return actualVisitDao.loadRelationalOfficeWorkReports(IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE)
    }

    override suspend fun loadAllAccountReportData(): List<AccountData> {
        return accountDao.loadAllAccountReportData()
    }

    override suspend fun updateAccountLocation(
        llFirst: String, lgFirst: String,
        id: Long, accTypeId: Int, lineId: Long, divId: Long, brickId: Long
    ) {
        accountDao.updateAccountLocation(llFirst, lgFirst, id, accTypeId, lineId, divId, brickId)
    }

    override suspend fun loadAllDoctorReportData(): List<DoctorData> {
        return doctorDao.loadAllDoctorReportData(IdAndNameTablesNamesEnum.SPECIALITY)
    }

    override suspend fun loadAllDoctorPlanningData(): List<DoctorPlanningData> {
        return doctorDao.loadAllDoctorPlanningData()
    }


    override suspend fun uploadedActualVisitData(actualVisitDTO: ActualVisitDTO) {
        return actualVisitDao.updateSyncedActualVisits(
            actualVisitDTO.visitId, actualVisitDTO.syncDate, actualVisitDTO.syncTime,
            true, actualVisitDTO.offlineId
        )
    }

    override suspend fun uploadedNewPlanData(newPlanDTO: NewPlanDTO) {
        return newPlanDao.updateSyncedNewPlans(
            newPlanDTO.plannedId, "newPlanDTO.syncDate", "newPlanDTO.syncTime",
            "(newPlanDTO.isSynced == 1)".isNotEmpty(), newPlanDTO.offlineId
        )
    }

    override suspend fun insertActualVisitWithValidation(actualVisit: ActualVisit): Long {
        return if (actualVisit.divisionId == -1L) {
            actualVisitDao.insertOfficeWorkWithValidation(
                actualVisit.onlineId, actualVisit.divisionId, actualVisit.accountTypeId,
                actualVisit.accountId, actualVisit.accountDoctorId, actualVisit.noOfDoctors,
                actualVisit.plannedVisitId, actualVisit.multiplicity, actualVisit.startDate,
                actualVisit.startTime, actualVisit.endDate, actualVisit.endTime, actualVisit.shift,
                actualVisit.userId, actualVisit.lineId, actualVisit.llStart, actualVisit.lgStart,
                actualVisit.llEnd, actualVisit.lgEnd, actualVisit.visitDuration,
                actualVisit.visitDeviation, actualVisit.isSynced, actualVisit.syncDate,
                actualVisit.syncTime, actualVisit.multipleListsInfo
            )
        }
        else {
            actualVisitDao.insertActualVisitWithValidation(
                actualVisit.onlineId, actualVisit.divisionId, actualVisit.accountTypeId,
                actualVisit.accountId, actualVisit.accountDoctorId, actualVisit.noOfDoctors,
                actualVisit.plannedVisitId, actualVisit.multiplicity, actualVisit.startDate,
                actualVisit.startTime, actualVisit.endDate, actualVisit.endTime, actualVisit.shift,
                actualVisit.userId, actualVisit.lineId, actualVisit.llStart, actualVisit.lgStart,
                actualVisit.llEnd, actualVisit.lgEnd, actualVisit.visitDuration,
                actualVisit.visitDeviation, actualVisit.isSynced, actualVisit.syncDate,
                actualVisit.syncTime, actualVisit.multipleListsInfo
            )
        }
    }

    override suspend fun saveMasterData(masterDataPharmaResponse: MasterDataPharmaResponse) {
        idAndNameDao.insertAll(masterDataPharmaResponse.data.collectAllIdAndNameRoomObjects())
        accountTypeDao.insertAll(masterDataPharmaResponse.data.collectAccTypeRoomObjects())
        brickDao.insertAll(masterDataPharmaResponse.data.collectBrickRoomObjects())
        divisionDao.insertAll(masterDataPharmaResponse.data.collectDivisionRoomObjects())
//        settingDao.insertAll(masterDataPharmaResponse.Data.collectSettingRoomObjects())
    }

    override suspend fun saveAccountAndDoctorData(
        accountsAndDoctorsDetailsPharmaResponse: AccountsAndDoctorsDetailsPharmaResponse
    ) {
        accountDao.insertAll(accountsAndDoctorsDetailsPharmaResponse.Data.collectAccountRoomObjects())
        doctorDao.insertAll(accountsAndDoctorsDetailsPharmaResponse.Data.collectDoctorRoomObjects())
    }

    override suspend fun savePresentationAndSlideData(
        presentationsAndSlidesDetailsPharmaResponse: PresentationsAndSlidesDetailsPharmaResponse
    ) {
        presentationDao.insertAll(presentationsAndSlidesDetailsPharmaResponse.Data.collectPresentationRoomObjects())
        slideDao.insertAll(presentationsAndSlidesDetailsPharmaResponse.Data.collectSlideRoomObjects())
    }

    override suspend fun savePlannedVisitData(plannedVisitsPharmaResponse: PlannedVisitsPharmaResponse) {
        plannedVisitDao.insertAll(
            plannedVisitsPharmaResponse.Data.stream().map { it.toRoomPlannedVisit() }.toList()
        )
    }

    override suspend fun saveOfflineLog(offlineLog: OfflineLog) {
        offlineLogDao.insert(offlineLog)
    }

    override suspend fun saveOfflineLoc(offlineLoc: OfflineLoc) {
        offlineLocDao.insert(offlineLoc)
    }

    override suspend fun saveAllNewPlans(newPlanList: List<NewPlanEntity>) {
        newPlanDao.insertAll(newPlanList)
    }

    override suspend fun saveNewPlans(newPlanList: List<NewPlanEntity>): HashMap<Int, Long> {
        val planingMap = HashMap<Int, Long>()
        newPlanList.forEachIndexed { index, planningObj ->
            try {
                val job = CoroutineManager.getScope().launch {
                    planingMap[index] = newPlanDao.insertNewPlanWithValidation(
                        planningObj.onlineId, planningObj.divisionId, planningObj.accountTypeId.toInt(),
                        planningObj.accountId, planningObj.accountDoctorId, planningObj.members,
                        planningObj.visitDate, planningObj.visitTime, planningObj.shift,
                        planningObj.insertionDate, planningObj.userId, planningObj.lineId,
                        planningObj.isApproved, planningObj.relatedId, planningObj.isSynced,
                        planningObj.syncDate, planningObj.syncTime
                    )
                }
                job.join()
            } catch (e: Exception) {
                planingMap[index] = -2
                Log.d("OfflineDataRepoImpl", "saveNewPlans: failed $e")
            }
        }
        return planingMap
    }

    override suspend fun uploadedOfflineLogData(offlineLogDTO: OfflineRecordDTO) {
        return offlineLogDao.updateSyncedOfflineLogs(
            offlineLogDTO.onlineId, (offlineLogDTO.isSynced == 1), offlineLogDTO.offlineId
        )
    }

    override suspend fun uploadedOfflineLocData(offlineLocDTO: OfflineRecordDTO) {
        return offlineLocDao.updateSyncedOfflineLocations(
            offlineLocDTO.onlineId, (offlineLocDTO.isSynced == 1), offlineLocDTO.offlineId
        )
    }

    override suspend fun loadUnSyncedOfflineLogData(): List<OfflineLog> {
        return offlineLogDao.loadUnSyncedRecords(false)
    }

    override suspend fun loadUnSyncedOfflineLocData(): List<OfflineLoc> {
        return offlineLocDao.loadUnSyncedRecords(false)
    }

    override suspend fun clearMasterData() {
        idAndNameDao.deleteAll()
        accountTypeDao.deleteAll()
        brickDao.deleteAll()
        divisionDao.deleteAll()
        settingDao.deleteAll()
    }

    override suspend fun clearAccountAndDoctorData() {
        accountDao.deleteAll()
        doctorDao.deleteAll()
    }

    override suspend fun clearPresentationAndSlideData() {
        presentationDao.deleteAll()
        slideDao.deleteAll()
    }

    override suspend fun clearPlannedVisitData() {
        plannedVisitDao.deleteAll()
    }

}