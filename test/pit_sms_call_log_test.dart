import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:pit_sms_call_log/pit_sms_call_log.dart';

void main() {
  const MethodChannel channel = MethodChannel('pit_sms_call_log');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await PitSmsCallLog.platformVersion, '42');
  });
}
