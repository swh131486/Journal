package com.journal.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.journal.R;

/**
 * 类名称：com.journal.widget
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2018.01.04
 */
public class SearchEditor extends RelativeLayout {
    private Button  mDeleteBtn;
    private EditText mWidgetSearchEditor;

    public SearchEditor(Context context) {
        super(context);
        initializeView();
    }

    public SearchEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    private void initializeView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_search_editor, this);
        mDeleteBtn = (Button) findViewById(R.id.mDeleteBtn);
        mWidgetSearchEditor = (EditText) findViewById(R.id.mWidgetSearchEditor);
        mDeleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWidgetSearchEditor.setText("");
                mDeleteBtn.setVisibility(View.INVISIBLE);
                if(onSearchListener != null){
                    onSearchListener.onDelete();
                }
            }
        });

        mWidgetSearchEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0){
                    mDeleteBtn.setVisibility(View.VISIBLE);
                }else{
                    mDeleteBtn.setVisibility(View.INVISIBLE);
                }
                if (onSearchListener != null && s != null){
                    onSearchListener.onSearch(s.toString());
                }
            }
        });
    }


    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    private OnSearchListener onSearchListener;

    public interface OnSearchListener {
         void onDelete();
         void onSearch(String key);
    }

    public String getKey(){
        return mWidgetSearchEditor.getText().toString();
    }

}
