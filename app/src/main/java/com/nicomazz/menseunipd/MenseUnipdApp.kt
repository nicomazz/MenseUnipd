package com.nicomazz.menseunipd

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration


/**
 * Created by Nicolò Mazzucato on 10/10/2017.
 */
class MenseUnipdApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("menseUnipd.realm").build()
        Realm.setDefaultConfiguration(config)
    }
}