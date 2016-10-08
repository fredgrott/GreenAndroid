/*
 Copyright 2015 florent37, Inc.
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
package com.github.shareme.greenandroid.materialleanback;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by florentchampigny on 28/08/15.
 */
public class PlaceHolderViewHolder extends RecyclerView.ViewHolder {

    public PlaceHolderViewHolder(View itemView, boolean horizontal, int dimen) {
        super(itemView);

        if(horizontal) {
            if (dimen != -1) {
                itemView.getLayoutParams().width = dimen;
                itemView.requestLayout();
            }
        }else{
            if (dimen != -1) {
                itemView.getLayoutParams().height = dimen;
                itemView.requestLayout();
            }
        }
    }

}