package com.nicomazz.menseunipd.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Nicolò Mazzucato on 10/10/2017.
 */

object MenuAlarmDataSource : PrefDataSource<MenuAlarm>() {
    override fun MenuAlarm.id() = id

    override fun List<MenuAlarm>.sorted() = sortedBy { it.time }

    override fun getName() = "MENU_ALARM"

    override fun getEmptyItem(): MenuAlarm = MenuAlarm("")

    fun get(context: Context, menuAlarmId: Long): MenuAlarm? {
        val all = getAll(context)
        return all.firstOrNull { it.id == menuAlarmId }
    }

    /**
     * PROBLEMA DI KOTLIN (PENSO). NON RIESCE A SOSTITUIRE I GENERICS IN TypeToken
     * quindi è necessario fare l'override
     * //todo verificare
     */
    override fun getItemsFromPref(): ArrayList<MenuAlarm>? {
        val itemsJson = getPref(appCntx).getString(getName(), "")

        if (itemsJson.isNullOrBlank()) return ArrayList()

        return try {
            val list: ArrayList<MenuAlarm> = Gson().fromJson(itemsJson, object : TypeToken<ArrayList<MenuAlarm>>() {}.type)
            list
        } catch (e: Exception) {
            e.printStackTrace()
            ArrayList()
        }
    }

    fun hasAlarmUntilMenuForRestaurantNamed(context: Context, name: String?): Boolean {
        appCntx = context.applicationContext
        getItemsIfNeeded()
        return items!!.any { it.untilMenuRelease && it.name == name }
    }

    override fun add(context: Context, toAdd: MenuAlarm) {
        super.add(context, toAdd)
        toAdd.schedule()

    }
/*
    override fun updateItem(toUpdate: MenuAlarm) {
        getItemsIfNeeded()
        items?.remove(toUpdate)
        items?.add(toUpdate)
        saveItemsToPref()
    }*/
}