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

import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by mikepenz on 03.08.15.
 */
@SuppressWarnings("unused")
public class GenericsUtil {

    /**
     * a helper to get the string fields from the R class
     *
     * @param ctx
     * @return
     */
    public static String[] getFields(Context ctx) {
        Class rStringClass = resolveRClass(ctx.getPackageName());
        if (rStringClass != null) {
            return getDefinedFonts(ctx, rStringClass.getFields());
        }
        return new String[0];
    }

    /**
     * a helper class to resolve the correct R Class for the package
     *
     * @param packageName
     * @return
     */
    private static Class resolveRClass(String packageName) {
        do {
            try {
                return Class.forName(packageName + ".R$string");
            } catch (ClassNotFoundException e) {
                packageName = packageName.contains(".") ? packageName.substring(0, packageName.lastIndexOf('.')) : "";
            }
        } while (!TextUtils.isEmpty(packageName));

        return null;
    }

    /**
     * A helper method to get a String[] out of a fieldArray
     *
     * @param fields R.strings.class.getFields()
     * @return a String[] with the string ids we need
     */
    private static String[] getDefinedFonts(Context ctx, Field[] fields) {
        ArrayList<String> fieldArray = new ArrayList<String>();
        for (Field field : fields) {
            if (field.getName().contains("define_font_")) {
                fieldArray.add(getStringResourceByName(ctx, field.getName()));
            }
        }
        return fieldArray.toArray(new String[fieldArray.size()]);
    }

    /**
     * helper class to retrieve a string by it's resource name
     *
     * @param ctx
     * @param resourceName
     * @return
     */
    private static String getStringResourceByName(Context ctx, String resourceName) {
        String packageName = ctx.getPackageName();
        int resId = ctx.getResources().getIdentifier(resourceName, "string", packageName);
        if (resId == 0) {
            return "";
        } else {
            return ctx.getString(resId);
        }
    }
}
