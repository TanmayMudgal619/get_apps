import 'package:get_apps/get_apps_channel.dart';

import 'get_apps_platform_interface.dart';
import 'models.dart';

/// GetApps exposes the methods defined in [GetAppsChannel].
/// GetApps has a caching system where once the core is initialized it caches
/// all the apps on the system. The core is auto-initialized when any method
/// except [getAppInfo] is called. [getAppInfo] has an optional parameter to
/// initialize the core.
class GetApps {
  /// Fetch details of a package by packageName
  /// shouldInitialize, is optional with default value as false
  /// shouldInitialize is used to auto-initialize the Core of GetApps.
  /// Throws an error if package doesn't exists.
  Future<AppInfo> getAppInfo(
    String packageName, {
    bool shouldInitialize = false,
  }) {
    return GetAppsPlatform.instance.getAppInfo(
      packageName,
      shouldInitialize: shouldInitialize,
    );
  }

  /// Fetch all the apps on the device.
  /// includeSystemApps is an optional parameter with default value as false.
  Future<List<AppInfo>> getApps({AppType appType = AppType.all, LaunchType launchType = LaunchType.all}) {
    return GetAppsPlatform.instance.getApps(
      appType: appType,
      launchType: launchType
    );
  }

  /// Opens an app if exists using packageName.
  /// Throws an error if packageName doesn't exists.
  Future<void> openApp(String packageName) async {
    return GetAppsPlatform.instance.openApp(packageName);
  }

  /// Starts the uninstallation process for an app if exists using packageName.
  /// Throws an error if packageName doesn't exists.
  Future<void> deleteApp(String packageName) async {
    return GetAppsPlatform.instance.deleteApp(packageName);
  }

  /// Steams add/remove package notification provided by android.
  Stream<ActionNotification> appActionReceiver() async* {
    await for (final actionNotification
        in GetAppsPlatform.instance.appActionReceiver()) {
      yield actionNotification;
    }
  }
}
