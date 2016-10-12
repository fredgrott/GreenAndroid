/*
 * Copyright 2016 Rodolfo Navalon.
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
package com.github.shareme.greenandroid.shaperipple;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.github.shareme.greenandroid.shaperipple.data.ShapeRippleEntry;
import com.github.shareme.greenandroid.shaperipple.model.BaseShapeRipple;
import com.github.shareme.greenandroid.shaperipple.model.Circle;
import com.github.shareme.greenandroid.shaperipple.util.ShapePulseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class ShapeRipple extends View {

    private static final String TAG = ShapeRipple.class.getSimpleName();

    /**
     * The default duration of the ripples
     */
    private static final int DEFAULT_RIPPLE_DURATION = 1500;

    /**
     * Extra ripples for minimizing the empty space with in the
     * middle of the ripple
     */
    private static final int EXTRA_RIPPLES = 1;

    /**
     * The default ripple interval factor see {@link #rippleIntervalFactor} for
     * more details
     */
    private static final float DEFAULT_RIPPLE_INTERVAL_FACTOR = 1F;

    /**
     * Base ripple color, only used when {@link #enableColorTransition} flag is set to false
     */
    private int rippleColor;

    /**
     * Starting color for the color transition of the ripple, only
     * used when {@link #enableColorTransition} flag is set to true
     */
    private int rippleFromColor;

    /**
     * End color for the color transition of the ripple, only
     * used when {@link #enableColorTransition} flag is set to true
     */
    private int rippleToColor;

    /**
     * Base ripple duration for the animation, by default the value is {@value DEFAULT_RIPPLE_DURATION}
     */
    private int rippleDuration;

    /**
     * Base stroke width for each of the ripple
     */
    private int rippleStrokeWidth;

    /**
     * Ripple interval handles the actual timing of each spacing
     * of ripples in the list, calculated in {@link #onMeasure(int, int)}
     *
     * Warning: this always change overtime upon incrementing the {@link #DEFAULT_RIPPLE_INTERVAL_FACTOR}
     * see {@link #rippleIntervalCalculated} for the original interval
     */
    private float rippleInterval;

    /**
     * Ripple interval is the original calculated timing interval for each ripple
     * by default the calculation is exactly for when each ripple finishes
     */
    private float rippleIntervalCalculated;

    /**
     * Ripple interval factor is the spacing for each ripple
     * the more the factor the more the spacing
     */
    private float rippleIntervalFactor;

    /**
     * The width of the view in the layout which is calculated in {@link #onMeasure(int, int)}
     */
    private int viewWidth;

    /**
     * The height of the view in the layout which is calculated in {@link #onMeasure(int, int)}
     */
    private int viewHeight;

    /**
     * The maximum radius of the ripple which is calculated in the {@link #onMeasure(int, int)}
     */
    private int maxRippleRadius;

    /**
     * The last fraction value of the animation after invalidation of this view
     */
    private float lastShapeFractionValue = 0f;

    /**
     * Enables the color transition for each ripple, it is true by default
     */
    private boolean enableColorTransition = true;

    /**
     * Enables the single ripple, it is false by default
     */
    private boolean enableSingleRipple = false;

    /**
     * Enables the random positioning of the ripple, it is false by default
     */
    private boolean enableRandomPosition = false;

    /**
     * Enable the random color of the ripple, it is false by default
     */
    private boolean enableRandomColor = false;

    /**
     * The list of {@link ShapeRippleEntry} which is rendered in {@link #render(Float)}
     */
    private List<ShapeRippleEntry> shapeRippleEntries;

    /**
     * The list of developer predefined random colors which is used when {@link #enableRandomColor} is set to true.
     *
     * If this is not defined by the developer it will have a default value from {@link ShapePulseUtil#generateRandomColours(Context)}
     */
    private List<Integer> rippleRandomColors;

    /**
     * The actual animator for the ripples, used in {@link #render(Float)}
     */
    private ValueAnimator rippleValueAnimator;

    /**
     * The {@link Interpolator} of the {@link #rippleValueAnimator}, by default it is {@link LinearInterpolator}
     */
    private Interpolator rippleInterpolator;

    /**
     * The random generator object for both color ({@link #enableRandomColor} is set to true) and position ({@link #enableRandomPosition} is set to true)
     */
    private Random random;

    /**
     * The renderer of shape ripples which is drawn in the {@link BaseShapeRipple#draw(Canvas, int, int, float, int, int)}
     */
    private BaseShapeRipple rippleShape;

    public ShapeRipple(Context context) {
        super(context);
        init(context, null);
    }

    public ShapeRipple(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShapeRipple(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        this.shapeRippleEntries = new ArrayList<>();
        this.random = new Random();

        rippleShape = new Circle();

        rippleColor = Color.parseColor("#FFF44336");
        rippleFromColor = Color.parseColor("#FFF44336");
        rippleToColor = Color.parseColor("#00FFFFFF");
        rippleStrokeWidth = getResources().getDimensionPixelSize(R.dimen.default_stroke_width);
        rippleRandomColors = ShapePulseUtil.generateRandomColours(getContext());
        rippleDuration = DEFAULT_RIPPLE_DURATION;
        rippleIntervalFactor = DEFAULT_RIPPLE_INTERVAL_FACTOR;

        rippleInterpolator = new LinearInterpolator();

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ConnectingRipple, 0, 0);

            try {
                rippleColor = ta.getColor(R.styleable.ConnectingRipple_ripple_color, Color.parseColor("#FFF44336"));
                rippleFromColor = ta.getColor(R.styleable.ConnectingRipple_ripple_from_color, Color.parseColor("#FFF44336"));
                rippleToColor = ta.getColor(R.styleable.ConnectingRipple_ripple_to_color, Color.parseColor("#00FFFFFF"));
                setRippleDuration(ta.getInteger(R.styleable.ConnectingRipple_ripple_duration, DEFAULT_RIPPLE_DURATION));
                enableColorTransition = ta.getBoolean(R.styleable.ConnectingRipple_enable_color_transition, true);
                enableSingleRipple = ta.getBoolean(R.styleable.ConnectingRipple_enable_single_ripple, false);
                enableRandomPosition = ta.getBoolean(R.styleable.ConnectingRipple_enable_random_position, false);
                setEnableRandomColor(ta.getBoolean(R.styleable.ConnectingRipple_enable_random_color, false));
                setRippleStrokeWidth(ta.getDimensionPixelSize(R.styleable.ConnectingRipple_ripple_stroke_width, getResources().getDimensionPixelSize(R.dimen.default_stroke_width)));
            } finally {
                ta.recycle();
            }
        }

        start(rippleDuration);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (ShapeRippleEntry shapeRippleEntry : shapeRippleEntries) {

            if (shapeRippleEntry.isRender()) {
                // Each ripple entry is a rendered as a shape
                shapeRippleEntry.getBaseShapeRipple().draw(canvas,shapeRippleEntry.getX(), shapeRippleEntry.getY(),
                                                                shapeRippleEntry.getRadiusSize(), shapeRippleEntry.getChangingColorValue(),
                                                                shapeRippleEntries.size() - 1 - shapeRippleEntry.getRippleIndex());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Get the measure base of the measure spec
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        // the ripple radius based on the x or y
        maxRippleRadius = (Math.min(viewWidth, viewHeight) / 2 - (rippleStrokeWidth / 2));
        rippleIntervalCalculated = ((float)rippleStrokeWidth / (float)maxRippleRadius);
        rippleInterval = rippleIntervalCalculated * rippleIntervalFactor;
        initializeEntries(rippleShape);

        rippleShape.setWidth(viewWidth);
        rippleShape.setHeight(viewHeight);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        stop();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        stop();
    }

    /**
     * This method will initialize the list of {@link ShapeRippleEntry} with
     * initial position, color, index, and fraction value.
     *
     * The list will contain an extra ripple which is define in {@link #EXTRA_RIPPLES} that minimize the
     * extra space within the middle of the ripples.
     *
     * @param shapeRipple the renderer of shape ripples
     */
    private void initializeEntries(BaseShapeRipple shapeRipple) {

        shapeRipple.setStrokeWidth(rippleStrokeWidth);

        if (viewWidth == 0 && viewHeight == 0) {
            return;
        }

        // we remove all the shape ripples entries
        shapeRippleEntries.clear();
        int maxNumberOfRipples = maxRippleRadius / rippleStrokeWidth;
        for (int i = 0; i < maxNumberOfRipples + EXTRA_RIPPLES; i++) {


            ShapeRippleEntry shapeRippleEntry = new ShapeRippleEntry(shapeRipple);
            shapeRippleEntry.setX(enableRandomPosition ? random.nextInt(viewWidth) : viewWidth / 2);
            shapeRippleEntry.setY(enableRandomPosition ? random.nextInt(viewHeight) : viewHeight / 2);
            shapeRippleEntry.setFractionValue(-(rippleInterval * (float) i));
            shapeRippleEntry.setRippleIndex(i);

            if (enableRandomColor) {
                shapeRippleEntry.setOriginalColorValue(rippleRandomColors.get(random.nextInt(rippleRandomColors.size())));
            } else {
                shapeRippleEntry.setOriginalColorValue(rippleColor);
            }


            shapeRippleEntries.add(shapeRippleEntry);

            // we only render 1 ripple when it is enabled
            if (enableSingleRipple) {
                break;
            }
        }
    }

    /**
     * Refreshes the list of ticket entries after certain options are changed such as the {@link #rippleColor},
     * {@link #rippleShape}, {@link #enableRandomPosition}, etc.
     *
     * This will only execute after the {@link #initializeEntries(BaseShapeRipple)}, this is safe to call before it.
      */
    private void reconfigureEntries() {

        // we do not re configure when dimension is not calculated
        // or if the list is empty
        if (viewWidth == 0 && viewHeight == 0 && (shapeRippleEntries == null || shapeRippleEntries.size() == 0)) {
            return;
        }

        rippleShape.setStrokeWidth(rippleStrokeWidth);

        for (ShapeRippleEntry shapeRippleEntry : shapeRippleEntries) {
            if (enableRandomColor) {
                shapeRippleEntry.setOriginalColorValue(rippleRandomColors.get(random.nextInt(rippleRandomColors.size())));
            } else {
                shapeRippleEntry.setOriginalColorValue(rippleColor);
            }

            shapeRippleEntry.setBaseShapeRipple(rippleShape);
        }

    }

    /**
     * Start the {@link #rippleValueAnimator} with specified duration for each ripple.
     *
     * @param millis the duration in milliseconds
     */
    @SuppressWarnings("WrongConstant")
    private void start(int millis) {

        // Do a ripple value renderer
        rippleValueAnimator = ValueAnimator.ofFloat(0f, 1f);
        rippleValueAnimator.setDuration(millis);
        rippleValueAnimator.setRepeatMode(ValueAnimator.INFINITE);
        rippleValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rippleValueAnimator.setInterpolator(rippleInterpolator);
        rippleValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                render((Float) animation.getAnimatedValue());
            }
        });

        rippleValueAnimator.start();
    }

    /**
     * This is the main renderer for the list of ripple, we always check that the first ripple is already
     * finished.
     *
     * When the ripple is finished it is {@link ShapeRippleEntry#reset()} and move to the end of the list to be reused all over again
     * to prevent creating a new instance of it.
     *
     * Each ripple will be configured to be either rendered or not rendered to the view to prevent extra rendering process.
     *
     * @param fractionValue the current fraction value of the {@link #rippleValueAnimator}
     */
    private void render(Float fractionValue) {

        // Do not render when entries are empty
        if (shapeRippleEntries.size() == 0) {
            return;
        }

        float shapeFractionValue = fractionValue;

        ShapeRippleEntry firstEntry = shapeRippleEntries.get(0);

        // Calculate the fraction value of the first entry
        float firstEntryFractionValue = firstEntry.getFractionValue() + Math.max(shapeFractionValue - lastShapeFractionValue, 0);

        // Check if the first entry is done the ripple (happens when the ripple reaches to end)
        if (firstEntryFractionValue >= 1.0f) {

            // Remove and relocate the first entry to the last entry
            ShapeRippleEntry removedEntry = shapeRippleEntries.remove(0);
            removedEntry.reset();
            removedEntry.setOriginalColorValue(enableRandomColor ? rippleRandomColors.get(random.nextInt(rippleRandomColors.size())) : rippleColor);
            shapeRippleEntries.add(removedEntry);

            // Get the new first entry of the list
            firstEntry = shapeRippleEntries.get(0);

            // Calculate the new fraction value of the first entry of the list
            firstEntryFractionValue = firstEntry.getFractionValue() + Math.max(shapeFractionValue - lastShapeFractionValue, 0);

            firstEntry.setX(enableRandomPosition ? random.nextInt(viewWidth) : viewWidth / 2);
            firstEntry.setY(enableRandomPosition ? random.nextInt(viewHeight) : viewHeight / 2);

            if (enableSingleRipple) {
                firstEntryFractionValue = 0;
            }
        }

        int index = 0;
        for (ShapeRippleEntry shapeRippleEntry : shapeRippleEntries) {

            // set the updated index
            shapeRippleEntry.setRippleIndex(index);

            // calculate the shape fraction by index
            float currentShapeFractionValue = firstEntryFractionValue - rippleInterval * index;

            // Check if we render the current ripple in the list
            // We render when the fraction value is >= 0
            if (currentShapeFractionValue >= 0) {
                shapeRippleEntry.setRender(true);
            } else {
                // We continue to the next item
                // since we know that we do not
                // need the calculations below
                shapeRippleEntry.setRender(false);
                continue;
            }

            // We already calculated the fraction value of the first entry of the list
            if (index == 0) {
                shapeRippleEntry.setFractionValue(firstEntryFractionValue);
            } else {
                shapeRippleEntry.setFractionValue(firstEntryFractionValue > 0 ? currentShapeFractionValue : currentShapeFractionValue + 1);
            }

            // calculate the color if we enabled the color transition
            shapeRippleEntry.setChangingColorValue(enableColorTransition
                    ? ShapePulseUtil.evaluateTransitionColor(currentShapeFractionValue, shapeRippleEntry.getOriginalColorValue(), rippleToColor)
                    : rippleColor);
            // calculate the current ripple size
            shapeRippleEntry.setRadiusSize(maxRippleRadius * currentShapeFractionValue);

            index += 1;
        }

        // save the last fraction value
        lastShapeFractionValue = shapeFractionValue;

        // we draw the shapes
        invalidate();
    }

    /**
     * Stop the {@link #rippleValueAnimator} and clears the {@link #shapeRippleEntries}
     */
    private void stop() {

        if (rippleValueAnimator != null) {
            rippleValueAnimator.cancel();
            rippleValueAnimator.end();
            rippleValueAnimator.removeAllUpdateListeners();
            rippleValueAnimator.removeAllListeners();
            rippleValueAnimator = null;
        }

        if (shapeRippleEntries != null) {
            shapeRippleEntries.clear();
            invalidate();
        }
    }

    /**
     * Starts the ripple by stopping the current {@link #rippleValueAnimator} using the {@link #stop()}
     * then initializing ticket entries using the {@link #initializeEntries(BaseShapeRipple)}
     * and lastly starting the {@link #rippleValueAnimator} using {@link #start(int)}
     */
    public void startRipple() {
        //stop the animation from previous before starting it again
        stop();
        initializeEntries(rippleShape);
        start(rippleDuration);
    }

    /**
     * Stops the ripple see {@link #stop()} for more details
     */
    public void stopRipple() {
        stop();
    }

    /**
     * @return The interval for each ripple
     */
    public float getRippleInterval() {
        return rippleInterval;
    }

    /**
     * @return True if color transition is enabled
     */
    public boolean isEnableColorTransition() {
        return enableColorTransition;
    }

    /**
     * @return True of single ripple is enabled
     */
    public boolean isEnableSingleRipple() {
        return enableSingleRipple;
    }

    /**
     * @return True of random ripple position is enabled
     */
    public boolean isEnableRandomPosition() {
        return enableRandomPosition;
    }

    /**
     * @return The stroke width(in pixels) for each ripple
     */
    public int getRippleStrokeWidth() {
        return rippleStrokeWidth;
    }

    /**
     * @return The base ripple color
     */
    public int getRippleColor() {
        return rippleColor;
    }

    /**
     * @return The starting ripple color of the color transition
     */
    public int getRippleFromColor() {
        return rippleFromColor;
    }

    /**
     * @return The end ripple color of the color transition
     */
    public int getRippleToColor() {
        return rippleToColor;
    }

    /**
     * @return The duration of each ripple in milliseconds
     */
    public int getRippleDuration() {
        return rippleDuration;
    }

    /**
     * @return The interpolator of the value animator
     */
    public Interpolator getRippleInterpolator() {
        return rippleInterpolator;
    }

    /**
     * @return True if random color for each ripple is enabled
     */
    public boolean isEnableRandomColor() {
        return enableRandomColor;
    }

    /**
     * @return The shape renderer for the shape ripples
     */
    public BaseShapeRipple getRippleShape() {
        return rippleShape;
    }

    /**
     * @return The list of developer predefined random colors
     */
    public List<Integer> getRippleRandomColors() {
        return rippleRandomColors;
    }

    /**
     * Change the ripple interval for each ripple.
     *
     * Value must be between 0f - 2f.
     *
     * @param rippleInterval The floating ripple interval for each ripple
     */
    public void setRippleInterval(float rippleInterval) {

        if (rippleInterval > 2f || rippleInterval <= 0) {
            throw new IllegalArgumentException("Ripple Interval must be <= 2f and > 0");
        }

        this.rippleIntervalFactor = rippleInterval;
        this.rippleInterval = (this.rippleIntervalCalculated * rippleInterval);
    }

    /**
     * Enables the color transition for each ripple
     *
     * @param enableColorTransition flag for enabling color trasition
     */
    public void setEnableColorTransition(boolean enableColorTransition) {
        this.enableColorTransition = enableColorTransition;
    }

    /**
     * Enables the single ripple rendering
     *
     * @param enableSingleRipple flag for enabling single ripple
     */
    public void setEnableSingleRipple(boolean enableSingleRipple) {
        this.enableSingleRipple = enableSingleRipple;

        initializeEntries(rippleShape);
    }

    /**
     * Change the stroke width for each ripple
     *
     * @param rippleStrokeWidth The stroke width in pixel
     */
    public void setRippleStrokeWidth(int rippleStrokeWidth) {

        if (rippleStrokeWidth <= 0) {
            throw new IllegalArgumentException("Ripple duration must be > 0");
        }

        this.rippleStrokeWidth = rippleStrokeWidth;
    }

    /**
     * Change the base color of each ripple
     *
     * @param rippleColor The ripple color
     */
    public void setRippleColor(int rippleColor) {
        setRippleColor(rippleColor, true);
    }

    /**
     * Change the base color of each ripple
     *
     * @param rippleColor The ripple color
     * @param instant flag for when changing color is instant without delay
     */
    public void setRippleColor(int rippleColor, boolean instant) {
        this.rippleColor = rippleColor;

        if (instant) {
            reconfigureEntries();
        }
    }

    /**
     * Change the starting color of the color transition
     *
     * @param rippleFromColor The starting color
     */
    public void setRippleFromColor(int rippleFromColor) {
        setRippleFromColor(rippleFromColor, true);
    }

    /**
     * Change the starting color of the color transition
     *
     * @param rippleFromColor The starting color
     * @param instant flag for when changing color is instant without delay
     */
    public void setRippleFromColor(int rippleFromColor, boolean instant) {
        this.rippleFromColor = rippleFromColor;

        if (instant) {
            reconfigureEntries();
        }
    }

    /**
     * Change the end color of the color transition
     *
     * @param rippleToColor The end color
     */
    public void setRippleToColor(int rippleToColor) {
        setRippleToColor(rippleToColor, true);
    }

    /**
     * Change the end color of the color transition
     *
     * @param rippleToColor The end color
     * @param instant flag for when changing color is instant without delay
     */
    public void setRippleToColor(int rippleToColor, boolean instant) {
        this.rippleToColor = rippleToColor;

        if (instant) {
            reconfigureEntries();
        }
    }

    /**
     * Change the ripple duration of the animator
     *
     * @param millis The duration in milliseconds
     */
    public void setRippleDuration(int millis) {

        if (rippleDuration <= 0) {
            throw new IllegalArgumentException("Ripple duration must be > 0");
        }

        this.rippleDuration = millis;

        // We set the duration here this will auto change the animator
        if (rippleValueAnimator != null) {
            rippleValueAnimator.setDuration(rippleDuration);
        }
    }

    /**
     * Enables the random positioning of ripples
     *
     * @param enableRandomPosition flag for enabling random position
     */
    public void setEnableRandomPosition(boolean enableRandomPosition) {
        this.enableRandomPosition = enableRandomPosition;

        initializeEntries(rippleShape);
    }

    /**
     * Change the {@link Interpolator} of the animator
     *
     * @param rippleInterpolator The interpolator
     */
    public void setRippleInterpolator(Interpolator rippleInterpolator) {

        if (rippleInterpolator == null) {
            throw new NullPointerException("Ripple interpolator in null");
        }

        this.rippleInterpolator = rippleInterpolator;
    }

    /**
     * Enables the random coloring of each ripple
     *
     * @param enableRandomColor flag for enabling random color
     */
    public void setEnableRandomColor(boolean enableRandomColor) {
        this.enableRandomColor = enableRandomColor;

        reconfigureEntries();
    }

    /**
     * Change the shape renderer of the ripples
     *
     * @param rippleShape The renderer of shapes ripple
     */
    public void setRippleShape(BaseShapeRipple rippleShape) {
        this.rippleShape = rippleShape;

        reconfigureEntries();
    }

    /**
     * Change the developer predefined random colors
     *
     * @param rippleRandomColors The list of colors
     */
    public void setRippleRandomColors(List<Integer> rippleRandomColors) {

        if (rippleRandomColors == null) {
            throw new NullPointerException("List of colors cannot be null");
        }

        if (rippleRandomColors.size() == 0) {
            throw new IllegalArgumentException("List of color cannot be empty");
        }

        // We clear the list of colors before adding new colors
        this.rippleRandomColors.clear();

        this.rippleRandomColors = rippleRandomColors;

        reconfigureEntries();
    }
}
