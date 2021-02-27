package com.example.formviewdemo.customform.form_view.base;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;


import java.util.List;


public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private OnItemListener itemListener;
    private OnItemLongClickListener itemLongClickListener;
    public Context mContext;
    public List<T> mData;

    public BaseViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null && getLayoutPosition() >= 0) {
                    itemListener.onItem(view, getLayoutPosition());
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (itemLongClickListener != null && getLayoutPosition() >= 0) {
                    itemLongClickListener.onItemLongClick(view, getLayoutPosition());
                }
                return true;
            }
        });
    }

    public void setOnItemClickListener(OnItemListener itemClickListener) {
        this.itemListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemClickListener) {
        this.itemLongClickListener = itemClickListener;
    }

    public abstract void build(T object, int position);
}
