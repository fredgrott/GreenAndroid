/*
 Copyright 2016 KeepSafe Inc.
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
package com.github.shareme.greenandroid.multistateanimation;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Animates a series of separate AnimationDrawables on the background of a single View.
 * The view to animate is passed in the constructor. Animations are added with addSection, or they
 * can be defined in a JSON resource and passed to fromJsonResource. To start an animation,
 * call transitionNow or queueTransition.
 *
 * @author AJ Alt
 */
@SuppressWarnings("unused")
public class MultiStateAnimation implements NotifyingAnimationDrawable.OnAnimationFinishedListener {
    public static final int DEFAULT_FRAME_DURATION = 33;
    public static final boolean DEFAULT_ONESHOT_STATUS = true;

    /**
     * Cache to prevent duplicate json reads; map of resource id -> builder.
     */
    private static final Map<Integer, Builder> mBuilderCache = new HashMap<>();

    /**
     * A class that creates an AnimationDrawable from a list of frames.
     */
    private static class AnimationDrawableLoader {
        private int mFrameDuration;
        private boolean mIsOneShot;
        private int[] mFrameIds;
        private Context mContext;

        public AnimationDrawableLoader(Context context) {
            mContext = context;
        }

        public AnimationDrawableLoader(Context context, int frameDuration, boolean isOneShot, String[] frameNames) {
            mContext = context;
            mFrameDuration = frameDuration;
            mIsOneShot = isOneShot;
            mFrameIds = new int[frameNames.length];

            for (int i = 0; i < frameNames.length; i++) {
                mFrameIds[i] = getDrawableResourceId(mContext, frameNames[i]);
            }
        }

        public AnimationDrawableLoader(Context context, int frameDuration, boolean isOneShot, int[] frameIds) {
            mContext = context;
            mFrameDuration = frameDuration;
            mIsOneShot = isOneShot;
            mFrameIds = frameIds;
        }

        public NotifyingAnimationDrawable load() {
            NotifyingAnimationDrawable d = new NotifyingAnimationDrawable();
            d.setOneShot(mIsOneShot);

            // XXX: AnimationDrawable has a bug that causes it to be unresponsive
            // if exactly one frame is added. A workaround is to add the same frame
            // twice if there's only one.
            if (mFrameIds.length == 1) {
                for (int i = 0; i < 2; i++) {
                    d.addFrame(mContext.getResources().getDrawable(mFrameIds[0]), mFrameDuration);
                }
                d.setOneShot(true);
            } else {
                for (int resid : mFrameIds) {
                    d.addFrame(mContext.getResources().getDrawable(resid), mFrameDuration);
                }
            }
            return d;
        }

        /**
         * Returns the duration of this animation.
         *
         * @return int number of milliseconds that the animation will play.
         */
        public int totalDuration() {
            return mFrameDuration * mFrameIds.length;
        }
    }

    /**
     * A class that holds loaders for a single animation section and transitions to that section.
     */
    private static class AnimationSection {
        private String mId;
        private AnimationDrawableLoader mLoader;
        private Map<String, AnimationDrawableLoader> mTransitions;

        public AnimationSection(String id, AnimationDrawableLoader loader, Map<String, AnimationDrawableLoader> transitions) {
            mId = id;
            mLoader = loader;
            mTransitions = transitions;
        }

        /**
         * @param id     The id of this section.
         * @param loader A loader for this section's primary animation.
         */
        public AnimationSection(String id, AnimationDrawableLoader loader) {
            this(id, loader, new HashMap<String, AnimationDrawableLoader>());
        }

        public String getId() {
            return mId;
        }

        /**
         * Creates the primary animation drawable for this section.
         */
        public NotifyingAnimationDrawable loadDrawable() {
            return mLoader.load();
        }

        /**
         * @param fromId The id of the section to transition from.
         * @return The transition animation for fromId if one has been added, or null.
         */
        public NotifyingAnimationDrawable getTransition(String fromId) {
            if (mTransitions.containsKey(fromId)) {
                return mTransitions.get(fromId).load();
            }
            return null;
        }

        /**
         * @param fromId The Id of the section that will be transitioned from.
         * @param loader The loader for this transition animation.
         */
        public void addTransition(String fromId, AnimationDrawableLoader loader) {
            mTransitions.put(fromId, loader);
        }

