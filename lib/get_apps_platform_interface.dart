import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'get_apps_channel.dart';
import 'models.dart';

abstract class GetAppsPlatform extends PlatformInterface {
  /// Constructs a GetAppsPlatform.
  GetAppsPlatform() : super(token: _token);

  static final Object _token = Object();

  static GetAppsPlatform _instance = GetAppsChannel();

  /// The default instance of [GetAppsPlatform] to use.
  ///
  /// Defaults to [GetAppsChannel].
  static GetAppsPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [GetAppsPlatform] when
  /// they register themselves.
  static set instance(GetAppsPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<AppInfo> getAppInfo(
    String packageName, {
    bool shouldInitialize = false,
  }) {
    throw UnimplementedError('getAppInfo() has not been implemented.');
  }

  Future<List<AppInfo>> getApps({AppType appType = AppType.all, LaunchType launchType = LaunchType.all}) {
    throw UnimplementedError('getApps() has not been implemented.');
  }

  Future<void> openApp(String packageName) {
    throw UnimplementedError('openApp() has not been implemented.');
  }

  Future<void> deleteApp(String packageName) {
    throw UnimplementedError('deleteApp() has not been implemented.');
  }

  Stream<ActionNotification> appActionReceiver() async* {
    throw UnimplementedError("appActionReceiver() has not been implemented.");
  }
}
