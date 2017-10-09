package com.nicomazz.menseunipd

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nicomazz.menseunipd.adapter.RestaurantListAdapter
import com.nicomazz.menseunipd.services.EsuRestApi
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
        return rootView

    }

    fun initList() {
        rootView.list.layoutManager = LinearLayoutManager(context)
        val adapter = RestaurantListAdapter()
        adapter.bindToRecyclerView(rootView.list)
        adapter.setNewData(restaurants)

    }

    private fun fetchRestaurants() {
        EsuRestApi().getRestaurants(
                onSuccess = { fetchedRestaurant ->
                    restaurants = fetchedRestaurant
                    initList()
                },
                onError = { message ->
                    Toast.makeText(context, "Error in retrieve restaurant info: $message", Toast.LENGTH_LONG).show()
                })
    }

}