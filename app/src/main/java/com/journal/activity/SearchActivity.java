package com.journal.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.journal.Adapter.NoteAdapter;
import com.journal.BaseActivity;
import com.journal.R;
import com.journal.db.NoteController;
import com.journal.entities.Note;
import com.journal.utils.Constants;
import com.journal.utils.TextUtil;
import com.journal.utils.ToastUtils;
import com.journal.widget.SearchEditor;

import java.util.ArrayList;

/**
 * 类名称：com.exing.activity
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.06
 */
public class SearchActivity extends BaseActivity implements  AdapterView.OnItemClickListener {
    private ListView mNoteList;
    private SearchEditor mSearchEditor;
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NoteAdapter mNoteAdapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_search);
        setUpToolbar(R.string.title_serach, 0, MODE_HOME);
    }

    @Override
    protected void initializeView() {
        mNoteList = (ListView) findViewById(R.id.mNoteList);
        mSearchEditor = (SearchEditor) findViewById(R.id.mSearchEditor);
        mSearchEditor.setOnSearchListener(new SearchEditor.OnSearchListener() {
            @Override
            public void onDelete() {
                mNotes.clear();
                mNoteAdapter.notifyDatatChanged(mNotes);
            }

            @Override
            public void onSearch(String key) {
                loadDataDB(key);
            }
        });
    }

    @Override
    protected void initializeData() {

        mNoteList.setOnItemClickListener(this);

        mNoteAdapter = new NoteAdapter(this, mNotes);
        mNoteList.setAdapter(mNoteAdapter);
    }




    /**
     * 从服务端获取数据
     *
     * @param todoNO
     * @param hosId
     * @param officeid
     */
    private void loadDataDB(String key) {
        mNotes = NoteController.queryByKeyOnContent(key);
        mNoteAdapter.notifyDatatChanged(mNotes);
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
                    String keyStr = mSearchEditor.getKey();
                    if (TextUtil.isValidate(keyStr)) {
                        loadDataDB(mSearchEditor.getKey());
                    } else {
                        ToastUtils.showToast("请输入关键字");
                    }
                }
                break;
        }

    }

    @Override
    protected void onNavigationclick() {
        Intent data = getIntent();
        data.putExtra(Constants.KEY_IS_UPDATE_HOME, true);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent data = getIntent();
        data.putExtra(Constants.KEY_IS_UPDATE_HOME, true);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }
}
