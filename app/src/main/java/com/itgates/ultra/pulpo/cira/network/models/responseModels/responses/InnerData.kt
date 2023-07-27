package com.itgates.ultra.pulpo.cira.network.models.responseModels.responses

import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.Account
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.Doctor
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.PlannedVisit
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.google.gson.annotations.SerializedName
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Presentation
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Slide
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum
import kotlin.streams.toList

data class UserTokenData(
    val access_token: String,
)

data class UserDetailsData(
    @SerializedName("id")val id: Long,
    @SerializedName("emp_code")val empCode: String?,
    @SerializedName("name") val name: String,
    @SerializedName("fullname") val fullName: String,
    @SerializedName("url")val imageUrl: String,
)

data class OnlineMasterData(
    @SerializedName("lines") val lines: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("divisions") val divisions: ArrayList<OnlineDivisionData>,
    @SerializedName("bricks") val bricks: ArrayList<OnlineBrickData>,
    @SerializedName("accountTypes") val accTypes: ArrayList<OnlineAccountTypeData>,
    @SerializedName("products") val products: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("spcialities") val specialties: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("classes") val classes: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("visitFeedBack") val feedbacks: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("managers") val managers: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("officeWorkTypes") val officeWorkTypes: ArrayList<OnlineIdAndNameObjectData>,
    @SerializedName("giveways") val giveaways: ArrayList<OnlineIdAndNameObjectData>,
//    @SerializedName("settings") val settings: ArrayList<OnlineSettingData>,
) {
    constructor() : this(
        ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(),
        ArrayList(), ArrayList(), ArrayList(), ArrayList(),
    )

    fun collectAllIdAndNameRoomObjects(): List<IdAndNameEntity> {
        return lines.stream().map { it.toRoomLine() }.toList()
                .asSequence()
                .plus(classes.stream().map { it.toRoomClass() }.toList())
                .plus(specialties.stream().map { it.toRoomSpeciality() }.toList())
                .plus(giveaways.stream().map { it.toRoomGiveaway() }.toList())
                .plus(officeWorkTypes.stream().map { it.toRoomOfficeWorkTypes() }.toList())
                // TODO remove the comment
                .plus(managers.stream().map { it.toRoomManager() }.toList())
                .plus(products.stream().map { it.toRoomProduct() }.toList())
                .plus(feedbacks.stream().map { it.toRoomFeedback() }.toList())
                .toList()
    }

    fun collectAccTypeRoomObjects(): List<AccountType> {
        return accTypes.stream().map { it.toRoomAccType() }.toList()
    }

    fun collectBrickRoomObjects(): List<Brick> {
        return bricks.stream().map { it.toRoomBrick() }.toList()
    }

    fun collectDivisionRoomObjects(): List<Division> {
        return divisions.stream().map { it.toRoomDivision() }.toList()
    }

//    fun collectSettingRoomObjects(): List<Setting> {
//        return settings.stream().map { it.toRoomSetting() }.toList()
//    }
}

data class OnlineAccountsAndDoctorsData(
    @SerializedName("accoutns") val accounts: ArrayList<OnlineAccountData>,
    @SerializedName("doctors") val doctors: ArrayList<OnlineDoctorData>
) {
    constructor() : this(ArrayList(), ArrayList())

    fun collectAccountRoomObjects(): List<Account> {
        return accounts.stream().map { it.toRoomAccount() }.toList()
    }

    fun collectDoctorRoomObjects(): List<Doctor> {
        return doctors.stream().map { it.toRoomDoctor() }.toList()
    }
}

data class OnlinePresentationsAndSlidesData(
    @SerializedName("Presentations") val Presentations: ArrayList<OnlinePresentationData>,
    @SerializedName("Slides") val Slides: ArrayList<OnlineSlideData>
) {
    constructor() : this(ArrayList(), ArrayList())

    fun collectPresentationRoomObjects(): List<Presentation> {
        return Presentations.stream().map { it.toRoomPresentation() }.toList()
    }

    fun collectSlideRoomObjects(): List<Slide> {
        return Slides.stream().map { it.toRoomSlide() }.toList()
    }
}

// include classes such as Line & Brick & Speciality & Giveaway & OfficeWorkTypes & Product & Manager & Feedback
data class OnlineIdAndNameObjectData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("line_id") val lineId: Long?,
) {
    // -2 means that this obj don't have the field line id
    fun toRoomLine(): IdAndNameEntity = IdAndNameEntity(
        this.id, EmbeddedEntity(this.name), IdAndNameTablesNamesEnum.LINE, lineId ?: -2
    )
    fun toRoomClass(): IdAndNameEntity = IdAndNameEntity(
        this.id, EmbeddedEntity(this.name), IdAndNameTablesNamesEnum.CLASS, lineId ?: -2
    )
    fun toRoomSpeciality() = IdAndNameEntity(
        this.id, EmbeddedEntity(this.name), IdAndNameTablesNamesEnum.SPECIALITY, lineId ?: -2
    )
    fun toRoomGiveaway() = IdAndNameEntity(
        this.id, EmbeddedEntity(this.name), IdAndNameTablesNamesEnum.GIVEAWAY, lineId ?: -2
    )
    fun toRoomOfficeWorkTypes() = IdAndNameEntity(
        this.id, EmbeddedEntity(this.name), IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE, lineId ?: -2
    )
    fun toRoomProduct() = IdAndNameEntity(
        this.id, EmbeddedEntity(this.name), IdAndNameTablesNamesEnum.PRODUCT, lineId ?: -2
    )
    fun toRoomManager() = IdAndNameEntity(
        this.id, EmbeddedEntity(this.name), IdAndNameTablesNamesEnum.MANAGER, lineId ?: -2
    )
    fun toRoomFeedback() = IdAndNameEntity(
        this.id, EmbeddedEntity(this.name), IdAndNameTablesNamesEnum.FEEDBACK, lineId ?: -2
    )
}

