package com.example.shawn.anjeonmingug

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.jvm.java

class notiService : FirebaseMessagingService(){
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: " + remoteMessage!!.data);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage!!.getFrom());
        val type = remoteMessage.data["type"]
        // Check if message contains a data payload.
        if (remoteMessage!!.getData().size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification()!!.getBody());
        }
        when (type) {
            "HOMEACTIVITY" -> {
                val customerRebate = Intent(this, HomeActivity::class.java)
                customerRebate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                customerRebate.putExtra("check value", "soicem")
                val customerRebatePendingIntent = PendingIntent.getActivity(this, 0, customerRebate,
                        PendingIntent.FLAG_ONE_SHOT)
                val customerRebateBuilder = NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentIntent(customerRebatePendingIntent)
                val customerRebateManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                customerRebateManager.notify(0, customerRebateBuilder.build())
            }
        }
    }
}

