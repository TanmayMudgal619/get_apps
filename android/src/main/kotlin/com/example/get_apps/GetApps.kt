package com.example.get_apps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import java.io.ByteArrayOutputStream
import kotlin.text.get

class GetApps internal constructor(ctx: Context) {
    private var activity: Activity?
    private var context: Context = ctx
    private lateinit var systemApps: MutableList<Map<String, Any>>
    private lateinit var userApps: MutableList<Map<String, Any>>
    var isInitialized: Boolean = false

    init {
        activity = null
    }

    fun initCore(){
        synchronized(this){
            if (isInitialized){
                Log.d("GetApps", "initCore: skipping initialization...")
                return
            }

            Log.d("GetApps", "initCore: initializing get apps...")
            val packageManager = context.packageManager
            systemApps = ArrayList()
            userApps = ArrayList()
            val installedApps = packageManager.getInstalledApplications(0)
            for (applicationInfo in installedApps) {
                addAppInList(applicationInfo.packageName, applicationInfo)
            }
            isInitialized = true
        }
    }

    fun getAppsList(includeSystemApps: Boolean): List<Map<String, Any>> {
        initCheck()
        if (includeSystemApps){
            return systemApps + userApps
        }
        return userApps
    }

    fun openApp(packageName: String){
        initCheck()
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

    fun deleteApp(packageName: String){
        initCheck()
        if (activity == null){
            throw Exception("Null Activity")
        }
        try {
            val deleteIntent = Intent(Intent.ACTION_DELETE)
            deleteIntent.data = Uri.parse("package:$packageName")
            activity!!.startActivity(deleteIntent)
        }
        catch (err: Exception){
            throw Exception("Can't delete the app with error: $err")
        }
    }

    fun initCheck(){
        if (!isInitialized){
            initCore()
        }
    }

    fun setActivity(activity: Activity?){
        this.activity = activity
    }

    fun removeApp(packageName: String){
        initCheck()
        removeAppFromList(packageName)
    }

    fun addApp(packageName: String){
        initCheck()
        addAppInList(packageName, null)
    }

    private fun removeAppFromList(packageName: String) {
        systemApps = systemApps.filter {
            it["appPackage"].toString() != packageName
        } as ArrayList<Map<String, Any>>

        userApps = userApps.filter {
            it["appPackage"].toString() != packageName
        } as ArrayList<Map<String, Any>>
    }

    private fun addAppInList(packageName: String, applicationInfo: ApplicationInfo?) {
        val packageManager = context.packageManager;
        val appInfo = applicationInfo ?: packageManager.getApplicationInfo(packageName, 0)
        if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
            userApps.add(getAppInfo(packageManager, appInfo))
        }
        else{
            systemApps.add(getAppInfo(packageManager, appInfo))
        }
    }


    private fun getAppInfo(packageManager: PackageManager?, applicationInfo: ApplicationInfo): Map<String, Any> {
        val drawable = applicationInfo.loadIcon(packageManager)
        val iconBytes: ByteArray = when (drawable) {
            is BitmapDrawable -> {
                ByteArrayOutputStream().apply {
                    drawable.bitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
                }.toByteArray()
            }
            else -> {
                val bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth.takeIf { it > 0 } ?: 1,
                    drawable.intrinsicHeight.takeIf { it > 0 } ?: 1,
                    Bitmap.Config.ARGB_8888
                ).apply {
                    val canvas = Canvas(this)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                }

                ByteArrayOutputStream().apply {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
                }.toByteArray()
            }
        }

        return mapOf(
            "appName" to applicationInfo.loadLabel(packageManager!!).toString(),
            "appPackage" to applicationInfo.packageName,
            "appIcon" to iconBytes
        )
    }
}