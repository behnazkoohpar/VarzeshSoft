package com.koohpar.eram.custom;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.HashMap;

import com.koohpar.eram.activities.MainActivity;

/**
 * Created by sandeeppatel on 9/15/15.
 */
public class AsyncTask_Typeface extends AsyncTask<Void, Void, Void> {
    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */

    private Context mContext;

    public AsyncTask_Typeface(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (MainActivity.typefaceHashMap == null) {
            MainActivity.typefaceHashMap = new HashMap<String, Typeface>();
        }
        AssetManager am = mContext.getAssets();
        String[] getFonts;
        try {
            getFonts = am.list("fonts");
            for (String name : getFonts) {
                int nameOfFont = name.lastIndexOf(".");
                MainActivity.typefaceHashMap.put(name.substring(0, nameOfFont), Typeface.createFromAsset(mContext.getAssets(), "fonts/" + name));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Typeface.createFromAsset(mContext.getAssets())
        return null;
    }
}
