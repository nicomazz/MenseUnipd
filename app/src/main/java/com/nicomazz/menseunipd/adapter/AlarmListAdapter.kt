package com.nicomazz.menseunipd.adapter

import android.app.Activity
import android.app.TimePickerDialog
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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
import kotlinx.android.synthetic.main.view_week.view.*
import org.jetbrains.anko.forEachChildWithIndex
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onFocusChange


/**
 * Created by Nicolò Mazzucato on 09/10/2017.
 */


class AlarmListAdapter(private val activity: Activity,
                       private val updateFun: () -> Unit = {})
    : BaseQuickAdapter<MenuAlarm, BaseViewHolder>(R.layout.menu_alarm_layout) {

    override fun convert(viewHolder: BaseViewHolder, item: MenuAlarm) {
        with(viewHolder.itemView) {

            Handler().post {
                setupName(canteenName, item)
                setupWeekDays(group_weekdays, item)
                updateTime(timeLabel, item)
                setupTimeLabel(timeLabel, item)
                setupDelete(delete, item)

                weeks_day.visibility = if (item.untilMenuRelease) View.GONE else View.VISIBLE
                untilMenuTextView.visibility = if (item.untilMenuRelease) View.VISIBLE else View.GONE
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
            updateFun()
        }
    }

    private fun setupName(canteenName: AutoCompleteTextView, item: MenuAlarm) {
        canteenName.setText(item.name)
        canteenName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                item.setCanteenName(s.toString())
            }
        })
        val mense = mContext.resources.getStringArray(R.array.mense)
        val adapter = ArrayAdapter<String>(mContext,
                android.R.layout.simple_dropdown_item_1line, mense)
        canteenName.setAdapter<ArrayAdapter<String>>(adapter)
        canteenName.threshold = 0
        //canteenName.onClick { canteenName.showDropDown() }
        canteenName.onFocusChange { v, hasFocus -> if (hasFocus) canteenName.showDropDown() }
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
                        item.updateTimeAndRescheduleIfNeeded(cal.time)
                        updateTime(timeLabel, item)
                    }, hour, minute, true)
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()

        }
    }
}