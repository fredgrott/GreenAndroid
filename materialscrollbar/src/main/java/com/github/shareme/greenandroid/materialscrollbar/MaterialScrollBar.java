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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/*
 * Table Of Contents:
 *
 * I - Initial Setup
 * II - Abstraction for flavour differentiation
 * III - Customisation methods
 * IV - Misc Methods
 *
 * Outline for developers.
 *
 * The two flavours of the MaterialScrollBar are the DragScrollBar and the TouchScrollBar. They both
 * extend this class. Implementations which are unique to each flavour are implemented through
 * abstraction. The use of the T generic is used to maintain the identity of the subclass when
 * chaining settings (ie. So that DragScrollBar(...).addIndicator(...) will return dragScrollBar and
 * not MaterialScrollBar).
 *
 * The class can be instantiated programmatically or through XML. The framework for getting
 * everything set up the same regardless of the source is below. The methods are annotated to
 * indicate which invocation option uses which methods.
 *
 * Scrolling logic is computed separably in ScrollingUtilities. A unique instance is made for each
 * instance of the bar.
 */
@SuppressWarnings("unchecked")
abstract class MaterialScrollBar<T> extends RelativeLayout {

    private View background;
    Handle handle;
    int handleColour;
    int handleOffColour = Color.parseColor("#9c9c9c");
    protected boolean hidden = true;
    RecyclerView recyclerView;
    Indicator indicator;
    private int textColour = ContextCompat.getColor(getContext(), android.R.color.white);
    boolean lightOnTouch;
    boolean hiddenByUser = false;
    private TypedArray a;
    private int seekId = 0;
    //For some unknown reason, some behaviours are reversed when added programmatically versus xml. Can be handled but as yet not understood.
    boolean programmatic;
    ScrollingUtilities scrollUtils = new ScrollingUtilities(this);
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean customScroll = false;

    //CHAPTER I - INITIAL SETUP

    //Style-less XML Constructor
    MaterialScrollBar(Context context, AttributeSet attributeSet){
        this(context, attributeSet, 0);
    }

