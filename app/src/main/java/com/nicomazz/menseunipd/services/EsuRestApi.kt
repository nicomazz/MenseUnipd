package com.nicomazz.menseunipd.services

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


/**
 * Created by Nicolò Mazzucato on 08/10/2017.
 */
class EsuRestApi {
    private val esuApi: EsuClient

    private val BASE_URL = "http://mobile.esupd.gov.it"

    init {

        val httpClient = OkHttpClient.Builder()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create()
                )
                .client(httpClient.build())
                .build()

        esuApi = retrofit.create(EsuClient::class.java)
    }

    fun getRestaurants(onSuccess: (List<Restaurant?>?) -> Unit,
                       onError: (String) -> Unit) {
        val call = esuApi.getInfo()

        call.enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(call: Call<List<Restaurant>>?, response: Response<List<Restaurant>>?) {
                cachedRestaurant = response?.body()
                onSuccess(response?.body())
            }

            override fun onFailure(call: Call<List<Restaurant>>?, t: Throwable?) {
                onError(t?.message ?: "Unknown error")
            }
        })
    }

    fun getRestaurant(name: String,
                      onSuccess: (Restaurant?) -> Unit,
                      onError: (String) -> Unit) {
        getRestaurants(onSuccess = { restaurants ->
            cachedRestaurant = restaurants
            val target = restaurants?.firstOrNull { it?.name?.toLowerCase()?.contains(name) ?: false }
            if (target != null)
                onSuccess(target)
            else onError("Restaurant not found!")
        }, onError = onError)
    }


    private interface EsuClient {
        @GET("/api/reiservice.svc/canteens?lang=it")
        fun getInfo(): Call<List<Restaurant>>
    }

    companion object {
        var cachedRestaurant: List<Restaurant?>? = null
    }
}