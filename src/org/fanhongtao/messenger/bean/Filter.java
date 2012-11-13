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
package org.fanhongtao.messenger.bean;

/**
 * @author Fan Hongtao &ltfanhongtao@gmail.com&gt 
 */
public class Filter {
    /** row id */
    private long id;

    /** The number to be filtered */
    private String number;

    /** 
     * Filter type:
     * 0: Do not filter.
     * 1: Blacklist, delete the message
     * 2: Mute, save the message into INBOX, but do not notify the user
     * */
    private int type;

    public Filter() {
    }

    public Filter(long id, String number, int type) {
        super();
        this.id = id;
        this.number = number;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