    //Styled XML Constructor
    MaterialScrollBar(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet, defStyle);
        programmatic = false;
        addView(setUpBackground(context));
        setUpProps(context, attributeSet);
        addView(setUpHandle(context, a.getBoolean(R.styleable.MaterialScrollBar_msb_lightOnTouch, true)));
        if(!isInEditMode()){
            seekId = a.getResourceId(R.styleable.MaterialScrollBar_msb_recyclerView, 0);
        }
        implementPreferences();
        a.recycle();
    }

    //Programmatic Constructor
    MaterialScrollBar(Context context, final RecyclerView recyclerView, boolean lightOnTouch){
        super(context);
        programmatic = true;
        if(!(recyclerView.getParent() instanceof RelativeLayout)){
            throw new CustomExceptions.UnsupportedParentException();
        }
        setId(R.id.reservedNamedId); //Gives the view an ID so that it can be found in the parent.
        addView(setUpBackground(context)); //Adds the background
        addView(setUpHandle(context, lightOnTouch)); //Adds the handle
        LayoutParams layoutParams = new LayoutParams(Utils.getDP(20, this), ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(ALIGN_RIGHT, recyclerView.getId());
        layoutParams.addRule(ALIGN_TOP, recyclerView.getId());
        layoutParams.addRule(ALIGN_BOTTOM, recyclerView.getId());
        ((ViewGroup) recyclerView.getParent()).addView(this, layoutParams); //Fits bar to right edge of the relevant view.
        this.recyclerView = recyclerView;
        generalSetup();
    }

    //XML case only. Unpacks XML attributes and ensures that no mandatory attributes are missing.
    void setUpProps(Context context, AttributeSet attrs){
        a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MaterialScrollBar,
                0, 0);
        ArrayList<String> missing = new ArrayList<>();
        //Ensures that a recyclerView is associated with the bar.
        if(!a.hasValue(R.styleable.MaterialScrollBar_msb_recyclerView)){
            missing.add("recyclerView");
        }
        //Ensures that a preference is expressed for lightOnTouch.
        if(!a.hasValue(R.styleable.MaterialScrollBar_msb_lightOnTouch)){
            missing.add("lightOnTouch");
        }
        if(missing.size() != 0){
            throw new CustomExceptions.MissingAttributesException(missing);
        }
    }

    //Dual case. Sets up bar.
    View setUpBackground(Context context){
        background = new View(context);
        LayoutParams lp = new LayoutParams(Utils.getDP(12, this), LayoutParams.MATCH_PARENT);
        lp.addRule(ALIGN_PARENT_RIGHT);
        background.setLayoutParams(lp);
        background.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        ViewCompat.setAlpha(background, 0.4F);
        return(background);
    }

    //Dual case. Sets up handle.
    Handle setUpHandle(Context context, Boolean lightOnTouch){
        handle = new Handle(context, getMode(), programmatic);
        LayoutParams lp = new LayoutParams(Utils.getDP(12, this),
                Utils.getDP(72, this));
        lp.addRule(ALIGN_PARENT_RIGHT);
        handle.setLayoutParams(lp);

        this.lightOnTouch = lightOnTouch;
        int colourToSet;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            handleColour = fetchAccentColour(context);
        } else {
            handleColour = Color.parseColor("#9c9c9c");
        }
        if(lightOnTouch){
            colourToSet = Color.parseColor("#9c9c9c");
        } else {
            colourToSet = handleColour;
        }
        handle.setBackgroundColor(colourToSet);
        return handle;
    }

    //XML case only. Implements optional attributes.
    void implementPreferences(){
        if(a.hasValue(R.styleable.MaterialScrollBar_msb_barColour)){
            setBarColour(a.getColor(R.styleable.MaterialScrollBar_msb_barColour, 0));
        }
        if(a.hasValue(R.styleable.MaterialScrollBar_msb_handleColour)){
            setHandleColour(a.getColor(R.styleable.MaterialScrollBar_msb_handleColour, 0));
        }
        if(a.hasValue(R.styleable.MaterialScrollBar_msb_handleOffColour)){
            setHandleOffColour(a.getColor(R.styleable.MaterialScrollBar_msb_handleOffColour, 0));
        }
        if(a.hasValue(R.styleable.MaterialScrollBar_msb_textColour)){
            setTextColour(a.getColor(R.styleable.MaterialScrollBar_msb_textColour, 0));
        }
        if(a.hasValue(R.styleable.MaterialScrollBar_msb_barThickness)){
            setBarThickness(a.getDimensionPixelSize(R.styleable.MaterialScrollBar_msb_barThickness, 0));
        }
        implementFlavourPreferences(a);
    }

    //XML case only. Waits for all of the views to be attached to the window and then implements general setup.
    //Waiting must occur so that the relevant recyclerview can be found.
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if(seekId != 0){
            recyclerView = (RecyclerView) getRootView().findViewById(seekId);
            generalSetup();
        }
    }

    //Dual case. General setup.
    private void generalSetup(){
        recyclerView.setVerticalScrollBarEnabled(false); // disable any existing scrollbars
        recyclerView.addOnScrollListener(new scrollListener()); // lets us read when the recyclerView scrolls

        setTouchIntercept(); // catches touches on the bar

        identifySwipeRefreshParents();

        //Hides the view
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF, getHideRatio(), Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        anim.setDuration(0);
        anim.setFillAfter(true);
        hidden = true;
        startAnimation(anim);
    }

    //Identifies any SwipeRefreshLayout parent so that it can be disabled and enabled during scrolling.
    void identifySwipeRefreshParents(){
        boolean cycle = true;
        ViewParent parent = getParent();
        while(cycle){
            if(parent instanceof SwipeRefreshLayout){
                swipeRefreshLayout = (SwipeRefreshLayout)parent;
                cycle = false;
            } else {
                if(parent.getParent() == null){
                    cycle = false;
                } else {
                    parent = parent.getParent();
                }
            }
        }
    }

    boolean sizeUnchecked = true;

    //Checks each time the bar is laid out. If there are few enough view that
    //they all fit on the screen then the bar is hidden. If a view is added which doesn't fit on
    //the screen then the bar is unhidden.
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(sizeUnchecked && !isInEditMode()){
            scrollUtils.getCurScrollState();
            if(scrollUtils.getAvailableScrollHeight() <= 0){
                background.setVisibility(GONE);
                handle.setVisibility(GONE);
            } else {
                background.setVisibility(VISIBLE);
                handle.setVisibility(VISIBLE);
                sizeUnchecked = false;
            }
        }
    }

    // Makes the bar render correctly for XML
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = Utils.getDP(12, this);
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    //CHAPTER II - ABSTRACTION FOR FLAVOUR DIFFERENTIATION

    abstract void setTouchIntercept();

    abstract int getMode();

    abstract float getHideRatio();

    abstract void onScroll();

    abstract boolean getHide();

    abstract void implementFlavourPreferences(TypedArray a);

    abstract float getHandleOffset();

    abstract float getIndicatorOffset();

    //CHAPTER III - CUSTOMISATION METHODS

    private void checkCustomScrollingInterface(){
        if(!(recyclerView.getAdapter() instanceof  ICustomScroller)){
            throw new CustomExceptions.AdapterNotSetupForCustomScrollingException(recyclerView.getAdapter().getClass());
        }
        scrollUtils.customScroller = (ICustomScroller) recyclerView.getAdapter();
    }

    /**
     * The scrollBar should attempt to use dev provided scrolling logic and not default logic.
     *
     * The adapter must implement {@link ICustomScroller}.
     */
    public T useCustomScrolling(){
        customScroll = true;
        if (ViewCompat.isAttachedToWindow(this))
            checkCustomScrollingInterface();
        else
            addOnLayoutChangeListener(new OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
                {
                    MaterialScrollBar.this.removeOnLayoutChangeListener(this);
                    checkCustomScrollingInterface();
                }
            });
        return (T)this;
    }

    /**
     * Provides the ability to programmatically set the colour of the scrollbar handle.
     * @param colour to set the handle.
     */
    public T setHandleColour(String colour){
        handleColour = Color.parseColor(colour);
        setHandleColour();
        return (T)this;
    }

    /**
     * Provides the ability to programmatically set the colour of the scrollbar handle.
     * @param colour to set the handle.
     */
    public T setHandleColour(@ColorInt int colour){
        handleColour = colour;
        setHandleColour();
        return (T)this;
    }

    /**
     * Provides the ability to programmatically set the colour of the scrollbar handle.
     * @param colourResId to set the handle.
     */
    public T setHandleColourRes(@ColorRes int colourResId){
        handleColour = ContextCompat.getColor(getContext(), colourResId);
        setHandleColour();
        return (T)this;
    }

    private void setHandleColour(){
        if(indicator != null){
            ((GradientDrawable)indicator.getBackground()).setColor(handleColour);
        }
        if(!lightOnTouch){
            handle.setBackgroundColor(handleColour);
        }
    }

    /**
     * Provides the ability to programmatically set the colour of the scrollbar handle when unpressed. Only applies if lightOnTouch is true.
     * @param colour to set the handle when unpressed.
     */
    public T setHandleOffColour(String colour){
        handleOffColour = Color.parseColor(colour);
        if(lightOnTouch){
            handle.setBackgroundColor(handleOffColour);
        }
        return (T)this;
    }

    /**
     * Provides the ability to programmatically set the colour of the scrollbar handle when unpressed. Only applies if lightOnTouch is true.
     * @param colour to set the handle when unpressed.
     */
    public T setHandleOffColour(@ColorInt int colour){
        handleOffColour = colour;
        if(lightOnTouch){
            handle.setBackgroundColor(handleOffColour);
        }
        return (T)this;
    }

    /**
     * Provides the ability to programmatically set the colour of the scrollbar handle when unpressed. Only applies if lightOnTouch is true.
     * @param colourResId to set the handle when unpressed.
     */
    public T setHandleOffColourRes(@ColorRes int colourResId){
        handleOffColour = ContextCompat.getColor(getContext(), colourResId);
        if(lightOnTouch){
            handle.setBackgroundColor(handleOffColour);
        }
        return (T)this;
    }

    /**
     * Provides the ability to programmatically set the colour of the scrollbar.
     * @param colour to set the bar.
     */
    public T setBarColour(String colour){
        background.setBackgroundColor(Color.parseColor(colour));
        return (T)this;
    }

    /**
     * Provides the ability to programmatically set the colour of the scrollbar.
     * @param colour to set the bar.
     */
    public T setBarColour(@ColorInt int colour){
        background.setBackgroundColor(colour);
        return (T)this;
    }

    /**
     * Provides the ability to programmatically set the colour of the scrollbar.
     * @param colourResId to set the bar.
     */
    public T setBarColourRes(@ColorRes int colourResId){
        background.setBackgroundColor(ContextCompat.getColor(getContext(), colourResId));
        return (T)this;
    }

    /**
     * Provides the ability to programmatically set the text colour of the indicator. Will do nothing if there is no section indicator.
     * @param colour to set the text of the indicator.
     */
    public T setTextColour(@ColorInt int colour){
        textColour = colour;
        if(indicator != null){
            indicator.setTextColour(textColour);
        }
        return(T)this;
    }


    /**
     * Provides the ability to programmatically set the text colour of the indicator. Will do nothing if there is no section indicator.
     * @param colourResId to set the text of the indicator.
     */
    public T setTextColourRes(@ColorRes int colourResId){
        textColour = ContextCompat.getColor(getContext(), colourResId);
        if(indicator != null){
            indicator.setTextColour(textColour);
        }
        return (T)this;
    }

    /**
     * Provides the ability to programmatically set the text colour of the indicator. Will do nothing if there is no section indicator.
     * @param colour to set the text of the indicator.
     */
    public T setTextColour(String colour){
        textColour = Color.parseColor(colour);
        if(indicator != null){
            indicator.setTextColour(textColour);
        }
        return (T)this;
    }

    /**
     * Removes any indicator.
     */
    public T removeIndicator(){
        this.indicator = null;
        return (T)this;
    }

    /**
     * Adds an indicator which accompanies this scroll bar.
     *
     * @param addSpace Should space be put between the indicator and the bar or should they touch?
     */
    public T addIndicator(final Indicator indicator, final boolean addSpace) {

        if(ViewCompat.isAttachedToWindow(this)){
            this.indicator = indicator;
            indicator.testAdapter(recyclerView.getAdapter());
            indicator.linkToScrollBar(MaterialScrollBar.this, addSpace);
            indicator.setTextColour(textColour);
        } else {
            addOnLayoutChangeListener(new OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
                {
                    MaterialScrollBar.this.indicator = indicator;
                    indicator.testAdapter(recyclerView.getAdapter());
                    indicator.linkToScrollBar(MaterialScrollBar.this, addSpace);
                    indicator.setTextColour(textColour);
                    MaterialScrollBar.this.removeOnLayoutChangeListener(this);
                }
            });
        }
        return (T)this;
    }

    /**
     * Allows the developer to set a custom bar thickness.
     * @param thickness The desired bar thickness.
     */
    public T setBarThickness(int thickness){
        LayoutParams layoutParams = (LayoutParams) handle.getLayoutParams();
        layoutParams.width = thickness;
        handle.setLayoutParams(layoutParams);

        layoutParams = (LayoutParams) background.getLayoutParams();
        layoutParams.width = thickness;
        background.setLayoutParams(layoutParams);

        if(indicator != null){
            indicator.setSizeCustom(thickness);
        }
        return (T)this;
    }

    /**
     * Hide or unhide the scrollBar.
     */
    public void setScrollBarHidden(boolean hidden){
        hiddenByUser = hidden;
        fadeOut();
    }

    //CHAPTER IV - MISC METHODS

    //Fetch accent colour on devices running Lollipop or newer.
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private int fetchAccentColour(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray b = context.obtainStyledAttributes(typedValue.data, new int[] { android.R.attr.colorAccent });
        int colour = b.getColor(0, 0);

        b.recycle();

        return colour;
    }

    /**
     * Animates the bar out of view
     */
    void fadeOut(){
        if(!hidden){
            TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF, getHideRatio(), Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            anim.setDuration(150);
            anim.setFillAfter(true);
            hidden = true;
            startAnimation(anim);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    handle.expandHandle();
                }
            }, anim.getDuration() / 3);
        }
    }

    /**
     * Animates the bar into view
     */
    void fadeIn(){
        if(hidden && getHide() && !hiddenByUser){
            hidden = false;
            TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, getHideRatio(), Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            anim.setDuration(150);
            anim.setFillAfter(true);
            startAnimation(anim);
            handle.collapseHandle();
        }
    }

    protected void onDown(MotionEvent event){
        if (indicator != null && indicator.getVisibility() == INVISIBLE) {
            indicator.setVisibility(VISIBLE);
            if(Build.VERSION.SDK_INT >= 12){
                indicator.setAlpha(0F);
                indicator.animate().alpha(1F).setDuration(150).setListener(new AnimatorListenerAdapter() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        indicator.setAlpha(1F);
                    }
                });
            }
        }

        int top = handle.getHeight() / 2;
        int bottom = recyclerView.getHeight() - Utils.getDP(72, recyclerView.getContext());
        float boundedY = Math.max(top, Math.min(bottom, event.getY() - getHandleOffset()));
        scrollUtils.scrollToPositionAtProgress((boundedY - top) / (bottom - top));
        scrollUtils.scrollHandleAndIndicator();
        recyclerView.onScrolled(0, 0);

        if (lightOnTouch) {
            handle.setBackgroundColor(handleColour);
        }
    }

    protected void onUp(){
        if (indicator != null && indicator.getVisibility() == VISIBLE) {
            if (Build.VERSION.SDK_INT <= 12) {
                indicator.clearAnimation();
            }
            if(Build.VERSION.SDK_INT >= 12){
                indicator.animate().alpha(0F).setDuration(150).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        indicator.setVisibility(INVISIBLE);
                    }
                });
            } else {
                indicator.setVisibility(INVISIBLE);
            }
        }

        if (lightOnTouch) {
            handle.setBackgroundColor(handleOffColour);
        }
    }

    class scrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            scrollUtils.scrollHandleAndIndicator();
            if(dy != 0){
                onScroll();
            }

            //Disables any swipeRefreshLayout parent if the recyclerview is not at the top and enables it if it is.
            if(swipeRefreshLayout != null){
                if(((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0){
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        }
    }

}