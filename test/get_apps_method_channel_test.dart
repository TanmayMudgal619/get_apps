import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get_apps/get_apps_method_channel.dart';

void main() {
  MethodChannelGetApps platform = MethodChannelGetApps();
  const MethodChannel channel = MethodChannel('get_apps');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getAllApps', () async {
    expect(await platform.getAllApps(), []);
  });
  test('getUserApps', () async {
    expect(await platform.getUserApps(), []);
  });
}
