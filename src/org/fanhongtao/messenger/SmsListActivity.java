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
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * @author Fan Hongtao &ltfanhongtao@gmail.com&gt
 */
public class SmsListActivity extends BaseActivity {

    private ListView mListView;
    private ListAdapter mListAdapter;

    private int mPosition;
    private int mTop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TAG = "SmsListActivity";
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sms_list);
        initView();
        initData();
        addListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListView.setSelectionFromTop(mPosition, mTop);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPosition = mListView.getFirstVisiblePosition();
        View first = mListView.getChildAt(0);
        mTop = (first == null) ? 0 : first.getTop();
    }

    /**
     * 获取控件
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.sms_list);
        mListView.setSaveEnabled(true);
    }

    /**
     * 为控件加载数据
     */
    private void initData() {
        String[] projection = new String[] { "thread_id as _id", "thread_id", "msg_count", "snippet",
                "sms.address as address", "sms.date as date" };
        Uri uri = Uri.parse("content://sms/conversations");
        Cursor cursor = managedQuery(uri, projection, null, null, "sms.date desc");
        if (cursor == null) {
            Log.e(TAG, "Failed to query SMS list");
            return;
        }

        mListAdapter = new SmsAdapter(this, R.layout.sms_list_item, cursor);
        mListView.setAdapter(mListAdapter);
    }

    /**
     * 给控件添加事件
     */
    private void addListener() {
        // 点击列表记录时，显示该记录对应的短信会话
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CursorWrapper cw = (CursorWrapper) SmsListActivity.this.mListAdapter.getItem(position);
                TextView textView = (TextView) view.findViewById(R.id.sms_sender);
                long threadId = cw.getLong(0);
                Intent intent = new Intent(SmsListActivity.this, SmsThreadActivity.class);
                intent.putExtra(SmsThreadActivity.BUNDLE_THREAD_ID, threadId);
                intent.putExtra(SmsThreadActivity.BUNDLE_SENDER, textView.getText());
                startActivity(intent);
            }
        });
    }

    private class SmsAdapter extends ResourceCursorAdapter {
        public SmsAdapter(Context context, int layout, Cursor cursor) {
            super(context, layout, cursor, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView sender = (TextView) view.findViewById(R.id.sms_sender);
            TextView content = (TextView) view.findViewById(R.id.sms_content);
            TextView count = (TextView) view.findViewById(R.id.sms_count);
            TextView date = (TextView) view.findViewById(R.id.sms_date);
            sender.setText(cursor.getString(cursor.getColumnIndex("address")));
            content.setText(cursor.getString(cursor.getColumnIndex("snippet")));
            count.setText(cursor.getString(cursor.getColumnIndex("msg_count")));
            Long d = cursor.getLong(cursor.getColumnIndex("date"));
            date.setText(getyyyy_MM_ddHHmmss_DateFormat().format(new Date(d)));
        }
    }
}
