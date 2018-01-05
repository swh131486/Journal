package com.journal.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.czt.mp3recorder.MP3Recorder;
import com.journal.BaseActivity;
import com.journal.JournalApplication;
import com.journal.R;
import com.journal.db.NoteController;
import com.journal.entities.Note;
import com.journal.utils.AlarmManagerUtil;
import com.journal.utils.Constants;
import com.journal.utils.DateUtil;
import com.journal.utils.FileUtil;
import com.journal.utils.IOUtilities;
import com.journal.utils.KeyBoardUtils;
import com.journal.utils.TextUtil;
import com.journal.utils.ToastUtils;
import com.journal.widget.NoteEditorView;
import com.piterwilson.audio.MP3RadioStreamDelegate;
import com.piterwilson.audio.MP3RadioStreamPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 类名称：com.exing.activity
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.06
 */
public class NewNoteActivity extends BaseActivity implements View.OnClickListener, NoteEditorView.OnAudioPlayInterface {
    private NoteEditorView mNoteEditorView;
    private Uri tempUri;
    private Note mNote;
    private TextView mDeleteBtn;
    private TextView mAlarmTime;
    private TimePickerView mTimePickerView;
    private RelativeLayout mAudioPanel;
    private ImageView mStopBtn;
    private LinearLayout mMenuPanel;
    private TextView audioTime;


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_new_note);
        setUpToolbar(R.string.app_name, 0, MODE_BACK);
    }

    @Override
    protected void initializeView() {
        mNoteEditorView = (NoteEditorView) findViewById(R.id.mNoteEditorView);
        mDeleteBtn = (TextView) findViewById(R.id.mDeleteBtn);
        mAlarmTime = (TextView) findViewById(R.id.mAlarmTime);
        audioTime = (TextView) findViewById(R.id.audioTime);
        mAudioPanel = (RelativeLayout) findViewById(R.id.mAudioPanel);
        mMenuPanel = (LinearLayout) findViewById(R.id.mMenuPanel);
        mStopBtn = (ImageView) findViewById(R.id.mStopBtn);
        mNoteEditorView.setmOnAudioPlayInterface(this);
    }

    @Override
    protected void initializeData() {
        findViewById(R.id.mAlarmBtn).setOnClickListener(this);
        findViewById(R.id.mRecorderBtn).setOnClickListener(this);
        findViewById(R.id.mAlbumBtn).setOnClickListener(this);
        findViewById(R.id.mCameraBtn).setOnClickListener(this);
        findViewById(R.id.mSubmitBtn).setOnClickListener(this);
        findViewById(R.id.mStopBtn).setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
        if (getIntent().getSerializableExtra(Constants.KEY_NOTEDATA) != null) {
            mNote = (Note) getIntent().getSerializableExtra(Constants.KEY_NOTEDATA);
            mNoteEditorView.setTitle("");
            setUpNoteByNote(mNote);
        } else {
            mDeleteBtn.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 设置编辑框
     *
     * @param note
     */
    private void setUpNoteByNote(Note note) {
        long reminder = note.getReminder();
        if (reminder != 0) {
            mAlarmTime.setText(DateUtil.formatDate(new Date(reminder), DateUtil.DATE_PATTERN_10));
            mAlarmTime.setVisibility(View.VISIBLE);
        } else {
            mAlarmTime.setText("");
            mAlarmTime.setVisibility(View.GONE);
        }
        mNoteEditorView.setTitle(note.getTitle());
        String content = note.getContent();
        if (TextUtil.isValidate(content)) {
            try {
                JSONObject contentJson = new JSONObject(content);
                String text = contentJson.optString("text");
                if (TextUtil.isValidate(text)) {
                    String[] text_splits = text.trim().split("\\|");
                    boolean isFirst = true;
                    if (text_splits != null && text_splits.length > 0) {
                        JSONArray spans = contentJson.getJSONArray("spans");
                        for (int i = 0; i < text_splits.length; i++) {
                            String text_split = text_splits[i];
                            if (TextUtil.isPicture(text_split)) {
                                if (isFirst) {
                                    mNoteEditorView.removeFirstEditor();
                                    isFirst = false;
                                }
                                int index = TextUtil.getPictureIndex(text_split);
                                if (spans != null && spans.length() > 0 && index < spans.length()) {
                                    JSONObject span = spans.getJSONObject(index);
                                    String file_type = span.optString("file_type");
                                    String file_path = span.optString("file_path");
                                    long file_size = span.optLong("file_size");
                                    if (TextUtil.isValidate(file_type) && "picture".equals(file_type) && TextUtil.isValidate(file_path)) {
                                        mNoteEditorView.setBodyPicture(file_path, false);
                                    } else if (TextUtil.isValidate(file_type) && "audio".equals(file_type) && TextUtil.isValidate(file_path)) {
                                        mNoteEditorView.setBodyAudio(file_path, file_size, false);
                                    }
                                }
                            } else {
                                if (TextUtil.isValidate(text_split)){
                                    if (isFirst) {
                                        mNoteEditorView.setFirstEditor(text_split);
                                        isFirst = false;
                                    } else {
                                        mNoteEditorView.setBodyText(text_split);
                                    }
                                }
                            }
                        }
                        mNoteEditorView.setBodyText("");
                    }
                }
                mDeleteBtn.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * 编辑或者提交
     */
    private void doSubmit() {
        Note saveNote = new Note();
        String title = mNoteEditorView.getTitle();
        String content = mNoteEditorView.getContent();
        long reminder = 0l;
        if (TextUtil.isValidate(mAlarmTime.getText().toString().trim())) {
            reminder = DateUtil.getFormatDate(mAlarmTime.getText().toString().trim(), DateUtil.DATE_PATTERN_10).getTime();
        }
        if (!TextUtil.isValidate(title)) {
            ToastUtils.showToast("没有标题保存不太合适哦！");
            return;
        }
        if (mNote != null) {
            saveNote.set_id(mNote.get_id());
            saveNote.setDate_created(mNote.getDate_created());
            saveNote.setDate_modified(System.currentTimeMillis());
        } else {
            saveNote.setDate_created(System.currentTimeMillis());
            saveNote.setDate_modified(System.currentTimeMillis());
        }
        saveNote.setReminder(reminder);
        saveNote.setContent(content);
        saveNote.setTitle(title);
        saveNote.setSummary_picture_url(mNoteEditorView.getSummaryPictureUrl());
        saveNote.setSummary_text(mNoteEditorView.getSummaryText());
        int id = NoteController.addOrUpdate(saveNote);
        if (id > -1 && reminder != 0){
            AlarmManagerUtil.cancelAlarm(this,id);
            AlarmManagerUtil.setAlarm(this,reminder,true,mNoteEditorView.getSummaryText(),id);
        }
        Intent data = getIntent();
        data.putExtra(Constants.KEY_IS_UPDATE_HOME, true);
        setResult(RESULT_OK, data);
        finish();

    }


    /**
     * 删除
     */
    private void doDelete() {
        if (mNote != null) {
            int id = NoteController.delete(mNote);
            if (id > -1){
                AlarmManagerUtil.cancelAlarm(this,id);
            }
            Intent data = getIntent();
            data.putExtra(Constants.KEY_IS_UPDATE_HOME, true);
            setResult(RESULT_OK, data);
            finish();
        } else {
            ToastUtils.showToast("删除异常，没有找到_id字段");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.PICK_FROM_FILE_ACTIVITY_REQUEST_CODE:
                handleImg(data.getData());
                break;
            case Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                handleImg(tempUri);
                break;
        }
    }

    private void handleImg(Uri uri) {
        String type = getContentResolver().getType(uri);
        String path = null;
        if (TextUtil.isValidate(type)) {
            path = FileUtil.getFilePathByUri(this, uri);
        } else {
            path = uri.getPath();
        }
        path = IOUtilities.copyAttachmentToPackage(path, type);

        mNoteEditorView.setBodyPicture(path, true);

    }


    /**
     * 隐藏软键盘
     */
    public void hideKeyBoard() {
        View latestView = mNoteEditorView.getLatestFocusView();
        if (latestView instanceof EditText) {
            KeyBoardUtils.hideKeyBoard(NewNoteActivity.this, (EditText) latestView);
        }
    }


    //标记是否在播放
    boolean isCurPlaying = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mStopBtn:
                if (isCurPlaying) {
                    stopPlayer();
                } else {
                    stopAudio();
                }
                break;
            case R.id.mAlbumBtn:
                hideKeyBoard();
                if (!mNoteEditorView.isEdiTitle()) {
                    Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
                    mediaChooser.setType("image/*");
                    startActivityForResult(mediaChooser, Constants.PICK_FROM_FILE_ACTIVITY_REQUEST_CODE);
                } else {
                    ToastUtils.showToast("请在正文区域添加图片");
                }
                break;
            case R.id.mCameraBtn:
                hideKeyBoard();
                if (!mNoteEditorView.isEdiTitle()) {
                    Intent imageCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    tempUri = Uri.fromFile(new File(FileUtil.createTmpFile(System.currentTimeMillis() + ".jpg")));
                    imageCapture.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                    startActivityForResult(imageCapture, Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } else {
                    ToastUtils.showToast("请在正文区域添加图片");
                }
                break;
            case R.id.mSubmitBtn:
                hideKeyBoard();
                doSubmit();
                break;
            case R.id.mAlarmBtn:
                hideKeyBoard();
                showAlertTimeDialog();
                break;
            case R.id.mRecorderBtn:
                hideKeyBoard();
                if (!mNoteEditorView.isEdiTitle()) {
                    startAudio();
                } else {
                    ToastUtils.showToast("请在正文区域添加录音");
                }
                break;
            case R.id.mDeleteBtn:
                hideKeyBoard();
                doDelete();
                break;
        }
    }


    /**
     * 弹出选择提醒时间的对话框
     */
    private void showAlertTimeDialog() {
        if (mTimePickerView == null) {
            //时间选择器
            mTimePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    mAlarmTime.setVisibility(View.VISIBLE);
                    mAlarmTime.setText(DateUtil.formatDate(date, DateUtil.DATE_PATTERN_10));
                }
            }).build();
        }
        mTimePickerView.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        mTimePickerView.show();
    }


    /**
     * 开始录音
     */
    private void startAudio() {
        isCurPlaying = false;
        mAudioPanel.setVisibility(View.VISIBLE);
        audioTime.setVisibility(View.VISIBLE);
        mMenuPanel.setVisibility(View.GONE);
        audioTime.setText("00:00");
        recordFilePath = FileUtil.getAudioDir();
        recordFilePath = recordFilePath + File.separator + System.currentTimeMillis() + ".mp3";
        audio(recordFilePath);
    }

    /**
     * 停止录音
     */
    private void stopAudio() {
        isCurPlaying = false;
        mMenuPanel.setVisibility(View.VISIBLE);
        mAudioPanel.setVisibility(View.GONE);
        destoryRecord();
        mNoteEditorView.setBodyAudio(recordFilePath, System.currentTimeMillis() - audioStartTimeStamp, true);
        audioStartTimeStamp = 0;
        recordFilePath = "";
    }


    /**
     * 销毁录音相关资源
     */
    private void destoryRecord() {
        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.setPause(false);
            mRecorder.stop();
            mRecorder = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    Timer timer;
    long audioStartTimeStamp = 0;
    String recordFilePath = "";
    MP3Recorder mRecorder;

    /**
     * 进行录音
     *
     * @param audioPath
     */
    private void audio(String audioPath) {
        mRecorder = new MP3Recorder(new File(audioPath));
        mRecorder.setErrorHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MP3Recorder.ERROR_TYPE) {
                    ToastUtils.showToast("没有麦克风权限");
                }
            }
        });
        audioStartTimeStamp = System.currentTimeMillis();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.what = 10001;
                handler.sendMessage(message);
            }
        }, 1000, 1000);
        try {
            mRecorder.start();
        } catch (IOException e) {
            ToastUtils.showToast("录音出现异常");
            return;
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10001) {
                //录音
                audioTime.setText(DateUtil.toTime(System.currentTimeMillis() - audioStartTimeStamp));
                if ((System.currentTimeMillis() - audioStartTimeStamp) >= 30 * 1000) {
                    stopAudio();
                }
                //播放停止
            } else if (msg.what == 20001) {
                stopPlayer();
            } else if (msg.what == 20002) {
                //录音
                audioTime.setText(DateUtil.toTime(audioStartTimeStamp - System.currentTimeMillis()));
                if ((audioStartTimeStamp - System.currentTimeMillis()) <= 0) {
                    stopPlayer();
                }
            }
        }
    };


    MP3RadioStreamPlayer player;

    /**
     * 开始播放
     *
     * @param audioPath
     */
    public void play(String audioPath, long duration) {
        isCurPlaying = true;
        mMenuPanel.setVisibility(View.GONE);
        mAudioPanel.setVisibility(View.VISIBLE);
        audioTime.setVisibility(View.VISIBLE);
        destoryPlayer();
        startPlayer(audioPath, duration);
    }


    /**
     * 播放录音
     *
     * @param audioFilePath
     */
    private void startPlayer(String audioPath, long duration) {
        player = new MP3RadioStreamPlayer();
        player.setUrlString(audioPath);
        player.setDelegate(new MP3RadioStreamDelegate() {
            @Override
            public void onRadioPlayerPlaybackStarted(MP3RadioStreamPlayer mp3RadioStreamPlayer) {
            }

            @Override
            public void onRadioPlayerStopped(MP3RadioStreamPlayer mp3RadioStreamPlayer) {
                Message msg = handler.obtainMessage();
                msg.what = 20001;
                handler.sendMessage(msg);
            }

            @Override
            public void onRadioPlayerError(MP3RadioStreamPlayer mp3RadioStreamPlayer) {
                Message msg = handler.obtainMessage();
                msg.what = 20001;
                handler.sendMessage(msg);
            }

            @Override
            public void onRadioPlayerBuffering(MP3RadioStreamPlayer mp3RadioStreamPlayer) {

            }
        });
        try {
            audioStartTimeStamp = System.currentTimeMillis() + duration;
            audioTime.setText(DateUtil.toTime(audioStartTimeStamp - System.currentTimeMillis()));
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = handler.obtainMessage();
                    message.what = 20002;
                    handler.sendMessage(message);
                }
            }, 1000, 1000);
            player.play();
        } catch (IOException e) {
            ToastUtils.showToast("播放出现异常");
        }
    }


    /**
     * 停止播放录音
     */
    private void stopPlayer() {
        mMenuPanel.setVisibility(View.VISIBLE);
        mAudioPanel.setVisibility(View.GONE);
        destoryPlayer();
    }

    /**
     * 销毁播放相关资源
     */
    private void destoryPlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryRecord();
        destoryPlayer();
    }

    @Override
    public void onPaly(String audioPath, long duration) {
        play(audioPath, duration);
    }



}
