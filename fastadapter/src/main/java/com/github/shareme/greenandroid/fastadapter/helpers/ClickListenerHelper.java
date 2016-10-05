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
package com.github.shareme.greenandroid.fastadapter.helpers;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.shareme.greenandroid.fastadapter.FastAdapter;
import com.github.shareme.greenandroid.fastadapter.IItem;


/**
 * Created by mikepenz on 25.01.16.
 */
@SuppressWarnings("unused")
public class ClickListenerHelper<Item extends IItem> {
    //
    private FastAdapter<Item> mFastAdapter;

    /**
     * ctor
     *
     * @param fastAdapter the fastAdapter which manages these items
     */
    public ClickListenerHelper(FastAdapter<Item> fastAdapter) {
        this.mFastAdapter = fastAdapter;
    }

    /**
     * @param viewHolder      the viewHolder which got created
     * @param view            the view which listens for the click
     * @param onClickListener the listener which gets called
     */
    public void listen(final RecyclerView.ViewHolder viewHolder, View view, final OnClickListener<Item> onClickListener) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we get the adapterPosition from the viewHolder
                int pos = mFastAdapter.getHolderAdapterPosition(viewHolder);
                //make sure the click was done on a valid item
                if (pos != RecyclerView.NO_POSITION) {
                    //we update our item with the changed property
                    onClickListener.onClick(v, pos, mFastAdapter.getItem(pos));
                }
            }
        });
    }

    /**
     * @param viewHolder      the viewHolder which got created
     * @param viewId          the viewId which listens for the click
     * @param onClickListener the listener which gets called
     */
    public void listen(final RecyclerView.ViewHolder viewHolder, @IdRes int viewId, final OnClickListener<Item> onClickListener) {
        viewHolder.itemView.findViewById(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we get the adapterPosition from the viewHolder
                int pos = mFastAdapter.getHolderAdapterPosition(viewHolder);
                //make sure the click was done on a valid item
                if (pos != RecyclerView.NO_POSITION) {
                    //we update our item with the changed property
                    onClickListener.onClick(v, pos, mFastAdapter.getItem(pos));
                }
            }
        });
    }

    public interface OnClickListener<Item extends IItem> {
        /**
         * @param v        the view which got clicked
         * @param position the items position which got clicked
         * @param item     the item which is responsible for this position
         */
        void onClick(View v, int position, Item item);
    }
}
