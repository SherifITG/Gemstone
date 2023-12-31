package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.queries

import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames.NewPlanTable
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames.PlannedVisitTable
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesColumns.*

object MainRoomQuery {

    const val updateAccountLocationQuery = "UPDATE ${TablesNames.AccountTable}" +
            " SET ${AccountColumns.LL_FIRST} = :llFirst, ${AccountColumns.LG_FIRST} = :lgFirst" +
            " WHERE ${AccountColumns.ID} = :id" +
            " AND ${AccountColumns.ACCOUNT_TYPE_ID} = :accTypeId" +
            " AND ${AccountColumns.LINE_ID} = :lineId" +
            " AND ${AccountColumns.DIVISION_ID} = :divId" +
            " AND ${AccountColumns.BRICK_ID} = :brickId"

    const val settingByNamesListQuery = "SELECT * FROM ${TablesNames.SettingTable}" +
            " WHERE ${SettingColumns.EMBEDDED_SETTING_NAME} IN (:names)"

    private const val accountTypesIdsQueryWithoutBrick = "SELECT DISTINCT ${AccountColumns.ACCOUNT_TYPE_ID}" +
            " FROM ${TablesNames.AccountTable}" +
            " WHERE ${AccountColumns.DIVISION_ID} IN (:divIds)"

    const val accountTypesQueryWithoutBrick = "SELECT * FROM ${TablesNames.AccountTypeTable}" +
            " WHERE ${AccountTypeColumns.ID} IN ($accountTypesIdsQueryWithoutBrick)"

    private const val accountTypesIdsQuery = "SELECT DISTINCT ${AccountColumns.ACCOUNT_TYPE_ID}" +
            " FROM ${TablesNames.AccountTable}" +
            " WHERE ${AccountColumns.DIVISION_ID} IN (:divIds)" +
            " AND ${AccountColumns.BRICK_ID} IN (:brickIds)"

    const val accountTypesQuery = "SELECT * FROM ${TablesNames.AccountTypeTable}" +
            " WHERE ${AccountTypeColumns.ID} IN ($accountTypesIdsQuery)"

    const val bricksQuery = "SELECT * FROM ${TablesNames.BrickTable}" +
            " WHERE ${BrickColumns.TERRITORY_ID} IN (:divIds)"

    const val accountsQueryWithoutBrick = "SELECT * FROM ${TablesNames.AccountTable}" +
            " WHERE ${AccountColumns.LINE_ID} = :lineId" +
            " AND ${AccountColumns.DIVISION_ID} = :divId" +
            " AND ${AccountColumns.ACCOUNT_TYPE_ID} = :accTypeId"

    const val accountsQuery = "SELECT * FROM ${TablesNames.AccountTable}" +
            " WHERE ${AccountColumns.LINE_ID} = :lineId" +
            " AND ${AccountColumns.DIVISION_ID} = :divId" +
            " AND ${AccountColumns.BRICK_ID} = :brickId" +
            " AND ${AccountColumns.ACCOUNT_TYPE_ID} = :accTypeId"

    const val doctorsQuery = "SELECT * FROM ${TablesNames.DoctorTable}" +
            " WHERE ${DoctorColumns.LINE_ID} = :lineId" +
            " AND ${DoctorColumns.ACCOUNT_ID} = :accountId" +
            " AND ${DoctorColumns.ACCOUNT_TYPE_ID} = :accountTypeId"

    const val idAndNameObjectsQuery = "SELECT * FROM ${TablesNames.IdAndNameTable}" +
            " WHERE ${IdAndNameEntityColumns.TABLE_IDENTIFIER} = :tableId"

    const val idAndNameObjectsByTablesListQuery = "SELECT * FROM ${TablesNames.IdAndNameTable}" +
            " WHERE ${IdAndNameEntityColumns.TABLE_IDENTIFIER} IN (:tableIds)" +
            " AND (${IdAndNameEntityColumns.LINE_ID} = :lineId OR ${IdAndNameEntityColumns.LINE_ID} = ${-2})"

    const val idAndNameObjectsByTablesListWithoutLineIdQuery = "SELECT * FROM ${TablesNames.IdAndNameTable}" +
            " WHERE ${IdAndNameEntityColumns.TABLE_IDENTIFIER} IN (:tableIds)"

    const val unSyncedActualVisitQuery = "SELECT * FROM ${TablesNames.ActualVisitTable}" +
            " WHERE is_synced = :isSynced"

    const val updateSyncedActualVisitQuery = "UPDATE ${TablesNames.ActualVisitTable}" +
            " SET online_id = :onlineId, sync_date = :syncDate, sync_time = :syncTime, is_synced = :isSynced" +
            " WHERE id = :offlineId"

