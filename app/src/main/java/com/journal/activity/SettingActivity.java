package com.journal.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.journal.BaseActivity;
import com.journal.R;
import com.journal.utils.Constants;

/**
 * 类名称：com.exing.activity
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.07
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView mSettingQuitBtn;


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_setting);
        setUpToolbar(R.string.title_setting, 0, MODE_BACK);
    }

    @Override
    protected void initializeView() {
        mSettingQuitBtn = (TextView) findViewById(R.id.mSettingQuitBtn);
    }

    @Override
    protected void initializeData() {
        mSettingQuitBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mSettingQuitBtn:
                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                intent.putExtra(Constants.KEY_HOME_ACTION, Constants.ACTIION_RESTART_APP);
                startActivity(intent);
                SettingActivity.this.finish();
                break;
        }
    }





}
