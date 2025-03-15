package com.example.get_apps.event_channel.events

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink

abstract class PackageActionListerner{
    abstract fun onNotify(packageName: String, action: String, events: EventSink?)
}

class ActionReceiver : BroadcastReceiver() {
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
            callback.onNotify(intent.data.toString().replaceFirst("package:", ""), actionMapping[intent.action]!!, events)
        }
    }

    fun setListener(callback: PackageActionListerner){
        this.callback = callback
    }

}