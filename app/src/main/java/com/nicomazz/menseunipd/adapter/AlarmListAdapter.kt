package com.nicomazz.menseunipd.adapter

import android.app.Activity
import android.app.TimePickerDialog
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.nicomazz.menseunipd.R
import com.nicomazz.menseunipd.View.MyCircularToggle
import com.nicomazz.menseunipd.data.MenuAlarm
import kotlinx.android.synthetic.main.menu_alarm_layout.view.*
import kotlinx.android.synthetic.main.week_view.view.*
import org.jetbrains.anko.forEachChildWithIndex
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Nicolò Mazzucato on 09/10/2017.
 */


class AlarmListAdapter(val activity: Activity)
    : BaseQuickAdapter<MenuAlarm, BaseViewHolder>(R.layout.menu_alarm_layout) {

    override fun convert(viewHolder: BaseViewHolder, item: MenuAlarm) {
        Log.d(TAG, "call convert!");
        with(viewHolder.itemView) {

            Handler().post {
                setupName(canteenName, item)
                setupWeekDays(group_weekdays, item)
                updateTime(timeLabel, item)
                setupTimeLabel(timeLabel, item)
                setupDelete(delete, item)
            }

        }

    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return data[position].id
    }

    private fun setupDelete(delete: ImageButton?, item: MenuAlarm) {
        delete?.setOnClickListener {
            item.remove()
        }
    }

    private fun setupName(canteenName: EditText, item: MenuAlarm) {
        canteenName.setText(item.name)
        canteenName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (item.isValid)
                    item.setCanteenName(s.toString())
            }

        })
    }

    private fun updateTime(textView: TextView, item: MenuAlarm) {
        val localDateFormat = SimpleDateFormat("HH:mm")
        val time = localDateFormat.format(item.time)
        textView.text = time
    }

    fun setupWeekDays(container: ViewGroup, item: MenuAlarm) {
        val daysName = DateFormatSymbols().shortWeekdays

        val weeks_days = container
        weeks_days.forEachChildWithIndex { i, view ->
            val toggle = view as MyCircularToggle
            toggle.setCheckedWithoutAnimation(item.isActiveOnDay(i))
            toggle.text = daysName[i + 1]
            toggle.setOnClickListener {
                item.toggleDay(i)
                toggle.isChecked = item.isActiveOnDay(i)
            }
        }
    }



    private fun setupTimeLabel(timeLabel: TextView, item: MenuAlarm) {
        timeLabel.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog


            mTimePicker = TimePickerDialog(activity,
                    TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, selectedHour)
                        cal.set(Calendar.MINUTE, selectedMinute)
                        item.updateTime(cal.time)
                        updateTime(timeLabel, item)
                    }, hour, minute, true)
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()

        }
    }
}