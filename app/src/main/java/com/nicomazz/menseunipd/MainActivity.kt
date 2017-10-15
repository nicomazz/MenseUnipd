package com.nicomazz.menseunipd

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.nicomazz.menseunipd.data.FavouriteManager
import com.nicomazz.menseunipd.services.EsuRestApi
import com.nicomazz.menseunipd.services.Restaurant
import io.fabric.sdk.android.Fabric
import io.fabric.sdk.android.services.common.Crash
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.name

    private var restaurantsFetched: List<Restaurant?>? = null

    private var pageId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(Fabric.Builder(this).kits(Crashlytics()).debuggable(true).build())
        setContentView(R.layout.activity_main)


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (getFavouriteRestaurantName() != null) {
            navigation.selectedItemId = R.id.navigation_favourite
        }
        getJsonInfo()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
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
        }, onTime = { time ->
            Toast.makeText(this, "request time: $time ms", Toast.LENGTH_SHORT).show()
        })
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId != pageId) {
            pageId = item.itemId
            when (item.itemId) {
                R.id.navigation_home -> {
                    setRestaurantList()
                }
                R.id.navigation_favourite -> {
                    setFavouriteFragment()
                }
                R.id.navigation_notifications -> {
                    setNotificationFragment()

                }
            }
        }
        true
    }

    private fun setNotificationFragment() {
        val frag = NotificationFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, frag).commit(); }

    private fun getFavouriteRestaurantName(): String? = FavouriteManager.getFavourite(this)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.info -> showInfoDialog()
            else -> {
            }
        }
        return true
    }

    private fun showInfoDialog() {

        val s = SpannableString("Source code and more informations at: github.com/nicomazz/MenseUnipd");
        Linkify.addLinks(s, Linkify.ALL);

        val dialog = AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage(s)
                .setPositiveButton("Ok", { _, _ -> })
                .create()

        dialog.show()

        (dialog.findViewById<TextView>(android.R.id.message))?.movementMethod = LinkMovementMethod.getInstance()

    }
}
