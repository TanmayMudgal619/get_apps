package com.example.get_apps.event_channel

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import com.example.get_apps.event_channel.events.ActionReceiver
import com.example.get_apps.event_channel.events.OnPackageActionNotify
import com.example.get_apps.GetApps
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.StreamHandler

class EventChannelHandler: StreamHandler {

  private var context: Context
  private var getApps: GetApps
  private var actionReceiver : ActionReceiver
  private var packageIntentFilter: IntentFilter
  private var isReceiverRegistered = false

  constructor(context: Context, getApps: GetApps){
    this.context = context
    this.getApps = getApps
    this.actionReceiver = ActionReceiver(getApps)

    this.packageIntentFilter = IntentFilter()
    this.packageIntentFilter.addDataScheme("package")
    this.packageIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
    this.packageIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
  }

  override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
    Thread{
      getApps.initCheck()
      Handler(Looper.getMainLooper()).post {
        actionReceiver.setEventSink(events);
        actionReceiver.setListener(OnPackageActionNotify())
        context.registerReceiver(actionReceiver, packageIntentFilter)
        isReceiverRegistered = true
      }
    }.start()
  }

  override fun onCancel(arguments: Any?) {
    if(isReceiverRegistered) {
      try {
        // If the screen is rotated multiple times, errors may still occur
        context.unregisterReceiver(actionReceiver)
      }catch (e: Exception){
        //ignored
        isReceiverRegistered = false
      }
    }
  }
}