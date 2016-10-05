/*
  Copyright 2016 Mike Penz
  Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */
package com.github.shareme.greenandroid.fastadapter.utils;

import android.support.annotation.NonNull;

import com.github.shareme.greenandroid.fastadapter.IIdentifyable;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by mikepenz on 19.09.15.
 */
@SuppressWarnings("unused")
public class IdDistributor {
    private static final AtomicLong idDistributor = new AtomicLong(9000000000000000000L);

    /**
     * set an unique identifier for all items which do not have one set already
     *
     * @param items
     * @return
     */
    public static <T extends IIdentifyable> List<T> checkIds(@NonNull List<T> items) {
        for (int i = 0, size = items.size(); i < size; i++) {
            checkId(items.get(i));
        }
        return items;
    }

    /**
     * set an unique identifier for all items which do not have one set already
     *
     * @param items
     * @return
     */
    @SafeVarargs
    public static <T extends IIdentifyable> T[] checkIds(@NonNull T... items) {
        for (T item : items) {
            checkId(item);
        }
        return items;
    }

    /**
     * set an unique identifier for the item which do not have one set already
     *
     * @param item
     * @return
     */
    public static <T extends IIdentifyable> T checkId(@NonNull T item) {
        if (item.getIdentifier() == -1) {
            item.withIdentifier(idDistributor.incrementAndGet());
        }
        return item;
    }
}
