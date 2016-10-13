/*
 Copyright 2016 Kofi Gyan.
 Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   `http://www.apache.org/licenses/LICENSE-2.0`

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */
package com.github.shareme.greenandroid.stateprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

import com.github.shareme.greenandroid.stateprogressbar.utils.FontManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kofi Gyan on 4/19/2016.
 */
@SuppressWarnings("unused")
public class StateProgressBar extends View {


    public enum StateNumber {
        ONE(1), TWO(2), THREE(3), FOUR(4);
        private int value;

        StateNumber(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    private ArrayList<String> mStateDescriptionData = new ArrayList<String>();


    private float mStateRadius;
    private float mStateSize;
    private float mStateLineThickness;
    private float mStateNumberTextSize;
    private float mStateDescriptionSize;

    /**
     * width of one cell = stageWidth/noOfStates
     */
    private float mCellWidth;


    private float mCellHeight;


    /**
     * next cell(state) from previous cell
     */
    private float mNextCellWidth;

    /**
     * center of first cell(state)
     */
    private float mStartCenterX;


    /**
     * center of last cell(state)
     */
    private float mEndCenterX;


    /**
     * width of screen
     */
    private int mStageWidth;

    private int mMaxStateNumber;
    private int mCurrentStateNumber;

    private int mAnimStartDelay;
    private int mAnimDuration;

    private float mSpacing;

    private float mDescTopSpaceDecrementer;
    private float mDescTopSpaceIncrementer;


    private final static float DEFAULT_TEXT_SIZE = 15f;
    private final static float DEFAULT_STATE_SIZE = 25f;


    /**
     * Paints for drawing
     */
    private Paint mStateNumberForegroundPaint;
    private Paint mStateCheckedForegroundPaint;
    private Paint mStateNumberBackgroundPaint;
    private Paint mBackgroundPaint;
    private Paint mForegroundPaint;
    private Paint mCurrentStateDescriptionPaint;
    private Paint mStateDescriptionPaint;


    private int mBackgroundColor;
    private int mForegroundColor;
    private int mStateNumberBackgroundColor;
    private int mStateNumberForegroundColor;
    private int mCurrentStateDescriptionColor;
    private int mStateDescriptionColor;


    /**
     * animate inner line to current progress state
     */
    private Animator mAnimator;


    /**
     * tracks progress of line animator
     */
    private float mAnimStartXPos;
    private float mAnimEndXPos;


    private boolean mIsCurrentAnimStarted;

    private boolean mAnimateToCurrentProgressState;
    private boolean mEnableAllStatesCompleted;
    private boolean mCheckStateCompleted;

    private boolean mIsStateSizeSet;
    private boolean mIsStateTextSizeSet;


    private Typeface mCheckFont;


    public StateProgressBar(Context context) {
        this(context, null, 0);
    }


    public StateProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public StateProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
        initializePainters();
        updateCheckAllStatesValues(mEnableAllStatesCompleted);

    }


    private void init(Context context, AttributeSet attrs, int defStyle) {

        /**
         * Setting default values.
         */
        initStateProgressBar(context);

        mStateDescriptionSize = convertSpToPixel(mStateDescriptionSize);
        mStateLineThickness = convertDpToPixel(mStateLineThickness);
        mSpacing = convertDpToPixel(mSpacing);
        mCheckFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);


        if (attrs != null) {

            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateProgressBar, defStyle, 0);

            mBackgroundColor = a.getColor(R.styleable.StateProgressBar_spb_stateBackgroundColor, mBackgroundColor);
            mForegroundColor = a.getColor(R.styleable.StateProgressBar_spb_stateForegroundColor, mForegroundColor);
            mStateNumberBackgroundColor = a.getColor(R.styleable.StateProgressBar_spb_stateNumberBackgroundColor, mStateNumberBackgroundColor);
            mStateNumberForegroundColor = a.getColor(R.styleable.StateProgressBar_spb_stateNumberForegroundColor, mStateNumberForegroundColor);
            mCurrentStateDescriptionColor = a.getColor(R.styleable.StateProgressBar_spb_currentStateDescriptionColor, mCurrentStateDescriptionColor);
            mStateDescriptionColor = a.getColor(R.styleable.StateProgressBar_spb_stateDescriptionColor, mStateDescriptionColor);

            mCurrentStateNumber = a.getInteger(R.styleable.StateProgressBar_spb_currentStateNumber, mCurrentStateNumber);
            mMaxStateNumber = a.getInteger(R.styleable.StateProgressBar_spb_maxStateNumber, mMaxStateNumber);

