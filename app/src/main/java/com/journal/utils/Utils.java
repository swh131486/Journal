package com.journal.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.ImageView;

import com.journal.JournalApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Utils {

    public static boolean isImage(String file_type) {
        if (AttachmentType.BMP.fetchMimeType().equalsIgnoreCase(file_type) || AttachmentType.PNG.fetchMimeType().equalsIgnoreCase(file_type)
                || AttachmentType.JPEG.fetchMimeType().equalsIgnoreCase(file_type) || AttachmentType.JPG.fetchMimeType().equalsIgnoreCase(file_type)) {
            return true;
        }
        return false;
    }


    public static void showImage(String file_url, ImageView image) {
        ImageLoader.getInstance().displayImage(null, image, JournalApplication.mPictureOptions,
                null);
    }


    /**
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


}
