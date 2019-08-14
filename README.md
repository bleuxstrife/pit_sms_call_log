# PIT SMS and Call Logs

Use this Plugin to get SMS and Call Logs.

*Note*: This plugin is still under development, and some Components might not be available yet or still has so many bugs and this plugin just for android only.

## Installation

First, add `pit_sms_call_log` as a [dependency in your pubspec.yaml file](https://flutter.io/platform-plugins/).

```
pit_sms_call_log: ^0.1.0
```

## Important

this plugin depends on other plugins, you must have a permission to use this plugin, you can use `pit_permission` plugin or other permission plugin.

You must add this permission in AndroidManifest.xml for Android

```
for read sms =  <uses-permission android:name="android.permission.READ_SMS"/>

for read call logs = <uses-permission android:name="android.permission.READ_CALL_LOG"/>
```


## Example for Get SMS
```
     List<dynamic> smsList = await PitNetwork.getSmsLog();;
```
## Example for Get Call Logs
```
     List<dynamic> callLogs = await PitNetwork.getCallLog();
```


