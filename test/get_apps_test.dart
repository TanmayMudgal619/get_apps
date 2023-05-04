import 'package:flutter_test/flutter_test.dart';
import 'package:get_apps/app_info.dart';
import 'package:get_apps/get_apps.dart';
import 'package:get_apps/get_apps_platform_interface.dart';
import 'package:get_apps/get_apps_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockGetAppsPlatform
    with MockPlatformInterfaceMixin
    implements GetAppsPlatform {
  @override
  Future<List<AppInfo>> getAllApps() => Future.value([]);

  @override
  Future<List<AppInfo>> getUserApps() => Future.value([]);
}

void main() {
  final GetAppsPlatform initialPlatform = GetAppsPlatform.instance;

  test('$MethodChannelGetApps is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelGetApps>());
  });

  test('getPlatformVersion', () async {
    GetApps getAppsPlugin = GetApps();
    MockGetAppsPlatform fakePlatform = MockGetAppsPlatform();
    GetAppsPlatform.instance = fakePlatform;

    expect(await getAppsPlugin.getAllApps(), []);
    expect(await getAppsPlugin.getUserApps(), []);
  });
}
