package com.itgates.ultra.pulpo.cira.ui.utils

import com.itgates.ultra.pulpo.cira.CoroutineManager
import com.itgates.ultra.pulpo.cira.dataStore.PreferenceKeys
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.NewPlanEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.AccountType
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Brick
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Division
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.AccountData
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.DoctorPlanningData
import com.itgates.ultra.pulpo.cira.ui.activities.PlanningActivity
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import kotlin.streams.toList

class PlanningCurrentValues(private val activity: PlanningActivity) {
    var userId by Delegates.notNull<Long>()

    init {
        CoroutineManager.getScope().launch {
            userId = activity.getDataStoreService().getDataObjAsync(PreferenceKeys.USER_ID)
                .await().toLong()
        }
    }

    companion object {
        // start values
        val divisionStartValue: IdAndNameObj = IdAndNameEntity(0L, EmbeddedEntity("Select Division"), UN_SELECTED, -2)
        val brickStartValue: IdAndNameObj = IdAndNameEntity(0L, EmbeddedEntity("Select Brick"), UN_SELECTED, -2)
        val accTypeStartValue: IdAndNameObj = IdAndNameEntity(0L, EmbeddedEntity("Select Acc Type"), UN_SELECTED, -2)
        val classStartValue: IdAndNameObj = IdAndNameEntity(0L, EmbeddedEntity("Select Class"), UN_SELECTED, -2)
    }

    var accountsDataListToShow: List<AccountData> = listOf()
    var accountsDataList: List<AccountData> = listOf()

    var doctorsDataList: List<DoctorPlanningData> = listOf()

    val amDoctorsDataList: ArrayList<DoctorPlanningData> = ArrayList()
    var amDoctorsDataListToShow: ArrayList<DoctorPlanningData> = ArrayList()
    val pmDoctorsDataList: ArrayList<DoctorPlanningData> = ArrayList()
    var pmDoctorsDataListToShow: ArrayList<DoctorPlanningData> = ArrayList()
    val otherDoctorsDataList: ArrayList<DoctorPlanningData> = ArrayList()
    var otherDoctorsDataListToShow: ArrayList<DoctorPlanningData> = ArrayList()

    private val doctorAccountMapping: ArrayList<Int> = ArrayList()

    var divisionsList: List<Division> = listOf()
    var bricksList: List<Brick> = listOf()
    var accountTypesList: List<AccountType> = listOf()
    var allAccountTypesList: List<AccountType> = listOf()
    var classesList: List<IdAndNameEntity> = listOf()

    val selectedDoctors = ArrayList<DoctorPlanningData>()
    var selectedDoctorsStatus = HashMap<Int, Long>()

    // current values
    var divisionCurrentValue: IdAndNameObj = divisionStartValue
    var brickCurrentValue: IdAndNameObj = brickStartValue
    var accTypeCurrentValue: IdAndNameObj = accTypeStartValue
    var classCurrentValue: IdAndNameObj = classStartValue

    var tapNavigatingFun = {
        // any message
        Utilities.createCustomToast(activity, "Some error, You can refresh the activity our app")
    }

    fun isDivisionSelected(): Boolean = divisionCurrentValue !is IdAndNameEntity
    fun isBrickSelected(): Boolean = brickCurrentValue !is IdAndNameEntity
    fun isAccTypeSelected(): Boolean = accTypeCurrentValue !is IdAndNameEntity
    fun isClassSelected(): Boolean = classCurrentValue !is IdAndNameEntity

    fun isAllDataReady(): Boolean {
        return isDivisionSelected() && isBrickSelected() && isAccTypeSelected() && isClassSelected()
    }

    fun getDoctorListsMap(list: List<DoctorPlanningData>): Map<String, ArrayList<DoctorPlanningData>> {
        val dataMap: Map<String, ArrayList<DoctorPlanningData>> = allAccountTypesList.associate { accType ->
            accType.embedded.name to ArrayList()
        }
        list.forEach {
            dataMap[it.doctor.embedded.name]?.add(it)
        }
        return dataMap
    }

    fun distributeDoctorsList() {
        doctorsDataList.forEach {
            when(0) { // Todo when(it.shiftId)
                1 -> {
                    pmDoctorsDataList.add(it)
                    pmDoctorsDataListToShow.add(it)
                }
                2 -> {
                    amDoctorsDataList.add(it)
                    amDoctorsDataListToShow.add(it)
                }
                3 -> {
                    otherDoctorsDataList.add(it)
                    otherDoctorsDataListToShow.add(it)
                }
            }
        }
    }

    fun createNewPlanInstance(
        divisionId: Long, accountTypeId: Long, itemId: Long, itemDoctorId: Long, members: Long,
        visitDate: String, shift: Int, teamId: Long
    ): NewPlanEntity {
        println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
        println("$divisionId $accountTypeId $itemId $itemDoctorId")
        println("$members $visitDate $shift $userId $teamId")
        return NewPlanEntity(
            divisionId, accountTypeId, itemId, itemDoctorId, members, visitDate, shift,
            userId, teamId, 0
        )
    }

    fun notifyChanges(idAndNameObj: IdAndNameObj) {
        when(idAndNameObj) {
            is Division -> {
                divisionCurrentValue = idAndNameObj

                val divIds = if (divisionCurrentValue.embedded.name == "All") {
                    divisionsList.stream().map { it.id }.toList()
                }
                else {
                    listOf(divisionCurrentValue.id)
                }

                activity.loadBrickData(divIds)

                brickCurrentValue = ActualCurrentValues.brickStartValue
                accTypeCurrentValue = ActualCurrentValues.accTypeStartValue
            }
            is Brick -> {
                brickCurrentValue = idAndNameObj

                val divIds = if (divisionCurrentValue.embedded.name == "All") {
                    divisionsList.stream().map { it.id }.toList()
                }
                else {
                    listOf(divisionCurrentValue.id)
                }
                val brickIds = if (brickCurrentValue.embedded.name == "All") {
                    bricksList.stream().map { it.id }.toList()
                }
                else {
                    listOf(brickCurrentValue.id)
                }

                activity.loadAccTypeData(divIds, brickIds)

                accTypeCurrentValue = ActualCurrentValues.accTypeStartValue
            }
            is AccountType -> {
                accTypeCurrentValue = idAndNameObj

                val divIds = if (divisionCurrentValue.embedded.name == "All") {
                    divisionsList.stream().map { it.id }.toList()
                }
                else {
                    listOf(divisionCurrentValue.id)
                }
                val brickIds = if (brickCurrentValue.embedded.name == "All") {
                    bricksList.stream().map { it.id }.toList()
                }
                else {
                    listOf(brickCurrentValue.id)
                }
                val accTypeIds = if (accTypeCurrentValue.embedded.name == "All") {
                    accountTypesList.stream().map { it.id.toInt() }.toList()
                }
                else {
                    listOf(accTypeCurrentValue.id.toInt())
                }

                activity.loadClassesData(divIds, brickIds, accTypeIds)
            }
            is IdAndNameEntity -> {
                classCurrentValue = idAndNameObj
            }
        }
    }
}