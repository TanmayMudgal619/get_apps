package com.example.get_apps.event_channel.events.remove_package

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink

abstract class PackageRemovedListerner{
    abstract fun onRemoved(packageName: String, events: EventSink?)
}

class PackageRemovedReceiver : BroadcastReceiver() {
    private var events: EventChannel.EventSink? = null
    private lateinit var callback: PackageRemovedListerner

    fun setEventSink(events: EventSink?){
        this.events = events;
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && intent.action == Intent.ACTION_PACKAGE_REMOVED){
            callback.onRemoved(intent.data.toString(), events)
        }
    }

    fun setListener(callback: PackageRemovedListerner){
        this.callback = callback
    }

}