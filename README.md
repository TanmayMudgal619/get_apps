# get_apps

Flutter Plugin to get all installed apps.

## Important Note

For now code is not organized due to some urgency on my side but soon the whole code will be refactored though the APIs will be working the same(most probably).
For now I'm not very consistent with the way I make change but soon there will a defined way to make changes in this repo.

## TODO
- [ ] Adding a stream method to continously listen all apps rather then fetching them again manually on add/remove events.
- [ ] Add more details regarding apps, such as their version.
- [ ] `init` should be called automatically if hasn't been called before, rather than showing an error.
- [ ] Valid Test Cases.
- [ ] How about supporting other platforms too?

## Usage

To use this plugin, add `get_apps` as a [dependency in your pubspec.yaml file](https://pub.dev/packages/get_apps).


### Example

Here is how you can use this package

<?code-excerpt "basic.dart (basic-example)"?>
``` dart
import 'package:flutter/material.dart';
import 'package:get_apps/get_apps.dart';
import 'package:get_apps/models.dart';

void main() async{
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
    userApps = GetApps.init().then((_){
      GetApps().appActionReceiver().forEach((packageAction){
        userApps = GetApps().getApps();
        setState(() {});
      });
      return GetApps().getApps();
    });
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
          future: userApps,
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              final data = snapshot.requireData;
              return ListView(
                children: data
                    .map((e) => ListTile(
                  leading: Image.memory(e.appIcon),
                  title: Text(e.appName),
                  subtitle: Text(e.appPackage),
                  onLongPress: () {
                    GetApps().deleteApp(e.appPackage);
                  },
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
