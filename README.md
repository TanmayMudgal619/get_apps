# get_apps

Flutter Plugin to get all installed apps.

## Usage

To use this plugin, add `get_apps` as a [dependency in your pubspec.yaml file](https://pub.dev/packages/get_apps).


### Example

<?code-excerpt "basic.dart (basic-example)"?>
``` dart
import 'package:flutter/material.dart';
import 'package:get_apps/app_info.dart';
import 'package:get_apps/get_apps.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: FutureBuilder<List<AppInfo>>(
          future: GetApps().getApps(),
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              final data = snapshot.requireData;
              return ListView(
                children: data
                    .map((e) => ListTile(
                  leading: Image.memory(e.appIcon),
                  title: Text(e.appName),
                  subtitle: Text(e.appPackage),
                  onTap: () {
                    GetApps().openApp(e.appPackage);
                  },
                ))
                    .toList(),
              );
            }
            if (snapshot.hasError) {
              return Center(
                child: Text(snapshot.error.toString()),
              );
            }
            return const Center(
              child: CircularProgressIndicator(),
            );
          },
        ),
      ),
    );
  }
}
```