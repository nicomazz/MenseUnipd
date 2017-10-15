package com.nicomazz.menseunipd.View

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.nicomazz.menseunipd.R
import com.nicomazz.menseunipd.services.Food
import kotlinx.android.synthetic.main.view_plates_list.view.*

class SectionedDetailsView : FrameLayout {
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
        inflater.inflate(R.layout.view_plates_list, this, true)

        mRootView = getChildAt(0)
    }

    fun setCourse(course: List<Food?>?, courseName: String) {
        title.text = courseName
        val platesLayout = mRootView.plates_container
        platesLayout.removeAllViews()
        course?.forEach {
            platesLayout.addView(PlateView(context).apply { setName(it?.name ?: "-") })
        }
        //(platesLayout.getChildAt(platesLayout.childCount - 1) as PlateView).setLast(true)
    }


}