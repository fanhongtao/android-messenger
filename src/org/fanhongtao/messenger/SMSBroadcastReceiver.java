/*
 * Copyright (C) 2012 Fan Hongtao (http://www.fanhongtao.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fanhongtao.messenger;

import java.util.List;

import org.fanhongtao.messenger.bean.Filter;
import org.fanhongtao.messenger.db.FilterDatabase;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * A SMS filter.<br>
 * If the sender needs to be filtered, the broadcast is aborted.
 * @author Fan Hongtao &ltfanhongtao@gmail.com&gt 
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdusObjects = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdusObjects.length];
                for (int i = 0; i < pdusObjects.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdusObjects[i]);
                }
                for (SmsMessage message : messages) {
                    String smsAddress = message.getDisplayOriginatingAddress();
                    String smsContent = message.getDisplayMessageBody();
                    String loginfo = "from [" + smsAddress + "], " + "content [" + smsContent + ']';
                    int type = getFilterType(smsAddress);
                    switch (type) {
                    case 1:
                        Log.e(TAG, "Abort sms: " + loginfo);
                        abortBroadcast();
                        break;
                    case 2:
                        Log.i(TAG, "Mute sms: " + loginfo);
                        writeSmsToInbox(context, message);
                        abortBroadcast();
                        break;
                    default:
                        Log.d(TAG, "Receive sms: " + loginfo);
                        break;
                    }
                }
            }
        }
    }

    private int getFilterType(String smsAddress) {
        List<Filter> filters = FilterDatabase.queryAllFilters();
        for (Filter filter : filters) {
            if (filter.getNumber().equals(smsAddress)) {
                return filter.getType();
            }
        }
        return 0;
    }

    /**
     * Write received SMS into INBOX
     * @param context
     * @param message
     */
    private void writeSmsToInbox(Context context, SmsMessage message) {
        ContentValues values = new ContentValues();
        values.put("body", message.getMessageBody());
        values.put("address", message.getOriginatingAddress());
        values.put("date", System.currentTimeMillis());
        values.put("read", 1);
        values.put("type", 1);
        context.getContentResolver().insert(Uri.parse("content://sms"),values); 
    }
}
