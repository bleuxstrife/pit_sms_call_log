import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:pit_sms_call_log/pit_sms_call_log.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  List<dynamic> smsList = [] ;
  List<dynamic> callLogs = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }


  Future<void> initPlatformState() async {

    List<dynamic> smsList = [] ;
    List<dynamic> callLogs = [];
    try {
      smsList = await PitSmsCallLog.getSmsLog(daysBefore: 3);
      callLogs = await PitSmsCallLog.getCallLog(daysBefore: 3);
    } on PlatformException {
      print('error');
    }

    if (!mounted) return;
    setState(() {
      this.smsList = smsList;
      this.callLogs = callLogs;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
//          child: Text('Call Logs :\n $callLogs'),
          child: Text('SMS List :\n $smsList'),
        ),
      ),
    );
  }
}
