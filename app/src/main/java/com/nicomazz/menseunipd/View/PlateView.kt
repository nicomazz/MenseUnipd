package com.nicomazz.menseunipd.View

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.nicomazz.menseunipd.R
import com.nicomazz.menseunipd.toHtml
import kotlinx.android.synthetic.main.view_plate.view.*

class PlateView : FrameLayout {
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
        inflater.inflate(R.layout.view_plate, this, true)

        mRootView = getChildAt(0)
    }

    fun setLast(isLast: Boolean) {
        mRootView.separator.visibility = if (isLast) View.INVISIBLE else View.VISIBLE
    }

    fun setName(name: String) {
        mRootView.text.text = name.toHtml()
    }

}