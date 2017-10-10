package com.nicomazz.menseunipd.data

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by Nicolò Mazzucato on 11/10/2017.
 */
object FavouriteManager {

    private val FAVOURITE_KEY = "FAVOURITE"
    fun getFavourite(context: Context) =
            getPref(context).getString(FAVOURITE_KEY, "murialdo")

    fun setFavourite(context: Context, name: String?) {
        getPref(context).edit().putString(FAVOURITE_KEY,name ?: "murialdo").apply()
    }

    private fun getPref(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context)

}