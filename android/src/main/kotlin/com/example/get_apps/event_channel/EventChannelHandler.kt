package com.example.get_apps.event_channel

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
    getApps.initCheck()
    actionReceiver.setEventSink(events);
    actionReceiver.setListener(OnPackageActionNotify())
    context.registerReceiver(actionReceiver, packageIntentFilter)
  }

  override fun onCancel(arguments: Any?) {
    context.unregisterReceiver(actionReceiver)
  }
}