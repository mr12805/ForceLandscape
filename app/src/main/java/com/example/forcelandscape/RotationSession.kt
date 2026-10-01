package com.example.forcelandscape

import android.content.Context
import android.provider.Settings

object RotationSession {
    private const val PREFS = "rotation_session"
    private const val KEY_ACTIVE = "active"
    private const val KEY_AUTO = "old_auto"
    private const val KEY_ROTATION = "old_rotation"
    private const val KEY_PACKAGE = "target_package"

    fun begin(context: Context, targetPackage: String): Boolean {
        val resolver = context.contentResolver
        if (!Settings.System.canWrite(context)) return false

        val auto = Settings.System.getInt(
            resolver, Settings.System.ACCELEROMETER_ROTATION, 1
        )
        val rotation = Settings.System.getInt(
            resolver, Settings.System.USER_ROTATION, 0
        )

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_ACTIVE, true)
            .putInt(KEY_AUTO, auto)
            .putInt(KEY_ROTATION, rotation)
            .putString(KEY_PACKAGE, targetPackage)
            .apply()

        // Disable automatic rotation and force 90 degrees (landscape).
        Settings.System.putInt(resolver, Settings.System.ACCELEROMETER_ROTATION, 0)
        Settings.System.putInt(resolver, Settings.System.USER_ROTATION, 1)
        return true
    }

    fun restore(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (!prefs.getBoolean(KEY_ACTIVE, false)) return
        if (!Settings.System.canWrite(context)) return

        val resolver = context.contentResolver
        Settings.System.putInt(
            resolver, Settings.System.ACCELEROMETER_ROTATION,
            prefs.getInt(KEY_AUTO, 1)
        )
        Settings.System.putInt(
            resolver, Settings.System.USER_ROTATION,
            prefs.getInt(KEY_ROTATION, 0)
        )

        prefs.edit().clear().apply()
    }

    fun isActive(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_ACTIVE, false)

    fun targetPackage(context: Context): String? =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_PACKAGE, null)
}
