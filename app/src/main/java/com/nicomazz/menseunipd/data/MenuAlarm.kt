package com.nicomazz.menseunipd.data

import java.util.*

/**
 * Created by Nicolò Mazzucato on 09/10/2017.
 */
class MenuAlarm(
        val name: String,
        val time: Date,
        var weeksDay: BooleanArray = kotlin.BooleanArray(7),
        val id: Long = System.currentTimeMillis()
) {
    private val ALL_SETTED = 0b1111111

}