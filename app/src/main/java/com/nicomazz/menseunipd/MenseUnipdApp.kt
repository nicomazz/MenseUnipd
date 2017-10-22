package com.nicomazz.menseunipd

import android.app.Application
import com.evernote.android.job.JobManager
import com.nicomazz.menseunipd.services.FetchJobCreator


/**
 * Created by Nicolò Mazzucato on 10/10/2017.
 * banner url: https://www.norio.be/android-feature-graphic-generator/?config=%7B%22background%22%3A%7B%22color%22%3A%22%232b95ff%22%2C%22gradient%22%3A%7B%22type%22%3A%22radial%22%2C%22radius%22%3A%22600%22%2C%22angle%22%3A%22vertical%22%2C%22color%22%3A%22%23001837%22%7D%7D%2C%22title%22%3A%7B%22text%22%3A%22Mense%20Unipd%22%2C%22position%22%3A86%2C%22color%22%3A%22%23f7f6ff%22%2C%22size%22%3A200%2C%22font%22%3A%7B%22family%22%3A%22sans-serif%22%2C%22effect%22%3A%22bold%22%7D%7D%2C%22subtitle%22%3A%7B%22text%22%3A%22Ricevi%20aggiornamenti%20e%20notifiche%20sulle%20mense%20a%20padova%22%2C%22color%22%3A%22%23fdfefd%22%2C%22size%22%3A78%2C%22offset%22%3A14%2C%22font%22%3A%7B%22family%22%3A%22sans-serif%22%2C%22effect%22%3A%22normal%22%7D%7D%2C%22image%22%3A%7B%22position%22%3A%220.5%22%2C%22positionX%22%3A%220.5%22%2C%22scale%22%3A%221%22%7D%2C%22size%22%3A%22feature-graphic%22%7D
 */
class MenseUnipdApp : Application() {

    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(FetchJobCreator())

    }

}