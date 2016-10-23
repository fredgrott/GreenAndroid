package com.github.shareme.greenandroid.androidsupportpreferenceapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.XpPreferenceFragment;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import net.xpece.android.support.preference.ColorPreference;
import net.xpece.android.support.preference.EditTextPreference;
import net.xpece.android.support.preference.ListPreference;
import net.xpece.android.support.preference.LongClickablePreference;
import net.xpece.android.support.preference.MultiSelectListPreference;
import net.xpece.android.support.preference.OnPreferenceLongClickListener;
import net.xpece.android.support.preference.PreferenceCategory;
import net.xpece.android.support.preference.PreferenceDividerDecoration;
import net.xpece.android.support.preference.PreferenceIconHelper;
import net.xpece.android.support.preference.PreferenceScreenNavigationStrategy;
import net.xpece.android.support.preference.RingtonePreference;
import net.xpece.android.support.preference.SeekBarPreference;
import net.xpece.android.support.preference.SharedPreferencesCompat;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eugen on 7. 12. 2015.
 */
public class SettingsFragment extends XpPreferenceFragment implements ICanPressBack,
    PreferenceScreenNavigationStrategy.ReplaceRoot.Callbacks {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    // These are used to navigate back and forth between subscreens. Use with ReplaceRoot strategy.
//    private PreferenceScreenNavigationStrategy.ReplaceRoot mPreferenceScreenNavigation;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof SeekBarPreference) {
                SeekBarPreference pref = (SeekBarPreference) preference;
                int progress = (int) value;
                pref.setInfo(progress + "%");
            } else if (preference instanceof ColorPreference) {
                ColorPreference colorPreference = (ColorPreference) preference;
                int color = (int) value;
//                String colorString = String.format("#%06X", 0xFFFFFF & color);
//                preference.setSummary(colorString);
                int index = colorPreference.findIndexOfValue(color);
                if (index < 0) {
                    preference.setSummary(null);
                } else {
                    final CharSequence name = colorPreference.getNameForColor(color);
                    preference.setSummary(name);
                }
            } else if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                    index >= 0
                        ? listPreference.getEntries()[index]
                        : null);
            } else if (preference instanceof MultiSelectListPreference) {
                String summary = stringValue.trim().substring(1, stringValue.length() - 1); // strip []
                preference.setSummary(summary);
            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                        preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    public static SettingsFragment newInstance(String rootKey) {
        Bundle args = new Bundle();
        args.putString(SettingsFragment.ARG_PREFERENCE_ROOT, rootKey);
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String[] getCustomDefaultPackages() {
        return new String[]{BuildConfig.APPLICATION_ID};
    }

    @Override
    public void onCreatePreferences2(final Bundle savedInstanceState, final String rootKey) {
        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        // Manually tint PreferenceScreen icon.
        Preference subs = findPreference("subs_screen");
        PreferenceIconHelper subsHelper = PreferenceIconHelper.setup(subs,
            R.drawable.ic_inbox_black_24dp,
            Util.resolveResourceId(subs.getContext(), R.attr.asp_preferenceIconTint, R.color.accent),
            true);

        // Add 'notifications' preferences, and a corresponding header.
        PreferenceCategory fakeHeader = new PreferenceCategory(getPreferenceManager().getContext());
        fakeHeader.setTitle(R.string.pref_header_notifications);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_notification);

        // Add 'data and sync' preferences, and a corresponding header.
        fakeHeader = new PreferenceCategory(getPreferenceManager().getContext());
        fakeHeader.setTitle(R.string.pref_header_data_sync);
        fakeHeader.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Button);
        fakeHeader.setTitleTextColor(ContextCompat.getColor(fakeHeader.getContext(), R.color.primary)); // No disabled color state please.
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_data_sync);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference("example_text"));
        bindPreferenceSummaryToValue(findPreference("example_list"));
        bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        bindPreferenceSummaryToValue(findPreference("notif_content"));
        bindPreferenceSummaryToValue(findPreference("notif_color"));

        // Setup SeekBarPreference "info" text field.
        final SeekBarPreference volume2 = (SeekBarPreference) findPreference("notifications_new_message_volume2");
        volume2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume2.setInfo(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Setup EditTextPreference input field.
        ((EditTextPreference) findPreference("example_text")).setOnEditTextCreatedListener(new EditTextPreference.OnEditTextCreatedListener() {
            @Override
            public void onEditTextCreated(EditText edit) {
                Context context = edit.getContext();
                Drawable d = AppCompatDrawableManager.get().getDrawable(context, R.drawable.ic_create_black_24dp);
                d = DrawableCompat.wrap(d);
                DrawableCompat.setTintList(d, Util.resolveColorStateList(context, R.attr.colorControlNormal));
                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(edit, null, null, d, null);

                // These are inflated from XML. Undocumented API.
//                edit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
//                edit.setSingleLine(true);
//                edit.setSelectAllOnFocus(true);
            }
        });

        ((LongClickablePreference) findPreference("example_text")).setOnPreferenceLongClickListener(new OnPreferenceLongClickListener() {
            @Override
            public <T extends Preference & LongClickablePreference> boolean onLongClick(T preference, View view) {
                final Toast toast = Toast.makeText(getContext(), "This showcases long click listeners on preferences.", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });

        // Setup root preference title.
//        getPreferenceScreen().setTitle(R.string.app_name);
        getPreferenceScreen().setTitle(getActivity().getTitle());

        // Setup root preference.

        // Use with ReplaceRoot strategy.
//        mPreferenceScreenNavigation = new PreferenceScreenNavigationStrategy.ReplaceRoot(this, this);
//        mPreferenceScreenNavigation.onCreatePreferences(savedInstanceState);

        // Use with ReplaceFragment strategy.
        PreferenceScreenNavigationStrategy.ReplaceFragment.onCreatePreferences(this, rootKey);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        mPreferenceScreenNavigation.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Change activity title to preference title. Used with ReplaceFragment strategy.
        getActivity().setTitle(getPreferenceScreen().getTitle());
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        final String key = preference.getKey();
        if (preference instanceof MultiSelectListPreference) {
            Set<String> summary = SharedPreferencesCompat.getStringSet(
                PreferenceManager.getDefaultSharedPreferences(preference.getContext()),
                key,
                new HashSet<String>());
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, summary);
        } else if (preference instanceof ColorPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, ((ColorPreference) preference).getColor());
        } else if (preference instanceof SeekBarPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, ((SeekBarPreference) preference).getProgress());
        } else {
            String value = PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(key, "");
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, value);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView listView = getListView();

        // We're using alternative divider.
        listView.addItemDecoration(new PreferenceDividerDecoration(getContext()).drawBottom(true).drawBetweenCategories(false));
        setDivider(null);

        // We don't want this. The children are still focusable.
        listView.setFocusable(false);
    }

    // Here follows ReplaceRoot strategy stuff. ====================================================

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
//        if (preference instanceof PreferenceScreen) {
//            PreferenceScreen preferenceScreen = (PreferenceScreen) preference;
//            mPreferenceScreenNavigation.onPreferenceScreenClick(preferenceScreen);
//            return true;
//        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onBackPressed() {
//        if (mPreferenceScreenNavigation.onBackPressed()) {
//            return true;
//        }
        return false;
    }

    @Override
    public void onNavigateToPreferenceScreen(PreferenceScreen preferenceScreen) {
        getActivity().setTitle(preferenceScreen.getTitle());
    }
}
