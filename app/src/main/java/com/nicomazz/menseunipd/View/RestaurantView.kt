package com.nicomazz.menseunipd.View

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.Toast
import com.nicomazz.menseunipd.R
import com.nicomazz.menseunipd.data.FavouriteManager
import com.nicomazz.menseunipd.data.Meal
import com.nicomazz.menseunipd.data.MenuAlarm
import com.nicomazz.menseunipd.data.MenuAlarmDataSource
import com.nicomazz.menseunipd.services.Restaurant
import kotlinx.android.synthetic.main.view_restaurant.view.*

class RestaurantView : FrameLayout {
    private val TAG = "RestaurantView"


    private lateinit var mRootView: View
    private var restaurant: Restaurant? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        layoutParams = generateDefaultLayoutParams()

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_restaurant, this, true)

        mRootView = getChildAt(0)
        setupMenu()

    }

    fun expand() {
        mealContainer.visibility = View.VISIBLE
        expand_collapse.setImageResource(R.drawable.ic_expand_less_white_24dp)
    }

    fun collapse() {
        mealContainer.visibility = View.GONE
        expand_collapse.setImageResource(R.drawable.ic_expand_more_white_24dp)
    }

    fun setRestaurant(item: Restaurant) {
        restaurant = item
        with(mRootView) {
            mealContainer.removeAllViews()
            generateMealViews(item.menu?.lunch).forEach { mealContainer.addView(it) }

            restaurantName.text = item.name
            setupButtonNotifyVisibility(item)

            setupStarButton(item)

            setupExpandCollapse()

        }
    }

    private fun setupMenu() {
        val popup = PopupMenu(context, mRootView.menu)


        popup.menuInflater.inflate(R.menu.restaurant_menu, popup.menu);
        popup.setOnMenuItemClickListener {
            FavouriteManager.setFavourite(context, restaurant?.name ?: "")
            true
        }
        rootView.menu.setOnClickListener { popup.show() }
    }

    private fun setupExpandCollapse() {
        restaurantName.setOnClickListener {
            if (mealContainer.visibility == View.VISIBLE) {
                collapse()
            } else {
                expand()
            }
        }
    }

    private fun setupStarButton(item: Restaurant) {
        with(mRootView.star_button) {
            isLiked = FavouriteManager.getFavourite(context).contains(item.name ?: "-")
            setOnClickListener {
                FavouriteManager.setFavourite(context, item.name)
            }
        }
    }

    fun generateMealViews(item: Meal?): List<View> {
        if (item == null || item.isEmpty()) return ArrayList()
        return ArrayList<View>().apply {
            add(SectionedDetailsView(context).apply { setCourse(item.maincourse, context.getString(R.string.main)) })
            add(SectionedDetailsView(context).apply { setCourse(item.secondcourse, context.getString(R.string.second)) })
            add(SectionedDetailsView(context).apply { setCourse(item.sideorder, context.getString(R.string.side)) })
            add(SectionedDetailsView(context).apply { setCourse(item.dessert, context.getString(R.string.dessert)) })
        }
    }


    // se ci sono elementi con il nome in auto scheduling allora non aggiungo
    private fun setupButtonNotifyVisibility(item: Restaurant) {
        mRootView.buttonNotifyMenu.visibility =
                if (item.isEmpty() && MenuAlarmDataSource.hasAlarmUntilMenuForRestaurantNamed(context, item.name) == false)
                    View.VISIBLE
                else View.GONE

        buttonNotifyMenu.setOnClickListener {
            if (!MenuAlarmDataSource.hasAlarmUntilMenuForRestaurantNamed(context, item.name)) {
                val menuAlarm = MenuAlarm(name = item.name!!, untilMenuRelease = true)
                MenuAlarmDataSource.add(context, menuAlarm)
                menuAlarm.schedule()
                buttonNotifyMenu.visibility = View.GONE
                Toast.makeText(context, "Scheduled", Toast.LENGTH_SHORT).show()
            }

        }
    }


}