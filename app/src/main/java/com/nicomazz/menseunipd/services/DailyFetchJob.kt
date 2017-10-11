package com.nicomazz.menseunipd.services

import android.util.Log
import com.evernote.android.job.DailyJob
import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.nicomazz.menseunipd.data.MenuAlarm
import com.nicomazz.menseunipd.data.MenuAlarmDataSource
import java.util.concurrent.TimeUnit

class DailyFetchJob : DailyJob() {


    override fun onRunDailyJob(params: Job.Params): DailyJob.DailyJobResult {

        val menuAlarmId = params.extras.getLong(FetchJobUtils.MENU_ALARM_ID_KEY, -1)
        val menuAlarm = MenuAlarmDataSource.get(context, menuAlarmId)

        if (menuAlarmId < 0 || menuAlarm == null) {
            Log.e(TAG, "non ho informazioni sul menu alarm! finisco il job giornaliero!")
            return DailyJobResult.CANCEL
        }

        Log.d(TAG,"inizio un daily job con mensa: ${menuAlarm.name}")

        if (!menuAlarm.isActiveToday())
            return DailyJobResult.SUCCESS

        Log.d(TAG,"ed è attivo oggi!")

        val restaurant = EsuRestApi().getRestaurantSync(menuAlarm.name)

        if (restaurant == null || restaurant.isEmpty()) { // riproviamo domani..
            Log.e(TAG,"non ho trovato nulla..")
            return DailyJobResult.SUCCESS
        }
        // a questo punto c'è il ristorante ed il menu
        sendMenuNotify(context, restaurant)
        return DailyJobResult.SUCCESS
    }


    companion object {

        val TAG = "DailyFetchJob"

        fun scheduleJob(menuAlarm: MenuAlarm) {
            Log.d(TAG,"schedulo un  daily job")
            val extras = PersistableBundleCompat()
            extras.putLong(FetchJobUtils.MENU_ALARM_ID_KEY, menuAlarm.id)

            val startTime = menuAlarm.getStartMilliInDay()
            val jobId = DailyJob.schedule(JobRequest.Builder(TAG)
                    .addExtras(extras), startTime, startTime + TimeUnit.SECONDS.toMillis(30))

            menuAlarm.setJobId(jobId)

        }


    }
}