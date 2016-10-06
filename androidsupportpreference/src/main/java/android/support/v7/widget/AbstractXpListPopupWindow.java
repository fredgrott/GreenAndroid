/*
 * Copyright (C) 2014 The Android Open Source Project
 * Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.github.shareme.greenandroid.androidsupportpreference.R;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Static library support version of the framework's {@link android.widget.ListPopupWindow}.
 * Used to write apps that run on platforms prior to Android L. When running
 * on Android L or above, this implementation is still used; it does not try
 * to switch to the framework's implementation. See the framework SDK
 * documentation for a class overview.
 *
 * @see android.widget.ListPopupWindow
 */
public abstract class AbstractXpListPopupWindow {
    private static final String TAG = AbstractXpListPopupWindow.class.getSimpleName();
    private static final boolean DEBUG = false;

    private static final boolean API_18 = Build.VERSION.SDK_INT >= 18;

    /**
     * This value controls the length of time that the user
     * must leave a pointer down without scrolling to expand
     * the autocomplete dropdown list to cover the IME.
     */
    private static final int EXPAND_LIST_TIMEOUT = 250;

    private static Method sClipToWindowEnabledMethod;
    private static Method sGetMaxAvailableHeightMethod;
    private static Method sSetAllowScrollingAnchorParentMethod;

