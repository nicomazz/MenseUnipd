package com.nicomazz.menseunipd.adapter

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.nicomazz.menseunipd.R
import com.nicomazz.menseunipd.data.Meal

/**
 * Created by Nicolò Mazzucato on 09/10/2017.
 */


class PlatesAdapter : BaseQuickAdapter<Meal, BaseViewHolder>(R.layout.view_plates_list) {

    override fun convert(viewHolder: BaseViewHolder, item: Meal) {
        Log.d(TAG, "call convert!");
        with(viewHolder.itemView) {
        //   food.
        }

    }
}