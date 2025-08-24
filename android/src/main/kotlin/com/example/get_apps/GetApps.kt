package com.example.get_apps

import android.app.Activity
import android.content.ActivityNotFoundException
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
import java.io.File
import kotlin.jvm.Throws

@Suppress("DEPRECATION")
class GetApps internal constructor(ctx: Context) {
    private var activity: Activity?
    private var context: Context = ctx
    private lateinit var systemApps: MutableList<Map<String, Any?>>
    private lateinit var userApps: MutableList<Map<String, Any?>>
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

    fun getAppInfo(packageName: String, shouldInitialize: Boolean): Map<String, Any?>{
        if (initCheck(shouldInitialize)){
            val appInfo = getAppsList(true).firstOrNull {
                it["packageName"] == packageName
            }
            if (appInfo == null){
                throw Exception("Package Name $packageName not found!")
            }
            return appInfo
        }
        try{
            return getAppInfoMap(
                context.packageManager,
                context.packageManager.getApplicationInfo(packageName, 0))
        }
        catch (_: PackageManager.NameNotFoundException){
            throw Exception("Package Name $packageName not found!")
        }
    }

    fun getAppsList(includeSystemApps: Boolean): List<Map<String, Any?>> {
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

    fun shareAppByPackageName(packageName: String){
        initCheck()
        val appInfo = getAppsList(true).firstOrNull {
            it["packageName"] == packageName
        }
        if (appInfo == null){
            throw Exception("Package Name $packageName not found!")
        }
        if (appInfo["sourceDir"] == null){
            throw Exception("Can't locate sourceDir for $packageName!")
        }
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type= "*/*"
        }
        try {
            val sourceApkFile = File(appInfo["sourceDir"] as String)

            val destFile = File(context.externalCacheDir, "${appInfo["packageName"]}.apk")
            sourceApkFile.copyTo(destFile, overwrite = true)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/octet-stream"
                putExtra(Intent.EXTRA_STREAM, destFile.toURI().toString())
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(shareIntent)

        }
        catch (err: Exception){
            throw Exception("Can't share the package $packageName ${appInfo["sourceDir"]} $err!")
        }
    }

    fun initCheck(shouldInitialize: Boolean = true): Boolean {
        if (!isInitialized && shouldInitialize){
            initCore()
        }
        return isInitialized
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
            it["packageName"].toString() != packageName
        } as ArrayList<Map<String, Any?>>

        userApps = userApps.filter {
            it["packageName"].toString() != packageName
        } as ArrayList<Map<String, Any?>>
    }

    private fun addAppInList(packageName: String, applicationInfo: ApplicationInfo?) {
        val packageManager = context.packageManager;
        val appInfo = applicationInfo ?: packageManager.getApplicationInfo(packageName, 0)

        val appDataMap = getAppInfoMap(packageManager, appInfo)
        if (appDataMap["isSystemApp"] as Boolean){
            systemApps.add(appDataMap)
        }
        else{
            userApps.add(appDataMap)
        }
    }


    private fun getAppInfoMap(packageManager: PackageManager, applicationInfo: ApplicationInfo): Map<String, Any?> {
        val drawable = applicationInfo.loadIcon(packageManager)
        val description = applicationInfo.loadDescription(packageManager)
        val packageInfo = packageManager.getPackageInfo(applicationInfo.packageName, 0)
        val isSystemApp = packageManager.getLaunchIntentForPackage(applicationInfo.packageName) == null
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
            "appName" to applicationInfo.loadLabel(packageManager).toString(),
            "description" to description,
            "icon" to iconBytes,
            "isSystemApp" to isSystemApp,
            "packageName" to applicationInfo.packageName,
            "versionCode" to packageInfo.versionCode,
            "versionName" to packageInfo.versionName,
            "sourceDir" to applicationInfo.sourceDir,
        )
    }
}