/*
 Copyright 2015 Rey Pham.
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
package com.github.shareme.greenandroid.materialextras.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Rey on 12/23/2014.
 */
public class TypefaceUtil {

    private static final HashMap<String, Typeface> sCachedFonts = new HashMap<String, Typeface>();
    private static final String PREFIX_ASSET = "asset:";

    private TypefaceUtil() {
    }

    /**
     * @param familyName if start with 'asset:' prefix, then load font from asset folder.
     * @return
     */
    public static Typeface load(Context context, String familyName, int style) {
        if(familyName != null && familyName.startsWith(PREFIX_ASSET))
            synchronized (sCachedFonts) {
                try {
                    if (!sCachedFonts.containsKey(familyName)) {
                        final Typeface typeface = Typeface.createFromAsset(context.getAssets(), familyName.substring(PREFIX_ASSET.length()));
                        sCachedFonts.put(familyName, typeface);
                        return typeface;
                    }
                } catch (Exception e) {
                    return Typeface.DEFAULT;
                }

                return sCachedFonts.get(familyName);
            }

        return Typeface.create(familyName, style);
    }
}
