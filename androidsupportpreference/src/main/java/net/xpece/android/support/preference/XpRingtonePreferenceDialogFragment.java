package net.xpece.android.support.preference;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.github.shareme.greenandroid.androidsupportpreference.BuildConfig;
import com.github.shareme.greenandroid.androidsupportpreference.R;

import java.util.ArrayList;

/**
 * Created by Eugen on 07.12.2015.
 */
public class XpRingtonePreferenceDialogFragment extends XpPreferenceDialogFragment
    implements Runnable, AdapterView.OnItemSelectedListener {

    private static int RC_FALLBACK_RINGTONE_PICKER = 0xff00; // <0; 0xffff>

    private static String KEY_FALLBACK_RINGTONE_PICKER = BuildConfig.APPLICATION_ID + ".FALLBACK_RINGTONE_PICKER";

    private static final int POS_UNKNOWN = -1;

    private static final int DELAY_MS_SELECTION_PLAYED = 300;

    private static final String SAVE_CLICKED_POS = "clicked_pos";

    private RingtoneManager mRingtoneManager;
    private int mType;

    private Cursor mCursor;
    private Handler mHandler;

    /** The position in the list of the 'Silent' item. */
    private int mSilentPos = POS_UNKNOWN;

    /** The position in the list of the 'Default' item. */
    private int mDefaultRingtonePos = POS_UNKNOWN;

    /** The position in the list of the last clicked item. */
    int mClickedPos = POS_UNKNOWN;

    /** The position in the list of the ringtone to sample. */
    private int mSampleRingtonePos = POS_UNKNOWN;

    /** Whether this list has the 'Silent' item. */
    private boolean mHasSilentItem;

    /** The Uri to place a checkmark next to. */
    private Uri mExistingUri;

    /** The number of static items in the list. */
    private final ArrayList<XpHeaderViewListAdapter.FixedViewInfo> mStaticItems = new ArrayList<>();

    /** Whether this list has the 'Default' item. */
    private boolean mHasDefaultItem;

    /** The Uri to play when the 'Default' item is clicked. */
    private Uri mUriForDefaultItem;

    /**
     * A Ringtone for the default ringtone. In most cases, the RingtoneManager
     * will stop the previous ringtone. However, the RingtoneManager doesn't
     * manage the default ringtone for us, so we should stop this one manually.
     */
    private Ringtone mDefaultRingtone;

    /**
     * The ringtone that's currently playing, unless the currently playing one is the default
     * ringtone.
     */
    private Ringtone mCurrentRingtone;

    /**
     * Keep the currently playing ringtone around when changing orientation, so that it
     * can be stopped later, after the activity is recreated.
     */
    private static Ringtone sPlayingRingtone;

    private final DialogInterface.OnClickListener mRingtoneClickListener =
        new DialogInterface.OnClickListener() {

            /*
             * On item clicked
             */
            public void onClick(DialogInterface dialog, int which) {
                // Save the position of most recently clicked item
                mClickedPos = which;

                // Play clip
                playRingtone(which, 0);
            }

        };

    public static XpRingtonePreferenceDialogFragment newInstance(String key) {
        XpRingtonePreferenceDialogFragment fragment = new XpRingtonePreferenceDialogFragment();
        Bundle b = new Bundle(1);
        b.putString("key", key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        // Give the Activity so it can do managed queries
        mRingtoneManager = new RingtoneManagerCompat(getActivity());

        final boolean fallbackRingtonePicker;
        if (savedInstanceState != null) {
            mClickedPos = savedInstanceState.getInt(SAVE_CLICKED_POS, POS_UNKNOWN);
            fallbackRingtonePicker = savedInstanceState.getBoolean(KEY_FALLBACK_RINGTONE_PICKER);
        } else {
            fallbackRingtonePicker = false;
        }

        if (fallbackRingtonePicker) {
            setShowsDialog(false);
        } else {
            RingtonePreference preference = getRingtonePreference();

        /*
         * Get whether to show the 'Default' item, and the URI to play when the
         * default is clicked
         */
            mHasDefaultItem = preference.getShowDefault();
            mUriForDefaultItem = RingtoneManager.getDefaultUri(preference.getRingtoneType());

            // Get whether to show the 'Silent' item
            mHasSilentItem = preference.getShowSilent();

            // Get the types of ringtones to show
            mType = preference.getRingtoneType();
            if (mType != -1) {
                mRingtoneManager.setType(mType);
            }

            // Get the URI whose list item should have a checkmark
            mExistingUri = preference.onRestoreRingtone();

            mCursor = mRingtoneManager.getCursor();

            try {
                // Check if cursor is valid.
                mCursor.getColumnNames();
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
                mCursor = null;
                try {
                    // Alternatively try starting system picker.
                    Intent i = buildRingtonePickerIntent(preference);
                    startActivityForResult(i, RC_FALLBACK_RINGTONE_PICKER);
                    setShowsDialog(false);
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                    // If everything fails show empty list.
                }
            }
        }
    }

    @NonNull
    private Intent buildRingtonePickerIntent(RingtonePreference pref) {
        Intent i = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        i.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, mExistingUri);
        i.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, mUriForDefaultItem);
        i.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, mHasDefaultItem);
        i.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, mHasSilentItem);
        i.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, mType);
        i.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, pref.getNonEmptyDialogTitle());
        return i;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_FALLBACK_RINGTONE_PICKER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                onRingtoneSelected(uri);
            }
            dismiss();
        }
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        RingtonePreference preference = getRingtonePreference();

        // The volume keys will control the stream that we are choosing a ringtone for
        getActivity().setVolumeControlStream(mRingtoneManager.inferStreamType());

        CharSequence title = preference.getNonEmptyDialogTitle();
        builder.setTitle(title);

        final Context context = builder.getContext();
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.AlertDialog, R.attr.alertDialogStyle, 0);
        @SuppressLint("PrivateResource") int singleChoiceItemLayout = a.getResourceId(R.styleable.AlertDialog_singleChoiceItemLayout, 0);
        a.recycle();

        final LayoutInflater inflater = LayoutInflater.from(context);
        if (mHasDefaultItem) {
            mDefaultRingtonePos = addDefaultRingtoneItem(inflater, singleChoiceItemLayout);

            if (mClickedPos == POS_UNKNOWN && RingtoneManager.isDefault(mExistingUri)) {
                mClickedPos = mDefaultRingtonePos;
            }
        }
        if (mHasSilentItem) {
            mSilentPos = addSilentItem(inflater, singleChoiceItemLayout);

            // The 'Silent' item should use a null Uri
            if (mClickedPos == POS_UNKNOWN && mExistingUri == null) {
                mClickedPos = mSilentPos;
            }
        }

        if (mClickedPos == POS_UNKNOWN) {
            mClickedPos = getListPosition(mRingtoneManager.getRingtonePosition(mExistingUri));
        }

        SimpleCursorAdapter ringtoneAdapter = new SimpleCursorAdapter(getContext(), singleChoiceItemLayout, mCursor,
            new String[]{MediaStore.Audio.Media.TITLE}, new int[]{android.R.id.text1});

        XpHeaderViewListAdapter adapter = new XpHeaderViewListAdapter(mStaticItems, null, ringtoneAdapter);

        // Put a checkmark next to an item.
        builder.setSingleChoiceItems(adapter, mClickedPos, mRingtoneClickListener);

        builder.setOnItemSelectedListener(this);
    }

    /**
     * Adds a static item to the top of the list. A static item is one that is not from the
     * RingtoneManager.
     *
     * @param text Text for the item.
     * @return The position of the inserted item.
     */
    private int addStaticItem(LayoutInflater inflater, @LayoutRes int layout, CharSequence text) {
        TextView textView = (TextView) inflater.inflate(layout, null, false);
        textView.setText(text);

        XpHeaderViewListAdapter.FixedViewInfo item = new XpHeaderViewListAdapter.FixedViewInfo();
        item.view = textView;
        item.isSelectable = true;

        mStaticItems.add(item);
        return mStaticItems.size() - 1;
    }

    private int addDefaultRingtoneItem(LayoutInflater inflater, @LayoutRes int layout) {
        if (mType == RingtoneManager.TYPE_NOTIFICATION) {
            return addStaticItem(inflater, layout, RingtonePreference.getNotificationSoundDefaultString(getContext()));
        } else if (mType == RingtoneManager.TYPE_ALARM) {
            return addStaticItem(inflater, layout, RingtonePreference.getAlarmSoundDefaultString(getContext()));
        }

        return addStaticItem(inflater, layout, RingtonePreference.getRingtoneDefaultString(getContext()));
    }

    private int addSilentItem(LayoutInflater inflater, @LayoutRes int layout) {
        return addStaticItem(inflater, layout, RingtonePreference.getRingtoneSilentString(getContext()));
    }

    private int getListPosition(int ringtoneManagerPos) {

        // If the manager position is -1 (for not found), return that
        if (ringtoneManagerPos < 0) return ringtoneManagerPos;

        return ringtoneManagerPos + mStaticItems.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        playRingtone(position, DELAY_MS_SELECTION_PLAYED);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < 11 || !getActivity().isChangingConfigurations()) {
            stopAnyPlayingRingtone();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT < 11 || !getActivity().isChangingConfigurations()) {
            stopAnyPlayingRingtone();
        } else {
            saveAnyPlayingRingtone();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_CLICKED_POS, mClickedPos);
        outState.putBoolean(KEY_FALLBACK_RINGTONE_PICKER, !getShowsDialog());
    }

    public RingtonePreference getRingtonePreference() {
        return (RingtonePreference) getPreference();
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        // Stop playing the previous ringtone
        if (sPlayingRingtone == null) {
            mRingtoneManager.stopPreviousRingtone();
        }

        // The volume keys will control the default stream
        if (getActivity() != null) {
            getActivity().setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        if (positiveResult) {
            Uri uri;
            if (mClickedPos == mDefaultRingtonePos) {
                // Set it to the default Uri that they originally gave us
                uri = mUriForDefaultItem;
            } else if (mClickedPos == mSilentPos) {
                // A null Uri is for the 'Silent' item
                uri = null;
            } else {
                uri = mRingtoneManager.getRingtoneUri(getRingtoneManagerPosition(mClickedPos));
            }

            onRingtoneSelected(uri);
        }

    }

    private void onRingtoneSelected(Uri uri) {
        RingtonePreference preference = getRingtonePreference();
        if (preference.callChangeListener(uri != null ? uri.toString() : "")) {
            preference.onSaveRingtone(uri);
        }
    }

    void playRingtone(int position, int delayMs) {
        mHandler.removeCallbacks(this);
        mSampleRingtonePos = position;
        mHandler.postDelayed(this, delayMs);
    }

    public void run() {
        stopAnyPlayingRingtone();
        if (mSampleRingtonePos == mSilentPos) {
            return;
        }

        Ringtone ringtone;
        if (mSampleRingtonePos == mDefaultRingtonePos) {
            if (mDefaultRingtone == null) {
                mDefaultRingtone = RingtoneManager.getRingtone(getContext(), mUriForDefaultItem);
            }
           /*
            * Stream type of mDefaultRingtone is not set explicitly here.
            * It should be set in accordance with mRingtoneManager of this Activity.
            */
            if (mDefaultRingtone != null) {
                mDefaultRingtone.setStreamType(mRingtoneManager.inferStreamType());
            }
            ringtone = mDefaultRingtone;
            mCurrentRingtone = null;
        } else {
            ringtone = mRingtoneManager.getRingtone(getRingtoneManagerPosition(mSampleRingtonePos));
            mCurrentRingtone = ringtone;
        }

        if (ringtone != null) {
            ringtone.play();
        }
    }

    private void saveAnyPlayingRingtone() {
        if (mDefaultRingtone != null && mDefaultRingtone.isPlaying()) {
            sPlayingRingtone = mDefaultRingtone;
        } else if (mCurrentRingtone != null && mCurrentRingtone.isPlaying()) {
            sPlayingRingtone = mCurrentRingtone;
        }
    }

    private void stopAnyPlayingRingtone() {
        if (sPlayingRingtone != null && sPlayingRingtone.isPlaying()) {
            sPlayingRingtone.stop();
        }
        sPlayingRingtone = null;

        if (mDefaultRingtone != null && mDefaultRingtone.isPlaying()) {
            mDefaultRingtone.stop();
        }

        if (mRingtoneManager != null) {
            mRingtoneManager.stopPreviousRingtone();
        }
    }

    private int getRingtoneManagerPosition(int listPos) {
        return listPos - mStaticItems.size();
    }
}
