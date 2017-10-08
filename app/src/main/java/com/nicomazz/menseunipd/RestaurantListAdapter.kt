package com.nicomazz.menseunipd

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.nicomazz.menseunipd.services.Restaurant
import kotlinx.android.synthetic.main.restaurant_list_item.view.*

/**
 * Created by Nicolò Mazzucato on 09/10/2017.
 */


class RestaurantListAdapter : BaseQuickAdapter<Restaurant, BaseViewHolder>(R.layout.restaurant_list_item) {

    override fun convert(viewHolder: BaseViewHolder, item: Restaurant) {
        Log.d(TAG, "call convert!");
        with(viewHolder.itemView) {
            menuText.text = item.menu.toString().toHtml()
            restaurantName.text = item.name
        }

    }
}