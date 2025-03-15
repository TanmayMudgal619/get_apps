package com.example.get_apps.event_channel.events

import io.flutter.plugin.common.EventChannel

class OnPackageActionNotify : PackageActionListerner() {
  override fun onNotify(packageName: String, action: String, events: EventChannel.EventSink?) {
    events?.success(PackageNotification(packageName, action).toJson());
  }
}