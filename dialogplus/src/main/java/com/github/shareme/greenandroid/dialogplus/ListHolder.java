/*
 Copyright 2016 Orhan Obut
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
package com.github.shareme.greenandroid.dialogplus;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ListHolder implements HolderAdapter, AdapterView.OnItemClickListener {

  private int backgroundResource;

  private ListView listView;
  private OnHolderListener listener;
  private View.OnKeyListener keyListener;
  private View headerView;
  private View footerView;

  @Override public void addHeader(View view) {
    if (view == null) {
      return;
    }
    listView.addHeaderView(view);
    headerView = view;
  }

  @Override public void addFooter(View view) {
    if (view == null) {
      return;
    }
    listView.addFooterView(view);
    footerView = view;
  }

  @Override public void setAdapter(BaseAdapter adapter) {
    listView.setAdapter(adapter);
  }

  @Override public void setBackgroundResource(int colorResource) {
    this.backgroundResource = colorResource;
  }

  @Override public View getView(LayoutInflater inflater, ViewGroup parent) {
    View view = inflater.inflate(R.layout.dialog_list, parent, false);
    View outMostView = view.findViewById(R.id.dialogplus_outmost_container);
    outMostView.setBackgroundResource(backgroundResource);
    listView = (ListView) view.findViewById(R.id.dialogplus_list);
    listView.setOnItemClickListener(this);
    listView.setOnKeyListener(new View.OnKeyListener() {
      @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyListener == null) {
          throw new NullPointerException("keyListener should not be null");
        }
        return keyListener.onKey(v, keyCode, event);
      }
    });
    return view;
  }

  @Override public void setOnItemClickListener(OnHolderListener listener) {
    this.listener = listener;
  }

  @Override public void setOnKeyListener(View.OnKeyListener keyListener) {
    this.keyListener = keyListener;
  }

  @Override public View getInflatedView() {
    return listView;
  }

  @Override public View getHeader() {
    return headerView;
  }

  @Override public View getFooter() {
    return footerView;
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (listener == null) {
      return;
    }
    //ListView counts header as position as well. For consistency we don't
    listener.onItemClick(
        parent.getItemAtPosition(position),
        view,
        headerView != null ? --position : position
    );
  }
}
