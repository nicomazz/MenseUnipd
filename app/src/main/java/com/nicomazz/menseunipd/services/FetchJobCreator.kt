package com.nicomazz.menseunipd.services

import android.support.annotation.Nullable
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator


/**
 * Created by Nicolò Mazzucato on 11/10/2017.
 */
class FetchJobCreator : JobCreator {

    @Nullable
    override fun create(tag: String): Job? {
        return when (tag) {
            UntilReleaseFetchJob.TAG -> UntilReleaseFetchJob()
            DailyFetchJob.TAG -> DailyFetchJob()
            else -> null
        }
    }
}