package com.nicomazz.menseunipd.data

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by Nicolò Mazzucato on 09/10/2017.
 */
open class MenuAlarm(
        var name: String = "",
        var time: Date = Calendar.getInstance().time,
        var weeksDay: Int = 0 ,
        @PrimaryKey
        var id: Long = System.currentTimeMillis()
) : RealmObject() {

    fun updateTime(newTime: Date) {
        Realm.getDefaultInstance().executeTransaction {
            time = newTime
        }
    }


    private fun get(mask: Int): Boolean {
        return (weeksDay and mask) == mask
    }

    private fun set(mask: Int, value: Boolean) {
        Realm.getDefaultInstance().executeTransaction {
            if (value) {
                weeksDay = weeksDay or mask
            } else {
                weeksDay = weeksDay and mask.inv()
            }
        }

    }

    fun isActiveOnDay(day : Int): Boolean {
        return get(1.shl(day))
    }

    fun setActiveOnDay(active: Boolean, day: Int) {
        set(1.shl(day),active)
    }

    fun toggleDay(day: Int){
        setActiveOnDay(!isActiveOnDay(day),day)
    }

    fun setCanteenName(newName: String) {
        Realm.getDefaultInstance().executeTransaction {
            name = newName
        }
    }

    fun remove() {
        Realm.getDefaultInstance().executeTransaction {
            deleteFromRealm()
        }
    }


}