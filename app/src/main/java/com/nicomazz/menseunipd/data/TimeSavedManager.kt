package com.nicomazz.menseunipd.data

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by Nicolò Mazzucato on 16/10/2017.
 */
object TimeSavedManager {

    private val OLD_WEB_SERVICE_TIME = 6000; // il servizio dell'esu ci mette in media 6000 ms per rispondere (15.000 nelle ore di punta)

    private val KEY_REQUESTS = "REQUEST_NUMBER"
    private val KEY_TIME_SPENT = "TIME_SPENT"

    fun addRequestTime(timeMs: Long, context: Context) {
        getPref(context).edit().putLong(KEY_TIME_SPENT, getAllRequestTime(context) + timeMs).apply()
        getPref(context).edit().putLong(KEY_REQUESTS, getRequestsNumber(context) + 1).apply()
    }

    //millisecondi di attesa risparmiati
    fun getMsSaved(context: Context) = Math.max(getRequestsNumber(context) * OLD_WEB_SERVICE_TIME - getAllRequestTime(context), 0)

    fun getAllRequestTime(context: Context) = getPref(context).getLong(KEY_TIME_SPENT, 0)

    fun getRequestsNumber(context: Context) = getPref(context).getLong(KEY_REQUESTS, 0)

    fun getMeanTime(context: Context): Long {
        if (getRequestsNumber(context) == 0L) return 0;
        return getAllRequestTime(context) / getRequestsNumber(context)
    }

    private fun getPref(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context)
}