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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class CopyInputStream {
    private static final String TAG = SvgToPath.TAG;

    private final InputStream _is;
    private ByteArrayOutputStream _copy;

    public CopyInputStream(InputStream is) {
        _is = is;

        try {
            copy();
        }
        catch(IOException ex) {
            Log.w(TAG, "IOException in CopyInputStream " + ex.toString());
        }
    }

    private void copy() throws IOException {
        _copy = new ByteArrayOutputStream();
        int chunk;
        byte[] data = new byte[256];

        while(-1 != (chunk = _is.read(data))) {
            _copy.write(data, 0, chunk);
        }
        _copy.flush();
    }

    public ByteArrayInputStream getCopy() {
        return new ByteArrayInputStream(_copy.toByteArray());
    }
}

