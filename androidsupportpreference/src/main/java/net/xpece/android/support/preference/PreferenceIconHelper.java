package net.xpece.android.support.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.preference.Preference;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;

import com.github.shareme.greenandroid.androidsupportpreference.R;

/**
 * @author Eugen on 6. 12. 2015.
 */
public class PreferenceIconHelper {
    private static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;

    private final Preference mPreference;

    /**
     * mIconResId is overridden by mIcon, if mIcon is specified.
     */
    protected int mIconResId;
    protected Drawable mIconInternal;
    protected Drawable mIcon;

    protected TintInfo mTintInfo = null;

    protected boolean mIconTintEnabled = false;
    protected boolean mIconPaddingEnabled = false;

    public static PreferenceIconHelper setup(Preference pref, @DrawableRes int icon, @ColorRes int tint, boolean padding) {
        PreferenceIconHelper helper = new PreferenceIconHelper(pref);
        helper.setIconPaddingEnabled(padding);
        helper.setIcon(icon);
        if (tint != 0) {
            helper.setTintList(ContextCompat.getColorStateList(pref.getPreferenceManager().getContext(), tint));
            helper.setIconTintEnabled(true);
        }
        return helper;
    }

    public PreferenceIconHelper(Preference preference) {
        mPreference = preference;
    }

    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Context context = getContext();

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.Preference, defStyleAttr, defStyleRes);
        for (int i = a.getIndexCount() - 1; i >= 0; i--) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.Preference_android_icon) {
                mIconResId = a.getResourceId(attr, 0);
            } else if (attr == R.styleable.Preference_asp_tint) {
                ensureTintInfo();
                mTintInfo.mTintList = getTintList(a, attr, context);
            } else if (attr == R.styleable.Preference_asp_tintMode) {
                ensureTintInfo();
                mTintInfo.mTintMode = PorterDuff.Mode.values()[a.getInt(attr, 0)];
            } else if (attr == R.styleable.Preference_asp_tintEnabled) {
                mIconTintEnabled = a.getBoolean(attr, false);
            } else if (attr == R.styleable.Preference_asp_iconPaddingEnabled) {
                mIconPaddingEnabled = a.getBoolean(attr, false);
            }
        }
        a.recycle();

        if (mIconResId != 0) {
            setIcon(mIconResId);
        }
    }

    protected ColorStateList getTintList(TintTypedArray a, int attr, Context context) {
        ColorStateList csl = a.getColorStateList(attr);
        csl = withDisabled(csl, context);
        return csl;
    }

    protected static ColorStateList withDisabled(ColorStateList csl, Context context) {
        if (csl != null && !csl.isStateful()) {
            int color = csl.getDefaultColor();
            int disabledAplha = (int) (Util.resolveFloat(context, android.R.attr.disabledAlpha, 0.5f) * 255);
            csl = Util.withDisabled(color, disabledAplha);
        }
        return csl;
    }

    public PorterDuff.Mode getTintMode() {
        return mTintInfo != null ? mTintInfo.mTintMode : null;
    }

    public void setTintMode(PorterDuff.Mode tintMode) {
        ensureTintInfo();
        mTintInfo.mTintMode = tintMode;
        applySupportIconTint();
    }

    public ColorStateList getTintList() {
        return mTintInfo != null ? mTintInfo.mTintList : null;
    }

    public void setTintList(ColorStateList tintList) {
        ensureTintInfo();
        mTintInfo.mTintList = withDisabled(tintList, getContext());
        applySupportIconTint();
    }

    public Context getContext() {return mPreference.getContext();}

    protected void ensureTintInfo() {
        if (mTintInfo == null) {
            mTintInfo = new TintInfo();
        }
    }

    /**
     * Sets the icon for this Preference with a Drawable.
     *
     * @param icon The optional icon for this Preference.
     */
    public void setIcon(Drawable icon) {
        if ((icon == null && mIcon != null) || (icon != null && mIcon != icon)) {
            if (icon != null) {
                icon.mutate();
            }

            mIconInternal = icon;

            if (mIconPaddingEnabled) {
                icon = applyIconPadding(icon);
            }

            mIcon = icon;
            mIcon = DrawableCompat.wrap(mIcon).mutate();

            applySupportIconTint();

            onSetIcon();
        }
    }

    protected void onSetIcon() {
        mPreference.setIcon(mIcon);
    }

    /**
     * Sets the icon for this Preference with a resource ID.
     *
     * @param iconResId The icon as a resource ID.
     * @see #setIcon(Drawable)
     */
    public void setIcon(int iconResId) {
        Context context = getContext();
        Drawable d = null;
        try {
            d = AppCompatResources.getDrawable(context, iconResId);
        } catch (Exception ex) {
            try {
                d = AppCompatDrawableManager.get().getDrawable(context, iconResId);
            } catch (Exception ex2) {
                d = ContextCompat.getDrawable(context, iconResId);
            }
        }
        setIcon(d);
        mIconResId = iconResId;
    }

    /**
     * Returns the icon of this Preference.
     *
     * @return The icon.
     * @see #setIcon(Drawable)
     */
    public Drawable getIcon() {
        return mIconInternal;
    }

    public boolean isIconPaddingEnabled() {
        return mIconPaddingEnabled;
    }

    public void setIconPaddingEnabled(final boolean iconPaddingEnabled) {
        if (mIconPaddingEnabled != iconPaddingEnabled) {
            mIconPaddingEnabled = iconPaddingEnabled;

            int resId = mIconResId;
            setIcon(getIcon());
            mIconResId = resId;
        }
    }

    public boolean isIconTintEnabled() {
        return mIconTintEnabled;
    }

    public void setIconTintEnabled(final boolean iconTintEnabled) {
        if (mIconTintEnabled != iconTintEnabled) {
            mIconTintEnabled = iconTintEnabled;

            applySupportIconTint();
        }
    }

    private Drawable applyIconPadding(Drawable icon) {
        if (icon != null) {
            int padding = Util.dpToPxOffset(getContext(), 4);
//            icon = Util.addDrawablePadding(icon, padding);
            icon = XpInsetDrawable.create(icon, padding);
        }
        return icon;
    }

    private void applySupportIconTint() {
        final Drawable icon = mIcon;
        if (icon != null) {
            if (mIconTintEnabled && mTintInfo != null) {
                DrawableCompat.setTintList(icon, mTintInfo.mTintList);
                DrawableCompat.setTintMode(icon, mTintInfo.mTintMode != null ? mTintInfo.mTintMode : DEFAULT_TINT_MODE);
            } else {
                DrawableCompat.setTintList(icon, null);
            }
        }
    }
}
