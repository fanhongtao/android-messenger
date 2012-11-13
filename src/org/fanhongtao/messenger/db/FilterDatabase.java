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
package org.fanhongtao.messenger.db;

import java.util.ArrayList;
import java.util.List;

import org.fanhongtao.messenger.App;
import org.fanhongtao.messenger.bean.Filter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * For table Filter
 * @author Fan Hongtao &ltfanhongtao@gmail.com&gt 
 */
public class FilterDatabase {

    public static final String TABLE_NAME = "filter";

    /**
     * Common fields for table Filter
     */
    public interface FilterColumns extends BaseColumns {
        public static final String NUMBER = "number";
        public static final String TYPE = "type";
    }

    private static String[] ALL_COLUMNS = { FilterColumns._ID, FilterColumns.NUMBER, FilterColumns.TYPE };

    public static final String CREATE_SQL = "CREATE TABLE filter (" + FilterColumns._ID
            + " integer primary key autoincrement, " + FilterColumns.NUMBER + " text not null, " + FilterColumns.TYPE + " integer);";

    public static Filter addFilter(String number, int type) {
        ContentValues values = new ContentValues();
        values.put(FilterColumns.NUMBER, number);
        values.put(FilterColumns.TYPE, type);

        SQLiteDatabase database = App.getReadableDatabase();
        long insertId = database.insert(TABLE_NAME, null, values);
        Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, FilterColumns.NUMBER + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        cursor.close();

        return new Filter(insertId, number, type);
    }

    public static List<Filter> queryAllFilters() {
        List<Filter> filters = new ArrayList<Filter>();
        SQLiteDatabase database = App.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Filter filter = cursorToFilter(cursor);
            filters.add(filter);
            cursor.moveToNext();
        }
        cursor.close();
        return filters;
    }

    public static void deleteFilter(Filter filter) {
        long id = filter.getId();
        SQLiteDatabase database = App.getReadableDatabase();
        database.delete(TABLE_NAME, FilterColumns._ID + " = " + id, null);
    }

    private static Filter cursorToFilter(Cursor cursor) {
        Filter filter = new Filter();
        filter.setId(cursor.getLong(0));
        filter.setNumber(cursor.getString(1));
        filter.setType(cursor.getInt(2));
        return filter;
    }

}
