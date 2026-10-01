package com.example.forcelandscape

import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Process

object ForegroundApp {
    fun find(context: Context): String? {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        if (mode != AppOpsManager.MODE_ALLOWED) return null

        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val end = System.currentTimeMillis()
        val begin = end - 10_000
        val events = usm.queryEvents(begin, end)
        val event = UsageEvents.Event()
        var latestPackage: String? = null
        var latestTime = 0L

        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if ((event.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
                 event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) &&
                event.timeStamp >= latestTime) {
                latestTime = event.timeStamp
                latestPackage = event.packageName
            }
        }
        return latestPackage
    }
}
