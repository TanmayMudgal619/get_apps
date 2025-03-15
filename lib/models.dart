import 'dart:typed_data';

class AppInfo {
  String appName;
  String appPackage;
  Uint8List appIcon;

  AppInfo({
    required this.appName,
    required this.appPackage,
    required this.appIcon
  });

  factory AppInfo.fromAndroidData(dynamic appData) {
    return AppInfo(
        appName: appData["appName"],
        appPackage: appData["appPackage"],
        appIcon: Uint8List.fromList(appData["appIcon"])
    );
  }
}

class ActionNotification{
  String packageName;
  String action;

  ActionNotification({
    required this.packageName,
    required this.action,
  });

  factory ActionNotification.fromMap(Map<dynamic, dynamic> actionNotification){
    return ActionNotification(
        packageName: actionNotification["packageName"]!,
        action: actionNotification["action"]!
    );
  }
}