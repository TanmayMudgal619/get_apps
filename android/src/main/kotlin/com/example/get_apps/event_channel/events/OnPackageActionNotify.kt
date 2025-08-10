package com.example.get_apps.event_channel.events

import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.EventChannel

class OnPackageActionNotify : PackageActionListerner() {
  override fun onNotify(packageName: String, action: String, events: EventChannel.EventSink?) {
    Handler(Looper.getMainLooper()).post {
      events?.success(PackageNotification(packageName, action).toJson());
    }
  }
}