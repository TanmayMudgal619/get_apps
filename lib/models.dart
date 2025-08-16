import 'dart:typed_data';

class AppInfo {
  String appName;
  String appPackage;
  Uint8List appIcon;
  String? description;
  bool isSystemApp;
  int versionCode;
  String? versionName;

  AppInfo({
    required this.appName,
    required this.appPackage,
    required this.appIcon,
    required this.description,
    required this.isSystemApp,
    required this.versionCode,
    required this.versionName,
  });

  factory AppInfo.fromAndroidData(dynamic appData) {
    return AppInfo(
      appName: appData["appName"],
      appPackage: appData["packageName"],
      appIcon: Uint8List.fromList(appData["icon"]),
      description: appData["description"],
      isSystemApp: appData["isSystemApp"],
      versionCode: appData["versionCode"],
      versionName: appData["versionName"],
    );
  }
}

class ActionNotification {
  String packageName;
  String action;

  ActionNotification({required this.packageName, required this.action});

  factory ActionNotification.fromMap(Map<dynamic, dynamic> actionNotification) {
    return ActionNotification(
      packageName: actionNotification["packageName"]!,
      action: actionNotification["action"]!,
    );
  }
}
