import 'dart:async';

import 'package:flutter/services.dart';

class PitSmsCallLog {
  static const MethodChannel _channel =
      const MethodChannel('pit_sms_call_log');


  static Future<dynamic> getSmsLog({int daysBefore = 1}) async {
     final dynamic smsLog = await _channel.invokeMethod("getSmsLog",{"days":daysBefore});
     return smsLog;
  }

  static Future<dynamic> getCallLog({int daysBefore = 1}) async {
     final dynamic callLog = await _channel.invokeMethod("getCallLog",{"days":daysBefore});
     return callLog;
  }
}
