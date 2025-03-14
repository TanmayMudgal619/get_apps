package com.example.get_apps

import android.content.Context
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.StreamHandler

class PackageRemovedReceiverChannel: FlutterPlugin, StreamHandler {

  private lateinit var eventChannel: EventChannel
  private lateinit var removedReceiver: PackageRemovedReceiver
  private lateinit var context: Context

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    eventChannel = EventChannel(binding.binaryMessenger, "package_removed_channel");
    eventChannel.setStreamHandler(this)
    context = binding.applicationContext
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    eventChannel.setStreamHandler(null)
  }

  override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
    TODO("Not yet implemented")
  }

  override fun onCancel(arguments: Any?) {
    TODO("Not yet implemented")
  }

}