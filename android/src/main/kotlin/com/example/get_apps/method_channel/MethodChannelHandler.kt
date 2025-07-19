package com.example.get_apps.method_channel

import android.app.Activity
import android.content.Context
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class MethodChannelHandler: MethodCallHandler {
  private var context: Context
  private var activity: Activity? = null
  private var getApps : GetApps

  constructor(context: Context, getApps: GetApps){
    this.context = context
    this.getApps = getApps
  }

  fun setActivity(activity: Activity?){
    this.getApps.setActivity(activity)
  }

  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
    when (call.method){
      "getApps" -> handleGetApps(call, result)
      "openApp" -> handleOpenApp(call, result)
      "deleteApp" -> handleDeleteApp(call, result)
      else -> result.notImplemented()
    }
  }

  fun handleGetApps(call: MethodCall, result: MethodChannel.Result){
    if (call.argument<Boolean>("includeSystemApps") == false){
      result.success(getApps.userApps)
    }
    else{
      result.success(getApps.allApps)
    }
  }

  fun handleOpenApp(call: MethodCall, result: MethodChannel.Result){
    var packageName = call.argument<String>("packageName")
    if (packageName == null || packageName.isEmpty()){
      throw Exception("Package name can't be null or empty!")
    }
    getApps.openApp(packageName)
  }

  fun handleDeleteApp(call: MethodCall, result: MethodChannel.Result){
    var packageName = call.argument<String>("packageName")
    if (packageName == null || packageName.isEmpty()){
      throw Exception("Package name can't be null or empty!")
    }
    getApps.deleteApp(packageName)
  }
}