        /**
         * Calculates the total duration if the animation, including the transition.
         *
         * @param fromId If a transition exists from this id, the duration will include the transition duration.
         * @return int number of milliseconds.
         */
        public int getDuration(String fromId) {
            int total = mLoader.totalDuration();
            AnimationDrawableLoader loader = mTransitions.get(fromId);
            if (loader != null) {
                total += loader.totalDuration();
            }
            return total;
        }

        /**
         * Calculates the duration of the animation, excluding any transition.
         *
         * @return int number of milliseconds.
         */
        public int getDuration() {
            return getDuration(null);
        }
    }


    public interface AnimationSeriesListener {
        /**
         * Called when a playing animation finishes and before the drawable is replaced.
         */
        void onAnimationFinished();

        /**
         * Called after a new animation has been created, but before the animation has started.
         * The new animation can be accessed through getCurrentDrawable.
         */
        void onAnimationStarting();
    }

    private WeakReference<AnimationSeriesListener> mListener = new WeakReference<AnimationSeriesListener>(null);
    private AnimationSection mCurrentSection;

    /**
     * The id of the animation that will be started as soon as the current animation
     * finishes, or null if no animation is queued.
     */
    private String mQueuedSectionId;
    private NotifyingAnimationDrawable mCurrentDrawable;

    /**
     * The id of the previous section if a transition is currently playing, or null
     * id no transition is playing.
     */
    private String mTransitioningFromId;
    private View mView;

    /**
     * An array of resource IDs corresponding to animations that can be played.
     */
    private Map<String, AnimationSection> mSectionsById;

    /**
     * Create a new instance and automatically set animations as the background of the given view.
     *
     * @param view If not null, animations will be set as the background of this view.
     */
    public MultiStateAnimation(View view) {
        mSectionsById = new HashMap<>();
        mView = view;
    }

    /**
     * Create an instance without giving a view to hold the animations.
     * <p/>
     * Note that due to a limitation in AnimationDrawable, you must set
     * created animations as the image or background of a View in an
     * onAnimationStarting listener, otherwise the animation will not
     * advance.
     *
     * @see MultiStateAnimation.AnimationSeriesListener#onAnimationStarting()
     */
    public MultiStateAnimation() {
        this(null);
    }

