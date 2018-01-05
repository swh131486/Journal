package com.journal.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.journal.R;
import com.journal.utils.BitmapUtils;
import com.journal.utils.DateUtil;
import com.journal.utils.FileUtil;
import com.journal.utils.KeyBoardUtils;
import com.journal.utils.TextUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 类名称：com.exing.widget
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.05
 */
public class NoteEditorView extends FrameLayout implements View.OnKeyListener, View.OnFocusChangeListener, View.OnClickListener {
    private Context context;
    private LinearLayout mContentPanel;
    //标题
    private TextEditor mTitleLabel;
    //正文
    private TextEditor mBodyLabel;
    // 最近被聚焦的EditText
    private View latestFocusView;
    private int padding = 5;
    private LayoutInflater inflater;
    private LayoutTransition mTransitioner;
    private int disappearingImageIndex = 0;
    private float density;

    public View getLatestFocusView() {
        return latestFocusView;
    }

    public NoteEditorView(Context context) {
        super(context);
        this.context = context;
        initializeView();
    }

    public NoteEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeView();
    }


    private void initializeView() {
        density = getContext().getResources().getDisplayMetrics().density;
        padding = (int) (density * 5);
        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.widget_note_editor, this);
        mContentPanel = (LinearLayout) findViewById(R.id.mContentPanel);
        mBodyLabel = (TextEditor) findViewById(R.id.mBodyLabel);
        mTitleLabel = (TextEditor) findViewById(R.id.mTitleEditor);
        LinearLayout root = (LinearLayout) findViewById(R.id.root);

        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latestFocusView instanceof TextEditor) {
                    if ((latestFocusView == mTitleLabel && mBodyLabel != null) || (latestFocusView == mBodyLabel && mBodyLabel != null)) {
                        mBodyLabel.requestFocus();
                        KeyBoardUtils.showKeyBoard(context, mBodyLabel);
                    } else {
                        ((TextEditor) latestFocusView).requestFocus();
                        KeyBoardUtils.showKeyBoard(context, (TextEditor) latestFocusView);
                    }
                }

            }
        });

        setUpLayoutTransitions();
        mTitleLabel.setOnKeyListener(this);
        mTitleLabel.setOnFocusChangeListener(this);
        mBodyLabel.setOnKeyListener(this);
        mBodyLabel.setOnFocusChangeListener(this);
        //字体加粗
        TextPaint paint = mTitleLabel.getPaint();
        paint.setFakeBoldText(true);
        latestFocusView = mTitleLabel;
    }


    /**
     * 当前编辑页面是不是标题栏
     *
     * @return
     */
    public boolean isEdiTitle() {
        if (mTitleLabel == latestFocusView) {
            return true;
        }
        return false;
    }


    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (TextUtil.isValidate(title)) {
            mTitleLabel.setText(title);
        }
    }

    /**
     * 获取标题
     *
     * @return
     */
    public String getTitle() {
        if (TextUtil.isValidate(mTitleLabel.getText().toString())) {
            return mTitleLabel.getText().toString().trim();
        } else {
            return null;
        }
    }


    /**
     * 获取第一条正文记录
     *
     * @return
     */
    public String getFirstText() {
        if (TextUtil.isValidate(mBodyLabel.getText().toString())) {
            return mBodyLabel.getText().toString().trim();
        } else {
            return null;
        }
    }


    /**
     * 设置第一条记录
     */
    public void setFirstEditor(String text) {
        if (TextUtil.isValidate(text)) {
            mBodyLabel.setText(text);
            latestFocusView = mBodyLabel;
        }
    }

    /**
     * 去除第一条记录
     */
    public void removeFirstEditor() {
        mContentPanel.removeView(mBodyLabel);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            latestFocusView = v;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            EditText edit = (EditText) v;
            onBackspacePress(edit);
        }
        return false;
    }


    /**
     * 设置正文
     *
     * @param bodyText
     */
    public void setBodyText(String bodyText) {
        int latestEditorIndex = mContentPanel.getChildCount();
        addEditortAtIndex(latestEditorIndex, bodyText, true);
    }

    /**
     * 设置图片资源
     *
     * @param picturePath
     */
    public void setBodyPicture(String picturePath, boolean isLocal) {
        if (isLocal) {
            setBodyImage(picturePath, true, 0);
        } else {
            int latestEditorIndex = mContentPanel.getChildCount();
            addPictureToView(latestEditorIndex, picturePath);
        }
    }


    /**
     * 设置录音资源
     *
     * @param andioPath
     * @param duration
     */
    public void setBodyAudio(String andioPath, long duration, boolean isLocal) {
        if (isLocal) {
            setBodyImage(andioPath, false, duration);
        } else {
            int latestEditorIndex = mContentPanel.getChildCount();
            addAudioViewAtIndex(latestEditorIndex, andioPath, duration);
        }
    }


    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    private EditText addEditortAtIndex(final int index, String text, boolean isSelection) {
        EditText editor = createBodyEditor();
        editor.setText(text);
        if (isSelection) {
            editor.setSelection(editor.getText().toString().length());
        }
        // 请注意此处，EditText添加、或删除不触动Transition动画
        mContentPanel.setLayoutTransition(null);
        mContentPanel.addView(editor, index);
        mContentPanel.setLayoutTransition(mTransitioner); // remove之后恢复transition动画
        latestFocusView = editor;
        return editor;
    }


    /**
     * 创建正文编辑框
     *
     * @return
     */
    private EditText createBodyEditor() {
        EditText editor = (EditText) inflater.inflate(R.layout.widget_text_editor,
                null);
        editor.setOnKeyListener(this);
        editor.setOnFocusChangeListener(this);
        editor.setPadding(padding, padding, padding, 0);
        latestFocusView = editor;
        return editor;
    }


    /**
     * 设置文件资源到正文中
     *
     * @param resPath
     * @param isPicture
     */
    private void setBodyImage(String resPath, boolean isPicture, long duration) {
        String lastEditStr = "";
        int cursorIndex = 0;
        String ediText = "";
        int latestEditorIndex = mContentPanel.indexOfChild(latestFocusView);
        if (latestFocusView instanceof EditText) {
            EditText latestFocusEdit = (EditText) latestFocusView;
            lastEditStr = latestFocusEdit.getText().toString();
            cursorIndex = latestFocusEdit.getSelectionStart();
            ediText = lastEditStr.substring(0, cursorIndex).trim();
            if (lastEditStr.length() == 0 || ediText.length() == 0) {
                // 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
                if (isPicture) {
                    addPictureToView(latestEditorIndex, resPath);
                } else {
                    addAudioViewAtIndex(latestEditorIndex, resPath, duration);
                }
            } else {
                // 如果EditText非空且光标不在最顶端，则需要添加新的imageView和EditText
                latestFocusEdit.setText(ediText);
                String editStr2 = lastEditStr.substring(cursorIndex).trim();
                if (mContentPanel.getChildCount() - 1 == latestEditorIndex
                        || editStr2.length() > 0) {
                    addEditortAtIndex(latestEditorIndex + 1, editStr2, true);
                }
                if (isPicture) {
                    addPictureToView(latestEditorIndex + 1, resPath);
                } else {
                    addAudioViewAtIndex(latestEditorIndex + 1, resPath, duration);
                }
                latestFocusEdit.requestFocus();
                latestFocusEdit.setSelection(ediText.length(), ediText.length());
            }
            KeyBoardUtils.hideKeyBoard(context, latestFocusEdit);
        } else {
            latestEditorIndex = mContentPanel.indexOfChild(latestFocusView);
            if (isPicture) {
                addPictureToView(latestEditorIndex + 1, resPath);
            } else {
                addAudioViewAtIndex(latestEditorIndex + 1, resPath, duration);
            }
        }
    }


    /**
     * 添加图片到视图中
     *
     * @param index
     * @param imagePath
     */
    private void addPictureToView(final int index, String imagePath) {
        final RelativeLayout imageLayout = createImageView(R.layout.widget_image_view);
        final ImageEditor mImaeEditor = (ImageEditor) imageLayout
                .findViewById(R.id.mImageEditor);
        if (!imagePath.startsWith("file:")) {
            imagePath = Uri.fromFile(new File(imagePath)).toString();
        }
        imagePath = imagePath.replace("\\", "//");
        mImaeEditor.setAbsolutePath(imagePath);
        ViewGroup.LayoutParams lp = mImaeEditor.getLayoutParams();
        lp.width = getPictureWidth();
        lp.height = LayoutParams.WRAP_CONTENT;
        mImaeEditor.setLayoutParams(lp);
        mImaeEditor.setMaxWidth(getPictureWidth());
        mImaeEditor.setMaxHeight(getPictureWidth() * 1); //这里其实可以根据需求而定，我这里测试为最大宽度的1倍

        mContentPanel.addView(imageLayout, index);
        ImageLoader.getInstance().displayImage(imagePath, mImaeEditor);
    }


    public int getPictureWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (dm.widthPixels - (density * 10));
    }


    /**
     * 创建image视图
     *
     * @param imagelayoutId
     * @return
     */
    private RelativeLayout createImageView(int imagelayoutId) {
        RelativeLayout imageRoot = (RelativeLayout) inflater.inflate(
                imagelayoutId, null);
        imageRoot.setTag("picture");
        View closeView = imageRoot.findViewById(R.id.mImageClose);
        closeView.setOnClickListener(this);
        return imageRoot;
    }


    String name;

    /**
     * 添加Audio
     *
     * @param index
     * @param audioPath
     * @param duration
     */
    private void addAudioViewAtIndex(final int index, String audioPath, final long duration) {
        final RelativeLayout audioLayout = createAudioView(R.layout.widget_audio_view);
        final AudioLabel mAudioLabel = (AudioLabel) audioLayout
                .findViewById(R.id.mAudioLabel);
        audioPath = audioPath.replace("\\", "//");
        name = audioPath.substring(audioPath.lastIndexOf("/") + 1);
        if (FileUtil.hasAudioByFileName(name)) {
            mAudioLabel.setAudioFilePath(FileUtil.getAudioDir() + File.separator + name);
            mAudioLabel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnAudioPlayInterface != null) {
                        String audioFilePath = mAudioLabel.getAudioFilePath();
                        if (TextUtil.isValidate(audioFilePath)) {
                            audioFilePath = mAudioLabel.getAudioFilePath();
                            mOnAudioPlayInterface.onPaly(audioFilePath, duration);
                        }

                    }
                }
            });
            mAudioLabel.setDuration(duration);
            mAudioLabel.setText(DateUtil.toTime(duration));
            mContentPanel.addView(audioLayout, index);
        }

    }


    /**
     * 创建录音视图
     *
     * @param audiolayoutId
     * @return
     */

    private RelativeLayout createAudioView(int audiolayoutId) {
        RelativeLayout audioRoot = (RelativeLayout) inflater.inflate(
                audiolayoutId, null);
        audioRoot.setTag("audio");
        View closeView = audioRoot.findViewById(R.id.mImageClose);
        closeView.setTag(audioRoot.getTag());
        closeView.setOnClickListener(this);
        TextView mAudioLabel = (TextView) audioRoot.findViewById(R.id.mAudioLabel);
        return audioRoot;
    }


    @Override
    public void onClick(View v) {
        RelativeLayout parentView = (RelativeLayout) v.getParent();
        onImageCloseClick(parentView);
    }


    /**
     * 处理软键盘backSpace回退事件
     *
     * @param editTxt
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = mContentPanel.indexOfChild(editTxt);
            View preView = mContentPanel.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof RelativeLayout) {
                    // 光标EditText的上一个view对应的是图片
                    onImageCloseClick(preView);
                } else if (preView instanceof EditText) {
                    // 光标EditText的上一个view对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (EditText) preView;
                    String str2 = preEdit.getText().toString();

                    // 合并文本view时，不需要transition动画
                    mContentPanel.setLayoutTransition(null);
                    mContentPanel.removeView(editTxt);
                    mContentPanel.setLayoutTransition(mTransitioner); // 恢复transition动画

                    // 文本合并
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    latestFocusView = preEdit;
                }
            }
        }
    }


    /**
     * 处理图片叉掉的点击事件
     *
     * @param view 整个image对应的relativeLayout view
     * @type 删除类型 0代表backspace删除 1代表按红叉按钮删除
     */
    private void onImageCloseClick(View view) {
        if (!mTransitioner.isRunning()) {
            disappearingImageIndex = mContentPanel.indexOfChild(view);
            mContentPanel.removeView(view);
        }
    }

    /**
     * 初始化transition动画
     */
    private void setUpLayoutTransitions() {
        mTransitioner = new LayoutTransition();
        mContentPanel.setLayoutTransition(mTransitioner);
        mTransitioner.addTransitionListener(new LayoutTransition.TransitionListener() {

            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {

            }

            @Override
            public void endTransition(LayoutTransition transition,
                                      ViewGroup container, View view, int transitionType) {
                if (!transition.isRunning()
                        && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                }
            }
        });
        mTransitioner.setDuration(300);
    }

    OnAudioPlayInterface mOnAudioPlayInterface;

    public OnAudioPlayInterface getmOnAudioPlayInterface() {
        return mOnAudioPlayInterface;
    }

    public void setmOnAudioPlayInterface(OnAudioPlayInterface mOnAudioPlayInterface) {
        this.mOnAudioPlayInterface = mOnAudioPlayInterface;
    }


    /**
     * 录音播放接口
     */
    public interface OnAudioPlayInterface {
        void onPaly(String audioPath, long duration);
    }

    /**
     * 一览图片地址
     */
    String summaryPictureUrl;
    /**
     * 一览文本
     */
    String summaryText;

    public String getSummaryPictureUrl() {
        return summaryPictureUrl;
    }

    public String getSummaryText() {
        return summaryText;
    }

    /**
     * 获取文本信息
     *
     * @return
     */
    public String getContent() {
        String text = "";
        summaryText = "";
        summaryPictureUrl = "";
        JSONObject content = new JSONObject();
        JSONArray spans = new JSONArray();
        try {
            int spanCout = 0;
            int num = mContentPanel.getChildCount();
            for (int index = 0; index < num; index++) {
                View itemView = mContentPanel.getChildAt(index);
                if (itemView == mTitleLabel) {
                } else if (itemView instanceof EditText && (itemView != mTitleLabel)) {
                    EditText item = (EditText) itemView;
                    if (TextUtil.isValidate(item.getText().toString())) {
                        text = text + item.getText().toString() + "|";
                        summaryText = summaryText + item.getText().toString();
                    }
                } else if (itemView instanceof RelativeLayout) {
                    if (itemView.getTag() != null && itemView.getTag() instanceof String) {
                        String tag = (String) itemView.getTag();
                        if ("picture".equals(tag)) {
                            text = text + "{" + spanCout + "}" + "|";
                            ImageEditor item = (ImageEditor) itemView
                                    .findViewById(R.id.mImageEditor);
                            JSONObject span = new JSONObject();
                            if (!TextUtil.isValidate(summaryPictureUrl)) {
                                summaryPictureUrl = item.getAbsolutePath();
                            }
                            span.put("file_path", item.getAbsolutePath());
                            span.put("file_type", "picture");
                            span.put("file_size", "0");
                            spans.put(span);
                            spanCout = spanCout + 1;
                        } else if ("audio".equals(tag)) {
                            text = text + "{" + spanCout + "}" + "|";
                            AudioLabel item = (AudioLabel) itemView
                                    .findViewById(R.id.mAudioLabel);
                            JSONObject span = new JSONObject();
                            span.put("file_path", item.getAudioFilePath());
                            span.put("file_type", "audio");
                            span.put("file_size", item.getDuration());
                            spans.put(span);
                            spanCout = spanCout + 1;
                        }
                    }
                }
            }
            content.put("text", text);
            content.put("spans", spans);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return content.toString();
    }


}
