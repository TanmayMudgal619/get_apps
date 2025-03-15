package com.example.get_apps.event_channel

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.get_apps.event_channel.events.remove_package.OnPackageRemove
import com.example.get_apps.event_channel.events.remove_package.PackageRemovedReceiver
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.StreamHandler

class EventChannelHandler: StreamHandler {

  private var context: Context
  private var removedReceiver : PackageRemovedReceiver
  private var packageRemoveIntentFilter: IntentFilter


  constructor(context: Context){
    this.context = context
    this.removedReceiver = PackageRemovedReceiver()
    this.packageRemoveIntentFilter = IntentFilter(Intent.ACTION_PACKAGE_REMOVED)
    this.packageRemoveIntentFilter.addDataScheme("package")
  }

  override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
    removedReceiver.setEventSink(events);
    removedReceiver.setListener(OnPackageRemove())
    context.registerReceiver(removedReceiver, packageRemoveIntentFilter)
  }

  override fun onCancel(arguments: Any?) {
    context.unregisterReceiver(removedReceiver)
  }
}