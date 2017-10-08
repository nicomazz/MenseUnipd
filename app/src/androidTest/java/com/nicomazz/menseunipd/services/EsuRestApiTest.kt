package com.nicomazz.menseunipd.services

import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by Nicolò Mazzucato on 08/10/2017.
 */
class EsuRestApiTest {

    @Test
    fun getInfo() {
        val countdown = CountDownLatch(1)

        val api = EsuRestApi()

        var message = ""
        var successfull = true

        api.getRestaurants(
                onSuccess = { list ->
                    if (list == null || list.isEmpty()) {
                        message = "Empty list!"
                        successfull = false
                    } else
                        message = "parsed ${list.size} restaurants"
                    countdown.countDown()
                },
                onError = { why ->
                    message = "error: $why"
                    successfull = false
                    countdown.countDown()
                })
        countdown.await(10, TimeUnit.SECONDS)


        if (!successfull)
            throw Exception(message)

    }

}