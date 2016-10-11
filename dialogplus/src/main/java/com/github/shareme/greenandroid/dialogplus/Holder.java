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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface Holder {

  void addHeader(View view);

  void addFooter(View view);

  void setBackgroundResource(int colorResource);

  View getView(LayoutInflater inflater, ViewGroup parent);

  void setOnKeyListener(View.OnKeyListener keyListener);

  View getInflatedView();

  View getHeader();

  View getFooter();

}
