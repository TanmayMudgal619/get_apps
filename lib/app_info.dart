import 'dart:typed_data';

class AppInfo {
  String appName;
  String appPackage;
  Uint8List appIcon;
  AppInfo(
      {required this.appName, required this.appPackage, required this.appIcon});
  factory AppInfo.fromAndroidData(dynamic appData) {
    return AppInfo(
        appName: appData["appName"],
        appPackage: appData["appPackage"],
        appIcon: Uint8List.fromList(appData["appIcon"]));
  }
}
