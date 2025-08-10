import 'package:flutter_test/flutter_test.dart';
import 'package:get_apps/get_apps.dart';
import 'package:get_apps/get_apps_platform_interface.dart';
import 'package:get_apps/get_apps_method_channel.dart';
import 'package:get_apps/models.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockGetAppsPlatform
    with MockPlatformInterfaceMixin
    implements GetAppsPlatform {
  @override
  Future<List<AppInfo>> getApps({bool includeSystemApps = false}) => Future.value([]);

  @override
  Future<void> openApp(String packageName) => Future.value();

  @override
  Stream<ActionNotification> appActionReceiver() {
    throw UnimplementedError();
  }

  @override
  Future<void> deleteApp(String packageName) {
    throw UnimplementedError();
  }

  @override
  Future<void> init() {
    throw UnimplementedError();
  }
}

void main() {
  final GetAppsPlatform initialPlatform = GetAppsPlatform.instance;

  test('$MethodChannelGetApps is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelGetApps>());
  });

  test('getApps Test', () async {
    GetApps getAppsPlugin = GetApps();
    MockGetAppsPlatform fakePlatform = MockGetAppsPlatform();
    GetAppsPlatform.instance = fakePlatform;

    expect(await getAppsPlugin.getApps(), []);
  });
}