    /**
     * Load the id for a drawable resource from its name
     */
    private static int getDrawableResourceId(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    /**
     * Convert a JSONArray containing only strings to a String[].
     *
     * @param jsonArray the array to convert.
     * @return a String[] with the contents of the JSONArray.
     * @throws JSONException
     */
    private static String[] jsonArrayToArray(JSONArray jsonArray) throws JSONException {
        String[] array = new String[jsonArray.length()];
        for (int i = 0; i < array.length; i++) {
            array[i] = jsonArray.getString(i);
        }
        return array;
    }

    /**
     * Convert a List of Integers to an int[]
     *
     * @param list the List to convert
     * @return an int[] with the same values as the list
     */
    private static int[] integerListToArray(List<Integer> list) {
        int[] array = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * A Builder for a transition from one section to another.
     * <p/>
     * It's possible to use the same transition between more than one set of sections.
     */
    public static class TransitionBuilder {
        private List<Integer> mFrames = new ArrayList<>();
        private int mFrameDuration = DEFAULT_FRAME_DURATION;

        /**
         * Add a frame to the transition animation.
         *
         * @param imageResource The resource id of a image drawable.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public TransitionBuilder addFrame(int imageResource) {
            mFrames.add(imageResource);
            return this;
        }

        /**
         * Set the duration that each frame in this section will play.
         *
         * @param frameDuration The number of milliseconds that each frame will be displayed.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public TransitionBuilder setFrameDuration(int frameDuration) {
            mFrameDuration = frameDuration;
            return this;
        }

        private AnimationDrawableLoader build(Context context) {
            return new AnimationDrawableLoader(context, mFrameDuration, true, integerListToArray(mFrames));
        }
    }

    /**
     * A builder for an animation section.
     */
    public static class SectionBuilder {
        private String mId;
        private List<Integer> mFrames = new ArrayList<>();
        private boolean mIsOneshot = DEFAULT_ONESHOT_STATUS;
        private int mFrameDuration = DEFAULT_FRAME_DURATION;
        private Map<String, TransitionBuilder> mTransitions = new HashMap<>();

        /**
         * A constructor that takes the ID of this section.
         *
         * @param id The ID of the section
         */
        public SectionBuilder(String id) {
            mId = id;
        }

        /**
         * Add a frame to the section animation.
         *
         * @param imageResource The resource id of a image drawable.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public SectionBuilder addFrame(int imageResource) {
            mFrames.add(imageResource);
            return this;
        }

        /**
         * Set that oneshot status of this section.
         *
         * @param isOneshot If true, the animation for this section will pause playing on the final frame.
         *                  If false, the animation for this section will loop until a new section is transitioned to.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public SectionBuilder setOneshot(boolean isOneshot) {
            mIsOneshot = isOneshot;
            return this;
        }

        /**
         * Set the duration that each frame in this section will play.
         *
         * @param frameDuration The number of milliseconds that each frame will be displayed
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public SectionBuilder setFrameDuration(int frameDuration) {
            mFrameDuration = frameDuration;
            return this;
        }

        /**
         * Add an animation that will play when transitioning to this section.
         *
         * @param fromId     A section name. When a transition is queued from the named section to this one, this transition will play.
         * @param transition The transition that will play.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public SectionBuilder addTransition(String fromId, TransitionBuilder transition) {
            mTransitions.put(fromId, transition);
            return this;
        }

        private AnimationSection build(Context context) {
            AnimationDrawableLoader loader = new AnimationDrawableLoader(context, mFrameDuration, mIsOneshot, integerListToArray(mFrames));
            AnimationSection section = new AnimationSection(mId, loader);

            for (Map.Entry<String, TransitionBuilder> entry : mTransitions.entrySet()) {
                loader = entry.getValue().build(context);
                section.addTransition(entry.getKey(), loader);
            }

            return section;
        }
    }

    /**
     * A builder for manually constructing a MultiStateAnimation.
     */
    public static class Builder {
        List<SectionBuilder> mSections = new ArrayList<>();
        View mView = null;

        /**
         * Set a view to attach this animation to.
         *
         * The background of the view will contain the animation.
         * @param view The view that will hold the animation.
         */
        public Builder(View view) {
            mView = view;
        }

        /**
         * Add an animation section.
         * @param section The section to add.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder addSection(SectionBuilder section) {
            mSections.add(section);
            return this;
        }

        /**
         * Construct a MultiStateAnimation.
         * @param context A context used to load resources.
         * @return A new MultiStateAnimation.
         */
        public MultiStateAnimation build(Context context) {
            MultiStateAnimation animation = new MultiStateAnimation(mView);
            for (SectionBuilder section : mSections) {
                animation.addSection(section.build(context));
            }
            return animation;
        }
    }

    /**
     * Creates a new MultiStateAnimation object from a json string.
     * <p/>
     * The document must have the following structure:
     * <pre>
     *  {
     *      "first_section": {
     *          "oneshot": false,
     *          "frame_duration": 33,
     *          "frames": [
     *              "frame_01",
     *              "frame_02"
     *          ],
     *
     *      }
     *      "second_section": {
     *          "oneshot": true,
     *          "frames": [
     *              "other_frame_01"
     *          ],
     *          "transitions_from": {
     *              "first_section": {
     *                  "frame_duration": 33,
     *                  "frames": [
     *                          "first_to_second_transition_001",
     *                          "first_to_second_transition_002"
     *                  ]
     *              }
     *              "": {
     *                  "frames": [
     *                      "nothing_to_second_001",
     *                      "nothing_to_second_002"
     *                  ]
     *              }
     *          }
     *      }
     *  }
     * </pre>
     * The key for each entry is the ID of the state.
     * <dl>
     * <dt>"oneshot"</dt>
     * <dd>If false, the animation will play in a loop instead of stopping at the last
     * frame.</dd>
     * <dt>"frame_duration"</dt><dd>The number of milliseconds that each frame in the "frame"
     * list will play. It defaults to 33 (30fps) if not given.</dd>
     * <dt>"frames"</dt><dd>A list of string resource ID names that must correspond to a
     * drawable resource.</dd>
     * <dt>"transitions_from"</dt><dd>Optional, and is a set of animations that play when transitioning to
     * the current state from another given state. A transition will play when the ID of the
     * current state matches the transition's key and the state is transitioning to the state
     * in which the transition is defined.</dd>
     * </dl>
     *
     * @param context The application Context.
     * @param view    If not null, animations will be set as the background of this view.
     * @param resid   The resource ID the the raw json document.
     * @return A new MultiStateAnimation.
     * @throws RuntimeException
     */
    public static MultiStateAnimation fromJsonResource(Context context, View view, int resid) {
        // Use the cached builder, if one exists.
        if (mBuilderCache.containsKey(resid)) {
            return mBuilderCache.get(resid).build(context);
        }

        // Read the resource into a string
        BufferedReader r = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resid)));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException ignored) {
            throw new RuntimeException("Cannot Read JSON sync animation Resource");
        }

        // Parse
        Builder animationBuilder = new Builder(view);
        try {
            JSONObject root = new JSONObject(stringBuilder.toString());

            // The root is a an object with keys that are sequence IDs
            for (Iterator<String> iter = root.keys(); iter.hasNext(); ) {
                String sectionId = iter.next();
                JSONObject obj = root.getJSONObject(sectionId);
                int frameDuration = obj.optInt("frame_duration", DEFAULT_FRAME_DURATION);
                boolean isOneShot = obj.optBoolean("oneshot", DEFAULT_ONESHOT_STATUS);
                JSONArray frames = obj.getJSONArray("frames");

                SectionBuilder sectionBuilder = new SectionBuilder(sectionId)
                        .setFrameDuration(frameDuration)
                        .setOneshot(isOneShot);

                for (String frame : jsonArrayToArray(frames)) {
                    sectionBuilder.addFrame(getDrawableResourceId(context, frame));
                }

                JSONObject transitions_from;
                if (obj.has("transitions_from")) {
                    transitions_from = obj.getJSONObject("transitions_from");
                } else {
                    transitions_from = new JSONObject();
                }

                // The optional "transitions" entry is another list of objects
                for (Iterator<String> transition_iter = transitions_from.keys(); transition_iter.hasNext(); ) {
                    String from = transition_iter.next();

                    JSONObject t_obj = transitions_from.getJSONObject(from);
                    frameDuration = t_obj.optInt("frame_duration", DEFAULT_FRAME_DURATION);
                    frames = t_obj.getJSONArray("frames");
                    TransitionBuilder transitionBuilder = new TransitionBuilder()
                            .setFrameDuration(frameDuration);
                    for (String frame : jsonArrayToArray(frames)) {
                        transitionBuilder.addFrame(getDrawableResourceId(context, frame));
                    }
                    sectionBuilder.addTransition(from, transitionBuilder);
                }
                animationBuilder.addSection(sectionBuilder);
            }
        } catch (JSONException ignored) {
            throw new RuntimeException("Invalid sync animation JSON file format.");
        }

        mBuilderCache.put(resid, animationBuilder);
        return animationBuilder.build(context);
    }

