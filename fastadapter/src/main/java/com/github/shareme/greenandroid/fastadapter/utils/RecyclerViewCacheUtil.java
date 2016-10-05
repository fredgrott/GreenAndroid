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

import android.support.v7.widget.RecyclerView;

import com.github.shareme.greenandroid.fastadapter.IItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by mikepenz on 18.09.15.
 * This util prefills the cache of the RecyclerView to allow fast lag-free scrolling with many different views
 */
@SuppressWarnings("unused")
public class RecyclerViewCacheUtil<Item extends IItem> {
    private int mCacheSize = 2;

    /**
     * define the amount of elements which should be cached for a specific item type
     *
     * @param cacheSize
     * @return
     */
    public RecyclerViewCacheUtil withCacheSize(int cacheSize) {
        mCacheSize = cacheSize;
        return this;
    }

    /**
     * init the cache on your own.
     *
     * @param recyclerView
     * @param items
     */
    public void apply(RecyclerView recyclerView, Iterable<Item> items) {
        if (items != null) {
            //we pre-create the views for our cache
            HashMap<Integer, Stack<RecyclerView.ViewHolder>> cache = new HashMap<>();
            for (Item d : items) {
                if (!cache.containsKey(d.getType())) {
                    cache.put(d.getType(), new Stack<RecyclerView.ViewHolder>());
                }

                if (mCacheSize == -1 || cache.get(d.getType()).size() <= mCacheSize) {
                    cache.get(d.getType()).push(d.getViewHolder(recyclerView));
                }

                RecyclerView.RecycledViewPool recyclerViewPool = new RecyclerView.RecycledViewPool();

                //we fill the pool
                for (Map.Entry<Integer, Stack<RecyclerView.ViewHolder>> entry : cache.entrySet()) {
                    recyclerViewPool.setMaxRecycledViews(entry.getKey(), mCacheSize);

                    for (RecyclerView.ViewHolder holder : entry.getValue()) {
                        recyclerViewPool.putRecycledView(holder);
                    }

                    //make sure to clear the stack
                    entry.getValue().clear();
                }

                //make sure to clear the cache
                cache.clear();

                recyclerView.setRecycledViewPool(recyclerViewPool);
            }
        }
    }
}
