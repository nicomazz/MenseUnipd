package com.nicomazz.menseunipd

import android.app.AlarmManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicomazz.menseunipd.adapter.AlarmListAdapter
import com.nicomazz.menseunipd.adapter.RestaurantListAdapter
import com.nicomazz.menseunipd.data.MenuAlarm
import kotlinx.android.synthetic.main.fragment_restaurant_list.view.*
import java.util.*


class NotificationFragment : Fragment() {

    lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView =  inflater!!.inflate(R.layout.fragment_notification, container, false)
        initList()
        return rootView
    }

    fun initList() {
        rootView.list.layoutManager = LinearLayoutManager(context)
        val adapter = AlarmListAdapter(activity)
        adapter.bindToRecyclerView(rootView.list)

        val dummyData = arrayListOf<MenuAlarm>(MenuAlarm("Murialdo",Calendar.getInstance().time))
        adapter.setNewData(dummyData)

    }

}// Required empty public constructor
