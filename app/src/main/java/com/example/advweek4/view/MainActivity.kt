package com.example.advweek4.view

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.advweek4.R
import com.example.advweek4.databinding.ActivityMainBinding
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.example.advweek4.util.createNotificationChannel


class MainActivity : AppCompatActivity() {
    init {
        instance = this
    }
    companion object {
        private var instance:MainActivity ?= null

        fun showNotification(title: String, content: String, icon: Int) {
            val channelId = "${instance?.packageName}-${instance?.getString(R.string.app_name)}"
            val notificationId = 1001

            // Check if the app has permission to post notifications
            if (ActivityCompat.checkSelfPermission(instance!!, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                // If permission is not granted, request it from the user
                ActivityCompat.requestPermissions(instance!!, arrayOf(Manifest.permission.POST_NOTIFICATIONS), notificationId)
                return
            }

            // Build and display the notification
            val notificationBuilder = NotificationCompat.Builder(instance!!.applicationContext, channelId)
                .apply {
                    setSmallIcon(icon)
                    setContentTitle(title)
                    setContentText(content)
                    setStyle(NotificationCompat.BigTextStyle())
                    priority = NotificationCompat.PRIORITY_DEFAULT
                    setAutoCancel(true)
                }

            val notificationManager = NotificationManagerCompat.from(instance!!.applicationContext)
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel(this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel.")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}