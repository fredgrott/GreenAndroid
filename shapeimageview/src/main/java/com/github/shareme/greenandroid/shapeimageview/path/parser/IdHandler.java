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

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

@SuppressWarnings("StatementWithEmptyBody")
class IdHandler {
    private static final String TAG = SvgToPath.TAG;

    final HashMap<String, String> idXml = new HashMap<String, String>();
    private final Stack<IdRecording> idRecordingStack = new Stack<IdRecording>();

    private final XmlPullParser atts;

    IdHandler(XmlPullParser atts) {
        this.atts = atts;
    }

    class IdRecording {
        final String id;
        int level;
        final StringBuilder sb;

        public IdRecording (String id) {
            this.id = id;
            this.level = 0;
            this.sb = new StringBuilder();
        }
    }

    public void processIds() throws XmlPullParserException, IOException {
        int eventType = atts.getEventType();
        do {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                // no op
            } else if(eventType == XmlPullParser.END_DOCUMENT) {
                // no op
            } else if(eventType == XmlPullParser.START_TAG) {
                startElement();
            } else if(eventType == XmlPullParser.END_TAG) {
                endElement();
            } else if(eventType == XmlPullParser.TEXT) {
                // not implemented
            }
            eventType = atts.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);
    }


    private void appendElementString(StringBuilder sb, String localName, XmlPullParser atts) {
        sb.append("<");
        sb.append(localName);
        for (int i = 0; i < atts.getAttributeCount(); i++) {
            sb.append(" ");
            sb.append(atts.getAttributeName(i));
            sb.append("='");
            sb.append(ParseUtil.escape(atts.getAttributeValue(i)));
            sb.append("'");
        }
        sb.append(">");
    }

    void startElement() {
        String localName = atts.getName();
        String id = ParseUtil.getStringAttr("id", atts);
        if (id != null) {
            IdRecording ir = new IdRecording(id);
            idRecordingStack.push(ir);
        }
        if (idRecordingStack.size() > 0){
            IdRecording ir = idRecordingStack.lastElement();
            ir.level++;
            //appendElementString(ir.sb, atts.getNamespace(), localName, atts.getName(), atts);
            appendElementString(ir.sb, localName, atts);
        }
    }

    void endElement() {
        String localName = atts.getName();
        if (idRecordingStack.size() > 0){
            IdRecording ir = idRecordingStack.lastElement();
            ir.sb.append("</");
            ir.sb.append(localName);
            ir.sb.append(">");
            ir.level--;
            if (ir.level == 0) {
                String xml = ir.sb.toString();
                //Log.d(TAG, "Added element with id " + ir.id + " and content: " + xml);
                idXml.put(ir.id, xml);
                idRecordingStack.pop();
                if (idRecordingStack.size() > 0){
                    idRecordingStack.lastElement().sb.append(xml);
                }
                Log.w(TAG, xml);
            }
        }
    }
}