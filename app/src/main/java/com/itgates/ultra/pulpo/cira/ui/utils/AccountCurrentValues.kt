package com.itgates.ultra.pulpo.cira.ui.utils

import com.itgates.ultra.pulpo.cira.CoroutineManager
import com.itgates.ultra.pulpo.cira.dataStore.PreferenceKeys
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.AccountType
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Brick
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Division
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.AccountData
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.DoctorData
import com.itgates.ultra.pulpo.cira.ui.activities.AccountsReportActivity
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import kotlin.streams.toList

class AccountCurrentValues(private val activity: AccountsReportActivity) {
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
    var doctorsDataList: List<DoctorData> = listOf()

    var divisionsList: List<Division> = listOf()
    var bricksList: List<Brick> = listOf()
    var accountTypesList: List<AccountType> = listOf()
    var classesList: List<IdAndNameEntity> = listOf()

    // current values
    var divisionCurrentValue: IdAndNameObj = divisionStartValue
    var brickCurrentValue: IdAndNameObj = brickStartValue
    var accTypeCurrentValue: IdAndNameObj = accTypeStartValue
    var classCurrentValue: IdAndNameObj = classStartValue

    fun isDivisionSelected(): Boolean = divisionCurrentValue !is IdAndNameEntity
    fun isBrickSelected(): Boolean = brickCurrentValue !is IdAndNameEntity
    fun isAccTypeSelected(): Boolean = accTypeCurrentValue !is IdAndNameEntity
    fun isClassSelected(): Boolean = classCurrentValue !is IdAndNameEntity

    fun isAllDataReady(): Boolean {
        return isDivisionSelected() && isBrickSelected() && isAccTypeSelected() && isClassSelected()
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