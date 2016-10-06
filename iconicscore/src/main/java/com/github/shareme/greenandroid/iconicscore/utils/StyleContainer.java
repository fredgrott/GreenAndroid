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
package com.github.shareme.greenandroid.iconicscore.utils;

import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.style.CharacterStyle;

import com.github.shareme.greenandroid.iconicscore.typeface.ITypeface;

@SuppressWarnings("unused")
public class StyleContainer {
    public int startIndex;
    public int endIndex;
    public String icon;
    public ITypeface font;
    public ParcelableSpan span;
    public CharacterStyle style;
    public int flags = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;

    public StyleContainer(int startIndex, int endIndex, String icon, ITypeface font, int flags) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.icon = icon;
        this.font = font;
        this.flags = flags;
    }

    public StyleContainer(int startIndex, int endIndex, String icon, ITypeface font) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.icon = icon;
        this.font = font;
    }

    public StyleContainer(int startIndex, int endIndex, ParcelableSpan span) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.span = span;
    }

    public StyleContainer(int startIndex, int endIndex, ParcelableSpan span, int flags) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.span = span;
        this.flags = flags;
    }

    public StyleContainer(int startIndex, int endIndex, CharacterStyle style) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.style = style;
    }

    public StyleContainer(int startIndex, int endIndex, CharacterStyle style, int flags) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.style = style;
        this.flags = flags;
    }
}
