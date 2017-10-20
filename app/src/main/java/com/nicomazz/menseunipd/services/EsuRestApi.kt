package com.nicomazz.menseunipd.services

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


/**
 * Created by Nicolò Mazzucato on 08/10/2017.
 */
class EsuRestApi {
    private val esuApi: EsuClient

    //private val BASE_URL = "http://mobile.esupd.gov.it"
    private val BASE_URL = "http://taptaptap.altervista.org"
   // private val BASE_URL = "https://jsonplaceholder.typicode.com"

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
                       onError: (String) -> Unit,
                       onTime: (Long) -> Unit = {}) { // millis impiegati
        val call = esuApi.getInfo()

        val startMillis = System.currentTimeMillis()
        call.enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(call: Call<List<Restaurant>>?, response: Response<List<Restaurant>>?) {

                onTime(System.currentTimeMillis() - startMillis)
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
                      onError: (String) -> Unit,
                      onTime: (Long) -> Unit = {}) {
        getRestaurants(onSuccess = { restaurants ->
            cachedRestaurant = restaurants
            val target = restaurants?.firstOrNull { it?.name?.toLowerCase()?.contains(name.trim().toLowerCase()) ?: false }
            if (target != null)
                onSuccess(target)
            else onError("Restaurant not found!")
        }, onError = onError,
                onTime = onTime)
    }

    fun getRestaurantSync(name: String): Restaurant? {
        val cowndownLatch = CountDownLatch(1)
        var target: Restaurant? = null
        getRestaurants(onSuccess = { restaurants ->
            cachedRestaurant = restaurants
            target = restaurants?.firstOrNull { it?.name?.toLowerCase()?.contains(name.trim().toLowerCase()) ?: false }
            Log.d("EsuApi","Target: $target")
            cowndownLatch.countDown()
        }, onError = { cowndownLatch.countDown() })
        cowndownLatch.await(20, TimeUnit.SECONDS)
        return target
    }


    private interface EsuClient {
        //  @GET("/api/reiservice.svc/canteens?lang=it")
        @GET("/ristoresu/cacher.php")
      //  @GET("/posts/1")
        fun getInfo(): Call<List<Restaurant>>
    }

    // http://mobile.esupd.gov.it/api/reiservice.svc/canteens?lang=it
// http://taptaptap.altervista.org/ristoresu/cacher.php
    companion object {
        var cachedRestaurant: List<Restaurant?>? = null
    }
}