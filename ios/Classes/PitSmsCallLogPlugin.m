#import "PitSmsCallLogPlugin.h"
#import <pit_sms_call_log/pit_sms_call_log-Swift.h>

@implementation PitSmsCallLogPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPitSmsCallLogPlugin registerWithRegistrar:registrar];
}
@end
