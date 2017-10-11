package com.nicomazz.menseunipd.services

import android.util.Log
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.nicomazz.menseunipd.data.MenuAlarm
import com.nicomazz.menseunipd.data.MenuAlarmDataSource
import java.util.*
import java.util.concurrent.TimeUnit


class UntilReleaseFetchJob : Job() {


    override fun onRunJob(params: Job.Params): Job.Result {

        val menuAlarmId = params.extras.getLong(FetchJobUtils.MENU_ALARM_ID_KEY, -1)
        val menuAlarm = MenuAlarmDataSource.get(context, menuAlarmId)

        if (menuAlarmId < 0 || menuAlarm == null) {
            Log.e(TAG, "non ho informazioni sul menu alarm! finisco il job!")
            cancel()
            return Job.Result.RESCHEDULE
        }

        Log.d(TAG,"run job until release per la mensa: ${menuAlarm.name}")

        val restaurantFetched = EsuRestApi().getRestaurantSync(menuAlarm.name)

        menuAlarm.updateTime(getTimeInNMinutes(15)) // solo per aggiornare l'interfaccia grafica, non è davvero utile

        if (restaurantFetched == null || restaurantFetched.isEmpty()) {
            Log.e(TAG,"un fallimento per la mensa: ${menuAlarm.name}")
            return Job.Result.RESCHEDULE
        }
        // a questo punto c'è il ristorante ed il menu
        Log.d(TAG,"un successo per la mensa: ${menuAlarm.name}")

        sendMenuNotify(context, restaurantFetched)
        menuAlarm.remove()
        cancel() // rimuoviamo il job
        return Job.Result.SUCCESS

    }

    private fun getTimeInNMinutes(i: Int): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, i)
        return cal.time
    }


    companion object {

        val TAG = "UntilReleaseFetchJob"

        fun scheduleJob(menuAlarm: MenuAlarm) {
            Log.d(UntilReleaseFetchJob.TAG, "schedulo un  until release job")

            val extras = PersistableBundleCompat()
            extras.putLong(FetchJobUtils.MENU_ALARM_ID_KEY, menuAlarm.id)

            /**
             * ogni 10 minuti fa una richiesta, finchè il menu esce definitivamente. Ad ogni
             * scheduling verifica se c'è il menu:
             * - Se il menu è presente, allora elimina il menuAlarm e ferma il job
             * - Se il menu è assente viene rischedulato (al massimo fino alle 13!)
             */
            if (menuAlarm.untilMenuRelease) {
                val jobId = JobRequest.Builder(UntilReleaseFetchJob.TAG)
                        //.startNow()
                        //.setBackoffCriteria(5000,JobRequest.BackoffPolicy.LINEAR)
                        .setPeriodic(TimeUnit.MINUTES.toMillis(15))
                        // .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                        .setExtras(extras)
                        .build()
                        .schedule()
                menuAlarm.setJobId(jobId)
            } else {
                Log.e(TAG, "per errore si ha schedulato con until release un menu alarm giornaliero. correggo")
                DailyFetchJob.scheduleJob(menuAlarm)
            }
        }


    }
}