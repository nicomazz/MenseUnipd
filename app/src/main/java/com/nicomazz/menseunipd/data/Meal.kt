package com.nicomazz.menseunipd.data

import com.nicomazz.menseunipd.services.Food
import com.nicomazz.menseunipd.toHtml

open class Meal {
    val secondcourse: List<Food?>? = null
    val sideorder: List<Food?>? = null
    val dessert: List<Food?>? = null
    val maincourse: List<Food?>? = null

    open fun getName() = "Meal"

    override fun toString(): String {
        return StringBuilder().apply {
            append("<h3>${getName()}:</h3><br>")
            append(toCourseString())
            append("<br>")
        }.toString()
    }

    fun toCourseString() : String{
        return StringBuilder().apply {
            append("<b>Main:</b> ${maincourse.toString()}")
            append("<br>")
            append("<b>Second:</b> "+secondcourse.toString())
            append("<br>")
            append("<b>Side:</b> "+sideorder.toString())
            append("<br>")
            append("<b>Dessert:</b> "+dessert.toString())
        }.toString()
    }

    fun isEmpty(): Boolean{
        return maincourse?.isEmpty() ?: true
    }
}


class Lunch : Meal(){
    override fun getName() = "Lunch"
}

class Dinner : Meal() {
    override fun getName() = "Dinner"
}


fun List<Food?>?.toString(): String = this?.joinToString { it.toString() } ?: ""