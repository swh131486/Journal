package com.journal.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 类名称：com.journal.entities
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2018.01.02
 */
@DatabaseTable(tableName = "note")
public class Note implements Serializable{
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String LABEL = "label";
    public static final String DATE_CREATED = "date_created";
    public static final String DATE_MODIFIED = "date_modified";
    public static final String REMINDER = "reminder";
    public static final String SUMMARYPICTUREURL = "summary_picture_url";
    public static final String SUMMARY_TEXT = "summary_text";


    @DatabaseField(id = true)
    private long _id;
    @DatabaseField
    private String title;
    /**
     * {"text":"<photo:journal>文本<audio:journal>","spans":[{"origin_uri":"file:\/\/\/storage\/emulated\/0\/Pictures\/.Media\/photo\/1513408813822","pic_height":388,"pic_width":636,"flag":33,"start":0,"class":"com.journal.PhotoImageSpan","thumb_uri":"file:\/\/\/storage\/emulated\/0\/Pictures\/.Media\/thumbnail\/1513408814085","end":15}]}
     */
    @DatabaseField
    private String content;
    @DatabaseField
    private String label;
    @DatabaseField
    private long date_created;
    @DatabaseField
    private long date_modified;
    @DatabaseField
    private long reminder;
    @DatabaseField
    private String summary_picture_url;
    @DatabaseField
    private String summary_text;

    public String getSummary_text() {
        return summary_text;
    }

    public void setSummary_text(String summary_text) {
        this.summary_text = summary_text;
    }
    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getDate_created() {
        return date_created;
    }

    public void setDate_created(long date_created) {
        this.date_created = date_created;
    }

    public long getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(long date_modified) {
        this.date_modified = date_modified;
    }

    public long getReminder() {
        return reminder;
    }

    public void setReminder(long reminder) {
        this.reminder = reminder;
    }

    public String getSummary_picture_url() {
        return summary_picture_url;
    }

    public void setSummary_picture_url(String summary_picture_url) {
        this.summary_picture_url = summary_picture_url;
    }
}
