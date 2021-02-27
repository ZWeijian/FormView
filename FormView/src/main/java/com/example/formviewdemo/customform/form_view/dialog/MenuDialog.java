package com.example.formviewdemo.customform.form_view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.formviewdemo.R;
import com.example.formviewdemo.customform.form_view.base.OnItemListener;
import com.example.formviewdemo.customform.form_view.function.MenuDialogAdapter;

import java.util.Arrays;


/**
 * @author Zhu
 * 列表弹窗
 */
public class MenuDialog extends Dialog {

    private OnMenuListener onMenuListener;
    private String[] menu;

    public MenuDialog(Context context, String[] menu, OnMenuListener onMenuListener) {
        super(context, R.style.MyDialog);
        this.menu = menu;
        this.onMenuListener = onMenuListener;
        this.setCanceledOnTouchOutside(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_list);

        VerticalRecyclerView rv = findViewById(R.id.rv);
        RelativeLayout rlOutsize = (RelativeLayout) findViewById(R.id.rl_out_side);

        rlOutsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMenuListener != null) {
                    onMenuListener.onCancel();
                }
                dismiss();
            }
        });

        rv.setupDivider();
        MenuDialogAdapter adapter = new MenuDialogAdapter(Arrays.asList(menu));
        adapter.setOnItemClickListener(new OnItemListener() {
            @Override
            public void onItem(View view, int position) {
                if (onMenuListener != null) {
                    onMenuListener.onSelect(menu[position], position);
                }
                dismiss();
            }
        });
        rv.setAdapter(adapter);

        //解决dialog弹出时，状态栏变黑色的问题
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    }


    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        //dialog 居中显示
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            if (lp != null) {
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //设置dialog在布局中的位置
                lp.gravity = Gravity.CENTER;
                lp.windowAnimations = R.style.dialog_fade_in_out;
                window.setAttributes(lp);
            }
        }
    }

    public interface OnMenuListener {
        void onSelect(String text, int pos);

        void onCancel();
    }

}
