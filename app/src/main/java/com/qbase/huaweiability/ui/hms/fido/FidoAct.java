package com.qbase.huaweiability.ui.hms.fido;

import android.content.Intent;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qbase.huaweiability.ItemBean;
import com.qbase.huaweiability.R;
import com.qbase.huaweiability.base.BaseAct;
import com.qbase.huaweiability.ui.adapter.ItemAdapter;

import java.util.Arrays;
import java.util.List;

public class FidoAct extends BaseAct {

    private List<ItemBean> mItemData = Arrays.asList(
            new ItemBean(R.string.fido_bioauthn, BioAuthnAct.class),
            new ItemBean(R.string.fido_android, BioAuthnAndroidAct.class)
    );

    @Override
    protected int initContentViewLayout() {
        return R.layout.main_act;
    }

    @Override
    protected void onInitialize() {
        initToolBarTitle(getString(R.string.fido_bioauthn));
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter mAdapter = new ItemAdapter(mItemData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((view, position) -> startActivity(new Intent(FidoAct.this, mItemData.get(position).getItemClass())));
    }
}