    /**
     * Create a MultiStateAnimation from a JSON resource without a connected View.
     *
     * @param context the Application context.
     * @param resid   The resource ID the the raw json document.
     * @return A new MultiStateAnimation instance.
     * @throws RuntimeException
     */
    public static MultiStateAnimation fromJsonResource(Context context, int resid)  {
        return MultiStateAnimation.fromJsonResource(context, null, resid);
    }

    /**
     * Add an animation section to this series.
     *
     * @param section the section to add.
     */
    private void addSection(AnimationSection section) {
        mSectionsById.put(section.getId(), section);
    }

    /**
     * Returns the registered listener, if one exists.
     */
    public AnimationSeriesListener getSeriesAnimationFinishedListener() {
        return mListener.get();
    }

    /**
     * Registers a listener that will be called when a running animation finishes. If the
     * animation is continuous, the listener will be called every time the last frame of the
     * animation is played.
     *
     * @param listener The listener to register.
     */
    public void setSeriesAnimationFinishedListener(AnimationSeriesListener listener) {
        this.mListener = new WeakReference<AnimationSeriesListener>(listener);
    }

    /**
     * Calculates the total duration of the current animation section, including the transition
     * if applicable. If the the animation is not a oneshot, the total will be for a single loop.
     *
     * @return The total animation duration, or 0 if no animation is playing.
     */
    public int currentSectionDuration() {
        if (mCurrentSection == null) return 0;
        return mCurrentSection.getDuration(mTransitioningFromId);
    }

