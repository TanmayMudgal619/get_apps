import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'get_apps_platform_interface.dart';
import 'app_info.dart';

/// An implementation of [GetAppsPlatform] that uses method channels.
class MethodChannelGetApps extends GetAppsPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('get_apps');

  @override
  Future<List<AppInfo>> getUserApps() async {
    final result = await methodChannel.invokeListMethod('getUserApps');
    List<AppInfo> ourApps =
        (result ?? []).map((e) => AppInfo.fromAndroidData(e)).toList();
    return ourApps;
  }

  @override
  Future<List<AppInfo>> getAllApps() async {
    final result = await methodChannel.invokeListMethod('getAllApps');
    List<AppInfo> ourApps =
        (result ?? []).map((e) => AppInfo.fromAndroidData(e)).toList();
    return ourApps;
  }
}
