package com.nicomazz.menseunipd.adapter

import android.util.Log
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.nicomazz.menseunipd.R
import com.nicomazz.menseunipd.data.FavouriteManager
import com.nicomazz.menseunipd.services.Restaurant
import com.nicomazz.menseunipd.toHtml
import kotlinx.android.synthetic.main.restaurant_list_item.view.*
import kotlinx.android.synthetic.main.view_restaurant.view.*
import org.jetbrains.anko.wrapContent

/**
 * Created by Nicolò Mazzucato on 09/10/2017.
 */


class RestaurantListAdapter : BaseQuickAdapter<Restaurant, BaseViewHolder>(R.layout.restaurant_list_item) {

    override fun convert(viewHolder: BaseViewHolder, item: Restaurant) {
        viewHolder.itemView.restaurantView.setRestaurant(item)
        viewHolder.itemView.setOnLongClickListener {
            FavouriteManager.setFavourite( mContext, item.name)
            Toast.makeText(mContext,"Favourite setted!",Toast.LENGTH_SHORT).show()
            true
        }
    }
}