            mStateSize = a.getDimension(R.styleable.StateProgressBar_spb_stateSize, mStateSize);
            mStateNumberTextSize = a.getDimension(R.styleable.StateProgressBar_spb_stateTextSize, mStateNumberTextSize);
            mStateDescriptionSize = a.getDimension(R.styleable.StateProgressBar_spb_stateDescriptionSize, mStateDescriptionSize);
            mStateLineThickness = a.getDimension(R.styleable.StateProgressBar_spb_stateLineThickness, mStateLineThickness);

            mCheckStateCompleted = a.getBoolean(R.styleable.StateProgressBar_spb_checkStateCompleted, mCheckStateCompleted);
            mAnimateToCurrentProgressState = a.getBoolean(R.styleable.StateProgressBar_spb_animateToCurrentProgressState, mAnimateToCurrentProgressState);
            mEnableAllStatesCompleted = a.getBoolean(R.styleable.StateProgressBar_spb_enableAllStatesCompleted, mEnableAllStatesCompleted);

            mDescTopSpaceDecrementer = a.getDimension(R.styleable.StateProgressBar_spb_descriptionTopSpaceDecrementer, mDescTopSpaceDecrementer);
            mDescTopSpaceIncrementer = a.getDimension(R.styleable.StateProgressBar_spb_descriptionTopSpaceIncrementer, mDescTopSpaceIncrementer);

            mAnimDuration = a.getInteger(R.styleable.StateProgressBar_spb_animationDuration, mAnimDuration);
            mAnimStartDelay = a.getInteger(R.styleable.StateProgressBar_spb_animationStartDelay, mAnimStartDelay);


            if (!mAnimateToCurrentProgressState)
                stopAnimation();

            resolveStateSize();
            validateLineThickness(mStateLineThickness);
            validateStateNumber(mCurrentStateNumber);

            mStateRadius = mStateSize / 2;

