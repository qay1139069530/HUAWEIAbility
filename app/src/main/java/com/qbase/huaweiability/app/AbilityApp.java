package com.qbase.huaweiability.app;

import androidx.multidex.MultiDexApplication;

import com.qbase.huaweiability.R;
import com.qbase.huaweiability.util.StatusBarOption;

public class AbilityApp extends MultiDexApplication {

    private StatusBarOption barOption;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 获取statusbar option
     */
    public StatusBarOption getBarOption() {
        if (barOption == null) {
            barOption = getDefaultBarOption();
        }
        return barOption;
    }

    /**
     * 设置statusbar option
     */
    public void setBarOption(StatusBarOption barOption) {
        this.barOption = barOption;
    }

    /**
     * 获取默认设置的bar option
     */
    private StatusBarOption getDefaultBarOption() {
        StatusBarOption option = new StatusBarOption();
        option.setStatusColor(getResources().getColor(R.color.colorPrimary));
        option.setTitleTextColor(getResources().getColor(R.color.White));
        option.setLeftImage(R.mipmap.back);
        option.setPop_them(R.style.qbase_menu);
        option.setShowBack(true);
        return option;
    }
}
