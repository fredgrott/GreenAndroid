/*
 Copyright 2016 Intrusoft
 Modifications Copyright 2016 Fred Grott(GrottWorkShop)

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
package com.github.shareme.greenandroid.blaze;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * To convert the bitmap from resID with desired height and width.
 *
 * This is singleton instance of {@link BitmapResolver}
 *
 * @author Intruder Shanky
 * @since  October 2016
 */


public class BitmapResolver {


    /**
     *
     * @param resId  The drawable resourceId
     * @param height  Desired height of the bitmap
     * @param width  Desired width of the bitmap
     * @param finalBitmap  An interface which will return the bitmap
     */
    public static void getBitmap(final Context context, final Integer resId, final int height, final int width, final FinalBitmap finalBitmap) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return decodeSampledBitmapFromResource(context.getResources(), resId, width, height);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                finalBitmap.getBitmap(bitmap);
            }
        }.execute();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    interface FinalBitmap {
        void getBitmap(Bitmap bitmap);
    }
}
