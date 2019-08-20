```
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

  List<SmsLog> smsList = [] ;
  List<CallLog> callLogs = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }


  Future<void> initPlatformState() async {

    List<SmsLog> smsList = [] ;
    List<CallLog> callLogs = [];
    try {
      smsList = await PitSmsCallLog.getSmsLog(daysBefore: 3);
      callLogs = await PitSmsCallLog.getCallLog(daysBefore: 20);
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
        body: SingleChildScrollView(
//          child: Text('Call Logs :\n $callLogs'),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Text('SMS List :\n'),
              _buildItemSmsLog(),
              Text('Call Log :\n'),
              _buildItemCallLog()
            ],
          )

        ),
      ),
    );
  }

  _buildItemSmsLog(){
    List<Widget> item = [];
    smsList.forEach((sms){
      item.add(
        Container(
          child: Text("Address : ${sms.address}, Body : ${sms.body}, Date : ${sms.date}"),
          margin: EdgeInsets.only(left: 16, right: 16, bottom: 16),
        )

      );
    });
    return Column(children: item,);
  }

  _buildItemCallLog(){
    List<Widget> item = [];
    callLogs.forEach((call){
      item.add(
          Container(
            child: Text("Call Type : ${call.callType}, Call Number : ${call.callNumber}, Call Date : ${call.callDate}, "
                +"Call Duration : ${call.callDuration} second" ),
            margin: EdgeInsets.only(left: 16, right: 16, bottom: 16),
          )

      );
    });
    return Column(children: item,);
  }
}

```
