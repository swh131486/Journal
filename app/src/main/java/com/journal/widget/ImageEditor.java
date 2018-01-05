package com.journal.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 类名称：com.exing.widget
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.06
 */
public class ImageEditor extends ImageView {
    private String absolutePath;

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public ImageEditor(Context context) {
        super(context);
    }

    public ImageEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
