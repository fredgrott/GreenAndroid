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

import android.support.annotation.Nullable;

import com.github.shareme.greenandroid.fastadapter.IItem;


/**
 * Created by mikepenz on 24.08.16.
 */
@SuppressWarnings("unused")
public class DiffCallbackImpl<Item extends IItem> implements DiffCallback<Item> {
    @Override
    public boolean areItemsTheSame(Item oldItem, Item newItem) {
        return oldItem.getIdentifier() == newItem.getIdentifier();
    }

    @Override
    public boolean areContentsTheSame(Item oldItem, Item newItem) {
        return oldItem.equals(newItem);
    }

    @Nullable
    @Override
    public Object getChangePayload(Item oldItem, int oldItemPosition, Item newItem, int newItemPosition) {
        return null;
    }
}
