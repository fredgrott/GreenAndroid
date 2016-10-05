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
package com.github.shareme.greenandroid.fastadapter.adapters;


import com.github.shareme.greenandroid.fastadapter.IItem;

/**
 * Created by mikepenz on 27.12.15.
 * Based on the ItemAdapter, with a different order to show it's items after the ItemAdapter
 */
@SuppressWarnings("unused")
public class FooterAdapter<Item extends IItem> extends ItemAdapter<Item> {

    /**
     * @return the order
     */
    @Override
    public int getOrder() {
        return 1000;
    }

}
