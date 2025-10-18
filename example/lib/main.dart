import 'package:flutter/material.dart';
import 'package:get_apps/get_apps.dart';
import 'package:get_apps/models.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  late Future<List<AppInfo>> userApps;

  @override
  void initState() {
    GetApps()
        .getAppInfo("com.android.chrome", shouldInitialize: false)
        .then((appInfo) {
          print("Chrome is installed on your device");
        })
        .onError((e, s) {
          print("Chrome is not installed on your device $e");
        });
    userApps = GetApps().getApps(launchType: LaunchType.launchable);
    GetApps().appActionReceiver().forEach((packageAction) {
      userApps = GetApps().getApps(launchType: LaunchType.launchable);
      setState(() {});
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('Plugin example app')),
        body: FutureBuilder<List<AppInfo>>(
          future: userApps,
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              final data = snapshot.requireData;
              return ListView(
                children:
                    data
                        .map(
                          (e) => ListTile(
                            leading: Image.memory(e.appIcon),
                            title: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  "AppName: ${e.appName}",
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                ),
                                Text(
                                  "PackageName: ${e.appPackage}",
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                ),
                                Text(
                                  "Version: v${e.versionName}",
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                ),
                              ],
                            ),
                            subtitle: Text(e.description ?? ""),
                            onLongPress: () {
                              GetApps().deleteApp(e.appPackage);
                            },
                            onTap: () {
                              GetApps().openApp(e.appPackage);
                            },
                          ),
                        )
                        .toList(),
              );
            }
            if (snapshot.hasError) {
              return Center(child: Text(snapshot.error.toString()));
            }
            return const Center(child: CircularProgressIndicator());
          },
        ),
      ),
    );
  }
}