    static {
        try {
            sClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod(
                "setClipToScreenEnabled", boolean.class);
            sClipToWindowEnabledMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
        }
        try {
            sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod(
                "getMaxAvailableHeight", View.class, int.class, boolean.class);
            sGetMaxAvailableHeightMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Could not find method getMaxAvailableHeight(View, int, boolean)"
                + " on PopupWindow. Oh well.");
        }
        try {
            sSetAllowScrollingAnchorParentMethod = PopupWindow.class.getDeclaredMethod(
                "setAllowScrollingAnchorParent", boolean.class);
            sSetAllowScrollingAnchorParentMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Could not find method setAllowScrollingAnchorParent(boolean)"
                + " on PopupWindow. Oh well.");
        }
    }

    private Context mContext;
    PopupWindow mPopup;
    private ListAdapter mAdapter;
    XpDropDownListView mDropDownList;

    private int mDropDownMaxWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private float mDropDownPreferredWidthUnit = 0;

    private int mDropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mDropDownWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mDropDownHorizontalOffset;
    private int mDropDownVerticalOffset;
    private int mDropDownWindowLayoutType = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;
    private boolean mDropDownVerticalOffsetSet;

    private int mDropDownGravity = Gravity.NO_GRAVITY;

    private boolean mDropDownAlwaysVisible = false;
    private boolean mForceIgnoreOutsideTouch = false;
    int mListItemExpandMaximum = Integer.MAX_VALUE;

    private View mPromptView;
    private int mPromptPosition = POSITION_PROMPT_ABOVE;

    private DataSetObserver mObserver;

    private View mDropDownAnchorView;
    private View mDropDownBoundsView;
    private final Rect mMargins = new Rect();

    private Drawable mDropDownListHighlight;

    private AdapterView.OnItemClickListener mItemClickListener;
    private AdapterView.OnItemSelectedListener mItemSelectedListener;

    final ResizePopupRunnable mResizePopupRunnable = new ResizePopupRunnable();
    private final PopupTouchInterceptor mTouchInterceptor = new PopupTouchInterceptor();
    private final PopupScrollListener mScrollListener = new PopupScrollListener();
    private final ListSelectorHider mHideSelector = new ListSelectorHider();
    private Runnable mShowDropDownRunnable;

    final Handler mHandler;

    private final Rect mTempRect = new Rect();
    private final int[] mTempLocation = new int[2];

    private boolean mModal;

    private int mLayoutDirection;

    /**
     * The provided prompt view should appear above list content.
     *
     * @see #setPromptPosition(int)
     * @see #getPromptPosition()
     * @see #setPromptView(View)
     */
    public static final int POSITION_PROMPT_ABOVE = 0;

    /**
     * The provided prompt view should appear below list content.
     *
     * @see #setPromptPosition(int)
     * @see #getPromptPosition()
     * @see #setPromptView(View)
     */
    public static final int POSITION_PROMPT_BELOW = 1;

    /**
     * Alias for {@link ViewGroup.LayoutParams#MATCH_PARENT}.
     * If used to specify a popup width, the popup will match the width of the anchor view.
     * If used to specify a popup height, the popup will fill available space.
     */
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;

    /**
     * Alias for {@link ViewGroup.LayoutParams#WRAP_CONTENT}.
     * If used to specify a popup width, the popup will use the width of its content.
     */
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    public static final int PREFERRED = -3;

    /**
     * Mode for {@link #setInputMethodMode(int)}: the requirements for the
     * input method should be based on the focusability of the popup.  That is
     * if it is focusable than it needs to work with the input method, else
     * it doesn't.
     */
    public static final int INPUT_METHOD_FROM_FOCUSABLE = PopupWindow.INPUT_METHOD_FROM_FOCUSABLE;

    /**
     * Mode for {@link #setInputMethodMode(int)}: this popup always needs to
     * work with an input method, regardless of whether it is focusable.  This
     * means that it will always be displayed so that the user can also operate
     * the input method while it is shown.
     */
    public static final int INPUT_METHOD_NEEDED = PopupWindow.INPUT_METHOD_NEEDED;

    /**
     * Mode for {@link #setInputMethodMode(int)}: this popup never needs to
     * work with an input method, regardless of whether it is focusable.  This
     * means that it will always be displayed to use as much space on the
     * screen as needed, regardless of whether this covers the input method.
     */
    public static final int INPUT_METHOD_NOT_NEEDED = PopupWindow.INPUT_METHOD_NOT_NEEDED;

    public void setMarginTop(int px) {
        mMargins.top = px;
    }

    public int getMarginTop() {
        return mMargins.top;
    }

    public void setMarginBottom(int px) {
        mMargins.bottom = px;
    }

    public int getMarginBottom() {
        return mMargins.bottom;
    }

    public void setMarginLeft(int px) {
        mMargins.left = px;
    }

    public void setMarginStart(int px) {
        if (mLayoutDirection == LayoutDirection.RTL) {
            mMargins.right = px;
        } else {
            mMargins.left = px;
        }
    }

    public int getMarginStart(int px) {
        if (mLayoutDirection == LayoutDirection.RTL) {
            return mMargins.right;
        } else {
            return mMargins.left;
        }
    }

    public int getMarginLeft() {
        return mMargins.left;
    }

    public void setMarginRight(int px) {
        mMargins.right = px;
    }

    public void setMarginEnd(int px) {
        if (mLayoutDirection == LayoutDirection.RTL) {
            mMargins.left = px;
        } else {
            mMargins.right = px;
        }
    }

    public int getMarginEnd(int px) {
        if (mLayoutDirection == LayoutDirection.RTL) {
            return mMargins.left;
        } else {
            return mMargins.right;
        }
    }

    public int getMarginRight() {
        return mMargins.right;
    }

    public void setMargin(int margin) {
        mMargins.set(margin, margin, margin, margin);
    }

    public void setMargin(int horizontal, int vertical) {
        mMargins.set(horizontal, vertical, horizontal, vertical);
    }

    public void setMargin(int left, int top, int right, int bottom) {
        mMargins.set(left, top, right, bottom);
    }

    public void setMarginRelative(int start, int top, int end, int bottom) {
        int left, right;
        if (mLayoutDirection == LayoutDirection.RTL) {
            right = start;
            left = end;
        } else {
            left = start;
            right = end;
        }
        mMargins.set(left, top, right, bottom);
    }

    /**
     * @return
     */
    public boolean hasMultiLineItems() {
        if (mDropDownList == null) {
            buildDropDown();
        }
        final XpDropDownListView list = mDropDownList;
        if (list != null) {
            return list.hasMultiLineItems();
        }
        return false;
    }

    int measureItemsUpTo(int position) {
        if (mDropDownList == null) {
            buildDropDown();
        }
        final XpDropDownListView list = mDropDownList;
        if (list != null) {
            int widthSpec = MeasureSpec.makeMeasureSpec(getListWidthSpec(), MeasureSpec.AT_MOST);
            return list.measureHeightOfChildrenCompat(widthSpec, 0, position, Integer.MAX_VALUE, 1);
        }
        return 0;
    }

    int measureItems(int fromIncl, int toExcl) {
        if (mDropDownList == null) {
            buildDropDown();
        }
        final XpDropDownListView list = mDropDownList;
        if (list != null) {
            int widthSpec = MeasureSpec.makeMeasureSpec(getListWidthSpec(), MeasureSpec.AT_MOST);
            return list.measureHeightOfChildrenCompat(widthSpec, fromIncl, toExcl, Integer.MAX_VALUE, 1);
        }
        return 0;
    }

    int measureItem(int position) {
        if (mDropDownList == null) {
            buildDropDown();
        }
        final XpDropDownListView list = mDropDownList;
        if (list != null) {
            int widthSpec = MeasureSpec.makeMeasureSpec(getListWidthSpec(), MeasureSpec.AT_MOST);
            return list.measureHeightOfChildrenCompat(widthSpec, position, position + 1, Integer.MAX_VALUE, 1);
        }
        return 0;
    }

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context Context used for contained views.
     */
    public AbstractXpListPopupWindow(Context context) {
        this(context, null, R.attr.listPopupWindowStyle);
    }

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context Context used for contained views.
     * @param attrs Attributes from inflating parent views used to style the popup.
     */
    public AbstractXpListPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.listPopupWindowStyle);
    }

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context Context used for contained views.
     * @param attrs Attributes from inflating parent views used to style the popup.
     * @param defStyleAttr Default style attribute to use for popup content.
     */
    public AbstractXpListPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context Context used for contained views.
     * @param attrs Attributes from inflating parent views used to style the popup.
     * @param defStyleAttr Style attribute to read for default styling of popup content.
     * @param defStyleRes Style resource ID to use for default styling of popup content.
     */
    public AbstractXpListPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;
        mHandler = new Handler(context.getMainLooper());

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListPopupWindow,
            defStyleAttr, defStyleRes);
        mDropDownHorizontalOffset = a.getDimensionPixelOffset(
            R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
        mDropDownVerticalOffset = a.getDimensionPixelOffset(
            R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
        if (mDropDownVerticalOffset != 0) {
            mDropDownVerticalOffsetSet = true;
        }
        a.recycle();

        int defaultMargin = Util.dpToPxOffset(context, 8);
        final TypedArray b = context.obtainStyledAttributes(attrs, R.styleable.XpListPopupWindow, defStyleAttr, defStyleRes);
        if (b.hasValue(R.styleable.XpListPopupWindow_android_layout_margin)) {
            int margin = b.getDimensionPixelOffset(R.styleable.XpListPopupWindow_android_layout_margin, defaultMargin);
            mMargins.bottom = margin;
            mMargins.top = margin;
            mMargins.left = margin;
            mMargins.right = margin;
        } else {
            if (API_18 && b.hasValue(R.styleable.XpListPopupWindow_android_layout_marginEnd)) {
                int margin = b.getDimensionPixelOffset(R.styleable.XpListPopupWindow_android_layout_marginEnd, 0);
                if (mLayoutDirection == LayoutDirection.RTL) {
                    mMargins.left = margin;
                } else {
                    mMargins.right = margin;
                }
            } else {
                mMargins.right = b.getDimensionPixelOffset(R.styleable.XpListPopupWindow_android_layout_marginRight, defaultMargin);
            }
            if (API_18 && b.hasValue(R.styleable.XpListPopupWindow_android_layout_marginStart)) {
                int margin = b.getDimensionPixelOffset(R.styleable.XpListPopupWindow_android_layout_marginStart, 0);
                if (mLayoutDirection == LayoutDirection.RTL) {
                    mMargins.right = margin;
                } else {
                    mMargins.left = margin;
                }
            } else {
                mMargins.left = b.getDimensionPixelOffset(R.styleable.XpListPopupWindow_android_layout_marginLeft, defaultMargin);
            }
            mMargins.top = b.getDimensionPixelOffset(R.styleable.XpListPopupWindow_android_layout_marginTop, defaultMargin);
            mMargins.bottom = b.getDimensionPixelOffset(R.styleable.XpListPopupWindow_android_layout_marginBottom, defaultMargin);
        }
        b.recycle();

        mPopup = new AppCompatPopupWindow(context, attrs, defStyleAttr);
        mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        // Set the default layout direction to match the default locale one
        final Locale locale = mContext.getResources().getConfiguration().locale;
        mLayoutDirection = TextUtilsCompat.getLayoutDirectionFromLocale(locale);

        setAllowScrollingAnchorParent(false);
    }

    /**
     * Sets the adapter that provides the data and the views to represent the data
     * in this popup window.
     *
     * @param adapter The adapter to use to create this window's content.
     */
    public void setAdapter(ListAdapter adapter) {
        if (mObserver == null) {
            mObserver = new PopupDataSetObserver();
        } else if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            adapter.registerDataSetObserver(mObserver);
        }

        if (mDropDownList != null) {
            mDropDownList.setAdapter(mAdapter);
        }
    }

    /**
     * Set where the optional prompt view should appear. The default is
     * {@link #POSITION_PROMPT_ABOVE}.
     *
     * @param position A position constant declaring where the prompt should be displayed.
     * @see #POSITION_PROMPT_ABOVE
     * @see #POSITION_PROMPT_BELOW
     */
    public void setPromptPosition(int position) {
        mPromptPosition = position;
    }

    /**
     * @return Where the optional prompt view should appear.
     * @see #POSITION_PROMPT_ABOVE
     * @see #POSITION_PROMPT_BELOW
     */
    public int getPromptPosition() {
        return mPromptPosition;
    }

    /**
     * Set whether this window should be modal when shown.
     * <p/>
     * <p>If a popup window is modal, it will receive all touch and key input.
     * If the user touches outside the popup window's content area the popup window
     * will be dismissed.
     *
     * @param modal {@code true} if the popup window should be modal, {@code false} otherwise.
     */
    public void setModal(boolean modal) {
        mModal = modal;
        mPopup.setFocusable(modal);
    }

    /**
     * Returns whether the popup window will be modal when shown.
     *
     * @return {@code true} if the popup window will be modal, {@code false} otherwise.
     */
    public boolean isModal() {
        return mModal;
    }

    /**
     * Forces outside touches to be ignored. Normally if {@link #isDropDownAlwaysVisible()} is
     * false, we allow outside touch to dismiss the dropdown. If this is set to true, then we
     * ignore outside touch even when the drop down is not set to always visible.
     *
     * @hide Used only by AutoCompleteTextView to handle some internal special cases.
     */
    public void setForceIgnoreOutsideTouch(boolean forceIgnoreOutsideTouch) {
        mForceIgnoreOutsideTouch = forceIgnoreOutsideTouch;
    }

    /**
     * Sets whether the drop-down should remain visible under certain conditions.
     * <p/>
     * The drop-down will occupy the entire screen below {@link #getAnchorView} regardless
     * of the size or content of the list.  {@link #getBackground()} will fill any space
     * that is not used by the list.
     *
     * @param dropDownAlwaysVisible Whether to keep the drop-down visible.
     * @hide Only used by AutoCompleteTextView under special conditions.
     */
    public void setDropDownAlwaysVisible(boolean dropDownAlwaysVisible) {
        mDropDownAlwaysVisible = dropDownAlwaysVisible;
    }

    /**
     * @return Whether the drop-down is visible under special conditions.
     * @hide Only used by AutoCompleteTextView under special conditions.
     */
    public boolean isDropDownAlwaysVisible() {
        return mDropDownAlwaysVisible;
    }

    /**
     * Sets the operating mode for the soft input area.
     *
     * @param mode The desired mode, see
     * {@link WindowManager.LayoutParams#softInputMode}
     * for the full list
     * @see WindowManager.LayoutParams#softInputMode
     * @see #getSoftInputMode()
     */
    public void setSoftInputMode(int mode) {
        mPopup.setSoftInputMode(mode);
    }

    /**
     * Returns the current value in {@link #setSoftInputMode(int)}.
     *
     * @see #setSoftInputMode(int)
     * @see WindowManager.LayoutParams#softInputMode
     */
    public int getSoftInputMode() {
        return mPopup.getSoftInputMode();
    }

    /**
     * Sets a drawable to use as the list item selector.
     *
     * @param selector List selector drawable to use in the popup.
     */
    public void setListSelector(Drawable selector) {
        mDropDownListHighlight = selector;
    }

    /**
     * @return The background drawable for the popup window.
     */
    public Drawable getBackground() {
        return mPopup.getBackground();
    }

    /**
     * Sets a drawable to be the background for the popup window.
     *
     * @param d A drawable to set as the background.
     */
    public void setBackgroundDrawable(Drawable d) {
        mPopup.setBackgroundDrawable(d);
    }

    /**
     * Set an animation style to use when the popup window is shown or dismissed.
     *
     * @param animationStyle Animation style to use.
     */
    public void setAnimationStyle(int animationStyle) {
        mPopup.setAnimationStyle(animationStyle);
    }

    /**
     * Returns the animation style that will be used when the popup window is shown or dismissed.
     *
     * @return Animation style that will be used.
     */
    public int getAnimationStyle() {
        return mPopup.getAnimationStyle();
    }

    /**
     * Returns the view that will be used to anchor this popup.
     *
     * @return The popup's anchor view
     */
    public View getAnchorView() {
        return mDropDownAnchorView;
    }

    /**
     * Sets the popup's anchor view. This popup will always be positioned relative to the anchor
     * view when shown.
     *
     * @param anchor The view to use as an anchor.
     */
    public void setAnchorView(View anchor) {
        mDropDownAnchorView = anchor;
    }

    public View getBoundsView() {
        return mDropDownBoundsView;
    }

    public void setBoundsView(View bounds) {
        mDropDownBoundsView = bounds;
    }

    /**
     * @return The horizontal offset of the popup from its anchor in pixels.
     */
    public int getHorizontalOffset() {
        return mDropDownHorizontalOffset;
    }

    /**
     * Set the horizontal offset of this popup from its anchor view in pixels.
     *
     * @param offset The horizontal offset of the popup from its anchor.
     */
    public void setHorizontalOffset(int offset) {
        mDropDownHorizontalOffset = offset;
    }

    /**
     * @return The vertical offset of the popup from its anchor in pixels.
     */
    public int getVerticalOffset() {
        if (!mDropDownVerticalOffsetSet) {
            return 0;
        }
        return mDropDownVerticalOffset;
    }

    /**
     * Set the vertical offset of this popup from its anchor view in pixels.
     *
     * @param offset The vertical offset of the popup from its anchor.
     */
    public void setVerticalOffset(int offset) {
        mDropDownVerticalOffset = offset;
        mDropDownVerticalOffsetSet = true;
    }

    /**
     * Set the gravity of the dropdown list. This is commonly used to
     * set gravity to START or END for alignment with the anchor.
     *
     * @param gravity Gravity value to use
     */
    public void setDropDownGravity(int gravity) {
        mDropDownGravity = gravity;
    }

    public int getDropDownGravity() {
        if (mDropDownGravity == Gravity.NO_GRAVITY) {
            return Gravity.TOP | GravityCompat.START;
        }
        return mDropDownGravity;
    }

    /**
     * @return The width of the popup window in pixels.
     */
    public int getWidth() {
        return mDropDownWidth;
    }

    public int getMaxWidth() {
        return mDropDownMaxWidth;
    }

    public float getPreferredWidthUnit() {
        return mDropDownPreferredWidthUnit;
    }

    /**
     * Sets the width of the popup window in pixels. Can also be {@link #MATCH_PARENT}
     * or {@link #WRAP_CONTENT}.
     *
     * @param width Width of the popup window.
     */
    public void setWidth(int width) {
        mDropDownWidth = width;
    }

    public void setMaxWidth(int maxWidth) {
        mDropDownMaxWidth = maxWidth;
    }

    public void setPreferredWidthUnit(float unit) {
        mDropDownPreferredWidthUnit = unit;
    }

    /**
     * Sets the width of the popup window by the size of its content. The final width may be
     * larger to accommodate styled window dressing.
     *
     * @param width Desired width of content in pixels.
     */
    public void setContentWidth(int width) {
        Drawable popupBackground = mPopup.getBackground();
        if (popupBackground != null) {
            popupBackground.getPadding(mTempRect);
            mDropDownWidth = mTempRect.left + mTempRect.right + width;
        } else {
            setWidth(width);
        }
    }

    /**
     * @return The height of the popup window in pixels.
     */
    public int getHeight() {
        return mDropDownHeight;
    }

    /**
     * Sets the height of the popup window in pixels. Can also be {@link #MATCH_PARENT}.
     *
     * @param height Height of the popup window.
     */
    public void setHeight(int height) {
        mDropDownHeight = height;
    }

    /**
     * Set the layout type for this popup window.
     * <p/>
     * See {@link WindowManager.LayoutParams#type} for possible values.
     *
     * @param layoutType Layout type for this window.
     * @see WindowManager.LayoutParams#type
     */
    public void setWindowLayoutType(int layoutType) {
        mDropDownWindowLayoutType = layoutType;
    }

    /**
     * Sets a listener to receive events when a list item is clicked.
     *
     * @param clickListener Listener to register
     * @see ListView#setOnItemClickListener(AdapterView.OnItemClickListener)
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener clickListener) {
        mItemClickListener = clickListener;
        final XpDropDownListView list = mDropDownList;
        if (list != null) {
            list.setOnItemClickListener(clickListener);
        }
    }

    /**
     * Sets a listener to receive events when a list item is selected.
     *
     * @param selectedListener Listener to register.
     * @see ListView#setOnItemSelectedListener(AdapterView.OnItemSelectedListener)
     */
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener selectedListener) {
        mItemSelectedListener = selectedListener;
        final XpDropDownListView list = mDropDownList;
        if (list != null) {
            list.setOnItemSelectedListener(selectedListener);
        }
    }

    /**
     * Set a view to act as a user prompt for this popup window. Where the prompt view will appear
     * is controlled by {@link #setPromptPosition(int)}.
     *
     * @param prompt View to use as an informational prompt.
     */
    public void setPromptView(View prompt) {
        boolean showing = isShowing();
        if (showing) {
            removePromptView();
        }
        mPromptView = prompt;
        if (showing) {
            show();
        }
    }

    /**
     * Post a {@link #show()} call to the UI thread.
     */
    public void postShow() {
        mHandler.post(mShowDropDownRunnable);
    }

    /**
     * Show the popup list. If the list is already showing, this method
     * will recalculate the popup's size and position.
     */
    public void show() {
        final int height = buildDropDown();
        final int widthSpec = getListWidthSpec();

        boolean noInputMethod = isInputMethodNotNeeded();
        PopupWindowCompat.setWindowLayoutType(mPopup, mDropDownWindowLayoutType);

        final int marginsLeft = mMargins.left;
        final int marginsTop = mMargins.top;
        final int marginsBottom = mMargins.bottom;
        final int marginsRight = mMargins.right;

        getBackgroundPadding(mTempRect);
        final int backgroundLeft = mTempRect.left;
        final int backgroundTop = mTempRect.top;
        final int backgroundBottom = mTempRect.bottom;
        final int backgroundRight = mTempRect.right;

        int verticalOffset = mDropDownVerticalOffset;
        int horizontalOffset = mDropDownHorizontalOffset;

        final int anchorWidth = mDropDownAnchorView.getWidth();
        final int anchorHeight = mDropDownAnchorView.getHeight();

        getLocationInWindow(mDropDownAnchorView, mTempLocation);
        final int anchorLeft = mTempLocation[0];
        final int anchorRight = anchorLeft + anchorWidth;
        final int anchorTop = mTempLocation[1];
        final int anchorBottom = anchorTop + anchorHeight;

        final boolean rightAligned = GravityCompat.getAbsoluteGravity(getDropDownGravity() & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK, mLayoutDirection) == Gravity.RIGHT;
        if (rightAligned) {
            horizontalOffset += anchorWidth - widthSpec - (marginsRight - backgroundRight);
        } else {
            horizontalOffset += (marginsLeft - backgroundLeft);
        }

        final int bottomDecorations = getWindowFrame(mDropDownAnchorView, noInputMethod, mTempRect);
        final int windowLeft = mTempRect.left;
        final int windowRight = mTempRect.right;
        final int windowTop = mTempRect.top;
        final int windowBottom = mTempRect.bottom;

        final int windowWidth = windowRight - windowLeft;
        final int windowHeight = windowBottom - windowTop;

        getBoundsInWindow(mTempRect);
        final int boundsTop = mTempRect.top;
        final int boundsRight = mTempRect.right;
        final int boundsLeft = mTempRect.left;
        final int boundsBottom = mTempRect.bottom;

        final int screenRight = windowRight - (marginsRight - backgroundRight) - boundsRight;
        final int screenLeft = windowLeft + (marginsLeft - backgroundLeft) + boundsLeft;
        final int screenWidth = screenRight - screenLeft;

        if (!rightAligned && windowWidth < anchorLeft + horizontalOffset + widthSpec) {
            // When right aligned due to insufficient space ignore negative horizontal offset.
            horizontalOffset = mDropDownHorizontalOffset < 0 ? 0 : mDropDownHorizontalOffset;
            horizontalOffset -= widthSpec - (windowWidth - anchorLeft);
            horizontalOffset -= marginsRight - backgroundRight;
        } else if (rightAligned && 0 > anchorLeft + horizontalOffset) {
            // When left aligned due to insufficient space ignore positive horizontal offset.
            horizontalOffset = mDropDownHorizontalOffset > 0 ? 0 : mDropDownHorizontalOffset;
            horizontalOffset -= anchorLeft;
            horizontalOffset += marginsLeft - backgroundLeft;
        }

        // Width spec should always be resolved to concrete value. widthSpec > 0;
        if (windowWidth < widthSpec + horizontalOffset + anchorLeft) {
            int diff = Math.abs(windowWidth - (widthSpec + horizontalOffset + anchorLeft));
            horizontalOffset -= diff;
        } else if (0 > anchorLeft + horizontalOffset) {
            int diff = Math.abs(horizontalOffset + anchorLeft);
            horizontalOffset += diff;
        }

        int maxHeight = getMaxAvailableHeight(mDropDownAnchorView, noInputMethod) + backgroundTop + backgroundBottom;
        int availableHeight = maxHeight;
//        availableHeight -= Math.max(0, marginsTop - backgroundTop);
//        availableHeight -= Math.max(0, marginsBottom - backgroundBottom);
        availableHeight -= marginsTop - backgroundTop;
        availableHeight -= marginsBottom - backgroundBottom;

        int limitHeight = Math.min(windowHeight, availableHeight);

        final int heightSpec;
        if (mPopup.isShowing()) {
            if (mDropDownHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                // The call to PopupWindow's update method below can accept -1 for any
                // value you do not want to update.
//                heightSpec = noInputMethod ? height : ViewGroup.LayoutParams.MATCH_PARENT;
//                if (noInputMethod) {
//                    mPopup.setWidth(mDropDownWidth == ViewGroup.LayoutParams.MATCH_PARENT ?
//                        ViewGroup.LayoutParams.MATCH_PARENT : 0);
//                    mPopup.setHeight(0);
//                } else {
//                    mPopup.setWidth(mDropDownWidth == ViewGroup.LayoutParams.MATCH_PARENT ?
//                        ViewGroup.LayoutParams.MATCH_PARENT : 0);
//                    mPopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//                }
                heightSpec = limitHeight;
            } else if (mDropDownHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                heightSpec = Math.min(height, limitHeight);
            } else {
                heightSpec = Math.min(mDropDownHeight, limitHeight);
            }
        } else {
            if (mDropDownHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
//                heightSpec = ViewGroup.LayoutParams.MATCH_PARENT;
                heightSpec = limitHeight;
            } else if (mDropDownHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                heightSpec = Math.min(height, limitHeight);
            } else {
                heightSpec = Math.min(mDropDownHeight, limitHeight);
            }
        }

        final int screenBottom = windowBottom - (marginsBottom - backgroundBottom) - boundsBottom;
        final int screenTop = windowTop + (marginsTop - backgroundTop) + boundsTop;

        {
            // Position within bounds.

            final int popupTop = anchorBottom + verticalOffset;
            final int popupBottom = popupTop + heightSpec;
            final int popupHeight = popupBottom - popupTop;

            if (popupBottom > screenBottom) {
                verticalOffset -= (popupBottom - screenBottom);
            } else if (popupTop < screenTop) {
                verticalOffset += (screenTop - popupTop);
            }
        }

        {
            // Account for background padding.

            final int popupTop = anchorBottom + verticalOffset;
            final int popupBottom = popupTop + heightSpec;
            final int popupHeight = popupBottom - popupTop;

            if (windowBottom < popupBottom) {
                int diff = Math.abs(windowBottom - popupBottom);
                verticalOffset -= diff;
            } else if (windowTop > popupTop) {
                int diff = Math.abs(windowTop - popupTop);
                verticalOffset += diff;
            }
        }

//        verticalOffset -= bottomDecorations;
//        verticalOffset += Util.dpToPxOffset(mContext, 8);

        if (mPopup.isShowing()) {
            mPopup.setOutsideTouchable(!mForceIgnoreOutsideTouch && !mDropDownAlwaysVisible);

            mPopup.update(getAnchorView(), horizontalOffset,
                verticalOffset, (widthSpec < 0) ? -1 : widthSpec,
                (heightSpec < 0) ? -1 : heightSpec);
        } else {

            mPopup.setWidth(widthSpec);
            mPopup.setHeight(heightSpec);
            setPopupClipToScreenEnabled(true);

            // use outside touchable to dismiss drop down when touching outside of it, so
            // only set this if the dropdown is not always visible
            mPopup.setOutsideTouchable(!mForceIgnoreOutsideTouch && !mDropDownAlwaysVisible);
            mPopup.setTouchInterceptor(mTouchInterceptor);
            // We handle gravity manually. Just as everything else.
            PopupWindowCompat.showAsDropDown(mPopup, getAnchorView(), horizontalOffset, verticalOffset, Gravity.NO_GRAVITY);
            mDropDownList.setSelection(ListView.INVALID_POSITION);

            if (DEBUG) Log.e(TAG, "isAboveAnchor=" + mPopup.isAboveAnchor());

            if (!mModal || mDropDownList.isInTouchMode()) {
                clearListSelection();
            }
            if (!mModal) {
                mHandler.post(mHideSelector);
            }
        }
    }

    private int getListWidthSpec() {
        final int displayWidth = mContext.getResources().getDisplayMetrics().widthPixels;

        final int margins = mMargins.left + mMargins.right;
        final int paddings = getBackgroundHorizontalPadding();
        final int mps = margins - paddings;

        final int widthSpec;
        if (mDropDownWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
            // The call to PopupWindow's update method below can accept -1 for any
            // value you do not want to update.
            if (mDropDownMaxWidth == MATCH_PARENT) {
                widthSpec = displayWidth - mps;//-1;
            } else if (mDropDownMaxWidth == WRAP_CONTENT) {
                widthSpec = getAnchorView().getWidth() - mps;
            } else {
                widthSpec = mDropDownMaxWidth - mps;
            }
        } else if (mDropDownWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
            if (mDropDownMaxWidth < 0) {
                widthSpec = getAnchorView().getWidth() - mps;
            } else {
                widthSpec = mDropDownMaxWidth - mps;
            }
        } else if (mDropDownWidth == PREFERRED) {
            int preferredWidth = mDropDownList.compatMeasureContentWidth() + getBackgroundHorizontalPadding();
            if (mDropDownPreferredWidthUnit > 0) {
                int units = (int) Math.ceil(preferredWidth / mDropDownPreferredWidthUnit);
                preferredWidth = (int) (units * mDropDownPreferredWidthUnit);
            }
            if (mDropDownMaxWidth < 0) {
                int anchorWidthTemp = getAnchorView().getWidth() - mps;
                if (preferredWidth > anchorWidthTemp) {
                    if (mDropDownMaxWidth == MATCH_PARENT) {
                        widthSpec = Math.min(preferredWidth, displayWidth - mps);//-1;
                    } else { // WRAP_CONTENT
                        widthSpec = anchorWidthTemp;
                    }
                } else {
                    widthSpec = preferredWidth;
                }
            } else {
                if (preferredWidth > mDropDownMaxWidth - mps) {
                    widthSpec = mDropDownMaxWidth - mps;
                } else {
                    widthSpec = preferredWidth;
                }
            }
        } else {
            if (mDropDownMaxWidth < 0) {
                int anchorWidthTemp = getAnchorView().getWidth() - mps;
                if (mDropDownMaxWidth == WRAP_CONTENT && mDropDownWidth > anchorWidthTemp) {
                    widthSpec = anchorWidthTemp;
                } else {
                    widthSpec = mDropDownWidth;
                }
            } else {
                if (mDropDownWidth > mDropDownMaxWidth - mps) {
                    widthSpec = mDropDownMaxWidth - mps;
                } else {
                    widthSpec = mDropDownWidth;
                }
            }
        }

        return widthSpec;
    }

    private int getBackgroundHorizontalPadding() {
        Drawable background = mPopup.getBackground();
        if (background != null) {
            background.getPadding(mTempRect);
            return mTempRect.left + mTempRect.right;
        }
        return 0;
    }

    private void getBackgroundPadding(Rect out) {
        Drawable background = mPopup.getBackground();
        if (background != null) {
            background.getPadding(out);
        } else {
            out.set(0, 0, 0, 0);
        }
    }

    @Deprecated
    private int getBackgroundLeftPadding() {
        Drawable background = mPopup.getBackground();
        if (background != null) {
            background.getPadding(mTempRect);
            return mTempRect.left;
        }
        return 0;
    }

    @Deprecated
    private int getBackgroundRightPadding() {
        Drawable background = mPopup.getBackground();
        if (background != null) {
            background.getPadding(mTempRect);
            return mTempRect.right;
        }
        return 0;
    }

    @Deprecated
    private int getBackgroundBottomPadding() {
        Drawable background = mPopup.getBackground();
        if (background != null) {
            background.getPadding(mTempRect);
            return mTempRect.bottom;
        }
        return 0;
    }

    @Deprecated
    private int getBackgroundTopPadding() {
        Drawable background = mPopup.getBackground();
        if (background != null) {
            background.getPadding(mTempRect);
            return mTempRect.top;
        }
        return 0;
    }

    private int getBackgroundVerticalPadding() {
        Drawable background = mPopup.getBackground();
        if (background != null) {
            background.getPadding(mTempRect);
            return mTempRect.top + mTempRect.bottom;
        }
        return 0;
    }

    /**
     * Dismiss the popup window.
     */
    public void dismiss() {
        mPopup.dismiss();
        removePromptView();
        mPopup.setContentView(null);
        mDropDownList = null;
        mHandler.removeCallbacks(mResizePopupRunnable);
    }

    /**
     * Set a listener to receive a callback when the popup is dismissed.
     *
     * @param listener Listener that will be notified when the popup is dismissed.
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mPopup.setOnDismissListener(listener);
    }

    private void removePromptView() {
        if (mPromptView != null) {
            final ViewParent parent = mPromptView.getParent();
            if (parent instanceof ViewGroup) {
                final ViewGroup group = (ViewGroup) parent;
                group.removeView(mPromptView);
            }
        }
    }

    /**
     * Control how the popup operates with an input method: one of
     * {@link #INPUT_METHOD_FROM_FOCUSABLE}, {@link #INPUT_METHOD_NEEDED},
     * or {@link #INPUT_METHOD_NOT_NEEDED}.
     * <p/>
     * <p>If the popup is showing, calling this method will take effect only
     * the next time the popup is shown or through a manual call to the {@link #show()}
     * method.</p>
     *
     * @see #getInputMethodMode()
     * @see #show()
     */
    public void setInputMethodMode(int mode) {
        mPopup.setInputMethodMode(mode);
    }

    /**
     * Return the current value in {@link #setInputMethodMode(int)}.
     *
     * @see #setInputMethodMode(int)
     */
    public int getInputMethodMode() {
        return mPopup.getInputMethodMode();
    }

    /**
     * Set the selected position of the list.
     * Only valid when {@link #isShowing()} == {@code true}.
     *
     * @param position List position to set as selected.
     */
    public void setSelection(int position) {
        XpDropDownListView list = mDropDownList;
        if (isShowing() && list != null) {
            list.setListSelectionHidden(false);
            list.setSelection(position);

            if (position >= 0 && position != list.getCount() - 1) {
                if (Build.VERSION.SDK_INT >= 14 && list.canScrollVertically(-1)) {
                    list.scrollBy(0, -list.getPaddingTop());
                }
            }

        }
        setItemChecked(position);
    }

    private void setItemChecked(final int position) {
        final XpDropDownListView list = mDropDownList;
        if (list != null) {
            list.setItemChecked(position, true);
        }
    }

    public void setSelectionInitial(int position) {
        if (position > 0) {
            setSelection(position);
        } else {
            setItemChecked(position);
        }
    }

    public int getPreferredVerticalOffset(int position) {
        buildDropDown();

        final View anchor = getAnchorView();
        final AbstractXpListPopupWindow popup = this;

        final Context context = anchor.getContext();

        // Shadow is emulated below Lollipop, we have to account for that.
        final int backgroundPaddingTop = getBackgroundTopPadding();

        // Center selected item over anchor view.
        if (position < 0) position = 0;
        final int viewHeight = anchor.getHeight();
        final int dropDownListViewPaddingTop = mDropDownList.getPaddingTop();
        final int selectedItemHeight = popup.measureItem(position);
        final int beforeSelectedItemHeight = popup.measureItemsUpTo(position + 1);

        final int viewHeightAdjustedHalf = (viewHeight - anchor.getPaddingTop() - anchor.getPaddingBottom()) / 2 + anchor.getPaddingBottom();

        final int offset;
        if (selectedItemHeight >= 0 && beforeSelectedItemHeight >= 0) {
            offset = -(beforeSelectedItemHeight + (viewHeightAdjustedHalf - selectedItemHeight / 2) + dropDownListViewPaddingTop + backgroundPaddingTop);
        } else {
            final int height = Util.resolveDimensionPixelSize(context, R.attr.dropdownListPreferredItemHeight, 0);
            offset = -(height * (position + 1) + (viewHeightAdjustedHalf - height / 2) + dropDownListViewPaddingTop + backgroundPaddingTop);
        }
        return offset;
    }

    /**
     * Clear any current list selection.
     * Only valid when {@link #isShowing()} == {@code true}.
     */
    public void clearListSelection() {
        final XpDropDownListView list = mDropDownList;
        if (list != null) {
            // WARNING: Please read the comment where mListSelectionHidden is declared
            list.setListSelectionHidden(true);
            //list.hideSelector();
            list.requestLayout();
        }
    }

    /**
     * @return {@code true} if the popup is currently showing, {@code false} otherwise.
     */
    public boolean isShowing() {
        return mPopup.isShowing();
    }

    /**
     * @return {@code true} if this popup is configured to assume the user does not need
     * to interact with the IME while it is showing, {@code false} otherwise.
     */
    public boolean isInputMethodNotNeeded() {
        return mPopup.getInputMethodMode() == INPUT_METHOD_NOT_NEEDED;
    }

    /**
     * Perform an item click operation on the specified list adapter position.
     *
     * @param position Adapter position for performing the click
     * @return true if the click action could be performed, false if not.
     * (e.g. if the popup was not showing, this method would return false.)
     */
    public boolean performItemClick(int position) {
        if (isShowing()) {
            if (mItemClickListener != null) {
                final XpDropDownListView list = mDropDownList;
                final View child = list.getChildAt(position - list.getFirstVisiblePosition());
                final ListAdapter adapter = list.getAdapter();
                mItemClickListener.onItemClick(list, child, position, adapter.getItemId(position));
            }
            return true;
        }
        return false;
    }

    /**
     * @return The currently selected item or null if the popup is not showing.
     */
    public Object getSelectedItem() {
        if (!isShowing()) {
            return null;
        }
        return mDropDownList.getSelectedItem();
    }

    /**
     * @return The position of the currently selected item or {@link ListView#INVALID_POSITION}
     * if {@link #isShowing()} == {@code false}.
     * @see ListView#getSelectedItemPosition()
     */
    public int getSelectedItemPosition() {
        if (!isShowing()) {
            return ListView.INVALID_POSITION;
        }
        return mDropDownList.getSelectedItemPosition();
    }

    /**
     * @return The ID of the currently selected item or {@link ListView#INVALID_ROW_ID}
     * if {@link #isShowing()} == {@code false}.
     * @see ListView#getSelectedItemId()
     */
    public long getSelectedItemId() {
        if (!isShowing()) {
            return ListView.INVALID_ROW_ID;
        }
        return mDropDownList.getSelectedItemId();
    }

    /**
     * @return The View for the currently selected item or null if
     * {@link #isShowing()} == {@code false}.
     * @see ListView#getSelectedView()
     */
    public View getSelectedView() {
        if (!isShowing()) {
            return null;
        }
        return mDropDownList.getSelectedView();
    }

    /**
     * @return The {@link ListView} displayed within the popup window.
     * Only valid when {@link #isShowing()} == {@code true}.
     */
    public XpDropDownListView getListView() {
        return mDropDownList;
    }

    /**
     * The maximum number of list items that can be visible and still have
     * the list expand when touched.
     *
     * @param max Max number of items that can be visible and still allow the list to expand.
     */
    void setListItemExpandMax(int max) {
        mListItemExpandMaximum = max;
    }

    /**
     * Filter key down events. By forwarding key down events to this function,
     * views using non-modal ListPopupWindow can have it handle key selection of items.
     *
     * @param keyCode keyCode param passed to the host view's onKeyDown
     * @param event event param passed to the host view's onKeyDown
     * @return true if the event was handled, false if it was ignored.
     * @see #setModal(boolean)
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // when the drop down is shown, we drive it directly
        if (isShowing()) {
            // the key events are forwarded to the list in the drop down view
            // note that ListView handles space but we don't want that to happen
            // also if selection is not currently in the drop down, then don't
            // let center or enter presses go there since that would cause it
            // to select one of its items
            if (keyCode != KeyEvent.KEYCODE_SPACE
                && (mDropDownList.getSelectedItemPosition() >= 0
                || !isConfirmKey(keyCode))) {
                int curIndex = mDropDownList.getSelectedItemPosition();
                boolean consumed;

                final boolean below = !mPopup.isAboveAnchor();

                final ListAdapter adapter = mAdapter;

                boolean allEnabled;
                int firstItem = Integer.MAX_VALUE;
                int lastItem = Integer.MIN_VALUE;

                if (adapter != null) {
                    allEnabled = adapter.areAllItemsEnabled();
                    firstItem = allEnabled ? 0 :
                        mDropDownList.lookForSelectablePosition(0, true);
                    lastItem = allEnabled ? adapter.getCount() - 1 :
                        mDropDownList.lookForSelectablePosition(adapter.getCount() - 1, false);
                }

                if ((below && keyCode == KeyEvent.KEYCODE_DPAD_UP && curIndex <= firstItem) ||
                    (!below && keyCode == KeyEvent.KEYCODE_DPAD_DOWN && curIndex >= lastItem)) {
                    // When the selection is at the top, we block the key
                    // event to prevent focus from moving.
                    clearListSelection();
                    mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                    show();
                    return true;
                } else {
                    // WARNING: Please read the comment where mListSelectionHidden
                    //          is declared
                    mDropDownList.setListSelectionHidden(false);
                }

                consumed = mDropDownList.onKeyDown(keyCode, event);
                if (DEBUG) Log.v(TAG, "Key down: code=" + keyCode + " list consumed=" + consumed);

                if (consumed) {
                    // If it handled the key event, then the user is
                    // navigating in the list, so we should put it in front.
                    mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                    // Here's a little trick we need to do to make sure that
                    // the list view is actually showing its focus indicator,
                    // by ensuring it has focus and getting its window out
                    // of touch mode.
                    mDropDownList.requestFocusFromTouch();
                    show();

                    switch (keyCode) {
                        // avoid passing the focus from the text view to the
                        // next component
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                        case KeyEvent.KEYCODE_DPAD_UP:
                            return true;
                    }
                } else {
                    if (below && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        // when the selection is at the bottom, we block the
                        // event to avoid going to the next focusable widget
                        if (curIndex == lastItem) {
                            return true;
                        }
                    } else if (!below && keyCode == KeyEvent.KEYCODE_DPAD_UP &&
                        curIndex == firstItem) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Filter key down events. By forwarding key up events to this function,
     * views using non-modal ListPopupWindow can have it handle key selection of items.
     *
     * @param keyCode keyCode param passed to the host view's onKeyUp
     * @param event event param passed to the host view's onKeyUp
     * @return true if the event was handled, false if it was ignored.
     * @see #setModal(boolean)
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isShowing() && mDropDownList.getSelectedItemPosition() >= 0) {
            boolean consumed = mDropDownList.onKeyUp(keyCode, event);
            if (consumed && isConfirmKey(keyCode)) {
                // if the list accepts the key events and the key event was a click, the text view
                // gets the selected item from the drop down as its content
                dismiss();
            }
            return consumed;
        }
        return false;
    }

    /**
     * Filter pre-IME key events. By forwarding {@link View#onKeyPreIme(int, KeyEvent)}
     * events to this function, views using ListPopupWindow can have it dismiss the popup
     * when the back key is pressed.
     *
     * @param keyCode keyCode param passed to the host view's onKeyPreIme
     * @param event event param passed to the host view's onKeyPreIme
     * @return true if the event was handled, false if it was ignored.
     * @see #setModal(boolean)
     */
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isShowing()) {
            // special case for the back key, we do not even try to send it
            // to the drop down list but instead, consume it immediately
            final View anchorView = mDropDownAnchorView;
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                KeyEvent.DispatcherState state = anchorView.getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                KeyEvent.DispatcherState state = anchorView.getKeyDispatcherState();
                if (state != null) {
                    state.handleUpEvent(event);
                }
                if (event.isTracking() && !event.isCanceled()) {
                    dismiss();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>Builds the popup window's content and returns the height the popup
     * should have. Returns -1 when the content already exists.</p>
     *
     * @return the content's height or -1 if content already exists
     */
    private int buildDropDown() {
        ViewGroup dropDownView;
        int otherHeights = 0;

        if (mDropDownList == null) {
            Context context = mContext;

            /**
             * This Runnable exists for the sole purpose of checking if the view layout has got
             * completed and if so call showDropDown to display the drop down. This is used to show
             * the drop down as soon as possible after user opens up the search dialog, without
             * waiting for the normal UI pipeline to do it's job which is slower than this method.
             */
            mShowDropDownRunnable = new Runnable() {
                public void run() {
                    // View layout should be all done before displaying the drop down.
                    View view = getAnchorView();
                    if (view != null && view.getWindowToken() != null) {
                        show();
                    }
                }
            };

            mDropDownList = createDropDownListView(context, !mModal);
            if (mDropDownListHighlight != null) {
                mDropDownList.setSelector(mDropDownListHighlight);
            }
            mDropDownList.setAdapter(mAdapter);
            mDropDownList.setOnItemClickListener(mItemClickListener);
            mDropDownList.setFocusable(true);
            mDropDownList.setFocusableInTouchMode(true);
            mDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    if (position != -1) {
                        XpDropDownListView dropDownList = mDropDownList;

                        if (dropDownList != null) {
                            dropDownList.setListSelectionHidden(false);
                        }
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            mDropDownList.setOnScrollListener(mScrollListener);

            if (mItemSelectedListener != null) {
                mDropDownList.setOnItemSelectedListener(mItemSelectedListener);
            }

            dropDownView = mDropDownList;

            View hintView = mPromptView;
            if (hintView != null) {
                // if a hint has been specified, we accomodate more space for it and
                // add a text view in the drop down menu, at the bottom of the list
                LinearLayout hintContainer = new LinearLayout(context);
                hintContainer.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams hintParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f
                );

                switch (mPromptPosition) {
                    case POSITION_PROMPT_BELOW:
                        hintContainer.addView(dropDownView, hintParams);
                        hintContainer.addView(hintView);
                        break;

                    case POSITION_PROMPT_ABOVE:
                        hintContainer.addView(hintView);
                        hintContainer.addView(dropDownView, hintParams);
                        break;

                    default:
                        Log.e(TAG, "Invalid hint position " + mPromptPosition);
                        break;
                }

                // Measure the hint's height to find how much more vertical
                // space we need to add to the drop down's height.
                final int widthSize;
                final int widthMode;
                if (mDropDownWidth >= 0) {
                    widthMode = MeasureSpec.AT_MOST;
                    widthSize = mDropDownWidth > mDropDownMaxWidth ? mDropDownMaxWidth : mDropDownWidth;
//                    widthSize = mDropDownWidth;
                } else {
                    if (mDropDownMaxWidth >= 0) {
                        widthMode = MeasureSpec.AT_MOST;
                        widthSize = mDropDownMaxWidth;
                    } else {
                        widthMode = MeasureSpec.UNSPECIFIED;
                        widthSize = 0;
                    }
                }
                //noinspection Range
                final int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                final int heightSpec = MeasureSpec.UNSPECIFIED;
                hintView.measure(widthSpec, heightSpec);

                hintParams = (LinearLayout.LayoutParams) hintView.getLayoutParams();
                otherHeights = hintView.getMeasuredHeight() + hintParams.topMargin
                    + hintParams.bottomMargin;

                dropDownView = hintContainer;
            }

            mPopup.setContentView(dropDownView);
        } else {
            dropDownView = (ViewGroup) mPopup.getContentView();
            final View view = mPromptView;
            if (view != null) {
                LinearLayout.LayoutParams hintParams =
                    (LinearLayout.LayoutParams) view.getLayoutParams();
                otherHeights = view.getMeasuredHeight() + hintParams.topMargin
                    + hintParams.bottomMargin;
            }
        }

        // getMaxAvailableHeight() subtracts the padding, so we put it back
        // to get the available height for the whole window
        int padding = 0;
        Drawable background = mPopup.getBackground();
        if (background != null) {
            background.getPadding(mTempRect);
            padding = mTempRect.top + mTempRect.bottom;

            // If we don't have an explicit vertical offset, determine one from the window
            // background so that content will line up.
//            if (!mDropDownVerticalOffsetSet) {
//                mDropDownVerticalOffset = -mTempRect.top;
//            }
        } else {
            mTempRect.setEmpty();
        }

        final int verticalMargin = mMargins.top + mMargins.bottom;

        // Max height available on the screen for a popup.
        final boolean ignoreBottomDecorations =
            mPopup.getInputMethodMode() == PopupWindow.INPUT_METHOD_NOT_NEEDED;
//        final int maxHeight = getMaxAvailableHeight(getAnchorView(), mDropDownVerticalOffset, ignoreBottomDecorations);
        final int maxHeight = getMaxAvailableHeight(getAnchorView(), ignoreBottomDecorations);
        if (mDropDownAlwaysVisible || mDropDownHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
            return maxHeight - verticalMargin + padding;
        }

        final int childWidthSpec;
        switch (mDropDownWidth) {
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                childWidthSpec = MeasureSpec.makeMeasureSpec(
                    getAnchorView().getWidth() -
                        (mMargins.left + mMargins.right) -
                        (mTempRect.left + mTempRect.right),
                    MeasureSpec.AT_MOST);
                break;
            case ViewGroup.LayoutParams.MATCH_PARENT:
                childWidthSpec = MeasureSpec.makeMeasureSpec(
                    mContext.getResources().getDisplayMetrics().widthPixels -
                        (mMargins.left + mMargins.right) -
                        (mTempRect.left + mTempRect.right),
                    MeasureSpec.EXACTLY);
                break;
            case PREFERRED:
                int widthSize;
                int widthMode;
                if (mDropDownMaxWidth >= 0) {
                    widthSize = mDropDownMaxWidth -
                        (mMargins.left + mMargins.right) -
                        (mTempRect.left + mTempRect.right);
                    widthMode = MeasureSpec.AT_MOST;
                    childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                } else {
                    widthMode = MeasureSpec.AT_MOST;
                    if (mDropDownMaxWidth == WRAP_CONTENT) {
                        widthSize = getAnchorView().getWidth() -
                            (mMargins.left + mMargins.right) -
                            (mTempRect.left + mTempRect.right);
                    } else { // MATCH_PARENT
                        widthSize = mContext.getResources().getDisplayMetrics().widthPixels -
                            (mMargins.left + mMargins.right) -
                            (mTempRect.left + mTempRect.right);
                    }
                    childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                }
                break;
            default:
                //noinspection Range
                childWidthSpec = MeasureSpec.makeMeasureSpec(mDropDownWidth, MeasureSpec.EXACTLY);
                break;
        }

        final int listPadding = mDropDownList.getPaddingTop() + mDropDownList.getPaddingBottom();
        final int listContent = mDropDownList.measureHeightOfChildrenCompat(childWidthSpec,
            0, XpDropDownListView.NO_POSITION, maxHeight - otherHeights - verticalMargin - listPadding + padding, -1);
        // add padding only if the list has items in it, that way we don't show
        // the popup if it is not needed
        if (otherHeights > 0 || listContent > 0) otherHeights += padding + listPadding;

        return listContent + otherHeights;
    }

    @NonNull
    XpDropDownListView createDropDownListView(final Context context, final boolean hijackFocus) {
        final XpDropDownListView listView = new XpDropDownListView(context, hijackFocus);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        return listView;
    }

    private class PopupDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            if (isShowing()) {
                // Resize the popup to fit new content
                show();
            }
        }

        @Override
        public void onInvalidated() {
            dismiss();
        }
    }

    private class ListSelectorHider implements Runnable {
        public void run() {
            clearListSelection();
        }
    }

    private class ResizePopupRunnable implements Runnable {
        public void run() {
            if (mDropDownList != null && ViewCompat.isAttachedToWindow(mDropDownList)
                && mDropDownList.getCount() > mDropDownList.getChildCount()
                && mDropDownList.getChildCount() <= mListItemExpandMaximum) {
                mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                show();
            }
        }
    }

    private class PopupTouchInterceptor implements OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            final int action = event.getAction();
            final int x = (int) event.getX();
            final int y = (int) event.getY();

            if (action == MotionEvent.ACTION_DOWN &&
                mPopup != null && mPopup.isShowing() &&
                (x >= 0 && x < mPopup.getWidth() && y >= 0 && y < mPopup.getHeight())) {
                mHandler.postDelayed(mResizePopupRunnable, EXPAND_LIST_TIMEOUT);
            } else if (action == MotionEvent.ACTION_UP) {
                mHandler.removeCallbacks(mResizePopupRunnable);
            }
            return false;
        }
    }

    private class PopupScrollListener implements ListView.OnScrollListener {
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {

        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_TOUCH_SCROLL &&
                !isInputMethodNotNeeded() && mPopup.getContentView() != null) {
                mHandler.removeCallbacks(mResizePopupRunnable);
                mResizePopupRunnable.run();
            }
        }
    }

    private static boolean isConfirmKey(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER;
    }

    private void setPopupClipToScreenEnabled(boolean clip) {
        if (sClipToWindowEnabledMethod != null) {
            try {
                sClipToWindowEnabledMethod.invoke(mPopup, clip);
            } catch (Exception e) {
                Log.i(TAG, "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
            }
        }
    }

    private int getMaxAvailableHeight(View anchor, boolean ignoreBottomDecorations) {
        final View bounds = mDropDownBoundsView;
        if (bounds != null) {
            int returnedHeight = bounds.getHeight();
            returnedHeight -= getBackgroundVerticalPadding();
            return returnedHeight;
        }

        getWindowFrame(anchor, ignoreBottomDecorations, mTempRect);
        int returnedHeight = mTempRect.height();
        returnedHeight -= getBackgroundVerticalPadding();

        // 1 dp extra as part of 25 dp status bar. Prevents 1 dp scrolling when landscape 360dp.
        if (Build.VERSION.SDK_INT < 23) returnedHeight += Util.dpToPxSize(mContext, 1);

        return returnedHeight;
    }

    private void setAllowScrollingAnchorParent(boolean enabled) {
        if (sSetAllowScrollingAnchorParentMethod != null) {
            try {
                sSetAllowScrollingAnchorParentMethod.invoke(mPopup, enabled);
            } catch (Exception e) {
                Log.i(TAG, "Could not call setAllowScrollingAnchorParent() on PopupWindow. Oh well.");
            }
        }
    }

    private int getWindowFrame(final View anchor, final boolean ignoreBottomDecorations, final Rect out) {
        int bottomDecorations = 0;
        anchor.getWindowVisibleDisplayFrame(out);
//        if (ignoreBottomDecorations) {
//            Resources res = anchor.getContext().getResources();
//            int bottomEdge = res.getDisplayMetrics().heightPixels;
//            bottomDecorations = bottomEdge - out.bottom;
//            out.bottom = bottomEdge;
//        }
        return bottomDecorations;
    }

    private void getLocationInWindow(View anchor, @Size(2) int[] out) {
        anchor.getLocationInWindow(out);
    }

    /**
     * @param out Margins relative to left, top, right and bottom of the window.
     */
    private void getBoundsInWindow(Rect out) {
        final View bounds = mDropDownBoundsView;
        if (bounds != null) {
            bounds.getWindowVisibleDisplayFrame(mTempRect);
            final int windowTop = mTempRect.top;
            final int windowRight = mTempRect.right;
            final int windowLeft = mTempRect.left;
            final int windowBottom = mTempRect.bottom;

            bounds.getLocationInWindow(mTempLocation);
            final int boundsTop = mTempLocation[1];
            final int boundsLeft = mTempLocation[0];

            final int boundsHeight = bounds.getHeight();
            final int boundsWidth = bounds.getWidth();

            out.top = boundsTop - windowTop;
            out.left = boundsLeft - windowLeft;
            out.bottom = windowBottom - (boundsTop + boundsHeight);
            out.right = windowRight - (boundsLeft + boundsWidth);
            return;
        }

        out.set(0, 0, 0, 0);
    }
}
