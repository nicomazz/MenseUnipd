package com.nicomazz.menseunipd

import android.app.Application
import com.evernote.android.job.JobManager
import com.nicomazz.menseunipd.services.FetchJobCreator


/**
 * Created by Nicolò Mazzucato on 10/10/2017.
 */
class MenseUnipdApp : Application() {

    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(FetchJobCreator())

    }

}