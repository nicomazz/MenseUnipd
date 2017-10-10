package com.nicomazz.menseunipd.data

import io.realm.Realm

/**
 * Created by Nicolò Mazzucato on 10/10/2017.
 */
object MenuAlarmDataSource {

    private val realm = Realm.getDefaultInstance()

    fun getAllMenuAlarms() =
            realm.where<MenuAlarm>(MenuAlarm::class.java).findAll()

    fun addMenuAlarm(toAdd: MenuAlarm = MenuAlarm("")) {
        realm.executeTransaction {
            realm.copyToRealm(toAdd)
        }
    }

    fun removeMenuAlarm(toRemove: MenuAlarm) {
        realm.executeTransaction {
            toRemove.deleteFromRealm()
        }
    }

    fun removeAll() {
        realm.executeTransaction {
            realm.deleteAll()
        }
    }
}