package com.nicomazz.menseunipd.data

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * vengono usate shared pref in quanto si tratta davvero di pochi dati.
 * inizialmente veniva usato Realm, ma rende l'apk troppo grande.
 */
abstract class PrefDataSource<T> {

    var items: ArrayList<T>? = null

    lateinit var appCntx: Context

    fun getAll(context: Context): List<T> {
        appCntx = context.applicationContext

        getItemsIfNeeded()
        return items!!.sorted()
    }

    abstract fun List<T>.sorted(): List<T>
    abstract fun T.id(): Long

    abstract fun getName(): String
    abstract fun getEmptyItem(): T

    fun getItemsIfNeeded() {
        if (items == null) {
            items = getItemsFromPref()
            if (items == null) items = ArrayList()
        }
    }


    open fun add(context: Context, toAdd: T = getEmptyItem()) {
        appCntx = context.applicationContext
        updateItem(toAdd)
    }

    //todo RISOLVERE PROBLEMA CON GENERICS e KOTLIN
    open fun updateItem(toUpdate: T) {
        getItemsIfNeeded()
        items?.removeAll { it.id() == toUpdate.id() }
        items?.add(toUpdate)
        saveItemsToPref()
    }

    fun removeMenuAlarm(toRemove: T) {
        getItemsIfNeeded()
        items?.removeAll { it.id() == toRemove.id() }
        saveItemsToPref()
    }

    fun removeAll() {
        getItemsIfNeeded()
        items?.clear()
        saveItemsToPref()
    }

    open fun getItemsFromPref(): ArrayList<T>? {
        val itemsJson = getPref(appCntx).getString(getName(), "")

        if (itemsJson.isNullOrBlank()) return ArrayList()

        return try {
            val list: ArrayList<T> = Gson().fromJson(itemsJson, object : TypeToken<ArrayList<T>>() {}.type)
            // Log.d("List", "list: $list")
            list
        } catch (e: Exception) {
            e.printStackTrace()
            ArrayList()
        }
    }

    fun saveItemsToPref() {
        getItemsIfNeeded()
        val toSave = Gson().toJson(items)
        getPref(appCntx).edit().putString(getName(), toSave).commit()
    }

    fun getPref(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
}