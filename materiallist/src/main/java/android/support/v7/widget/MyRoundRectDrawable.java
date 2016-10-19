package android.support.v7.widget;

import android.content.res.ColorStateList;

public class MyRoundRectDrawable extends RoundRectDrawable {

    public MyRoundRectDrawable(int backgroundColor, float radius) {
        super(ColorStateList.valueOf(backgroundColor), radius);
    }

}