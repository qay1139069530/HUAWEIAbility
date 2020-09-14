package com.qbase.huaweiability.util;

/**
 * Created by dell on 2016/9/6.
 */
public class StatusBarOption {

    /**状态栏颜色*/
    private int statusColor;
    /** title是否显示返回按钮，true:显示，false:不显示*/
    private boolean showBack;
    /** title中间字体颜色*/
    private int titleTextColor = 0;
    /**title左上角图片id*/
    private int leftImage;
    /**title中间标题内容*/
    private String title;
    /**title按钮弹出框样式*/
    private int pop_them;


    public int getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(int statusColor) {
        this.statusColor = statusColor;
    }

    public boolean isShowBack() {
        return showBack;
    }

    public void setShowBack(boolean showBack) {
        this.showBack = showBack;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    public int getLeftImage() {
        return leftImage;
    }

    public void setLeftImage(int leftImage) {
        this.leftImage = leftImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPop_them() {
        return pop_them;
    }

    public void setPop_them(int pop_them) {
        this.pop_them = pop_them;
    }
}
