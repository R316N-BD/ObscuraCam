package org.witness.obscuracam.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import org.witness.sscphase1.R;

/**
 * Photo editing settings (#144 output quality, #145 automatic face detection).
 * Keys and defaults are defined once here and in res/xml/imagepreferences.xml.
 */
public class ImagePreferences extends PreferenceActivity {

    public static final String KEY_AUTODETECT_FACES = "pref_autodetect_faces";
    public static final String KEY_JPEG_QUALITY = "pref_jpeg_quality";

    private static final boolean DEFAULT_AUTODETECT_FACES = true;
    private static final int DEFAULT_JPEG_QUALITY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.imagepreferences);
    }

    public static boolean isAutoDetectFacesEnabled(Context context) {
        return prefs(context).getBoolean(KEY_AUTODETECT_FACES, DEFAULT_AUTODETECT_FACES);
    }

    /** JPEG quality, 1-100. ListPreference stores strings, so parse defensively. */
    public static int getJpegQuality(Context context) {
        try {
            int quality = Integer.parseInt(prefs(context).getString(KEY_JPEG_QUALITY, String.valueOf(DEFAULT_JPEG_QUALITY)));
            return Math.max(1, Math.min(100, quality));
        } catch (NumberFormatException e) {
            return DEFAULT_JPEG_QUALITY;
        }
    }

    private static SharedPreferences prefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
