package net.xpece.android.support.preference;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceGroupAdapter;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.TintTypedArray;
import android.view.View;

import com.github.shareme.greenandroid.androidsupportpreference.R;

/**
 * Use this class to add dividers between {@link Preference} items.
 */
public class PreferenceDividerDecoration extends RecyclerView.ItemDecoration {

    private boolean mDrawTop = false;
    private boolean mDrawBottom = false;
    private boolean mDrawBetweenItems = true;
    private boolean mDrawBetweenCategories = true;

    private Drawable mDivider;
    private int mDividerHeight;

    public PreferenceDividerDecoration(Drawable divider, int dividerHeight) {
        mDivider = divider;
        mDividerHeight = dividerHeight;
    }

    public PreferenceDividerDecoration(Context context, @DrawableRes int divider, @DimenRes int dividerHeight) {
        try {
            mDivider = AppCompatResources.getDrawable(context, divider);
        }catch (Exception ex) {
            try {
                mDivider = AppCompatDrawableManager.get().getDrawable(context, divider);
            } catch (Exception ex2) {
                mDivider = ContextCompat.getDrawable(context, divider);
            }
        }
        mDividerHeight = context.getResources().getDimensionPixelSize(dividerHeight);
    }

    public PreferenceDividerDecoration(Context context) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, null, new int[]{R.attr.dividerHorizontal});
        mDivider = a.getDrawable(0);
        a.recycle();

        if (mDivider != null) {
            mDividerHeight = mDivider.getIntrinsicHeight();
        }
    }

    public boolean getDrawTop() {
        return mDrawTop;
    }

    /**
     * Controls whether to draw divider above the first item.
     *
     * @param drawTop
     * @return
     */
    public PreferenceDividerDecoration drawTop(boolean drawTop) {
        mDrawTop = drawTop;
        return this;
    }

    public boolean getDrawBottom() {
        return mDrawBottom;
    }

    /**
     * Controls whether to draw divider at the bottom of the last item.
     *
     * @param drawBottom
     * @return
     */
    public PreferenceDividerDecoration drawBottom(boolean drawBottom) {
        mDrawBottom = drawBottom;
        return this;
    }

    public boolean getDrawBetweenItems() {
        return mDrawBetweenItems;
    }

    /**
     * Controls whether to draw divider at the bottom of each {@link Preference} and {@link PreferenceScreen} item.
     *
     * @param drawBetweenItems
     * @return
     */
    public PreferenceDividerDecoration drawBetweenItems(boolean drawBetweenItems) {
        mDrawBetweenItems = drawBetweenItems;
        return this;
    }

    public boolean getDrawBetweenCategories() {
        return mDrawBetweenCategories;
    }

    /**
     * Controls whether to draw divider above each {@link PreferenceGroup} usually {@link PreferenceCategory}.
     *
     * @param drawBetweenCategories
     * @return
     */
    public PreferenceDividerDecoration drawBetweenCategories(boolean drawBetweenCategories) {
        mDrawBetweenCategories = drawBetweenCategories;
        return this;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) return;

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        final PreferenceGroupAdapter adapter = (PreferenceGroupAdapter) parent.getAdapter();
        final int adapterCount = adapter.getItemCount();

        boolean wasLastPreferenceGroup = false;
        for (int i = 0, childCount = parent.getChildCount(); i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final int adapterPosition = parent.getChildAdapterPosition(child);
            Preference preference = adapter.getItem(adapterPosition);

            boolean skipNextAboveDivider = false;
            if (adapterPosition == 0) {
                if (mDrawTop) {
                    drawAbove(c, left, right, child);
                }
                skipNextAboveDivider = true;
            }

            if (preference instanceof PreferenceGroup
                && !(preference instanceof PreferenceScreen)) {
                if (mDrawBetweenCategories) {
                    if (!skipNextAboveDivider) {
                        drawAbove(c, left, right, child);
                        skipNextAboveDivider = true;
                    }
                }
                wasLastPreferenceGroup = true;
            } else {
                if (mDrawBetweenItems && !wasLastPreferenceGroup) {
                    if (!skipNextAboveDivider) {
                        drawAbove(c, left, right, child);
                        skipNextAboveDivider = true;
                    }
                }
                wasLastPreferenceGroup = false;
            }

            if (adapterPosition == adapterCount - 1) {
                if (mDrawBottom) {
                    drawBottom(c, left, right, child);
                }
            }
        }
    }

    private void drawAbove(Canvas c, int left, int right, View child) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        final int top = child.getTop() - params.topMargin - mDividerHeight;
        final int bottom = top + mDividerHeight;
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    private void drawBottom(Canvas c, int left, int right, View child) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        final int top = child.getBottom() + params.bottomMargin - mDividerHeight;
        final int bottom = top + mDividerHeight;
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }
}
