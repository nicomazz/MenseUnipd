package com.nicomazz.menseunipd.services

import com.nicomazz.menseunipd.data.Dinner
import com.nicomazz.menseunipd.data.Lunch

data class Menu(
        val lunch: Lunch? = null,
        val dinner: Dinner? = null
){
    override fun toString(): String = lunch.toString()+"<br>"+dinner.toString()
}
