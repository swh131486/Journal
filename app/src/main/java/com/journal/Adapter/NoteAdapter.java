package com.journal.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.journal.JournalApplication;
import com.journal.R;
import com.journal.activity.HomeActivity;
import com.journal.entities.Note;
import com.journal.utils.DateUtil;
import com.journal.utils.TextUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.sql.Date;
import java.util.ArrayList;

/**
 * 类名称：com.journal.Adapter
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2018.01.03
 */
public class NoteAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Note> mNotes;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.mNotes = notes;
        this.context = context;
    }

    private ViewHolder holder;

    @Override
    public int getCount() {
        return mNotes == null ? 0 : mNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return mNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Note note = mNotes.get(position);
        String summaryPictureUrl = note.getSummary_picture_url();
        if (TextUtil.isValidate(summaryPictureUrl)) {
            return HomeActivity.VIEW_IMAGE;
        } else {
            return HomeActivity.VIEW_TXT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = mNotes.get(position);
        int type = getItemViewType(position);
        if (convertView == null || convertView.getTag() == null) {
            switch (type) {
                case HomeActivity.VIEW_TXT:
                    convertView = LayoutInflater.from(context).inflate(R.layout.activity_home_item_txt, null);
                    holder = new TxtToHolder();
                    break;
                case HomeActivity.VIEW_IMAGE:
                    convertView = LayoutInflater.from(context).inflate(R.layout.activity_home_item_image, null);
                    holder = new ImageHolder();
                    break;
                default:
                    break;
            }
            holder.initializeView(convertView);
            convertView.setTag(holder);
        } else {
            switch (type) {
                case HomeActivity.VIEW_TXT:
                    holder = (TxtToHolder) convertView.getTag();
                    break;
                case HomeActivity.VIEW_IMAGE:
                    holder = (ImageHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }
        holder.initializeData(note);
        return convertView;
    }


    public void notifyDatatChanged(ArrayList<Note> mNotes) {
        this.mNotes = mNotes;
        this.notifyDataSetChanged();
    }


}

abstract class ViewHolder {
    public abstract void initializeView(View convertView);

    public abstract void initializeData(Note note);
}

class TxtToHolder extends ViewHolder {

    private ImageView mMarkIsAlert;
    private TextView mTitleLabel;
    private TextView mContentLabel;
    private TextView mTimeLabel;

    @Override
    public void initializeView(View convertView) {
        mMarkIsAlert = (ImageView) convertView.findViewById(R.id.mMarkIsAlert);
        mTitleLabel = (TextView) convertView.findViewById(R.id.mTitleLabel);
        mContentLabel = (TextView) convertView.findViewById(R.id.mContentLabel);
        mTimeLabel = (TextView) convertView.findViewById(R.id.mTimeLabel);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initializeData(final Note note) {
        String title = note.getTitle();
        if (TextUtil.isValidate(title)) {
            mTitleLabel.setText(title);
        }
        String summaryText = note.getSummary_text();
        if (TextUtil.isValidate(summaryText)) {
            mContentLabel.setText(summaryText);
        }

        long reminder = note.getReminder();
        long modified = note.getDate_modified();
        mTimeLabel.setText(DateUtil.formatDate(new Date(modified), DateUtil.DATE_PATTERN_11));
        if (reminder == 0) {
            mMarkIsAlert.setVisibility(View.INVISIBLE);
        } else {
            mMarkIsAlert.setVisibility(View.VISIBLE);
        }

    }
}

class ImageHolder extends ViewHolder {

    private TextView mTitleLabel;
    private TextView mContentLabel;
    private TextView mTimeLabel;
    private ImageView mMarkIsAlert;
    private ImageView mAvatarImg;

    @Override
    public void initializeView(View convertView) {
        float density = convertView.getContext().getResources().getDisplayMetrics().density;
        mMarkIsAlert = (ImageView) convertView.findViewById(R.id.mMarkIsAlert);
        mAvatarImg = (ImageView) convertView.findViewById(R.id.mAvatarImg);
        mTitleLabel = (TextView) convertView.findViewById(R.id.mTitleLabel);
        mContentLabel = (TextView) convertView.findViewById(R.id.mContentLabel);
        mTimeLabel = (TextView) convertView.findViewById(R.id.mTimeLabel);

        ViewGroup.LayoutParams lp = mAvatarImg.getLayoutParams();
        lp.width = (int) (80 * density);
        lp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        mAvatarImg.setLayoutParams(lp);
        mAvatarImg.setMaxWidth((int) (80 * density));
        mAvatarImg.setMaxHeight(((int) (80 * density)) * 3 / 4); //这里其实可以根据需求而定，我这里测试为最大宽度的3/4倍
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initializeData(Note note) {

        String title = note.getTitle();
        if (TextUtil.isValidate(title)) {
            mTitleLabel.setText(title);
        }
        String summaryText = note.getSummary_text();
        if (TextUtil.isValidate(summaryText)) {
            mContentLabel.setText(summaryText);
        }

        long reminder = note.getReminder();
        long modified = note.getDate_modified();
        mTimeLabel.setText(DateUtil.formatDate(new Date(modified), DateUtil.DATE_PATTERN_11));
        if (reminder == 0) {
            mMarkIsAlert.setVisibility(View.INVISIBLE);
        } else {
            mMarkIsAlert.setVisibility(View.VISIBLE);
        }

        String summaryPictureUrl = note.getSummary_picture_url();
        summaryPictureUrl = summaryPictureUrl.replace("\\", "//");
        ImageLoader.getInstance().displayImage(summaryPictureUrl, mAvatarImg, JournalApplication.mPictureOptions, null);
    }
}
