import 'get_apps_platform_interface.dart';
import 'app_info.dart';

class GetApps {
  Future<List<AppInfo>> getUserApps() {
    return GetAppsPlatform.instance.getUserApps();
  }

  Future<List<AppInfo>> getAllApps() {
    return GetAppsPlatform.instance.getAllApps();
  }
}
