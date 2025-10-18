import 'package:flutter/services.dart';

import 'get_apps_platform_interface.dart';
import 'models.dart';

/// An implementation of [GetAppsPlatform] that uses method and event channels.
class GetAppsChannel extends GetAppsPlatform {
  /// The method and event channel are used to interact with the native platform.
  final methodChannel = const MethodChannel('method_channel');
  final eventChannel = const EventChannel('event_channel');

  @override
  Future<AppInfo> getAppInfo(
    String packageName, {
    bool shouldInitialize = false,
  }) async {
    try {
      final dynamic result = await methodChannel.invokeMethod('getAppInfo', {
        "packageName": packageName,
        "shouldInitialize": shouldInitialize,
      });
      AppInfo appInfo = AppInfo.fromAndroidData(result);
      return appInfo;
    } catch (err) {
      throw Exception("Can't get the application: $err");
    }
  }

  @override
  Future<List<AppInfo>> getApps({AppType appType = AppType.all, LaunchType launchType = LaunchType.all}) async {
    try {
      final List<dynamic> result = await methodChannel.invokeMethod('getApps', {
        "appType": appType.index,
        "launchType": launchType.index
      });
      List<AppInfo> ourApps =
          result.map((e) => AppInfo.fromAndroidData(e)).toList();
      return ourApps;
    } catch (err) {
      throw Exception("Can't get the applications: $err");
    }
  }

  @override
  Future<void> openApp(String packageName) async {
    try {
      await methodChannel.invokeMethod('openApp', {"packageName": packageName});
    } catch (err) {
      throw Exception("Can't open the application: $err");
    }
  }

  @override
  Future<void> deleteApp(String packageName) async {
    try {
      await methodChannel.invokeMethod('deleteApp', {
        "packageName": packageName,
      });
    } catch (err) {
      throw Exception("Can't delete the application: $err");
    }
  }

  @override
  Stream<ActionNotification> appActionReceiver() {
    return eventChannel.receiveBroadcastStream().map(
          (event) => ActionNotification.fromMap(event),
        );
  }
}
