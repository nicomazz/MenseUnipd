package com.nicomazz.menseunipd.services

data class Food(
	val celiac: Boolean? = null,
	val pdg: Boolean? = null,
	val name: String? = null,
	val link: String? = null,
	val vegetarian: Boolean? = null,
	val frozen: Boolean? = null,
	val id: String? = null
){
    override fun toString() = name ?: "-"
}
