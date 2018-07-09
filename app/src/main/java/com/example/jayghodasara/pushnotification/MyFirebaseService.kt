package com.example.jayghodasara.pushnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService : FirebaseMessagingService() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationchannel: NotificationChannel
    lateinit var notification: Notification
    private val channelid = "com.example.jayghodasara.pushnotification"
    lateinit var builder: NotificationCompat.Builder


    override fun onNewToken(p0: String?) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(p0: RemoteMessage?) {
        var notificationBody: String = ""
        var notificationTitle: String = ""
        var notificationData: String = ""

        try {
            notificationData = p0!!.data.toString()
            notificationBody = p0.data["message"].toString()
            notificationTitle = p0.data["title"].toString()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }



        Log.i("data", notificationData)
        Log.i("body", notificationBody)
        Log.i("title", notificationTitle)


        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


        val pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        builder = NotificationCompat.Builder(this, channelid)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)

        notificationManager.notify(111, builder.build())
    }


}