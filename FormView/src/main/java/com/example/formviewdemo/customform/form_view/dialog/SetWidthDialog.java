package com.example.formviewdemo.customform.form_view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.formviewdemo.R;

import java.math.BigDecimal;

import static com.example.formviewdemo.customform.form_view.function.FormAdapter.maxWeight;
import static com.example.formviewdemo.customform.form_view.function.FormAdapter.minWeight;


public class SetWidthDialog extends Dialog {
    private final Builder mBuilder;
    TextView tvWeight;

    private SetWidthDialog(Builder builder) {
        super(builder.mContext, R.style.MyDialog);
        mBuilder = builder;
        this.setCanceledOnTouchOutside(true);
    }

    public void setWeight(float newWeight) {
        tvWeight.setText("" + newWeight);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_set_width);
        TextView ok = (TextView) findViewById(R.id.tv_ok);
        tvWeight = (TextView) findViewById(R.id.tv_weight);
        TextView tvAdd = (TextView) findViewById(R.id.tv_add);
        TextView tvReduce = (TextView) findViewById(R.id.tv_reduce);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_out_side);
        setTitle(mBuilder.title);
        float newWeight = new BigDecimal(mBuilder.weight).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        tvWeight.setText("" + newWeight);

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float newWeight = new BigDecimal(mBuilder.weight + 0.2).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

                //越界检查
                newWeight = Math.min(newWeight, maxWeight);
                newWeight = Math.max(newWeight, minWeight);

                mBuilder.weight = newWeight;
                tvWeight.setText("" + newWeight);
                mBuilder.onWeightChangeListener.onResult(newWeight);
            }
        });

        tvReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float newWeight = new BigDecimal(mBuilder.weight - 0.2).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

                //越界检查
                newWeight = Math.min(newWeight, maxWeight);
                newWeight = Math.max(newWeight, minWeight);

                mBuilder.weight = newWeight;
                tvWeight.setText("" + newWeight);
                mBuilder.onWeightChangeListener.onResult(newWeight);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.tv_ok) {
                    dismiss();
                } else if (view.getId() == R.id.rl_out_side) {
                    if (mBuilder.mOutSizeListener != null) {
                        mBuilder.mOutSizeListener.onResult(mBuilder.bundle);
                    }
                    dismiss();
                }
            }
        };

        ok.setOnClickListener(listener);
        rl.setOnClickListener(listener);
        ll.setOnClickListener(listener);


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
        private OnWeightChangeListener onWeightChangeListener;
        private Bundle bundle;
        private boolean showCancel = true;
        private OutSizeListener mOutSizeListener;
        private float weight;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setWeight(float weight) {
            this.weight = weight;
            return this;
        }

        public Builder setOnWeightChangeListener(OnWeightChangeListener user) {
            this.onWeightChangeListener = user;
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

        public SetWidthDialog build() {
            if (TextUtils.isEmpty(title)) {
                title = "温馨提醒";
            }

            if (bundle == null) {
                bundle = new Bundle();
            }


            if (mOutSizeListener == null) {
                mOutSizeListener = new OutSizeListener() {
                    @Override
                    public void onResult(Bundle bundle) {

                    }
                };
            }
            return new SetWidthDialog(this);
        }
    }


    public interface OnWeightChangeListener {
        void onResult(float newWeight);
    }

    public interface OutSizeListener {
        void onResult(Bundle bundle);
    }

}

