package com.example.formviewdemo.customform.form_view.function;

import android.view.View;
import android.widget.TextView;


import com.example.formviewdemo.R;
import com.example.formviewdemo.customform.form_view.base.BaseViewHolder;
import com.example.formviewdemo.customform.form_view.base.RecyclerAdapter;

import java.util.List;

public class MenuDialogAdapter extends RecyclerAdapter<String> {

    List<String> data;

    public MenuDialogAdapter(List<String> data) {
        super(data, R.layout.layout_item_menu_dialog);
        this.data = data;
    }

    @Override
    public BaseViewHolder holder(View view, int viewType) {
        return new ViewHolder(view);
    }

    public class ViewHolder extends BaseViewHolder<String> {

         TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }

        @Override
        public void build(String str, int position) {
            tv.setText(str);
        }
    }
}