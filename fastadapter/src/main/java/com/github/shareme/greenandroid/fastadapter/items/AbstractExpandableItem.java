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
package com.github.shareme.greenandroid.fastadapter.items;

import android.support.v7.widget.RecyclerView;

import com.github.shareme.greenandroid.fastadapter.IClickable;
import com.github.shareme.greenandroid.fastadapter.IExpandable;
import com.github.shareme.greenandroid.fastadapter.IItem;
import com.github.shareme.greenandroid.fastadapter.ISubItem;

import java.util.List;

/**
 * Created by mikepenz on 28.12.15.
 */
@SuppressWarnings("unused")
public abstract class AbstractExpandableItem<Parent extends IItem & IExpandable & ISubItem & IClickable, VH extends RecyclerView.ViewHolder, SubItem extends IItem & ISubItem> extends AbstractItem<Parent, VH> implements IExpandable<AbstractExpandableItem, SubItem>, ISubItem<AbstractExpandableItem, Parent> {
    private List<SubItem> mSubItems;
    private Parent mParent;
    private boolean mExpanded = false;

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public AbstractExpandableItem<Parent, VH, SubItem> withIsExpanded(boolean expanded) {
        mExpanded = expanded;
        return this;
    }

    @Override
    public List<SubItem> getSubItems() {
        return mSubItems;
    }

    @Override
    public boolean isAutoExpanding() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractExpandableItem<Parent, VH, SubItem> withSubItems(List<SubItem> subItems) {
        this.mSubItems = subItems;
        for (SubItem subItem : subItems) {
            subItem.withParent(this);
        }
        return this;
    }

    @Override
    public Parent getParent() {
        return mParent;
    }

    @Override
    public AbstractExpandableItem<Parent, VH, SubItem> withParent(Parent parent) {
        this.mParent = parent;
        return this;
    }

    @Override
    public boolean isSelectable() {
        //this might not be true for your application
        return getSubItems() == null;
    }
}
