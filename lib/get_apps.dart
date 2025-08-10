import 'get_apps_platform_interface.dart';
import 'models.dart';

class GetApps {

  Future<List<AppInfo>> getApps({bool includeSystemApps = false}) {
    return GetAppsPlatform.instance.getApps(includeSystemApps: includeSystemApps);
  }

  Future<void> openApp(String packageName) async{
    return GetAppsPlatform.instance.openApp(packageName);
  }

  Future<void> deleteApp(String packageName) async{
    return GetAppsPlatform.instance.deleteApp(packageName);
  }

  Stream<ActionNotification> appActionReceiver() async* {
    await for (final actionNotification in GetAppsPlatform.instance.appActionReceiver()){
      yield actionNotification;
    }
  }
}
