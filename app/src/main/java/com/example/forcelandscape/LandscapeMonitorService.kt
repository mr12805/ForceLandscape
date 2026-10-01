package com.example.forcelandscape

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Handler
import android.os.Looper

class LandscapeMonitorService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private val checkRunnable = object : Runnable {
        override fun run() {
            if (!RotationSession.isActive(this@LandscapeMonitorService)) {
                stopSelf()
                return
            }

            val target = RotationSession.targetPackage(this@LandscapeMonitorService)
            val foreground = ForegroundApp.find(this@LandscapeMonitorService)

            // System UI is temporarily shown when pulling down Quick Settings.
            // Keep the session alive while SystemUI is foreground.
            val temporarySystemUi = foreground == "com.android.systemui"

            if (!temporarySystemUi && target != null && foreground != target) {
                RotationSession.restore(this@LandscapeMonitorService)
                stopSelf()
                return
            }

            handler.postDelayed(this, 500)
        }
    }

    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            "rotation_monitor",
            "Rotation Monitor",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        val notification: Notification =
            Notification.Builder(this, "rotation_monitor")
                .setContentTitle("Force Landscape")
                .setContentText("Temporary landscape mode is active")
                .setSmallIcon(com.example.forcelandscape.R.drawable.ic_landscape)
                .setOngoing(true)
                .build()

        startForeground(1001, notification)
        handler.post(checkRunnable)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
