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
import org.fanhongtao.messenger.db.SmsDbHelper;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Fan Hongtao &ltfanhongtao@gmail.com&gt 
 */
public class App extends Application {

    private static App instance;

    private SmsDbHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dbHelper = new SmsDbHelper(this);
        List<Filter> filters = FilterDatabase.queryAllFilters();
        if ((filters == null) || (filters.isEmpty())) {
            FilterDatabase.addFilter("95566", 2);
            FilterDatabase.addFilter("10010", 2);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        dbHelper.close();
    }

    public SmsDbHelper getDbHelper() {
        return dbHelper;
    }

    public static App getInstance() {
        return instance;
    }

    public static SQLiteDatabase getReadableDatabase() {
        return getInstance().dbHelper.getReadableDatabase();
    }
}
