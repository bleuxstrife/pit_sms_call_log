package com.padimas.pit_sms_call_log;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.Telephony;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * PitSmsCallLogPlugin
 */
public class PitSmsCallLogPlugin implements MethodCallHandler {
    Context context;


    public PitSmsCallLogPlugin(Registrar registrar) {
        context = registrar.context();
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "pit_sms_call_log");
        channel.setMethodCallHandler(new PitSmsCallLogPlugin(registrar));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if(call.method.equals("getSmsLog")){

            int days = call.argument("days");
            try {
                List<Map<String, Object>> smsLogs = getSmsLog(days);
                result.success(smsLogs);
            } catch (Exception e1) {
                e1.printStackTrace();
                result.error("Error ", "getSmsLog Error : " + e1.getLocalizedMessage(), e1);
            }

        } else if(call.method.equals("getCallLog")){

            int days = call.argument("days");
            try {
                List<Map<String, Object>> callLogs = getCallLog(days);
                result.success(callLogs);
            } catch (Exception e) {
                e.printStackTrace();
                result.error("Error ", "getCallLog Error : " + e.getLocalizedMessage(), e);
            }

        } else {
            result.notImplemented();
        }
    }

    private List<Map<String, Object>> getCallLog(int days) throws Exception{
        List<Map<String, Object>> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, - days);
        long timeBefore = calendar.getTimeInMillis();
        String condition = CallLog.Calls.DATE + " > " +timeBefore;
        String [] projections = {
                CallLog.Calls.TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
        };

            Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projections,
                    condition, null, null);

            if(cursor!=null){

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                    Map<String, Object> result = new HashMap<>();
                    for (int i = 0; i < projections.length; i++) {

                        Object value;
                        if (projections[i].equals(CallLog.Calls.TYPE)){
                            value = getCallType(cursor.getString(i));
                        } else if (projections[i].equals(CallLog.Calls.DATE)){
                            value = Long.parseLong(cursor.getString(i));
                        } else if (projections[i].equals(CallLog.Calls.DURATION)){
                            value = Integer.parseInt(cursor.getString(i));
                        } else {
                            value = cursor.getString(i);
                        }
                        result.put(projections[i], value);
                    }
                    list.add(result);
                }
                cursor.close();
            }

        return list;
    }

    private List<Map<String, Object>> getSmsLog(int days) throws Exception{
        List<Map<String, Object>> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, - days);
        long timeBefore = calendar.getTimeInMillis();
        String condition = Telephony.Sms.DATE + " > " +timeBefore;
        String [] projections = {
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE
        };
            Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, projections, condition, null, null);

            if(cursor!=null){

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                    Map<String, Object> result = new HashMap<>();
                    for (int i = 0; i < projections.length; i++) {
                        Object value;
                        if(projections[i].equals( Telephony.Sms.DATE)){
                            value = Long.parseLong(cursor.getString(i));
                        } else {
                            value = cursor.getString(i);
                        }

                        result.put(projections[i], value);
                    }
                    list.add(result);
                }
                cursor.close();
            }

        return list;
    }

    private String getCallType(String type){
        String callType = "";
        int code = Integer.parseInt(type);
        switch (code) {
            case CallLog.Calls.OUTGOING_TYPE:
                callType = "OUTGOING";
                break;

            case CallLog.Calls.INCOMING_TYPE:
                callType = "INCOMING";
                break;

            case CallLog.Calls.MISSED_TYPE:
                callType = "MISSED";
                break;
        }
        return callType;
    }

}
