/*
 Copyright 2015 siyamed
 Modifications Copyright(C0 2016 Fred Grott(GrottWorkShop)

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
package com.github.shareme.greenandroid.shapeimageview.path.parser;

import org.xmlpull.v1.XmlPullParser;

@SuppressWarnings("FinalStaticMethod")
class ParseUtil {

    static final String escape (String s) {
        return s
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;");
    }

    static final String getStringAttr(String name, XmlPullParser attributes) {
        int n = attributes.getAttributeCount();
        for (int i = 0; i < n; i++) {
            if (attributes.getAttributeName(i).equals(name)) {
                return attributes.getAttributeValue(i);
            }
        }
        return null;
    }

    /*
     * Some SVG unit conversions.  This is approximate
     */
    static final Float convertUnits(String name, XmlPullParser atts, float dpi, float width, float height) {
        String value = getStringAttr(name, atts);
        if (value == null) {
            return null;
        } else if (value.endsWith("px")) {
            return Float.parseFloat(value.substring(0, value.length() - 2));
        } else if (value.endsWith("pt")) {
            return Float.valueOf(value.substring(0, value.length() - 2)) * dpi / 72;
        } else if (value.endsWith("pc")) {
            return Float.valueOf(value.substring(0, value.length() - 2)) * dpi / 6;
        } else if (value.endsWith("cm")) {
            return Float.valueOf(value.substring(0, value.length() - 2)) * dpi / 2.54f;
        } else if (value.endsWith("mm")) {
            return Float.valueOf(value.substring(0, value.length() - 2)) * dpi / 254;
        } else if (value.endsWith("in")) {
            return Float.valueOf(value.substring(0, value.length() - 2)) * dpi;
        } else if (value.endsWith("%")) {
            Float result = Float.valueOf(value.substring(0, value.length() - 1));
            float mult;
            if (name.contains("x") || name.equals("width") ) {
                mult = width / 100f;
            } else if (name.contains("y") || name.equals("height")) {
                mult = height / 100f;
            } else {
                mult = (height + width) / 2f;
            }
            return result * mult;
        } else {
            return Float.valueOf(value);
        }
    }
}
