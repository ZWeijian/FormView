package com.example.formviewdemo.customform.form_view.function;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.formviewdemo.customform.form_view.FormUtil;

import java.util.ArrayList;

public class ColumnMenuUtil {
    /**
     * 显示列菜单
     *
     * @param colPos        列号
     * @param rowWeightList 此行可见列的格子的权重
     * @param narrowList    此行可见列的格子的被合并情况
     */
    public static void showColMenu(Context context, int colPos, ArrayList<Float> rowWeightList, ArrayList<Boolean> narrowList, ArrayList<ImageView> colMenuList) {
        int visibleColumnNum = rowWeightList.size();

        //显示列菜单
        //处理可见部分
        for (int i = 0; i < visibleColumnNum; i++) {
            colMenuList.get(i).setVisibility(colPos == i ? View.VISIBLE : View.INVISIBLE);
            setWeight(context, colMenuList.get(i), rowWeightList.get(i));
            //被合并的格子要隐藏
            if (narrowList.get(i)) {
                colMenuList.get(i).setVisibility(View.GONE);
            }
        }

        //处理不可见部分
        for (int i = visibleColumnNum; i < colMenuList.size(); i++) {
            colMenuList.get(i).setVisibility(View.GONE);
        }
    }

    /**
     * 设置列菜单的权重
     */
    private static void setWeight(Context context, ImageView iv, float newWeight) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, FormUtil.dip2px(context, 40), newWeight);
        layoutParams.gravity = Gravity.FILL_VERTICAL;
        iv.setLayoutParams(layoutParams);
    }

}
