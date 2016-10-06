/*
Copyright 2016 Xpece
Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)

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
package android.support.v7.preference;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import net.xpece.android.support.preference.EditTextPreference;
import net.xpece.android.support.preference.ListPreference;
import net.xpece.android.support.preference.MultiSelectListPreference;
import net.xpece.android.support.preference.RingtonePreference;
import net.xpece.android.support.preference.SeekBarDialogPreference;
import net.xpece.android.support.preference.XpEditTextPreferenceDialogFragment;
import net.xpece.android.support.preference.XpListPreferenceDialogFragment;
import net.xpece.android.support.preference.XpMultiSelectListPreferenceDialogFragment;
import net.xpece.android.support.preference.XpRingtonePreferenceDialogFragment;
import net.xpece.android.support.preference.XpSeekBarPreferenceDialogFragment;

import java.lang.reflect.Field;

/**
 * @author Eugen on 6. 12. 2015.
 */
public abstract class XpPreferenceFragment extends PreferenceFragmentCompat {
    private static final String TAG = XpPreferenceFragment.class.getSimpleName();

    public final String DIALOG_FRAGMENT_TAG = "android.support.v7.preference.PreferenceFragment.DIALOG";

    private static final Field FIELD_PREFERENCE_MANAGER;

    static {
        Field preferenceManager = null;
        try {
            preferenceManager = PreferenceFragmentCompat.class.getDeclaredField("mPreferenceManager");
            preferenceManager.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        FIELD_PREFERENCE_MANAGER = preferenceManager;
    }

    private void setPreferenceManager(PreferenceManager manager) {
        try {
            FIELD_PREFERENCE_MANAGER.set(this, manager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void onCreatePreferences(final Bundle bundle, final String s) {
        onCreatePreferences1();
        onCreatePreferences2(bundle, s);
    }

    void onCreatePreferences1() {
        // Clear the original Preference Manager
        PreferenceManager manager = getPreferenceManager();
        manager.setOnNavigateToScreenListener(null);

        // Setup custom Preference Manager
        manager = new XpPreferenceManager(getStyledContext(), getCustomDefaultPackages());
        setPreferenceManager(manager);
        manager.setOnNavigateToScreenListener(this);
    }

    public String[] getCustomDefaultPackages() {
        return null;
    }

    public abstract void onCreatePreferences2(final Bundle savedInstanceState, final String rootKey);

    private Context getStyledContext() {
        return getPreferenceManager().getContext();
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        boolean handled = false;

        // This has to be done first. Doubled call in super :(
        if (this.getCallbackFragment() instanceof OnPreferenceDisplayDialogCallback) {
            handled = ((OnPreferenceDisplayDialogCallback) this.getCallbackFragment()).onPreferenceDisplayDialog(this, preference);
        }
        if (!handled && this.getActivity() instanceof OnPreferenceDisplayDialogCallback) {
            handled = ((OnPreferenceDisplayDialogCallback) this.getActivity()).onPreferenceDisplayDialog(this, preference);
        }

        // Handling custom preferences.
        if (!handled) {
            if (this.getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) == null) {
                DialogFragment f;
                if (preference instanceof EditTextPreference) {
                    f = XpEditTextPreferenceDialogFragment.newInstance(preference.getKey());
                } else if (preference instanceof ListPreference) {
                    f = XpListPreferenceDialogFragment.newInstance(preference.getKey());
                } else if (preference instanceof MultiSelectListPreference) {
                    f = XpMultiSelectListPreferenceDialogFragment.newInstance(preference.getKey());
                } else if (preference instanceof SeekBarDialogPreference) {
                    f = XpSeekBarPreferenceDialogFragment.newInstance(preference.getKey());
                } else if (preference instanceof RingtonePreference) {
                    f = XpRingtonePreferenceDialogFragment.newInstance(preference.getKey());
                } else {
                    super.onDisplayPreferenceDialog(preference);
                    return;
                }

                f.setTargetFragment(this, 0);
                f.show(this.getFragmentManager(), DIALOG_FRAGMENT_TAG);
            }
        }
    }

    @Override
    protected RecyclerView.Adapter onCreateAdapter(final PreferenceScreen preferenceScreen) {
        return new XpPreferenceGroupAdapter(preferenceScreen);
    }

    @Override
    public Fragment getCallbackFragment() {
        return this;
    }
}
