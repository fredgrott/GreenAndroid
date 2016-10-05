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
package com.github.shareme.greenandroid.materialize.holder;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mikepenz on 13.07.15.
 */
@SuppressWarnings("unused")
public class StringHolder {
    private String mText;
    private int mTextRes = -1;

    public StringHolder(String text) {
        this.mText = text;
    }

    public StringHolder(@StringRes int textRes) {
        this.mTextRes = textRes;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public int getTextRes() {
        return mTextRes;
    }

    public void setTextRes(int mTextRes) {
        this.mTextRes = mTextRes;
    }

    public void applyTo(TextView textView) {
        if (mText != null) {
            textView.setText(mText);
        } else if (mTextRes != -1) {
            textView.setText(mTextRes);
        } else {
            textView.setText("");
        }
    }

    public boolean applyToOrHide(TextView textView) {
        if (mText != null) {
            textView.setText(mText);
            textView.setVisibility(View.VISIBLE);
            return true;
        } else if (mTextRes != -1) {
            textView.setText(mTextRes);
            textView.setVisibility(View.VISIBLE);
            return true;
        } else {
            textView.setVisibility(View.GONE);
            return false;
        }
    }

    public String getText(Context ctx) {
        if (mText != null) {
            return mText;
        } else if (mTextRes != -1) {
            return ctx.getString(mTextRes);
        }
        return null;
    }


    public static void applyTo(StringHolder text, TextView textView) {
        if (text != null && textView != null) {
            text.applyTo(textView);
        }
    }

    public static boolean applyToOrHide(StringHolder text, TextView textView) {
        if (text != null && textView != null) {
            return text.applyToOrHide(textView);
        } else if (textView != null) {
            textView.setVisibility(View.GONE);
            return false;
        }
        return false;
    }

    @Override
    public String toString() {
        return mText;
    }
}
