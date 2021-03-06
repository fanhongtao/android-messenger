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

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


/**
 * @author Fan Hongtao &ltfanhongtao@gmail.com&gt
 */
public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addTabView(getString(R.string.title_activity_sms_list), null, SmsListActivity.class);
        addTabView(getString(R.string.title_activity_sms_filter), null, SmsFilterActivity.class);
    }

    protected void addTabView(String tabId, Drawable icon, Class<?> viewClass) {
        TabHost tabHost = getTabHost();
        TabSpec tabSpec = tabHost.newTabSpec(tabId);
        tabSpec.setIndicator(tabId, icon);
        tabSpec.setContent(new Intent(this, viewClass));
        tabHost.addTab(tabSpec);
    }

}
