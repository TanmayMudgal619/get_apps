package com.example.get_apps

import io.flutter.plugin.common.EventChannel

class OnPackageRemove : PackageRemovedListerner() {
  override fun onRemoved(packageName: String, events: EventChannel.EventSink?) {
    events?.success(PackageNotification(packageName, "remove").toJson());
  }
}