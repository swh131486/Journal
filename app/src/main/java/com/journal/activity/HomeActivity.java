package com.journal.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.journal.Adapter.NoteAdapter;
import com.journal.BaseActivity;
import com.journal.JournalApplication;
import com.journal.R;
import com.journal.db.NoteController;
import com.journal.entities.Note;
import com.journal.utils.Constants;
import com.journal.utils.DateUtil;
import com.journal.utils.SpacesItemDecoration;
import com.journal.utils.TextUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.sql.Date;
import java.util.ArrayList;

/**
 * 类名称：com.exing.activity
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.06
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int VIEW_TXT = 0;
    public static final int VIEW_IMAGE = 1;
    private ListView mNoteList;
    private ArrayList<Note> mNotes;
    private NoteAdapter mNoteAdapter;
    private StaggeredAdapter adapter;
    private RecyclerView mRecyclerView;
    private boolean isList = true;
    private float density;


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_home);
        setUpToolbar(R.string.app_name, R.menu.menu_home, MODE_HOME);
    }

    @Override
    protected void initializeView() {
        density = getResources().getDisplayMetrics().density;
        mNoteList = (ListView) findViewById(R.id.mNoteList);
        mRecyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.mRecyclerView);
    }

    @Override
    protected void initializeData() {
        findViewById(R.id.mNewNoteBtn).setOnClickListener(this);
        findViewById(R.id.mSearchBtn).setOnClickListener(this);
        findViewById(R.id.mSettingBtn).setOnClickListener(this);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new StaggeredAdapter();
        mRecyclerView.setAdapter(adapter);

        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration((int)(5 * density));
        mRecyclerView.addItemDecoration(decoration);
        mNoteList.setOnItemClickListener(this);
        mNoteAdapter = new NoteAdapter(this, mNotes);
        mNoteList.setAdapter(mNoteAdapter);
        loadDataFromDB();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mNewNoteBtn:
                Intent intent = new Intent(this, NewNoteActivity.class);
                startActivityForResult(intent, Constants.NEW_NOTE_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.mSearchBtn:
                Intent search = new Intent(this, SearchActivity.class);
                startActivityForResult(search, Constants.NEW_NOTE_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.mSettingBtn:
                Intent setting = new Intent(this, SettingActivity.class);
                startActivity(setting);
                break;
        }
    }


    /**
     * 从数据库获取数据列表
     *
     * @param todoNO
     * @param hosId
     * @param officeid
     */
    private void loadDataFromDB() {
        mNotes = NoteController.queryAllByTimeAsc();
        mNoteAdapter.notifyDatatChanged(mNotes);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, NewNoteActivity.class);
        Note note = mNotes.get(position);
        intent.putExtra(Constants.KEY_NOTEDATA, note);
        startActivityForResult(intent, Constants.NEW_NOTE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.NEW_NOTE_ACTIVITY_REQUEST_CODE:
                boolean isUpdataHome = data.getBooleanExtra(Constants.KEY_IS_UPDATE_HOME, false);
                if (isUpdataHome) {
                    mNotes.clear();
                    mNoteAdapter.notifyDatatChanged(mNotes);
                    loadDataFromDB();
                }
                break;
        }

    }


    class StaggeredAdapter extends RecyclerView.Adapter<BaseViewHolder> {
        @Override
        public int getItemViewType(int position) {
            Note note = mNotes.get(position);
            String summaryPictureUrl = note.getSummary_picture_url();
            if (TextUtil.isValidate(summaryPictureUrl)) {
                return VIEW_IMAGE;
            } else {
                return VIEW_TXT;
            }
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_IMAGE) {
                return new RecyclerImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_item_image1, parent, false));
            } else {
                return new RecyclerTxtToHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_item_txt1, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            holder.onBind(position);
        }


        @Override
        public int getItemCount() {
            return mNotes == null ? 0 : mNotes.size();
        }
    }


    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, getAdapterPosition());
                }
            });

        }

        public abstract void onBind(int position);

        public abstract void onItemClick(View itemView, int position);
    }


    class RecyclerTxtToHolder extends BaseViewHolder {
        private ImageView mMarkIsAlert;
        private TextView mTitleLabel;
        private TextView mContentLabel;
        private TextView mTimeLabel;

        public RecyclerTxtToHolder(View itemView) {
            super(itemView);
            mMarkIsAlert = (ImageView) itemView.findViewById(R.id.mMarkIsAlert);
            mTitleLabel = (TextView) itemView.findViewById(R.id.mTitleLabel);
            mContentLabel = (TextView) itemView.findViewById(R.id.mContentLabel);
            mTimeLabel = (TextView) itemView.findViewById(R.id.mTimeLabel);
        }

        @Override
        public void onBind(int position) {
            Note note = mNotes.get(position);
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

        @Override
        public void onItemClick(View itemView, int position) {
            Intent intent = new Intent(HomeActivity.this, NewNoteActivity.class);
            Note note = mNotes.get(position);
            intent.putExtra(Constants.KEY_NOTEDATA, note);
            startActivityForResult(intent, Constants.NEW_NOTE_ACTIVITY_REQUEST_CODE);
        }
    }
    public int getPictureWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (dm.widthPixels / 2 - (density * (10 + 5)));
    }

    class RecyclerImageHolder extends BaseViewHolder {

        private TextView mTitleLabel;
        private TextView mContentLabel;
        private TextView mTimeLabel;
        private ImageView mMarkIsAlert;
        private ImageView mAvatarImg;

        public RecyclerImageHolder(View itemView) {
            super(itemView);
            mMarkIsAlert = (ImageView) itemView.findViewById(R.id.mMarkIsAlert);
            mAvatarImg = (ImageView) itemView.findViewById(R.id.mAvatarImg);
            mTitleLabel = (TextView) itemView.findViewById(R.id.mTitleLabel);
            mContentLabel = (TextView) itemView.findViewById(R.id.mContentLabel);
            mTimeLabel = (TextView) itemView.findViewById(R.id.mTimeLabel);

            ViewGroup.LayoutParams lp = mAvatarImg.getLayoutParams();
            lp.width = getPictureWidth();
            lp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            mAvatarImg.setLayoutParams(lp);
            mAvatarImg.setMaxWidth(getPictureWidth());
            mAvatarImg.setMaxHeight(getPictureWidth() * 3 / 4); //这里其实可以根据需求而定，我这里测试为最大宽度的5倍
        }

        @Override
        public void onBind(int position) {
            Note note = mNotes.get(position);
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

        @Override
        public void onItemClick(View itemView, int position) {
            Intent intent = new Intent(HomeActivity.this, NewNoteActivity.class);
            Note note = mNotes.get(position);
            intent.putExtra(Constants.KEY_NOTEDATA, note);
            startActivityForResult(intent, Constants.NEW_NOTE_ACTIVITY_REQUEST_CODE);
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_switch:
                if (isList) {
                    mNoteList.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mNoteList.setVisibility(View.VISIBLE);
                }
                isList = !isList;
                break;
        }
        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(Constants.KEY_HOME_ACTION, Constants.ACTOIN_BACK_TO_HOME);
        switch (action) {
            case Constants.ACTIION_RESTART_APP:
                this.finish();
                break;
            case Constants.ACTIION_RELOGIN_APP:
                protectApp();
                break;
        }
    }

    protected void protectApp() {
        startActivity(new Intent(this, LoadActivity.class));
        finish();
    }

}
