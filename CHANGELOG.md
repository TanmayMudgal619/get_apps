## 0.0.1

* Initial release.


## 1.0.0

* Updated Gradle version.


## 1.0.1

* Added Open App feature.


## 1.0.2

* Added own native function to open app.
* Removed URL Launcher.
* Merged UserApps and AllApps function into one with includeSystemApps parameter.


## 1.0.3

* Minor issue removed.


## 1.0.4

* Updated example.


## 1.0.5

* Package add or remove notification added.


## 1.0.6

* Uninstall app feature added.


## 1.0.7

* Set Min SDK version 16.


## 1.0.8

* Set Min SDK version 21.


## 1.0.9

* Optimized the refresh speed in case of removal of any application.


## 1.0.10

* Optimized the refresh speed in case of addition of any application.


## 2.0.0

* Static Initializer for the GetApps.
* All the functional calls are now moved from main thread to secondary threads.


## 2.0.1

* Removed Race condition for initialization and other method calls.


## 3.0.0

* Removed `init` method as wasn't much useful, now any call to native code will trigger
  initialization if not done already.


## 3.0.1

* Added `getAppInfo` to fetch data of one app.
* Added `versionName`, `versionCode`, `description` and `isSystemApp` fields in `AppInfo`.
