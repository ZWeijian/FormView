package com.example.formviewdemo.customform.form_view.base;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;


public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mData;
    private int resource;
    protected OnItemListener mItemListener;
    protected OnItemLongClickListener mItemLongClickListener;
    protected Context mContext;

    public RecyclerAdapter(List<T> data, int resource) {
        super();
        this.mData = data;
        this.resource = resource;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(
                resource, parent, false);
        BaseViewHolder holder = holder(view, viewType);
        holder.mContext = this.mContext;
        holder.mData = this.mData;
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setOnItemClickListener(mItemListener);
        holder.setOnItemLongClickListener(mItemLongClickListener);
        if (mData.get(position) != null) {
            holder.build(mData.get(position), position);
        }
    }

    public void clear() {
        mData.clear();
    }

    /**
     * 刷新数据
     *
     * @param data 数据集
     * @param clear 是否清空数据
     */
    public void setData(List<T> data, boolean clear) {
        if (clear) {
            mData.clear();
        }
        if (data == null || data.size() <= 0) {
            notifyDataSetChanged();
            return;
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 刷新数据
     *
     * @param data 数据集
     */
    public void setData(List<T> data) {
        setData(data, true);
    }

    public List<T> getData() {
        return mData;
    }

    public abstract BaseViewHolder holder(View view, int viewType);

    public void setOnItemClickListener(OnItemListener itemClickListener) {
        this.mItemListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemClickListener) {
        this.mItemLongClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //带动画的移除数据
    public void remove(int position) {
        mData.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据
    }
}
