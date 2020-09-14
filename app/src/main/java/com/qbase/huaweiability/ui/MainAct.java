package com.qbase.huaweiability.ui;

import android.content.Intent;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qbase.huaweiability.ItemBean;
import com.qbase.huaweiability.R;
import com.qbase.huaweiability.base.BaseAct;
import com.qbase.huaweiability.ui.adapter.ItemAdapter;
import com.qbase.huaweiability.ui.android.AndroidAct;
import com.qbase.huaweiability.ui.hms.HmsAct;

import java.util.Arrays;
import java.util.List;

public class MainAct extends BaseAct {

    private List<ItemBean> mItemData = Arrays.asList(
            new ItemBean(R.string.android, AndroidAct.class),
            new ItemBean(R.string.hms, HmsAct.class)
    );

    @Override
    protected int initContentViewLayout() {
        return R.layout.main_act;
    }

    @Override
    protected void onInitialize() {
        initToolBarTitle(getString(R.string.app_name));
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter mAdapter = new ItemAdapter(mItemData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((view, position) -> startActivity(new Intent(MainAct.this, mItemData.get(position).getItemClass())));
    }
}