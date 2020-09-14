package com.qbase.huaweiability.base;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.qbase.huaweiability.R;
import com.qbase.huaweiability.app.AbilityApp;
import com.qbase.huaweiability.common.CommonHandler;
import com.qbase.huaweiability.common.IHandlerListener;
import com.qbase.huaweiability.util.StatusBarCompat;
import com.qbase.huaweiability.util.StatusBarOption;

import butterknife.ButterKnife;

public abstract class BaseAct extends AppCompatActivity implements IHandlerListener {

    protected AbilityApp mApp;
    /**
     * base handler
     */
    protected CommonHandler mHandler = new CommonHandler(this);

    /**
     * tool bar
     */
    protected Toolbar mTbToolbar;

    /**
     * title
     */
    protected TextView mToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (AbilityApp) getApplication();
        onBeforeContentView();
        setContentView(initContentViewLayout());
        ButterKnife.bind(this);
        initStatusColor();
        onInitialize();
    }

    protected void onBeforeContentView() {
    }

    protected abstract int initContentViewLayout();

    protected abstract void onInitialize();

    /**
     * 设置状态栏颜色
     */
    protected void initStatusColor() {
        this.initStatusColor(mApp.getBarOption());
    }

    /**
     * 设置状态栏颜色
     *
     * @param option option
     */
    protected void initStatusColor(StatusBarOption option) {
        if (option == null) {
            return;
        }
        StatusBarCompat.setColor(this, option.getStatusColor());
    }

    /**
     * 设置toolbar title
     *
     * @param title title
     */
    protected void initToolbar(String title) {
        StatusBarOption option = mApp.getBarOption();
        option.setTitle(title);
        this.initToolbar(option);
    }

    /**
     * 设置toolbar title
     *
     * @param option option
     */
    protected void initToolbar(StatusBarOption option) {
        if (option == null) {
            return;
        }
        try {
            ViewStub toolbarStub = findViewById(R.id.view_title_viewstub);
            if (toolbarStub != null) {
                View view = toolbarStub.inflate();
                CoordinatorLayout coordinatorLayout = view.findViewById(R.id.view_title_coordinatorLayout);
                coordinatorLayout.setBackgroundColor(option.getStatusColor());
                mTbToolbar = view.findViewById(R.id.view_title_toolbar);
                mToolbarTitle = view.findViewById(R.id.view_title_toolbar_title);
                mTbToolbar.setTitle("");
                if (option.getPop_them() != 0) {
                    mTbToolbar.setPopupTheme(option.getPop_them());
                }
                mToolbarTitle.setText(TextUtils.isEmpty(option.getTitle()) ? "" : option.getTitle());
                if (option.getTitleTextColor() != 0) {
                    mToolbarTitle.setTextColor(option.getTitleTextColor());
                }
                if (option.getLeftImage() != 0) {
                    mTbToolbar.setNavigationIcon(option.getLeftImage());
                }
                setSupportActionBar(mTbToolbar);
            }
            if (getSupportActionBar() != null) {
                if (option.isShowBack()) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置toolbar title
     *
     * @param title title
     */
    protected void initToolBarTitle(String title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        } else {
            initToolbar(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }


    @Override
    public void onHandlerMessage(Message msg) {
        //消息处理
    }

}