    /**
     * Returns the currently playing animation. If no animation has played since this object was
     * created or since a call to {@link #clearAnimation()}, null is returned.
     */
    public AnimationDrawable getCurrentDrawable() {
        return mCurrentDrawable;
    }

    /**
     * Return the ID of the current section if one is playing, or null otherwise.
     */
    public String getCurrentSectionId() {
        return mCurrentSection == null ? null : mCurrentSection.getId();
    }

    /**
     * If the currently playing animation is a transition, return the ID of the
     * section that is being transitioned from. Otherwise return null.
     */
    public String getTransitioningFromId() {
        return mTransitioningFromId;
    }

    /**
     * Play an animation drawable.
     *
     * @param drawable The drawable to play.
     */
    @TargetApi(16)
    private void playDrawable(NotifyingAnimationDrawable drawable) {
        mCurrentDrawable = drawable;
        mCurrentDrawable.setAnimationFinishedListener(this);

        AnimationSeriesListener listener = mListener.get();
        if (listener != null) {
            listener.onAnimationStarting();
        }

        if (mView != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                mView.setBackground(mCurrentDrawable);
            } else {
                mView.setBackgroundDrawable(mCurrentDrawable);
            }
        }
        mCurrentDrawable.start();
    }

    /**
     * Queues a section to start as soon as the current animation finishes.
     * If no animation is playing, the queued animation will be started immediately.
     * Queueing a transition to the currently playing section has no effect.
     *
     * @param id The name of the section that will be queued.
     */
    public void queueTransition(String id) {
        if (id.equals(getCurrentSectionId())) return;
        if (mCurrentSection == null ||
                mCurrentDrawable != null &&
                        mCurrentDrawable.isOneShot() &&
                        mCurrentDrawable.isFinished()) {
            transitionNow(id);
        } else {
            mQueuedSectionId = id;
        }
    }

    /**
     * Starts a specific section without waiting for the current animation to finish.
     * If there is a defined transition from the current section to the new one, the
     * transition will be played, followed immediately by the regular section animation.
     * Transitioning to the currently playing section will restart the animation.
     *
     * @param id The name of the section that will be played.
     */
    public void transitionNow(String id) {
        AnimationSection newSection = mSectionsById.get(id);
        if (newSection == null) {
            throw new IllegalArgumentException("transitionNow called with invalid id: " + id);
        }

        // If the section has a transition from the old section, play the
        // transition before the main animation.
        NotifyingAnimationDrawable transition = mCurrentSection == null ?
                newSection.getTransition("") :
                newSection.getTransition(mCurrentSection.getId());
        if (transition != null) {
            mCurrentDrawable = transition;
            mTransitioningFromId = mCurrentSection == null ? "" : mCurrentSection.getId();
        } else {
            mCurrentDrawable = newSection.loadDrawable();
            mTransitioningFromId = null;
        }
        mCurrentSection = newSection;
        mQueuedSectionId = null;

        playDrawable(mCurrentDrawable);
    }

    /**
     * Clear any currently playing animation. This will cause a "" transition to
     * be played before the next queued section, if one was defined.
     */
    public void clearAnimation() {
        if (mCurrentDrawable != null) {
            mCurrentDrawable.stop();
        }
        if (mView != null) {
            mView.setBackgroundResource(0);
        }
        mCurrentDrawable = null;
        mCurrentSection = null;
        mQueuedSectionId = null;
        mTransitioningFromId = null;
    }

    /**
     * Callback that is run when a playing animation finishes.
     */
    @Override
    public void onAnimationFinished() {
        AnimationSeriesListener listener = mListener.get();
        if (listener != null) {
            listener.onAnimationFinished();
        }
        if (mTransitioningFromId != null) {
            mTransitioningFromId = null;
            playDrawable(mCurrentSection.loadDrawable());
        } else if (mQueuedSectionId != null) {
            transitionNow(mQueuedSectionId);
        }
    }
}