    // ---------------------------------------------------------------------------------------------
    private const val actualVisitValidationQuery = "SELECT 1 FROM ${TablesNames.ActualVisitTable}" +
            " WHERE user_id = :userId AND start_date = :startDate " +
            "AND account_id = :accountId AND account_doctor_id = :accountDoctorId " +
            "AND account_type_id = :accountTypeId"

    private const val officeWorkValidationQuery = "SELECT 1 FROM ${TablesNames.ActualVisitTable}" +
            " WHERE user_id = :userId AND start_date = :startDate AND shift = :shift"

    private const val insertActualVisitWithValidationFirstPartQuery =
        "INSERT INTO ${TablesNames.ActualVisitTable} (" +
                " online_id, division_id, account_type_id, account_id, account_doctor_id, no_of_doctors," +
                " planned_visit_id, multiplicity, start_date, start_time, end_date, end_time, shift," +
                " user_id, line_id, ll_start, lg_start, ll_end, lg_end, visit_duration," +
                " visit_deviation, is_synced, sync_date, sync_time, multiple_lists_info" +
                " ) SELECT " +
                " :onlineId, :divisionId, :accountTypeId, :accountId, :accountDoctorId, :noOfDoctors," +
                " :plannedVisitId, :multiplicity, :startDate, :startTime, :endDate, :endTime," +
                " :shift, :userId, :lineId, :llStart, :lgStart, :llEnd, :lgEnd, :visitDuration," +
                " :visitDeviation, :isSynced, :syncDate, :syncTime, :multipleListsInfo"

    const val insertActualVisitWithValidationQuery =
        "$insertActualVisitWithValidationFirstPartQuery WHERE NOT EXISTS ( $actualVisitValidationQuery )"

    const val insertOfficeWorkWithValidationQuery =
        "$insertActualVisitWithValidationFirstPartQuery WHERE NOT EXISTS ( $officeWorkValidationQuery )"



    const val unSyncedNewPlanQuery = "SELECT * FROM ${TablesNames.NewPlanTable}" +
            " WHERE is_synced = :isSynced"

    const val updateSyncedNewPlanQuery = "UPDATE ${TablesNames.NewPlanTable}" +
            " SET online_id = :onlineId, sync_date = :syncDate, sync_time = :syncTime, is_synced = :isSynced" +
            " WHERE id = :offlineId"

    // ---------------------------------------------------------------------------------------------
    private const val newPlanValidationQuery = "SELECT 1 FROM $NewPlanTable" +
            " WHERE $NewPlanTable.user_id = :userId AND $NewPlanTable.visit_date = :visitDate" +
            " AND $NewPlanTable.account_id = :accountId AND $NewPlanTable.account_doctor_id = :accountDoctorId" +
            " AND $NewPlanTable.acc_type_id = :accTypeId"

    private const val plannedVisitValidationQuery = "SELECT 1 FROM $PlannedVisitTable" +
            " WHERE $PlannedVisitTable.user_id = :userId AND $PlannedVisitTable.visit_date = :visitDate" +
            " AND $PlannedVisitTable.account_id = :accountId AND $PlannedVisitTable.account_doctor_id = :accountDoctorId" +
            " AND $PlannedVisitTable.acc_type_id = :accTypeId"

    private const val insertNewPlanWithValidationFirstPartQuery =
        "INSERT INTO $NewPlanTable (" +
                " online_id, div_id, acc_type_id, account_id, account_doctor_id, members, visit_date," +
                " visit_time, shift, insertion_date, user_id, line_id, is_approved, related_id," +
                " is_synced, sync_date, sync_time" +
                " ) SELECT " +
                " :onlineId, :divId, :accTypeId, :accountId, :accountDoctorId, :members, :visitDate," +
                " :visitTime, :shift, :insertionDate, :userId, :lineId, :isApproved, :relatedId," +
                " :isSynced, :syncDate, :syncTime"

    const val insertNewPlanWithValidationQuery =
        "$insertNewPlanWithValidationFirstPartQuery WHERE NOT EXISTS ( $newPlanValidationQuery )" +
                " AND NOT EXISTS ( $plannedVisitValidationQuery )"




    private const val updateValQuery = " SET online_id = :onlineId, is_synced = :isSynced" + " WHERE id = :offlineId"
    const val updateSyncedOfflineLogQuery = "UPDATE ${TablesNames.OfflineLogTable}" + updateValQuery
    const val updateSyncedOfflineLocQuery = "UPDATE ${TablesNames.OfflineLocTable}" + updateValQuery

    const val unSyncedOfflineLogQuery = "SELECT * FROM ${TablesNames.OfflineLogTable} WHERE is_synced = :isSynced"
    const val unSyncedOfflineLocQuery = "SELECT * FROM ${TablesNames.OfflineLocTable} WHERE is_synced = :isSynced"


}