package com.nicomazz.menseunipd.View

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.nex3z.togglebuttongroup.button.CircularToggle

/**
 * Created by Nicolò Mazzucato on 10/10/2017.
 */
class MyCircularToggle(context: Context?, attrs: AttributeSet?) : CircularToggle(context, attrs) {

    override  fun getCheckedTextColor(): Int {
        return Color.WHITE
    }
    fun setCheckedWithoutAnimation(checked: Boolean) {
        super.setChecked(checked)
        val defaultTextColor = defaultTextColor
        val checkedTextColor = Color.WHITE
        if (checked) {
            mIvBg.visibility = View.VISIBLE
            mIvBg.scaleX = 1f
            mIvBg.scaleY = 1f
            mTvText.setTextColor(checkedTextColor)
        } else {
            mIvBg.visibility = View.INVISIBLE
            mTvText.setTextColor(defaultTextColor)
        }
    }
}