            a.recycle();

        }

    }


    private void initializePainters() {

        Typeface typefaceBold = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

        mBackgroundPaint = setPaintAttributes(mStateLineThickness, mBackgroundColor);

        mForegroundPaint = setPaintAttributes(mStateLineThickness, mForegroundColor);

        mStateNumberForegroundPaint = setPaintAttributes(mStateNumberTextSize, mStateNumberForegroundColor, typefaceBold);

        mStateCheckedForegroundPaint = setPaintAttributes(mStateNumberTextSize, mStateNumberForegroundColor, mCheckFont);

        mStateNumberBackgroundPaint = setPaintAttributes(mStateNumberTextSize, mStateNumberBackgroundColor, typefaceBold);

        mCurrentStateDescriptionPaint = setPaintAttributes(mStateDescriptionSize, mCurrentStateDescriptionColor, typefaceBold);

        mStateDescriptionPaint = setPaintAttributes(mStateDescriptionSize, mStateDescriptionColor, typefaceBold);

    }


    private void validateLineThickness(float lineThickness) {
        float halvedStateSize = mStateSize / 2;
        if (lineThickness > halvedStateSize)
            mStateLineThickness = halvedStateSize;
    }

    private void validateStateSize() {
        if (mStateSize <= mStateNumberTextSize)
            mStateSize = mStateNumberTextSize + mStateNumberTextSize / 2;
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        mBackgroundPaint.setColor(mBackgroundColor);
        invalidate();
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }


    public void setForegroundColor(int foregroundColor) {
        mForegroundColor = foregroundColor;
        mForegroundPaint.setColor(mForegroundColor);
        invalidate();
    }

    public int getForegroundColor() {
        return mForegroundColor;
    }

    public void setStateLineThickness(float stateLineThickness) {
        this.mStateLineThickness = convertDpToPixel(stateLineThickness);
        validateLineThickness(mStateLineThickness);
        mBackgroundPaint.setStrokeWidth(mStateLineThickness);
        mForegroundPaint.setStrokeWidth(mStateLineThickness);
        invalidate();
    }

    public float getStateLineThickness() {
        return mStateLineThickness;
    }


    public void setStateNumberBackgroundColor(int stateNumberBackgroundColor) {
        mStateNumberBackgroundColor = stateNumberBackgroundColor;
        mStateNumberBackgroundPaint.setColor(mStateNumberBackgroundColor);
        invalidate();
    }


    public int getStateNumberBackgroundColor() {
        return mStateNumberBackgroundColor;
    }


    public void setStateNumberForegroundColor(int stateNumberForegroundColor) {
        mStateNumberForegroundColor = stateNumberForegroundColor;
        mStateNumberForegroundPaint.setColor(mStateNumberForegroundColor);
        mStateCheckedForegroundPaint.setColor(mStateNumberForegroundColor);
        invalidate();
    }


    public int getStateNumberForegroundColor() {
        return mStateNumberForegroundColor;
    }

    public void setStateDescriptionColor(int stateDescriptionColor) {
        this.mStateDescriptionColor = stateDescriptionColor;
        mStateDescriptionPaint.setColor(mStateDescriptionColor);
        invalidate();
    }

    public int getStateDescriptionColor() {
        return mStateDescriptionColor;
    }

    public void setCurrentStateDescriptionColor(int currentStateDescriptionColor) {
        this.mCurrentStateDescriptionColor = currentStateDescriptionColor;
        mCurrentStateDescriptionPaint.setColor(mCurrentStateDescriptionColor);
        invalidate();
    }


    public int getCurrentStateDescriptionColor() {
        return mCurrentStateDescriptionColor;
    }


    public void setCurrentStateNumber(StateNumber currentStateNumber) {
        validateStateNumber(currentStateNumber.getValue());
        this.mCurrentStateNumber = currentStateNumber.getValue();
        updateCheckAllStatesValues(mEnableAllStatesCompleted);
        invalidate();
    }

    public int getCurrentStateNumber() {
        return mCurrentStateNumber;
    }


    public void setMaxStateNumber(StateNumber maximumState) {
        this.mMaxStateNumber = maximumState.getValue();
        validateStateNumber(mCurrentStateNumber);
        updateCheckAllStatesValues(mEnableAllStatesCompleted);
        invalidate();
    }

    public int getMaxStateNumber() {
        return mMaxStateNumber;
    }


    public void setStateSize(float stateSize) {
        this.mStateSize = convertDpToPixel(stateSize);
        mIsStateSizeSet = true;
        resetStateSizeValues();
    }

    public float getStateSize() {
        return mStateSize;
    }

    public void setStateNumberTextSize(float textSize) {
        this.mStateNumberTextSize = convertSpToPixel(textSize);
        mIsStateTextSizeSet = true;
        resetStateSizeValues();
    }


    private void resetStateSizeValues() {

        resolveStateSize(mIsStateSizeSet, mIsStateTextSizeSet);

        mStateNumberForegroundPaint.setTextSize(mStateNumberTextSize);
        mStateNumberBackgroundPaint.setTextSize(mStateNumberTextSize);
        mStateCheckedForegroundPaint.setTextSize(mStateNumberTextSize);

        this.mStateRadius = mStateSize / 2;

        validateLineThickness(mStateLineThickness);

        mBackgroundPaint.setStrokeWidth(mStateLineThickness);
        mForegroundPaint.setStrokeWidth(mStateLineThickness);
        requestLayout();
    }

    public void setStateDescriptionSize(float stateDescriptionSize) {
        this.mStateDescriptionSize = convertSpToPixel(stateDescriptionSize);
        mCurrentStateDescriptionPaint.setTextSize(mStateDescriptionSize);
        mStateDescriptionPaint.setTextSize(mStateDescriptionSize);
        requestLayout();
    }

    public float getStateNumberTextSize() {
        return mStateNumberTextSize;
    }

    public void checkStateCompleted(boolean checkStateCompleted) {
        this.mCheckStateCompleted = checkStateCompleted;
        invalidate();
    }


    public void setAllStatesCompleted(boolean enableAllStatesCompleted) {
        this.mEnableAllStatesCompleted = enableAllStatesCompleted;
        updateCheckAllStatesValues(mEnableAllStatesCompleted);
        invalidate();
    }

    private void updateCheckAllStatesValues(boolean enableAllStatesCompleted) {
        if (enableAllStatesCompleted) {
            this.mCheckStateCompleted = true;
            this.mCurrentStateNumber = mMaxStateNumber = StateNumber.FOUR.getValue();
            mStateDescriptionPaint.setColor(mCurrentStateDescriptionPaint.getColor());
        } else
            mStateDescriptionPaint.setColor(mStateDescriptionPaint.getColor());
    }


    public void enableAnimationToCurrentState(boolean animateToCurrentProgressState) {
        this.mAnimateToCurrentProgressState = animateToCurrentProgressState;
        if (mAnimator == null)
            startAnimator();

        invalidate();
    }

    private void validateStateNumber(int stateNumber) {
        if (stateNumber > mMaxStateNumber)
            throw new IllegalStateException("State number (" + stateNumber + ") cannot be greater than total number of states " + mMaxStateNumber);
    }


    public void setDescriptionTopSpaceIncrementer(float spaceIncrementer) {
        this.mDescTopSpaceIncrementer = spaceIncrementer;
        requestLayout();
    }


    public void setDescriptionTopSpaceDecrementer(float spaceDecrementer) {
        this.mDescTopSpaceDecrementer = spaceDecrementer;
        requestLayout();
    }

    public float getDescriptionTopSpaceDecrementer() {
        return mDescTopSpaceDecrementer;
    }

    public float getDescriptionTopSpaceIncrementer() {
        return mDescTopSpaceIncrementer;
    }


    public void setAnimationDuration(int animDuration) {
        this.mAnimDuration = animDuration;
        invalidate();
    }

    public int getAnimationDuration() {
        return mAnimDuration;
    }

    public void setAnimationStartDelay(int animStartDelay) {
        this.mAnimStartDelay = animStartDelay;
        invalidate();
    }

    public int getAnimationStartDelay() {
        return mAnimStartDelay;
    }


    private Paint setPaintAttributes(float strokeWidth, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);

        return paint;
    }

    private Paint setPaintAttributes(float textSize, int color, Typeface typeface) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        paint.setColor(color);
        paint.setTypeface(typeface);

        return paint;
    }


    private void initStateProgressBar(Context context) {

        mBackgroundColor = ContextCompat.getColor(context, R.color.background_color);
        mForegroundColor = ContextCompat.getColor(context, R.color.foreground_color);
        mStateNumberBackgroundColor = ContextCompat.getColor(context, R.color.background_text_color);
        mStateNumberForegroundColor = ContextCompat.getColor(context, R.color.foreground_text_color);
        mCurrentStateDescriptionColor = ContextCompat.getColor(context, R.color.foreground_color);
        mStateDescriptionColor = ContextCompat.getColor(context, R.color.background_text_color);

        mStateSize = 0.0f;
        mStateLineThickness = 4.0f;
        mStateNumberTextSize = 0.0f;
        mStateDescriptionSize = 15f;

        mMaxStateNumber = StateNumber.FOUR.getValue();
        mCurrentStateNumber = StateNumber.ONE.getValue();

        mSpacing = 4.0f;

        mDescTopSpaceDecrementer = 0.0f;
        mDescTopSpaceIncrementer = 0.0f;

        mCheckStateCompleted = false;
        mAnimateToCurrentProgressState = false;
        mEnableAllStatesCompleted = false;

        mAnimStartDelay = 100;
        mAnimDuration = 4000;

    }


    private void resolveStateSize() {
        if (mStateSize == 0 && mStateNumberTextSize == 0) {
            mIsStateSizeSet = false;
            mIsStateTextSizeSet = false;
            resolveStateSize(mIsStateSizeSet, mIsStateTextSizeSet);

        } else if (mStateSize != 0 && mStateNumberTextSize != 0) {
            mIsStateSizeSet = true;
            mIsStateTextSizeSet = true;
            resolveStateSize(mIsStateSizeSet, mIsStateTextSizeSet);

        } else if (mStateSize == 0 && mStateNumberTextSize != 0) {
            mIsStateSizeSet = false;
            mIsStateTextSizeSet = true;
            resolveStateSize(mIsStateSizeSet, mIsStateTextSizeSet);

        } else if (mStateSize != 0 && mStateNumberTextSize == 0) {
            mIsStateSizeSet = true;
            mIsStateTextSizeSet = false;
            resolveStateSize(mIsStateSizeSet, mIsStateTextSizeSet);
        }

    }


    private void resolveStateSize(boolean isStateSizeSet, boolean isStateTextSizeSet) {
        if (!isStateSizeSet && !isStateTextSizeSet) {
            mStateSize = convertDpToPixel(DEFAULT_STATE_SIZE);
            mStateNumberTextSize = convertSpToPixel(DEFAULT_TEXT_SIZE);

        } else if (isStateSizeSet && isStateTextSizeSet) {
            validateStateSize();

        } else if (!isStateSizeSet && isStateTextSizeSet) {
            mStateSize = mStateNumberTextSize + mStateNumberTextSize / 2;

        } else if (isStateSizeSet && !isStateTextSizeSet) {
            mStateNumberTextSize = mStateSize - (mStateSize * 0.375f);
        }

    }


    private void drawCircles(Canvas canvas, Paint paint, int noOfCircles) {
        for (int i = 0; i < noOfCircles; i++) {
            canvas.drawCircle(mNextCellWidth - (mCellWidth / 2), mCellHeight / 2, mStateRadius, paint);
            mNextCellWidth += mCellWidth;
        }

        mNextCellWidth = mCellWidth;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mStageWidth = getWidth();
        mCellWidth = mStageWidth / mMaxStateNumber;
        mNextCellWidth = mCellWidth;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawState(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getDesiredHeight();
        int width = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(width, height);

        mCellHeight = getCellHeight();

    }


    private int getDesiredHeight() {
        if (mStateDescriptionData.isEmpty())
            return (int) (2 * mStateRadius) + (int) (mSpacing);
        else
            return (int) (2 * mStateRadius) + (int) (1.3 * mStateDescriptionSize) + (int) (mSpacing) - (int) (mDescTopSpaceDecrementer) + (int) (mDescTopSpaceIncrementer); // mStageHeight = mCellHeight + ( 2 * description Text Size)
    }

    private int getCellHeight() {
        return (int) (2 * mStateRadius) + (int) (mSpacing);
    }


    private void drawState(Canvas canvas) {

        drawLines(canvas, mBackgroundPaint, mMaxStateNumber);
        drawCircles(canvas, mBackgroundPaint, mMaxStateNumber);
        drawCircles(canvas, mForegroundPaint, mCurrentStateNumber);
        drawLines(canvas, mForegroundPaint, mCurrentStateNumber - 1);

        setAnimatorStartEndCenterX();

        drawCurrentStateJoiningLine(canvas);
        drawStateNumberText(canvas, mMaxStateNumber);
        drawStateDescriptionText(canvas);

    }


    private void drawLines(Canvas canvas, Paint paint, int noOfLines) {

        float startCenterX;
        float endCenterX;

        float startX;
        float stopX;

        startCenterX = mNextCellWidth - (mCellWidth / 2);

        for (int i = 0; i < noOfLines - 1; i++) {

            mNextCellWidth += mCellWidth;
            endCenterX = mNextCellWidth - (mCellWidth / 2);

            startX = startCenterX + (mStateRadius * 0.75f);
            stopX = endCenterX - (mStateRadius * 0.75f);

            canvas.drawLine(startX, mCellHeight / 2, stopX, mCellHeight / 2, paint);

            startCenterX = endCenterX;
        }

        mNextCellWidth = mCellWidth;

    }


    private void setAnimatorStartEndCenterX() {
        if (mCurrentStateNumber > 1 && mCurrentStateNumber < 5) {
            for (int i = 0; i < mCurrentStateNumber - 1; i++) {
                if (i == 0)
                    mStartCenterX = mNextCellWidth - (mCellWidth / 2); //
                else
                    mStartCenterX = mEndCenterX;

                mNextCellWidth += mCellWidth;
                mEndCenterX = mNextCellWidth - (mCellWidth / 2);
            }
        }
    }


    private void drawCurrentStateJoiningLine(Canvas canvas) {
        if (mAnimateToCurrentProgressState)
            animateToCurrentState(canvas);
        else
            drawLineToCurrentState(canvas);
    }


    private void drawLineToCurrentState(Canvas canvas) {

        canvas.drawLine(mStartCenterX, mCellHeight / 2, mEndCenterX, mCellHeight / 2, mForegroundPaint);

        mNextCellWidth = mCellWidth;

        stopAnimation();
    }


    private void animateToCurrentState(Canvas canvas) {
        if (!mIsCurrentAnimStarted) {
            mAnimStartXPos = mStartCenterX;
            mAnimEndXPos = mAnimStartXPos;
            mIsCurrentAnimStarted = true;
        }
        if (mAnimEndXPos <= mEndCenterX) {
            canvas.drawLine(mStartCenterX, mCellHeight / 2, mAnimEndXPos, mCellHeight / 2, mForegroundPaint);
            mAnimStartXPos = mAnimEndXPos;
        } else {

            canvas.drawLine(mStartCenterX, mCellHeight / 2, mEndCenterX, mCellHeight / 2, mForegroundPaint);
        }

        mNextCellWidth = mCellWidth;
    }


    private void drawStateDescriptionText(Canvas canvas) {

        int xPos;
        int yPos;
        Paint innerPaintType;

        if (!mStateDescriptionData.isEmpty()) {

            for (int i = 0; i < mStateDescriptionData.size(); i++) {
                if (i < mMaxStateNumber) {
                    innerPaintType = selectDescriptionPaint(mCurrentStateNumber, i);
                    xPos = (int) (mNextCellWidth - (mCellWidth / 2));
                    yPos = (int) (mCellHeight + mStateDescriptionSize - mSpacing - mDescTopSpaceDecrementer + mDescTopSpaceIncrementer);//mSpacing = mStateNumberForegroundPaint.getTextSize()

                    canvas.drawText(mStateDescriptionData.get(i), xPos, yPos, innerPaintType);

                    mNextCellWidth += mCellWidth;
                }
            }

        }

        mNextCellWidth = mCellWidth;
    }


    private Paint selectDescriptionPaint(int currentState, int statePosition) {
        if (statePosition + 1 == currentState)
            return mCurrentStateDescriptionPaint;
        else
            return mStateDescriptionPaint;
    }


    public void setStateDescriptionData(String[] stateDescriptionData) {

        for (String value : stateDescriptionData)
            mStateDescriptionData.add(value);

        requestLayout();
    }

    public void setStateDescriptionData(ArrayList<String> stateDescriptionData) {
        this.mStateDescriptionData = stateDescriptionData;

        requestLayout();
    }

    public List<String> getStateDescriptionData() {
        return mStateDescriptionData;
    }


    private void drawStateNumberText(Canvas canvas, int noOfCircles) {

        int xPos;
        int yPos;
        Paint innerPaintType;
        boolean isChecked;

        for (int i = 0; i < noOfCircles; i++) {

            innerPaintType = selectPaintType(mCurrentStateNumber, i, mCheckStateCompleted);
            xPos = (int) (mNextCellWidth - (mCellWidth / 2));
            yPos = (int) ((mCellHeight / 2) - ((innerPaintType.descent() + innerPaintType.ascent()) / 2));

            isChecked = isCheckIconUsed(mCurrentStateNumber, i);

            if (mCheckStateCompleted && isChecked)
                canvas.drawText(getContext().getString(R.string.check_icon), xPos, yPos, innerPaintType);
            else
                canvas.drawText(String.valueOf(i + 1), xPos, yPos, innerPaintType);

            mNextCellWidth += mCellWidth;
        }

        mNextCellWidth = mCellWidth;
    }


    private Paint selectPaintType(int currentState, int statePosition, boolean checkStateCompleted) {

        if ((mEnableAllStatesCompleted && checkStateCompleted) || (statePosition + 1 < currentState && checkStateCompleted))
            return mStateCheckedForegroundPaint;

        else if ((statePosition + 1 == currentState) || (statePosition + 1 < currentState && !checkStateCompleted))
            return mStateNumberForegroundPaint;

        else
            return mStateNumberBackgroundPaint;

    }


    private boolean isCheckIconUsed(int currentState, int statePosition) {
        if (mEnableAllStatesCompleted || statePosition + 1 < currentState)
            return true;
        else
            return false;
    }


    private void startAnimator() {
        mAnimator = new Animator();
        mAnimator.start();
    }

    private void stopAnimation() {
        if (mAnimator != null)
            mAnimator.stop();
    }


    private class Animator implements Runnable {
        private Scroller mScroller;
        private boolean mRestartAnimation = false;

        public Animator() {
            mScroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());
        }

        public void run() {
            if (mAnimator != this)
                return;

            if (mRestartAnimation) {

                mScroller.startScroll(0, (int) mStartCenterX, 0, (int) mEndCenterX, mAnimDuration);

                mRestartAnimation = false;
            }

            boolean scrollRemains = mScroller.computeScrollOffset();

            mAnimStartXPos = mAnimEndXPos;
            mAnimEndXPos = mScroller.getCurrY();

            if (scrollRemains) {
                invalidate();
                post(this);
            } else {
                stop();
            }

        }

        public void start() {
            mRestartAnimation = true;
            postDelayed(this, mAnimStartDelay);
        }

        public void stop() {
            removeCallbacks(this);
            mAnimator = null;
        }

    }


    private float convertDpToPixel(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale;
    }

    private float convertSpToPixel(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        startAnimator();
    }


    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();

        super.onDetachedFromWindow();
    }


    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        switch (visibility) {
            case View.VISIBLE:

                startAnimator();

                break;

            default:

                startAnimator();

                break;
        }
    }


}
