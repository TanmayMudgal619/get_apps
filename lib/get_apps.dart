import 'package:url_launcher/url_launcher.dart';
import 'package:url_launcher/url_launcher_string.dart';

import 'get_apps_platform_interface.dart';
import 'app_info.dart';

class GetApps {
  Future<List<AppInfo>> getUserApps() {
    return GetAppsPlatform.instance.getUserApps();
  }

  Future<List<AppInfo>> getAllApps() {
    return GetAppsPlatform.instance.getAllApps();
  }

  Future<void> openApp(String packageName) async{
    if (! await canLaunchUrlString(packageName)){
      throw Exception("Can't launch the application.");
    }
    if (! await launchUrlString(packageName)){
      throw Exception("Unable to launch the application.");
    }
  }
}
