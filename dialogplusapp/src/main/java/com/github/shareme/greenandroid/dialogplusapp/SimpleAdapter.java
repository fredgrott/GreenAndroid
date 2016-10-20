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
package com.github.shareme.greenandroid.dialogplusapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class SimpleAdapter extends BaseAdapter {

  private LayoutInflater layoutInflater;
  private boolean isGrid;

  public SimpleAdapter(Context context, boolean isGrid) {
    layoutInflater = LayoutInflater.from(context);
    this.isGrid = isGrid;
  }

  @Override
  public int getCount() {
    return 6;
  }

  @Override
  public Object getItem(int position) {
    return position;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    View view = convertView;

    if (view == null) {
      if (isGrid) {
        view = layoutInflater.inflate(R.layout.simple_grid_item, parent, false);
      } else {
        view = layoutInflater.inflate(R.layout.simple_list_item, parent, false);
      }

      viewHolder = new ViewHolder();
      viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
      viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
      view.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) view.getTag();
    }

    Context context = parent.getContext();
    switch (position) {
      case 0:
        viewHolder.textView.setText(context.getString(R.string.google_plus_title));
        viewHolder.imageView.setImageResource(R.mipmap.ic_google_plus_icon);
        break;
      case 1:
        viewHolder.textView.setText(context.getString(R.string.google_maps_title));
        viewHolder.imageView.setImageResource(R.mipmap.ic_google_maps_icon);
        break;
      default:
        viewHolder.textView.setText(context.getString(R.string.google_messenger_title));
        viewHolder.imageView.setImageResource(R.mipmap.ic_google_messenger_icon);
        break;
    }

    return view;
  }

  static class ViewHolder {
    TextView textView;
    ImageView imageView;
  }
}
