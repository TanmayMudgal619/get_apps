package com.example.get_apps

import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull
import com.example.get_apps.event_channel.EventChannelHandler
import com.example.get_apps.method_channel.GetApps
import com.example.get_apps.method_channel.MethodChannelHandler

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

/** GetAppsPlugin */
class GetAppsPlugin: FlutterPlugin, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var methodChannel : MethodChannel
  private lateinit var eventChannel: EventChannel

  private lateinit var methodChannelHandler: MethodChannelHandler
  private lateinit var eventChannelHandler: EventChannelHandler

  private lateinit var context : Context
  private var activity: Activity? = null

  private lateinit var getApps: GetApps

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    context = flutterPluginBinding.applicationContext

    getApps = GetApps(context)

    methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "method_channel")
    methodChannelHandler = MethodChannelHandler(context, getApps)
    methodChannel.setMethodCallHandler(methodChannelHandler)

    eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "event_channel")
    eventChannelHandler = EventChannelHandler(context, getApps)
    eventChannel.setStreamHandler(eventChannelHandler)
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    eventChannelHandler.onCancel(null)
    methodChannel.setMethodCallHandler(null)
    eventChannel.setStreamHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding){
    methodChannelHandler.setActivity(binding.activity)
  }

  override fun onDetachedFromActivity(){
    methodChannelHandler.setActivity(null)
  }

  override fun onDetachedFromActivityForConfigChanges(){
    methodChannelHandler.setActivity(null)
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding){
    methodChannelHandler.setActivity(binding.activity)
  }

}
