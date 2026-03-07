package com.example.get_apps

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

enum class AppType(val value: Int) {
    ALL(0),
    SYSTEM(1),
    USER(2);

    companion object {
        fun fromValue(value: Int): AppType {
            return values().find { it.value == value }
                ?: throw IllegalArgumentException("Invalid AppType value: $value")
        }
    }
}

enum class LaunchType(val value: Int) {
    ALL(0),
    LAUNCHABLE(1),
    NON_LAUNCHABLE(2);

    companion object {
        fun fromValue(value: Int): LaunchType {
            return values().find { it.value == value }
                ?: throw IllegalArgumentException("Invalid LaunchType value: $value")
        }
    }
}


class GetApps internal constructor(ctx: Context) {

    private var activity: Activity?
    private var context: Context = ctx
    private lateinit var apps: MutableList<Map<String, Any?>>
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
            apps = ArrayList()
            val installedApps = packageManager.getInstalledApplications(0)
            for (applicationInfo in installedApps) {
                addAppInList(applicationInfo.packageName, applicationInfo)
            }
            isInitialized = true
        }
    }

    fun getAppInfo(packageName: String, shouldInitialize: Boolean): Map<String, Any?>{
        if (initCheck(shouldInitialize)){
            val appInfo = getAppsList(AppType.ALL, LaunchType.ALL).firstOrNull {
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

    fun getAppsList(
        appType: AppType = AppType.ALL,
        launchType: LaunchType = LaunchType.ALL
    ): List<Map<String, Any?>> {
        initCheck()

        return apps.filter { app ->
            val matchesLaunchType = when (launchType) {
                LaunchType.ALL -> true
                LaunchType.LAUNCHABLE -> app["isLaunchable"] == true
                LaunchType.NON_LAUNCHABLE -> app["isLaunchable"] == false
            }

            val matchesAppType = when (appType) {
                AppType.ALL -> true
                AppType.SYSTEM -> app["isSystemApp"] == true
                AppType.USER -> app["isSystemApp"] == false
            }

            matchesLaunchType && matchesAppType
        }
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
        apps = apps.filter {
            it["packageName"].toString() != packageName
        } as ArrayList<Map<String, Any?>>
    }

    private fun addAppInList(packageName: String, applicationInfo: ApplicationInfo?) {
        val packageManager = context.packageManager;
        val appInfo = try {
            applicationInfo ?: packageManager.getApplicationInfo(packageName, 0)
        } catch (e: Exception) {
            Log.e("GetApps", "Error getting app info: $packageName", e)
            return
        }
        val appDataMap = getAppInfoMap(packageManager, appInfo)
        apps.add(appDataMap)
    }


    private fun getAppInfoMap(packageManager: PackageManager, applicationInfo: ApplicationInfo): Map<String, Any?> {
        val drawable = applicationInfo.loadIcon(packageManager)
        val description = applicationInfo.loadDescription(packageManager)
        val packageInfo = packageManager.getPackageInfo(applicationInfo.packageName, 0)
        val isLaunchable = packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null
        val isSystemApp = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) ||
                (applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0)
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
            "packageName" to applicationInfo.packageName,
            "icon" to iconBytes,
            "description" to description,
            "versionName" to packageInfo.versionName,
            "versionCode" to packageInfo.versionCode,
            "isLaunchable" to isLaunchable,
            "isSystemApp" to isSystemApp
        )
    }
}