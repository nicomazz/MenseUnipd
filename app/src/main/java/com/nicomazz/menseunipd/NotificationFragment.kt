package com.nicomazz.menseunipd

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nicomazz.menseunipd.adapter.AlarmListAdapter
import com.nicomazz.menseunipd.data.MenuAlarm
import com.nicomazz.menseunipd.data.MenuAlarmDataSource
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_notification.view.*


class NotificationFragment : Fragment() {

    lateinit var rootView: View
    var adapter: AlarmListAdapter? = null
    var items: RealmResults<MenuAlarm>? = null
    var adapterAttached = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initItemsData()
    }

    private fun initItemsData() {
        items = MenuAlarmDataSource.getAllMenuAlarms()
        items?.removeAllChangeListeners()
        items?.addChangeListener { t, changeSet ->
            Log.d("NotificationFrag", "Items updated")
            if (changeSet.deletions.isNotEmpty() || changeSet.insertions.isNotEmpty()) {
                Log.d("NotificationFrag", "MEtto nuovi dati! size: ${t.size} (prima era ${items?.size}")
                adapter?.setNewData(t)
            }
            // adapter?.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_notification, container, false)
        //MenuAlarmDataSource.removeAll()
        initList()
        initAddFab()
        activity?.title = "Notification"
        return rootView
    }

    private fun initAddFab() {
        rootView.addMenuAlarm.setOnClickListener {
            if (items?.size ?: 100 > 5) {
                Toast.makeText(activity, "Too many schedule!", Toast.LENGTH_SHORT).show()
            } else
                MenuAlarmDataSource.addMenuAlarm()
            // updateListItems()
        }
    }

    fun initList() {
        rootView.list.layoutManager = LinearLayoutManager(context)
        adapter = AlarmListAdapter(activity).apply {
            bindToRecyclerView(rootView.list)
        }
        adapter?.setNewData(items)
        adapterAttached = true
    }

    override fun onDestroy() {
        super.onDestroy()
        items?.removeAllChangeListeners()
    }


}
