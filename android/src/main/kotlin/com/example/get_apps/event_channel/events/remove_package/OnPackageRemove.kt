package com.example.get_apps.event_channel.events.remove_package

import com.example.get_apps.event_channel.events.PackageNotification
import io.flutter.plugin.common.EventChannel

class OnPackageRemove : PackageRemovedListerner() {
  override fun onRemoved(packageName: String, events: EventChannel.EventSink?) {
    events?.success(PackageNotification(packageName, "remove").toJson());
  }
}