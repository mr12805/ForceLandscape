package com.example.forcelandscape

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        val info = TextView(this).apply {
            text = "Force Landscape\n\n" +
                    "1. Allow 'Modify system settings'.\n" +
                    "2. Allow Usage Access.\n" +
                    "3. Add the Force Landscape tile to Quick Settings.\n\n" +
                    "Tap the tile while an app is open. The device will switch to landscape. " +
                    "When you leave that app with Back or Home, the previous rotation settings are restored."
            textSize = 18f
        }
        layout.addView(info)

        val settingsButton = Button(this).apply {
            text = "Allow Modify System Settings"
            setOnClickListener {
                startActivity(Intent(
                    Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:$packageName")
                ))
            }
        }
        layout.addView(settingsButton)

        val usageButton = Button(this).apply {
            text = "Open Usage Access Settings"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }
        }
        layout.addView(usageButton)

        setContentView(layout)
    }
}
