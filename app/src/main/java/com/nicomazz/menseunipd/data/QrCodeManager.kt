package com.nicomazz.menseunipd.data

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager

object QrCodeManager {

    private val QR_CODE_KEY = "QR_CODE"
    fun getQrCodeString(context: Context) =
        getPref(context).getString(QR_CODE_KEY, "")

    @SuppressLint("ApplySharedPref")
    fun setQrCodeString(context: Context, name: String) {
        getPref(context).edit().putString(QR_CODE_KEY, name).commit()
    }

    fun hasQrCodeString(context: Context) = !getQrCodeString(context).isNullOrEmpty()

    private fun getPref(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)


}