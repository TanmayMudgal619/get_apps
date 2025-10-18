package com.example.get_apps.method_channel

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.get_apps.GetApps
import com.example.get_apps.AppType
import com.example.get_apps.LaunchType
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
    Thread{
      when(call.method){
        "getAppInfo"-> getAppInfoHandler(call, result)
        "getApps"-> getAppsHandler(call, result)
        "openApp" -> openAppHandler(call, result)
        "deleteApp" -> deleteAppHandler(call, result)
        else -> result.notImplemented()
      }
    }.start()
  }

  fun getAppInfoHandler(call: MethodCall, result: MethodChannel.Result){
    try{
      val packageName = call.argument<String?>("packageName")
      if (packageName == null || packageName.isEmpty()){
        throw Exception("Package name can't be null or empty!")
      }
      val appInfo = getApps.getAppInfo(packageName, call.argument<Boolean?>("shouldInitialize") ?: false)
      Handler(Looper.getMainLooper()).post {
        result.success(appInfo)
      }
    }
    catch (err: Exception){
      Handler(Looper.getMainLooper()).post {
        result.error("GetAppInfo Error", err.toString(), null)
      }
    }
  }

  fun getAppsHandler(call: MethodCall, result: MethodChannel.Result){
    try{
      val apps = getApps.getAppsList(
        AppType.fromValue(call.argument<Int?>("appType") ?: 0),
        LaunchType.fromValue(call.argument<Int?>("launchType") ?: 0)
      )
      Handler(Looper.getMainLooper()).post {
        result.success(apps)
      }
    }
    catch (err: Exception){
      Handler(Looper.getMainLooper()).post {
        result.error("GetApps Error", err.toString(), null)
      }
    }
  }

  fun openAppHandler(call: MethodCall, result: MethodChannel.Result){
    try{
      val packageName = call.argument<String?>("packageName")
      if (packageName == null || packageName.isEmpty()){
        throw Exception("Package name can't be null or empty!")
      }
      getApps.openApp(packageName)
      Handler(Looper.getMainLooper()).post {
        result.success(null)
      }
    }
    catch (err: Exception){
      Handler(Looper.getMainLooper()).post {
        result.error("GetApps Error", err.toString(), null)
      }
    }
  }

  fun deleteAppHandler(call: MethodCall, result: MethodChannel.Result){
    try{
      val packageName = call.argument<String?>("packageName")
      if (packageName == null || packageName.isEmpty()){
        throw Exception("Package name can't be null or empty!")
      }
      getApps.deleteApp(packageName)
    }
    catch (err: Exception){
      Handler(Looper.getMainLooper()).post {
        result.error("GetApps Error", err.toString(), null)
      }
    }
  }
}
