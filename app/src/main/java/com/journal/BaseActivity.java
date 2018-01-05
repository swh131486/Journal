package com.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.journal.activity.HomeActivity;
import com.journal.utils.Constants;


public abstract class BaseActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private Toolbar toolbar;
    private TextView toolbar_title;
    public static final int MODE_BACK = 0;
    public static final int MODE_HOME = 1;
    public static final int MODE_NO = 2;
    public static final int MODE_DRAWER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int appStatus = JournalApplication.getAppStatus();

        switch (JournalApplication.getAppStatus()) {
            case Constants.STATUS_FORCE_KILLED:
                protectApp();
                break;
            case Constants.STATUS_ONLINE:
                setContentView();
                initializeView();
                initializeData();
                break;

        }

    }

    private void protectApp() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.KEY_HOME_ACTION, Constants.ACTIION_RELOGIN_APP);
        startActivity(intent);
        finish();
    }


    protected abstract void setContentView();

    protected abstract void initializeView();

    protected abstract void initializeData();


    protected void setUpToolbar(int titleResId, int menuId, int mode) {
        if (mode != MODE_NO) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            toolbar_title = (TextView) findViewById(R.id.toolbar_title);
            if (mode == MODE_BACK) {
                toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
            }

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNavigationclick();
                }
            });

            setUpTitle(titleResId);
            setUpMenu(menuId);
        }
    }

    protected void setUpMenu(int menuId) {
        if (toolbar != null) {
            toolbar.getMenu().clear();
            if (menuId > 0) {
                toolbar.inflateMenu(menuId);
                toolbar.setOnMenuItemClickListener(this);
            }
        }
    }


    protected void setUpTitle(int titleResId) {
        if (titleResId > 0 && toolbar_title != null) {
            toolbar_title.setText(titleResId);
        }
    }

    protected void onNavigationclick() {
        // TODO 如果mode != MODE_BACK ,需要重写此方法
        finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


}
