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

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import net.xpece.android.support.preference.SharedPreferencesCompat;

import java.util.Set;

/**
 * Represents the basic Preference UI building
 * block displayed by a {@link PreferenceActivity} in the form of a
 * {@link ListView}. This class provides the {@link View} to be displayed in
 * the activity and associates with a {@link SharedPreferences} to
 * store/retrieve the preference data.
 * <p>
 * When specifying a preference hierarchy in XML, each element can point to a
 * subclass of {@link XpPreferenceCompat}, similar to the view hierarchy and layouts.
 * <p>
 * This class contains a {@code key} that will be used as the key into the
 * {@link SharedPreferences}. It is up to the subclass to decide how to store
 * the value.
 * <p></p>
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about building a settings UI with Preferences,
 * read the <a href="{@docRoot}guide/topics/ui/settings.html">Settings</a>
 * guide.</p>
 * </div>
 *
 * @hide
 */
public final class XpPreferenceCompat {

    public XpPreferenceCompat() {
    }

    /**
     * Attempts to persist a set of Strings to the {@link SharedPreferences}.
     * <p></p>
     * This will check if this Preference is persistent, get an editor from
     * the {@link android.preference.PreferenceManager}, put in the strings, and check if we should commit (and
     * commit if so).
     *
     * @param values The values to persist.
     * @return True if the Preference is persistent. (This is not whether the
     * value was persisted, since we may not necessarily commit if there
     * will be a batch commit later.)
     * @see #getPersistedStringSet(Preference, Set)
     */
    public static boolean persistStringSet(Preference preference, Set<String> values) {
        if (preference.shouldPersist()) {
            // Shouldn't store null
            if (values.equals(getPersistedStringSet(preference, null))) {
                // It's already there, so the same as persisting
                return true;
            }

            SharedPreferences.Editor editor = preference.getPreferenceManager().getEditor();
            SharedPreferencesCompat.putStringSet(editor, preference.getKey(), values);
            tryCommit(preference, editor);
            return true;
        }
        return false;
    }

    /**
     * Attempts to get a persisted set of Strings from the
     * {@link SharedPreferences}.
     * <p></p>
     * This will check if this Preference is persistent, get the SharedPreferences
     * from the {@link android.preference.PreferenceManager}, and get the value.
     *
     * @param defaultReturnValue The default value to return if either the
     * Preference is not persistent or the Preference is not in the
     * shared preferences.
     * @return The value from the SharedPreferences or the default return
     * value.
     * @see #persistStringSet(Preference, Set)
     */
    public static Set<String> getPersistedStringSet(Preference preference, Set<String> defaultReturnValue) {
        if (!preference.shouldPersist()) {
            return defaultReturnValue;
        }
        return SharedPreferencesCompat.getStringSet(preference.getSharedPreferences(), preference.getKey(), defaultReturnValue);
    }

    private static void tryCommit(Preference preference, @NonNull SharedPreferences.Editor editor) {
        if (preference.getPreferenceManager().shouldCommit()) {
            android.support.v4.content.SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
        }
    }
}
