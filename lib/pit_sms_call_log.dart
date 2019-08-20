import 'dart:async';
import 'dart:core';


import 'package:flutter/services.dart';


class PitSmsCallLog {
  static const MethodChannel _channel =
      const MethodChannel('pit_sms_call_log');


  static Future<List<SmsLog>> getSmsLog({int daysBefore = 1}) async {
     final List<dynamic> sms = await _channel.invokeMethod("getSmsLog",{"days":daysBefore});
     return convertToSmsLogs(sms).toList();
  }

  static Future<List<CallLog>> getCallLog({int daysBefore = 1}) async {
     final List<dynamic> call = await _channel.invokeMethod("getCallLog",{"days":daysBefore});
     return converToCallLogs(call).toList();
  }


  static List<SmsLog> convertToSmsLogs(List<dynamic> sms){
   if(sms == null) return [];

   List<SmsLog> result = [];
   sms.forEach((smsLog){
     result.add(SmsLog(
         address: smsLog[Constant.SMS_ADDRESS],
         body: smsLog[Constant.SMS_BODY],
         date: smsLog[Constant.SMS_DATE]
     ));
   });
    return result;
  }

  static List<CallLog> converToCallLogs(List<dynamic> call){
    if(call == null) return [];

    List<CallLog> result = [];
    call.forEach((callLog){
      result.add(CallLog(
          callType: callLog[Constant.CALL_TYPE],
          callNumber: callLog[Constant.CALL_NUMBER],
          callDate: callLog[Constant.CALL_DATE],
          callDuration: callLog[Constant.CALL_DURATION]));
    });

    return result;
  }


}


class Constant {

  static const SMS_ADDRESS = "address";
  static const SMS_BODY = "body";
  static const SMS_DATE = "date";

  static const CALL_TYPE = "type";
  static const CALL_NUMBER = "number";
  static const CALL_DATE = "date";
  static const CALL_DURATION = "duration";

}

class SmsLog {
  final String body; // SMS message
  final String address; //SMS Sender
  final int date; //

  SmsLog({this.address, this.body, this.date});

}

class CallLog {
  final String callType;
  final String callNumber;
  final int callDate;
  final int callDuration;

  CallLog({this.callType, this.callNumber, this.callDate, this.callDuration});

}
