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

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * @author Fan Hongtao &ltfanhongtao@gmail.com&gt
 */
public class SmsThreadActivity extends BaseActivity {
    public static final String BUNDLE_THREAD_ID = "thread_id";
    public static final String BUNDLE_SENDER = "sender";

    private ListAdapter mListAdapter;
    private ListView mListView;
    private TextView mSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG = "SmsThreadActivity";
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sms_thread_list);
        initView();
        initData();
    }

    private void initView() {
        mListView = ((ListView) findViewById(R.id.sms_thread_list));
        mSender = ((TextView) findViewById(R.id.sms_sender));
    }

    private void initData() {
        Intent intent = getIntent();
        long threadId = intent.getLongExtra(BUNDLE_THREAD_ID, 0L);
        Log.i(TAG, "Thread id: " + threadId);
        Uri uri = Uri.parse("content://sms");
        Cursor cursor = managedQuery(uri, null, "thread_id = " + threadId, null, null);
        if (cursor == null) {
            Log.e(TAG, "Failed to query");
            return;
        }

        mSender.setText(intent.getStringExtra(BUNDLE_SENDER));
        mListAdapter = new SmsThreadAdapter(this, R.layout.sms_thread_list_item, cursor);
        mListView.setAdapter(mListAdapter);
    }

    class SmsThreadAdapter extends ResourceCursorAdapter {
        public SmsThreadAdapter(Context context, int layout, Cursor cursor) {
            super(context, layout, cursor, false);
        }

        public void bindView(View view, Context context, Cursor cursor) {
            TextView sentTime = (TextView) view.findViewById(R.id.sms_send_time);
            TextView content = (TextView) view.findViewById(R.id.sms_content);
            String str = cursor.getString(cursor.getColumnIndex("date"));
            sentTime.setText(getyyyy_MM_ddHHmmss_DateFormat().format(new Date(Long.parseLong(str))));
            content.setText(cursor.getString(cursor.getColumnIndex("body")));
        }
    }
}
