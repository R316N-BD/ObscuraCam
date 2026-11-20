package org.witness.obscuracam.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

/**
 * Which runtime permissions the app needs to read and save media, per Android version.
 *
 * Android has changed this three times, and asking for the wrong one means the request is
 * silently denied (the dialog never appears and the result is always "denied"):
 *  - up to 9 (API 28): READ + WRITE_EXTERNAL_STORAGE
 *  - 10 to 12 (API 29-32): READ_EXTERNAL_STORAGE only; saving goes through MediaStore
 *  - 13+ (API 33+): READ_MEDIA_IMAGES + READ_MEDIA_VIDEO
 */
public final class MediaPermissions {

    private MediaPermissions() {
    }

    /** Permissions needed to browse the gallery. */
    public static String[] forReading() {
        if (Build.VERSION.SDK_INT >= 33) {
            return new String[]{"android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO"};
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    /** Permissions needed to save into the shared Pictures/Movies folders. Empty on Android 10+. */
    public static String[] forWriting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new String[0];
        }
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    public static boolean hasAll(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /** True when the result of a permission request granted every permission asked for. */
    public static boolean allGranted(int[] grantResults) {
        if (grantResults.length == 0) {
            return false; // request was cancelled
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
