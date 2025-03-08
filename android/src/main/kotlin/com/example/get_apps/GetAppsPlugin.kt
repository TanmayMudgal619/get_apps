package com.example.get_apps

import android.content.Context
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** GetAppsPlugin */
class GetAppsPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var getApps : GetApps
  private lateinit var context : Context

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "get_apps")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (!this::getApps.isInitialized){
      getApps = GetApps(context)
  }
when (call.method) {
    "getApps" -> {
        if (call.argument<Boolean>("includeSystemApps") == false){
            result.success(getApps.userApps)
        }
        else{
            result.success(getApps.userApps)
        }
    }
    "openApp" -> {
        var packageName = call.argument<String>("packageName")
        if (packageName == null || packageName.isEmpty()){
            throw Exception("Package name can't be null or empty!")
        }
        getApps.openApp(packageName)
    }
    else -> {
        result.notImplemented()
    }
}
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
