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

import com.github.shareme.greenandroid.fastadapter.IGenericItem;

import java.lang.reflect.ParameterizedType;

/**
 * Created by mikepenz on 14.07.15.
 * Implements the general methods of the IItem interface to speed up development.
 */
@SuppressWarnings("unused")
public abstract class GenericAbstractItem<Model, Item extends GenericAbstractItem<?, ?, ?>, VH extends RecyclerView.ViewHolder> extends AbstractItem<Item, VH> implements IGenericItem<Model, Item, VH> {
    private Model mModel;

    public GenericAbstractItem(Model model) {
        this.mModel = model;
    }

    public Model getModel() {
        return mModel;
    }

    @Deprecated
    public GenericAbstractItem<?, ?, ?> setModel(Model model) {
        return withModel(model);
    }

    public GenericAbstractItem<?, ?, ?> withModel(Model model) {
        this.mModel = model;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Class<? extends VH> viewHolderType() {
        return ((Class<? extends VH>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2]);
    }
}