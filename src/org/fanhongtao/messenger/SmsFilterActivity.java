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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This activity lists filter numbers.
 * @author Fan Hongtao &ltfanhongtao@gmail.com&gt 
 */
public class SmsFilterActivity extends Activity {

    /** Numbers need to be filtered */
    private List<Filter> filters;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_filter);
        filters = FilterDatabase.queryAllFilters();;
        listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(new FilterAdapter(this, R.layout.sms_filter_item, filters));
    }

    private static final int MENU_ADD = Menu.FIRST;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ADD, 1, "Add").setIcon(android.R.drawable.ic_menu_add);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_ADD:

            break;
        default:
            break;
        }
        return false;
    }

    class FilterAdapter extends ArrayAdapter<Filter> {
        private int resId;
        private LayoutInflater mInflater;

        /**
         * @param context
         * @param textViewResourceId
         * @param filters
         */
        public FilterAdapter(Context context, int textViewResourceId, List<Filter> filters) {
            super(context, textViewResourceId, filters);
            resId = textViewResourceId;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = mInflater.inflate(resId, parent, false);
            } else {
                view = convertView;
            }

            Filter filter = getItem(position);
            TextView number = (TextView) view.findViewById(R.id.number);
            TextView type = (TextView) view.findViewById(R.id.type);
            number.setText(filter.getNumber());
            type.setText(Integer.toString(filter.getType()));
            view.setMinimumHeight(100);
            return view;
        }

    }
}
