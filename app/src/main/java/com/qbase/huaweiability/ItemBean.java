package com.qbase.huaweiability;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

public class ItemBean {

    private int title;

    private Class<? extends AppCompatActivity> itemClass;

    public ItemBean(@StringRes int title, Class<? extends AppCompatActivity> itemClass) {
        this.title = title;
        this.itemClass = itemClass;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<? extends AppCompatActivity> getItemClass() {
        return itemClass;
    }

    public void setItemClass(Class<? extends AppCompatActivity> itemClass) {
        this.itemClass = itemClass;
    }
}
