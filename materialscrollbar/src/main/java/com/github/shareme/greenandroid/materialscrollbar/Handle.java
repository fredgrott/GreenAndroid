/*
 *  Copyright © 2016, Turing Technologies, an unincorporated organisation of Wynne Plaga
 *  Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.shareme.greenandroid.materialscrollbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.view.View;

public class Handle extends View {

    RectF rectF;
    Paint p = new Paint();
    Integer mode;
    boolean expanded = false;
    Context context;
    Boolean programmatic;

    public Handle(Context c, int m, boolean pro){
        super(c);

        context = c;
        mode = m;
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        programmatic = pro;
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);

        p.setColor(color);
    }

    public void collapseHandle(){
        expanded = true;
        rectF = new RectF(new Rect(getLeft(),getTop(),getLeft(),getBottom()));
        invalidate();
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
    }

    public void expandHandle(){
        expanded = false;
        rectF = makeRect();
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(mode == 0){
            rectF = makeRect();
        }
    }

    private RectF makeRect(){
        if(programmatic){
            return new RectF(new Rect(getLeft() - Utils.getDP(11, context),getTop(),getLeft()-Utils.getDP(4, context),getBottom()));
        } else {
            return new RectF(new Rect(getLeft() - Utils.getDP(4, context),getTop(),getLeft()+Utils.getDP(6, context),getBottom()));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mode == 0 && !expanded){
            Rect newRect = canvas.getClipBounds();
            newRect.inset(getLeft() - Utils.getDP(30, context), 0); //make the rect larger

            canvas.clipRect(newRect, Region.Op.REPLACE);

            canvas.drawArc(rectF, 90F, 180F, false, p); //335
        }
    }
}
