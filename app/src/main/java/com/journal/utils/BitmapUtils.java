package com.journal.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 类名称：com.exing.utils
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.06
 */
public class BitmapUtils {


    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param filePath
     * @param width
     * @return
     */
    public static Bitmap getScaledBitmap(String filePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int sampleSize = options.outWidth > width ? options.outWidth / width
                + 1 : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }


    public static Bitmap loadBitmap(String path, int mWidth, int mHeight,
                                    boolean isNeedToRotate) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = null;
        int be = 1;
        int needToRotate = 0;
        try {
            if (isNeedToRotate) {
                ExifInterface exif = new ExifInterface(path);
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, -1);
                if (orientation != -1) {
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            needToRotate = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            needToRotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            needToRotate = 270;
                            break;
                    }
                }
            }
            options.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
                    null, options);
            be = (int) Math.max(options.outWidth / mWidth, options.outHeight
                    / mHeight);
            if (be <= 0) {
                be = 1;
            }
            options.inSampleSize = be;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
                    null, options);
        } catch (OutOfMemoryError e) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            options.inSampleSize = be * 2;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
                        null, options);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap != null && isNeedToRotate && needToRotate != 0) {
            Matrix tempMatrix = new Matrix();
            tempMatrix.postRotate(needToRotate);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), tempMatrix, false);
        }
        return bitmap;
    }


    public static void compressBitmap(String path, Bitmap bitmap, int quality, long attachmentMaxSize) {
        BufferedOutputStream bos = null;
        File mFile = new File(path);
        try {
            if (mFile.exists()) {
                mFile.delete();
                mFile.createNewFile();
            }
            bos = new BufferedOutputStream(new FileOutputStream(path));
            if (bitmap != null && bos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
                if (mFile.exists() && mFile.length() > attachmentMaxSize) {
                    compressBitmap(path, bitmap, quality / 2, attachmentMaxSize);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }


    public static byte[] file2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static String audioToBytes(String filePath) {
      return Base64.encodeToString(file2byte(filePath), Base64.DEFAULT);
    }

}
