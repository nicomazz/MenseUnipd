package com.nicomazz.menseunipd

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nicomazz.menseunipd.adapter.AlarmListAdapter
import com.nicomazz.menseunipd.data.MenuAlarm
import com.nicomazz.menseunipd.data.MenuAlarmDataSource
import kotlinx.android.synthetic.main.fragment_notification.view.*


class NotificationFragment : Fragment() {

    lateinit var rootView: View
    var adapter: AlarmListAdapter? = null
    var items: List<MenuAlarm>? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_notification, container, false)

        initList()
        initAddFab()
        activity?.title = getString(R.string.notification)
        return rootView
    }

    private fun initAddFab() {
        rootView.addMenuAlarm.setOnClickListener {
            if (items?.size ?: 100 > 5) {
                Toast.makeText(activity, getString(R.string.schedule_limit), Toast.LENGTH_SHORT).show()
            } else {
                MenuAlarmDataSource.add(context)
                updateListItems()
            }
            // updateListItems()
        }
    }

    fun initList() {
        rootView.list.layoutManager = LinearLayoutManager(context)
        adapter = AlarmListAdapter(activity, { updateListItems() }).apply {
            bindToRecyclerView(rootView.list)
        }
        updateListItems()
    }

    private fun updateListItems() {
        items = MenuAlarmDataSource.getAll(context)
        adapter?.setNewData(items)
    }


}
