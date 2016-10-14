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

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;

public class PathInfo {
    private final float width;
    private final float height;
    private final Path path;

    PathInfo(Path path, float width, float height) {
        this.path = path;

        float tmpWidth = width;
        float tmpHeight = height;
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        if(width <= 0 && height <= 0) {
            tmpWidth = (float) Math.ceil(bounds.width());
            tmpHeight = (float) Math.ceil(bounds.height());
            path.offset(-1 * (float) Math.floor(bounds.left),
                    -1 * (float) Math.round(bounds.top));
        }

        this.width = tmpWidth;
        this.height = tmpHeight;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void transform(Matrix matrix, Path dst) {
        path.transform(matrix, dst);
    }
}
