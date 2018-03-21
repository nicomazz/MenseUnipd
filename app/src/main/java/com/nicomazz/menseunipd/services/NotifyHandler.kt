package com.nicomazz.menseunipd.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.nicomazz.menseunipd.MainActivity
import com.nicomazz.menseunipd.R
import com.nicomazz.menseunipd.toHtml
import android.app.NotificationChannel
import android.graphics.Color
import android.os.Build
import android.util.Log


/**
 * Created by Nicolò Mazzucato on 11/10/2017.
 */
fun sendMenuNotify(context: Context, restaurant: Restaurant) {

    val NOTIFY_ID = "ORARI_MENSE_UNIPD";
    val resultIntent = Intent(context, MainActivity::class.java)
    resultIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

// Because clicking the notification launches a new ("special") activity,
// there's no need to create an artificial back stack.
    val resultPendingIntent = PendingIntent.getActivity(
            context,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )
    val text = restaurant.menu?.lunch?.toCourseString()?.toHtml() ?: ""
    val mBuilder = NotificationCompat.Builder(context, "MenuNotify")
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle("${restaurant.name} menu")
            .setContentIntent(resultPendingIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(text))
            .setContentText(text)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        /* Create or update. */
        val channel = NotificationChannel(NOTIFY_ID,
            "Notifiche per i menu delle mense di padova",
            NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        mBuilder.setChannelId(NOTIFY_ID)

    }

    notificationManager.notify(12411, mBuilder.build())

}