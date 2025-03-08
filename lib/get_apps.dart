import 'get_apps_platform_interface.dart';
import 'app_info.dart';

class GetApps {
  Future<List<AppInfo>> getApps({bool includeSystemApps = false}) {
    return GetAppsPlatform.instance.getApps(includeSystemApps: includeSystemApps);
  }

  Future<void> openApp(String packageName) async{
    return GetAppsPlatform.instance.openApp(packageName);
  }
}
