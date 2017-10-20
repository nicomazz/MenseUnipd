package com.nicomazz.menseunipd

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.nicomazz.menseunipd.adapter.RestaurantListAdapter
import com.nicomazz.menseunipd.data.TimeSavedManager
import com.nicomazz.menseunipd.services.EsuRestApi
import io.fabric.sdk.android.services.common.Crash
import kotlinx.android.synthetic.main.fragment_restaurant_list.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantsListFragment : Fragment() {

    private var restaurants = EsuRestApi.cachedRestaurant

    private lateinit var rootView: View


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater!!.inflate(R.layout.fragment_restaurant_list, container, false)

        activity.title = getString(R.string.app_name)

        if (restaurants != null) {
            initList()
        } else fetchRestaurants()

        rootView.swipeRefresh.setOnRefreshListener {
            fetchRestaurants()
        }
        return rootView

    }

    fun initList() {
        rootView.list.layoutManager = LinearLayoutManager(context)
        val adapter = RestaurantListAdapter()
        adapter.bindToRecyclerView(rootView.list)
        val sorted = ArrayList(restaurants).sortedBy { it?.menu?.isEmpty() }

        adapter.setNewData(sorted)

    }

    private fun fetchRestaurants() {
        rootView.swipeRefresh.isRefreshing = true
        EsuRestApi().getRestaurants(
                onSuccess = { fetchedRestaurant ->
                    rootView.swipeRefresh.isRefreshing = false
                    restaurants = fetchedRestaurant
                    initList()
                },
                onError = { message ->
                    rootView.swipeRefresh.isRefreshing = false
                    Crashlytics.log(message)
                    Crashlytics.logException(Exception("Unable to download data!"))
                    Toast.makeText(context, getString(R.string.error_data_download), Toast.LENGTH_LONG).show()
                },
                onTime = { time ->
                    activity?.let {
                        Toast.makeText(it, "request time: $time ms", Toast.LENGTH_SHORT).show()
                        TimeSavedManager.addRequestTime(time,it)
                    }
                })
    }

}