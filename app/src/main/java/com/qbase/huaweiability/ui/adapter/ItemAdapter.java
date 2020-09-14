package com.qbase.huaweiability.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qbase.huaweiability.ItemBean;
import com.qbase.huaweiability.R;
import com.qbase.huaweiability.common.IRecyclerItemListener;
import com.qbase.huaweiability.util.CollectionUtil;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemBean> mItems;
    private IRecyclerItemListener onItemClickListener;

    public ItemAdapter(List<ItemBean> mItems) {
        this.mItems = mItems;
    }

    /**
     * 提供它的set方法，供activity设置回调
     */
    public void setOnItemClickListener(IRecyclerItemListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 将布局转换为View并传递给自定义的MyViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_item, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * 建立起MyViewHolder中视图与数据的关联
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ItemBean itemBean = mItems.get(position);
        viewHolder.mTvName.setText(viewHolder.itemView.getContext().getString(itemBean.getTitle()));
        onItemClick(viewHolder);
    }

    /**
     * 设置item的回调
     */
    private void onItemClick(final ViewHolder viewHolder) {
        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(v -> {
                int layoutPosition = viewHolder.getLayoutPosition();
                onItemClickListener.onItemClick(viewHolder.itemView, layoutPosition);
            });
        }
    }

    /**
     * 获取item的数目
     */
    @Override
    public int getItemCount() {
        return CollectionUtil.isEmpty(mItems) ? 0 : mItems.size();
    }

    /**
     * 自定义的ViewHolder，持有item的所有控件
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvName;

        ViewHolder(View view) {
            super(view);
            mTvName = view.findViewById(R.id.item_name);
        }
    }
}