data class OnlineDivisionData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("line_id") val LineId: Long,
    @SerializedName("type_id") val typeId: Long,
    @SerializedName("not_manger") val notManager: Int,
    @SerializedName("url") val url: String,
) {
    fun toRoomDivision(): Division = Division(
        this.id,
        EmbeddedEntity(this.name),
        this.LineId,
        this.typeId,
        this.notManager,
        this.url,
    )
}

data class OnlineBrickData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("line_id") val lineId: String,
    @SerializedName("line_division_id") val ter_id: String
) {
    fun toRoomBrick(): Brick = Brick(this.id, EmbeddedEntity(this.name), this.lineId, this.ter_id)
}

data class OnlineAccountTypeData(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("sort") val sort: Int,
    @SerializedName("shift_id") val shiftId: Int,
) {
    fun toRoomAccType(): AccountType = AccountType(
        this.id,
        EmbeddedEntity(this.name),
        this.sort,
        this.shiftId
    )
}

//data class OnlineSettingData(
//    @SerializedName("id") val id: Long,
//    @SerializedName("attribute_name") val attribute_name: String,
//    @SerializedName("attribute_value") val attribute_value: String,
//) {
//    fun toRoomSetting(): Setting = Setting(this.id, EmbeddedEntity(this.attribute_name), this.attribute_value)
//}

data class OnlineAccountData( // 5 IDs PK
    val id: Long,
    val name: String,
    val line_id: Long,
    val div_id: Long,
    val class_id: Long,
    val brick_id: Long,
    val code: String,
    val type_id: Int,
    val address: String,
    val tel: String,
    val mobile: String,
    val email: String,
    val ll: String,
    val lg: String
) {
    fun toRoomAccount(): Account {
        return Account(
            id, EmbeddedEntity(name), line_id, div_id, class_id,
            brick_id, type_id, address, tel, mobile, email, ll, lg
        )
    }
}

data class OnlineDoctorData( // 3 IDs PK
    val id: Long,
    val name: String,
    val line_id: Long,
    val account_id: Long,
    val type_id: Int,
    val active_date: String?,
    val inactive_date: String?,
    val speciality_id: Long,
    val class_id: Long,
    val gender: String,
) {
    fun toRoomDoctor(): Doctor {
        return Doctor(
            id, EmbeddedEntity(name), line_id, account_id, type_id, active_date ?: "",
            inactive_date ?: "", speciality_id, class_id, gender
        )
    }
}

data class OnlinePresentationData(
    val id: Long,
    val name: String,
    val description: String,
    val insert_date: String,
    val insert_time: String,
    val active: Int,
    val product_id: Long,
    val brand_id: Long,
    val team_id: Long,
    val Product: String,
    val structure: String,
) {
    fun toRoomPresentation(): Presentation {
        return Presentation(
            id, EmbeddedEntity(name), description, active, product_id, team_id, Product, structure
        )
    }
}

data class OnlineSlideData(
    val id: Long,
    val title: String,
    val description: String,
    val contents: String,
    val presentation_id: Long,
    val product_id: Long,
    val brand_id: Long,
    val slide_type: String,
    val file_path: String,
    val structure: String,
) {
    fun toRoomSlide(): Slide {
        return Slide(
            id, EmbeddedEntity(title), description, contents, presentation_id, product_id, slide_type, file_path, structure
        )
    }
}

data class OnlinePlannedVisitData(
    val id: Long,
    val line_id: Long,
    val division_id: Long,
    val brick_id: Long,
    val type_id: Long,
    val account_id: Long,
    val doctor_id: Long,
    val speciality_id: Long,
    val acc_class: Long,
    val doc_class: Long,
    val visit_type_id: Int,
    val type: String,
    val date: String,
    val time: String,
    val shift_id: Int,
) {
    fun toRoomPlannedVisit(): PlannedVisit {
        return PlannedVisit(
            id, line_id, division_id, brick_id, type_id, account_id,
            doctor_id, speciality_id, acc_class, doc_class,
            if (visit_type_id == 1)
                MultiplicityEnum.SINGLE_VISIT
            else
                MultiplicityEnum.DOUBLE_VISIT,
            date, time, false,  shift_id, // shift here must sent from api
            userId = 1, // user id here must sent from api
        )
    }
}

data class ActualVisitDTO(
    @SerializedName("visit_id") val visitId: Long,
    @SerializedName("offline_id") val offlineId: Long,
    @SerializedName("sync_date") val syncDate: String,
    @SerializedName("sync_time") val syncTime: String,
//    @SerializedName("products") val products: List<Long>,
//    @SerializedName("members") val members: List<Long>,
//    @SerializedName("giveaways") val giveaways: List<Long>
)

data class NewPlanDTO(
    @SerializedName("planned_id") val plannedId: Long,
    @SerializedName("offline_id") val offlineId: Long,
)

data class OfflineRecordDTO(
    @SerializedName("online_id") val onlineId: Long,
    @SerializedName("offline_id") val offlineId: Long,
    @SerializedName("is_synced") val isSynced: Int,
)