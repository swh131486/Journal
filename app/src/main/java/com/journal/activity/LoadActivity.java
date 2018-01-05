package com.journal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.journal.BaseActivity;
import com.journal.JournalApplication;
import com.journal.R;
import com.journal.utils.Constants;

/**
 * 类名称：com.journal.activity
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2018.01.04
 */
public class LoadActivity extends BaseActivity {
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        JournalApplication.mAppState = Constants.STATUS_ONLINE;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load);
    }

    @Override
    protected void initializeView() {
    }

    @Override
    protected void initializeData() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadActivity.this, HomeActivity.class));
                LoadActivity.this.finish();
            }
        }, 3 * 1000);
    }
}
