package com.example.get_apps

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.StreamHandler
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** GetAppsPlugin */
class GetAppsPlugin: FlutterPlugin, MethodCallHandler, StreamHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var methodChannel : MethodChannel
  private lateinit var eventChannel: EventChannel
  private lateinit var getApps : GetApps
  private lateinit var context : Context
  private lateinit var removedReceiver : PackageRemovedReceiver
  private lateinit var packageRemoveIntentFilter: IntentFilter

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    packageRemoveIntentFilter = IntentFilter(Intent.ACTION_PACKAGE_REMOVED)
    packageRemoveIntentFilter.addDataScheme("package");
    removedReceiver = PackageRemovedReceiver();
    eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "package_removed_channel");
    eventChannel.setStreamHandler(this)
    methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "get_apps")
    methodChannel.setMethodCallHandler(this)
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
    methodChannel.setMethodCallHandler(null)
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
