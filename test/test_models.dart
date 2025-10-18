import 'dart:typed_data';
import 'package:flutter_test/flutter_test.dart';
import 'package:get_apps/models.dart';

const validAppData = {
  "appName": "Chrome",
  "packageName": "com.android.chrome",
  "icon": [1, 2, 3],
  "description": null,
  "isLaunchable": false,
  "versionCode": 1,
  "versionName": "1.2"
};

const invalidKeyAppData = {
  "appName": "Chrome",
  "packageName": "com.android.chrome",
  "appIcon": [1, 2, 3],
  "description": null,
  "isLaunchable": false,
  "versionCode": 1,
  "versionName": "1.2"
};

void main() {
  test("App Info from Valid App Map", () {
    final chromeAppInfo = AppInfo(
        appName: "Chrome",
        appPackage: "com.android.chrome",
        appIcon: Uint8List.fromList(const [1, 2, 3]),
        description: null,
        isLaunchable: false,
        versionCode: 1,
        versionName: "1.2");
    final got = AppInfo.fromAndroidData(validAppData);
    expect(got.appName, chromeAppInfo.appName);
    expect(got.appPackage, chromeAppInfo.appPackage);
    expect(got.appIcon, chromeAppInfo.appIcon);
    expect(got.description, chromeAppInfo.description);
    expect(got.isLaunchable, chromeAppInfo.isLaunchable);
    expect(got.versionCode, chromeAppInfo.versionCode);
    expect(got.versionName, chromeAppInfo.versionName);
  });

  test("App Info throws TypeError from invalid app map", () {
    expect(() => AppInfo.fromAndroidData(invalidKeyAppData),
        throwsA(isA<TypeError>()));
  });
}
