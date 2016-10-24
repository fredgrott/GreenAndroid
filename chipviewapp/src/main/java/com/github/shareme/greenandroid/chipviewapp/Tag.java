package com.github.shareme.greenandroid.chipviewapp;

import com.github.shareme.greenandroid.chipview.Chip;


/**
 * Created by Plumillon Forge on 25/09/15.
 */
public class Tag implements Chip {
    private String mName;
    private int mType = 0;

    public Tag(String name, int type) {
        this(name);
        mType = type;
    }

    public Tag(String name) {
        mName = name;
    }

    @Override
    public String getText() {
        return mName;
    }

    public int getType() {
        return mType;
    }
}
