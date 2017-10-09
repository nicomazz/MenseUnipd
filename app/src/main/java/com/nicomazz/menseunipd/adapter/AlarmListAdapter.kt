package com.nicomazz.menseunipd.adapter

import android.app.Activity
import android.app.TimePickerDialog
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.nex3z.togglebuttongroup.button.CircularToggle
import com.nicomazz.menseunipd.R
import com.nicomazz.menseunipd.data.MenuAlarm
import kotlinx.android.synthetic.main.alarm_layout.view.*
import kotlinx.android.synthetic.main.week_view.view.*
import org.jetbrains.anko.forEachChildWithIndex
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Nicolò Mazzucato on 09/10/2017.
 */


class AlarmListAdapter(val activity: Activity)
    : BaseQuickAdapter<MenuAlarm, BaseViewHolder>(R.layout.alarm_layout) {

    override fun convert(viewHolder: BaseViewHolder, item: MenuAlarm) {
        Log.d(TAG, "call convert!");
        with(viewHolder.itemView) {
            canteenName.setText(item.name)

            val daysName = DateFormatSymbols().shortWeekdays

            val weeks_days = group_weekdays
            weeks_days.forEachChildWithIndex { i, view ->
                val toggle = view as CircularToggle
                toggle.isChecked = item.weeksDay[i]
                toggle.text = daysName[i+1]
            }

            val localDateFormat = SimpleDateFormat("HH:mm")
            val time = localDateFormat.format(item.time)
            timeLabel.text = time

            timeLabel.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                val minute = mcurrentTime.get(Calendar.MINUTE)
                val mTimePicker: TimePickerDialog


                mTimePicker = TimePickerDialog(activity,
                        TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                           timeLabel.text = "$selectedHour:$selectedMinute"
                        }, hour, minute, true)//Yes 24 hour time
                mTimePicker.setTitle("Select Time")
                mTimePicker.show()

            }
        }

    }
}