import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'get_apps_platform_interface.dart';
import 'models.dart';

/// An implementation of [GetAppsPlatform] that uses method channels.
class MethodChannelGetApps extends GetAppsPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('method_channel');
  final eventChannel = const EventChannel('event_channel');

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

  @override
  Future<void> deleteApp(String packageName) async {
    try{
      await methodChannel.invokeMethod('deleteApp', {"packageName": packageName});
    }
    catch (err){
      throw Exception("Can't delete the application: $err");
    }
  }
  
  @override
  Stream<ActionNotification> appActionReceiver() async*{
    await for (final actionNotification in eventChannel.receiveBroadcastStream()){
      yield ActionNotification.fromMap(actionNotification);
    }
  }
}
