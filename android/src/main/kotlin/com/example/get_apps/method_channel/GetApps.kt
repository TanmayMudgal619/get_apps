package com.example.get_apps.method_channel

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import java.io.ByteArrayOutputStream


class GetApps internal constructor(ctx: Context) {
    private var activity: Activity?

    init {
        context = ctx
        activity = null
        setApps()
    }

    fun setApps(){
        val packageManager = context.packageManager
        Companion.allApps = ArrayList()
        Companion.userApps = ArrayList()
        val installedApps = packageManager.getInstalledApplications(0)
        for (applicationInfo in installedApps) {
            addAppInList(applicationInfo.packageName, applicationInfo)
        }
    }

    public fun setActivity(activity: Activity?){
        this.activity = activity
    }

    val userApps: List<Map<String, Any>>
        get() = Companion.userApps
    val allApps: List<Map<String, Any>>
        get() = Companion.allApps

    private fun getAppInfo(packageManager: PackageManager?, applicationInfo: ApplicationInfo) : Map<String, Any>{
        val tempIcon:Drawable = applicationInfo.loadIcon(packageManager)
        val mainIcon:Any
        if (tempIcon is BitmapDrawable){
            val bitmapDrawableIcon: BitmapDrawable = tempIcon
            val outputStream = ByteArrayOutputStream()
            bitmapDrawableIcon.bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            mainIcon = outputStream.toByteArray()
        }
        else {
            val bitmap: Bitmap =
                if (tempIcon.intrinsicWidth <= 0 || tempIcon.intrinsicHeight <= 0) {
                    Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
                } else {
                    Bitmap.createBitmap(
                        tempIcon.intrinsicWidth,
                        tempIcon.intrinsicHeight,
                        Bitmap.Config.ARGB_8888
                    )
                }
            val canvas = Canvas(bitmap)
            tempIcon.setBounds(0, 0, canvas.width, canvas.height)
            tempIcon.draw(canvas)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            mainIcon = b
        }
        var appInfo : Map<String, Any> = mapOf(
                "appName" to applicationInfo.loadLabel(packageManager!!).toString(),
                "appPackage" to applicationInfo.packageName,
                "appIcon" to mainIcon
        );
        return appInfo
    }

    public fun openApp(packageName: String){
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName);
        if (launchIntent == null){
            throw Exception("Can't open the app!")
        }
        try{
            context.startActivity(launchIntent);
        }
        catch (err: Exception){
            throw Exception("Can't open the app with error: $err")
        }
    }

    public fun deleteApp(packageName: String){
        if (activity == null){
            throw Exception("Null Activity")
        }
        val deleteIntent = Intent(Intent.ACTION_DELETE)
        deleteIntent.data = Uri.parse("package:$packageName")
        try {
            activity!!.startActivity(deleteIntent)
        }
        catch (err: Exception){
            throw Exception("Can't delete the app with error: $err")
        }
    }

    fun removeAppFromList(packageName: String) {
        Companion.allApps = Companion.allApps.filter {
            it["appPackage"].toString() != packageName
        } as ArrayList<Map<String, Any>>

        Companion.userApps = Companion.userApps.filter {
            it["appPackage"].toString() != packageName
        } as ArrayList<Map<String, Any>>
    }

    fun addAppInList(packageName: String, applicationInfo: ApplicationInfo?) {
        val packageManager = context.packageManager;
        val appInfo = applicationInfo ?: packageManager.getApplicationInfo(packageName, 0)
        Companion.allApps.add(getAppInfo(packageManager, appInfo))
        if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
            Companion.userApps.add(getAppInfo(packageManager, appInfo))
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context
        private lateinit var allApps: MutableList<Map<String, Any>>
        private lateinit var userApps: MutableList<Map<String, Any>>
    }
}