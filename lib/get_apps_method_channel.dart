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
  Future<List<AppInfo>> getApps({bool includeSystemApps = false}) async {
    final List<dynamic> result = await methodChannel.invokeMethod('getApps', {"includeSystemApps": includeSystemApps});
    List<AppInfo> ourApps = result.map((e) => AppInfo.fromAndroidData(e)).toList();
    return ourApps;
  }

  @override
  Future<void> openApp(String packageName) async {
    try{
      await methodChannel.invokeMethod('openApp', {"packageName": packageName});
    }
    catch (err){
      throw Exception("Can't open the application: $err");
    }
  }
}
