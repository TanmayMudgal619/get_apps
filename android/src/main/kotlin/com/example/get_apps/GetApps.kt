package com.example.get_apps

import android.R
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.ByteArrayOutputStream


class GetApps internal constructor(ctx: Context) {
    init {
        context = ctx
        val packageManager = context.packageManager
        Companion.allApps = ArrayList()
        Companion.userApps = ArrayList()
        val installedApps = packageManager.getInstalledApplications(0)
        for (applicationInfo in installedApps) {
            Companion.allApps.add(getAppInfo(packageManager, applicationInfo))
            if (packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
                Companion.userApps.add(getAppInfo(packageManager, applicationInfo))
            }
        }
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
            val canvas: Canvas = Canvas(bitmap)
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
            throw Exception("Can't open the app with error: ${err.toString()}")
        }
    }

    public fun listenRemoveAction(): Sequence<String>{

    }

    companion object {
        private lateinit var context: Context
        private lateinit var allApps: MutableList<Map<String, Any>>
        private lateinit var userApps: MutableList<Map<String, Any>>
    }
}