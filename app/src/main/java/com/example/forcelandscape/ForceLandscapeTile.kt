package com.example.forcelandscape

import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast

class ForceLandscapeTile : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        qsTile?.state = if (RotationSession.isActive(this)) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile?.updateTile()
    }

    override fun onClick() {
        super.onClick()

        if (RotationSession.isActive(this)) {
            RotationSession.restore(this)
            qsTile.state = Tile.STATE_INACTIVE
            qsTile.updateTile()
            return
        }

        val target = ForegroundApp.find(this)
        if (target == null || target == packageName) {
            Toast.makeText(this, "Open an app first", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.provider.Settings.System.canWrite(this)) {
            Toast.makeText(this, "Allow Modify System Settings first", Toast.LENGTH_LONG).show()
            startActivityAndCollapse(
                Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    .setData(android.net.Uri.parse("package:$packageName"))
            )
            return
        }

        if (!RotationSession.begin(this, target)) {
            Toast.makeText(this, "Could not change rotation", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(this, LandscapeMonitorService::class.java)
        startForegroundService(intent)

        qsTile.state = Tile.STATE_ACTIVE
        qsTile.updateTile()
    }
}
