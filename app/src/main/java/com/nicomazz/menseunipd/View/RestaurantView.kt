package com.nicomazz.menseunipd.View

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.nicomazz.menseunipd.R
import com.nicomazz.menseunipd.services.Restaurant
import com.nicomazz.menseunipd.toHtml
import kotlinx.android.synthetic.main.restaurant_view.view.*

class RestaurantView : FrameLayout {
    private val TAG = "RestaurantView"


    private lateinit var mRootView: View


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
        inflater.inflate(R.layout.restaurant_view, this, true)

        mRootView = getChildAt(0)
    }

    fun setRestaurant(item: Restaurant) {
        with(mRootView) {
            lunch_text.text = item.menu?.lunch?.toCourseString()?.toHtml()
            dinner_text.text = item.menu?.dinner?.toCourseString()?.toHtml()
            restaurantName.text = item.name
        }
    }


}