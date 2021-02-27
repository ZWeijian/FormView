package com.example.formviewdemo.customform.form_view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.formviewdemo.R;


/**
 * @author Zhu
 * 用于提醒的提示弹窗
 */
public class RemindDialog extends Dialog {

    private Builder mBuilder;

    private RemindDialog(Builder builder) {
        super(builder.mContext, R.style.MyDialog);
        mBuilder = builder;
        this.setCanceledOnTouchOutside(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_exit_dialog);
        TextView cancel = (TextView) findViewById(R.id.tv_cancel);
        TextView ok = (TextView) findViewById(R.id.tv_ok);
        TextView titleView = (TextView) findViewById(R.id.title);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_out_side);
        TextView tvContent = (TextView) findViewById(R.id.alert_message);
        setTitle(mBuilder.title);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.tv_ok) {
                    if (mBuilder.user != null) {
                        mBuilder.user.onResult(true, mBuilder.bundle);
                    }
                    dismiss();
                } else if (view.getId() == R.id.tv_cancel) {
                    if (mBuilder.user != null) {
                        mBuilder.user.onResult(false, mBuilder.bundle);
                    }
                    dismiss();
                } else if (view.getId() == R.id.rl_out_side) {
                    if (mBuilder.mOutSizeListener != null) {
                        mBuilder.mOutSizeListener.onResult(mBuilder.bundle);
                    }
                    dismiss();
                } else if (view.getId() == R.id.ll) {

                }
            }
        };

        cancel.setOnClickListener(listener);
        ok.setOnClickListener(listener);
        rl.setOnClickListener(listener);
        ll.setOnClickListener(listener);

        if (!TextUtils.isEmpty(mBuilder.title)) {
            titleView.setText(mBuilder.title);
            titleView.setVisibility(View.VISIBLE);
        } else {
            titleView.setVisibility(View.GONE);
        }

        if (mBuilder.showCancel) {
            cancel.setVisibility(View.VISIBLE);
        } else {
            cancel.setVisibility(View.GONE);
        }

        if (mBuilder.msg != null) {
            tvContent.setText(mBuilder.msg);
            //一行居中，多行靠左
            if (mBuilder.msg.length() <= 24) {
                tvContent.setGravity(Gravity.CENTER | Gravity.TOP);
            } else {
                tvContent.setGravity(Gravity.LEFT | Gravity.TOP);
            }
        }
        //解决dialog弹出时，状态栏变黑色的问题
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }


    @Override
    public void show() {
        try {
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
        } catch (Exception e) {

        }
    }

    public void setCancelTouchOutSize(boolean cancelTouchOutSize) {
        this.setCanceledOnTouchOutside(cancelTouchOutSize);
    }

    public static final class Builder {
        private Context mContext;
        private String title;
        private String msg;
        private AlertDialogUser user;
        private Bundle bundle;
        private boolean showCancel = true;
        private OutSizeListener mOutSizeListener;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setUser(AlertDialogUser user) {
            this.user = user;
            return this;
        }

        public Builder setOutSizeListener(OutSizeListener outSizeListener) {
            this.mOutSizeListener = outSizeListener;
            return this;
        }

        public Builder setBundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public Builder showCancel(boolean showCancel) {
            this.showCancel = showCancel;
            return this;
        }

        public RemindDialog build() {
            if (TextUtils.isEmpty(title)) {
                title = "温馨提醒";
            }
            if (TextUtils.isEmpty(msg)) {
                msg = "";
            }
            if (bundle == null) {
                bundle = new Bundle();
            }
            if (user == null) {
                user = new AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {

                    }
                };
            }

            if (mOutSizeListener == null) {
                mOutSizeListener = new OutSizeListener() {
                    @Override
                    public void onResult(Bundle bundle) {

                    }
                };
            }
            return new RemindDialog(this);
        }
    }


    public interface AlertDialogUser {
        void onResult(boolean confirmed, Bundle bundle);
    }

    public interface OutSizeListener {
        void onResult(Bundle bundle);
    }

}