package com.example.get_apps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PackageRemovedReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && intent.action == Intent.ACTION_PACKAGE_REMOVED){

        }
    }
}