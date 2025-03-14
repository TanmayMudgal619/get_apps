import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'get_apps_method_channel.dart';
import 'app_info.dart';

abstract class GetAppsPlatform extends PlatformInterface {
  /// Constructs a GetAppsPlatform.
  GetAppsPlatform() : super(token: _token);

  static final Object _token = Object();

  static GetAppsPlatform _instance = MethodChannelGetApps();

  /// The default instance of [GetAppsPlatform] to use.
  ///
  /// Defaults to [MethodChannelGetApps].
  static GetAppsPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [GetAppsPlatform] when
  /// they register themselves.
  static set instance(GetAppsPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<List<AppInfo>> getApps({bool includeSystemApps = false}) {
    throw UnimplementedError('getApps() has not been implemented.');
  }

  Future<void> openApp(String packageName) {
    throw UnimplementedError('openApp() has not been implemented.');
  }

  Stream<String> appRemoveReceiver() async*{
    throw UnimplementedError("appRemoveReceiver() has not been implemented.");
  }
}
