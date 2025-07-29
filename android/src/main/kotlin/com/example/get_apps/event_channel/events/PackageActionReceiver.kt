package com.example.get_apps.event_channel.events

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.get_apps.method_channel.GetApps
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink

abstract class PackageActionListerner{
    abstract fun onNotify(packageName: String, action: String, events: EventSink?)
}

class ActionReceiver(private var getApps: GetApps) : BroadcastReceiver() {
    private var actionMapping: Map<String, String> = mapOf(
        Intent.ACTION_PACKAGE_REMOVED to "removed",
        Intent.ACTION_PACKAGE_ADDED to "added"
    )

    private var events: EventChannel.EventSink? = null
    private lateinit var callback: PackageActionListerner

    fun setEventSink(events: EventSink?){
        this.events = events;
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && intent.action in actionMapping){
            val packageName = intent.data.toString().replaceFirst("package:", "")
            if (intent.action == Intent.ACTION_PACKAGE_REMOVED){
                getApps.removeAppFromList(packageName)
            }
            if (intent.action == Intent.ACTION_PACKAGE_ADDED){
                getApps.setApps()
            }
            callback.onNotify(packageName, actionMapping[intent.action]!!, events)
        }
    }

    fun setListener(callback: PackageActionListerner){
        this.callback = callback
    }

}