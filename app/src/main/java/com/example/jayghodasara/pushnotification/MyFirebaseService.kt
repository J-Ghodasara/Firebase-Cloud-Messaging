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
import android.support.v4.app.NotificationManagerCompat
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
    lateinit var builder: Notification
    lateinit var builder2: Notification


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
        Log.i("message", notificationBody)
        Log.i("title", notificationTitle)

        if (p0!!.data["data_type"] == "Personal_Message") {
            notificationtrigger1(notificationTitle, notificationBody)
        } else {

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
                    .setContentIntent(pendingIntent).build()
            Log.i("first", "Triggered")
            notificationManager.notify(System.currentTimeMillis().toInt(), builder)
        }


    }

    fun notificationtrigger1(notificationTitle: String, notificationBody: String) {
        val notificationIntent = Intent(this, MessageActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


        val pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        var grp_key: String = "com.android.example.WORK_EMAIL"
        var grp_key2: String = "com.android.example.WORK_EMAIL"

        builder = NotificationCompat.Builder(this, channelid)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setGroup(grp_key)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent).build()
        Log.i("first", "Triggered")


        val summaryNotification = NotificationCompat.Builder(this@MyFirebaseService, channelid)
                .setContentTitle("Push Notification FCM")
                .setContentText("Messages")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setGroup(grp_key)
                .setGroupSummary(true)
                .build()

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder)
        notificationManager.notify(3, summaryNotification)
    }


}