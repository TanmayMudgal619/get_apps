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
    userApps = GetApps().getApps();
    GetApps().appActionReceiver().forEach((packageAction){
      userApps = GetApps().getApps();
      setState(() {});
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