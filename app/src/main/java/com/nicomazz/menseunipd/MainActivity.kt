package com.nicomazz.menseunipd

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.nicomazz.menseunipd.services.EsuRestApi
import com.nicomazz.menseunipd.services.Restaurant
import kotlinx.android.synthetic.main.activity_main.*
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric



class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.name

    private var restaurantsFetched: List<Restaurant?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)


        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (getFavouriteRestaurantName() != null) {
            navigation.selectedItemId = R.id.navigation_favourite
        }
        getJsonInfo()

    }

    private fun getJsonInfo() {
        EsuRestApi().getRestaurants(onSuccess = { restarants ->
            restaurantsFetched = restarants
            val murialdo = restarants?.firstOrNull { it?.name?.toLowerCase()?.contains("murialdo") ?: false }

            Log.d(TAG, "received ${restarants?.size} restaurants!")
            message.text = murialdo?.menu.toString().toHtml()// ?: "No menu found"
        }, onError = { description ->
            message.text = "Error in request!"
            Log.e(TAG, "Error in retrieve restaurants: $description")
        })
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                setRestaurantList()
            }
            R.id.navigation_favourite -> {
                setFavouriteFragment()
            }
            R.id.navigation_notifications -> {
            }
        }
        true
    }

    //todo set favourite name
    private fun getFavouriteRestaurantName(): String? = "murialdo"

    private fun getFavouriteRestaurant(): Restaurant? {
        if (restaurantsFetched == null)
            return null
        return restaurantsFetched?.firstOrNull { it?.name?.toLowerCase()?.contains(getFavouriteRestaurantName() ?: "-") ?: false }
    }

    private fun setFavouriteFragment() {
        val frag = RestaurantFragment.newInstance(getFavouriteRestaurantName(), getFavouriteRestaurant())
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, frag).commit();
    }

    private fun setRestaurantList() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, RestaurantsListFragment()).commit();
    }

}
