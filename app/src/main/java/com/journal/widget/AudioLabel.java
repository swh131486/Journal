package com.journal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 类名称：com.exing.widget
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.20
 */
public class AudioLabel extends TextView {
    private String audioFilePath;
    private long duration;

    public AudioLabel(Context context) {
        super(context);
    }

    public AudioLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AudioLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
