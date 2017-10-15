package com.nicomazz.menseunipd.data

import android.util.Log
import com.nicomazz.menseunipd.services.FetchJobUtils
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Nicolò Mazzucato on 09/10/2017.
 */
open class MenuAlarm(
        var name: String = "",
        var time: Date = Calendar.getInstance().time,
        private var weeksDay: Int = 0,
        var id: Long = System.currentTimeMillis(),
        private var jobId: Int? = null,
        val untilMenuRelease: Boolean = false // se a true, una volta uscito il menu (e inviata la notifica), finisce il rescheduling
) {

    private val TAG = "MenuAlarm"

    fun getStartMilliInDay(): Long {
        val millis =  TimeUnit.HOURS.toMillis(time.hour) + TimeUnit.MINUTES.toMillis(time.minute)
        Log.d(TAG,"millis for start this: $millis")
        return millis
    }

    fun updateTime(newTime: Date) {
        time = newTime
        MenuAlarmDataSource.updateItem(this)
    }

    fun updateTimeAndRescheduleIfNeeded(newTime: Date) {
        updateTime(newTime)
        rescheduleIfNeeded()
    }


    private fun get(mask: Int): Boolean = (weeksDay and mask) == mask

    private fun set(mask: Int, value: Boolean) {
        if (value) {
            weeksDay = weeksDay or mask
        } else {
            weeksDay = weeksDay and mask.inv()
        }
    }

    fun isActiveOnDay(day: Int) = get(1.shl(day)) // 0 = domenica

    fun isActiveToday(): Boolean {
        val cal = Calendar.getInstance()
        return isActiveOnDay(cal.get(Calendar.DAY_OF_WEEK)-1)
    }

    fun setActiveOnDay(active: Boolean, day: Int) {
        set(1.shl(day), active)
        MenuAlarmDataSource.updateItem(this)
    }

    fun toggleDay(day: Int) {
        setActiveOnDay(!isActiveOnDay(day), day)
    }

    fun setCanteenName(newName: String) {
        name = newName
        MenuAlarmDataSource.updateItem(this)
        rescheduleIfNeeded()
    }

    private fun rescheduleIfNeeded() {
        if (jobId == null) return // non era mai stato schedulato
        Log.d(TAG,"rischedulo!")
        schedule()
       // if (FetchJobUtils.isScheduled(jobId!!)) {

        /* }
         else {
             Log.d(TAG,"rischedulo!")
             jobId = null
         }*/
    }

    fun remove() {
        MenuAlarmDataSource.removeMenuAlarm(this)
        cancelJob()
    }

    fun setJobId(jobId: Int) {
        this.jobId = jobId
        MenuAlarmDataSource.updateItem(this)
    }

    fun cancelJob() {
        if (jobId == null) return
        FetchJobUtils.cancelJob(jobId!!)
        jobId = null
    }

    fun schedule() {
        cancelJob()
        FetchJobUtils.scheduleJob(this)
    }

    override fun equals(other: Any?): Boolean {
        if (other is MenuAlarm)
            return id == other.id
        return super.equals(other)
    }


}

private val Date.hour: Long
    get() {
        val calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.time = this;   // assigns calendar to given date
        return calendar.get(Calendar.HOUR_OF_DAY).toLong(); // gets hour in 24h format

    }

private val Date.minute: Long
    get() {
        val calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.time = this;   // assigns calendar to given date
        return calendar.get(Calendar.MINUTE).toLong(); // gets hour in 24h format

    }
