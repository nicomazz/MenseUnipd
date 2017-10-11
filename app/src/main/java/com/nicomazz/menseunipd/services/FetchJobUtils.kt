package com.nicomazz.menseunipd.services

import android.util.Log
import com.evernote.android.job.JobManager
import com.nicomazz.menseunipd.data.MenuAlarm

/**
 * Created by Nicolò Mazzucato on 11/10/2017.
 */
object FetchJobUtils {

    val MENU_ALARM_ID_KEY = "MENU_ALARM_ID_KEY"

    fun scheduleJob(menuAlarm: MenuAlarm) {
        if (menuAlarm.untilMenuRelease)
            UntilReleaseFetchJob.scheduleJob(menuAlarm = menuAlarm)
        else DailyFetchJob.scheduleJob(menuAlarm)
    }

    fun cancelJob(id: Int) {
        if (!JobManager.instance().cancel(id))
            Log.e("FetchUtils", "Problemi con l'eliminazione di un job!")
    }

    //todo da verificare
    //fun isScheduled(id: Int) = JobManager.instance().getJob(id)?.isFinished == false
}