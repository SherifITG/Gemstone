package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums

enum class ShiftEnum(private val _text: String, private val _index: Byte) {
    AM_SHIFT("AM", 1), PM_SHIFT("PM", 2), OTHER("Other", 3);

    val text: String get() = _text
    val index: Byte get() = _index
}