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
    Registrar registrar;

    public PitSmsCallLogPlugin(Registrar registrar) {
        this.registrar = registrar;
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
            result.success(getSmsLog(days));
        } else if(call.method.equals("getCallLog")){
            int days = call.argument("days");
            result.success(getCallLog(days));
        } else {
            result.notImplemented();
        }
    }

    private List<Map<String, Object>> getCallLog(int days){
        List<Map<String, Object>> list = new ArrayList<>();
        Context context = registrar.context();
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

        try {
            Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projections,
                    condition, null, null);

            if(cursor!=null){
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Map<String, Object> result = new HashMap<>();
                    for (int i = 0; i < projections.length; i++) {
                        result.put(getCallKey(projections[i]),
                                projections[i].equals(CallLog.Calls.TYPE)
                                        ? getCallType(cursor.getString(i))
                                        : projections[i].equals(CallLog.Calls.DATE)
                                            ? getDate((long) Double.parseDouble(cursor.getString(i)))
                                            : cursor.getString(i)
                                );
                    }
                    list.add(result);
                }
                cursor.close();
            }
            Log.d("Success", "getCallLogs:" +list);
        } catch (Exception e){
            Log.d("Error", "getCallLog:" + e.getLocalizedMessage());
        }

        return list;
    }

    private String getDate(long dateInMilli){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMilli);
        return formatter.format(calendar.getTime());
    }

    private List<Map<String, Object>> getSmsLog(int days){
        List<Map<String, Object>> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, - days);
        long timeBefore = calendar.getTimeInMillis();
        String condition = Telephony.Sms.DATE + " > " +timeBefore;
        Log.d("Success", "timeBefore: " +timeBefore);
        Context context = registrar.context();
        String [] projections = {
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE
        };

        try {
            Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, projections, condition, null, null);
            if(cursor!=null){
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Map<String, Object> result = new HashMap<>();
                    for (int i = 0; i < projections.length; i++) {
                        result.put(getSmsKey(projections[i]), projections[i].equals(Telephony.Sms.DATE)
                                ? getDate((long) Double.parseDouble(cursor.getString(i)))
                                : cursor.getString(i));
                    }
                    list.add(result);
                }
                cursor.close();
            }
            Log.d("Success", "getSmsLog:" +list);
        } catch (Exception e){
            Log.d("Error", "getSmsLog:" + e.getLocalizedMessage());
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

    private String getCallKey(String project){
        String key = "";
        switch (project){
            case CallLog.Calls.TYPE:
                key = "type";
                break;
            case CallLog.Calls.NUMBER:
                key = "phoneNumber";
                break;
            case CallLog.Calls.DATE:
                key = "date";
                break;
            case CallLog.Calls.DURATION:
                key = "duration";
        }
        return key;
    }

    private String getSmsKey(String project){
        String key = "";
        switch (project){
            case Telephony.Sms.ADDRESS:
                key = "address";
                break;
            case Telephony.Sms.BODY:
                key = "body";
                break;
            case Telephony.Sms.DATE:
                key = "date";
                break;
        }

        return key;
    }
}
