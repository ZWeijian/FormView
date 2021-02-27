package com.example.formviewdemo.customform.form_view.function;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.formviewdemo.R;
import com.example.formviewdemo.customform.LogUtil;
import com.example.formviewdemo.customform.form_view.FormUtil;
import com.example.formviewdemo.customform.form_view.dialog.SetWidthDialog;
import com.example.formviewdemo.customform.form_view.model.Box;
import com.example.formviewdemo.customform.form_view.model.Row;

import java.util.ArrayList;

import static com.example.formviewdemo.customform.form_view.function.FormAdapter.maxWeight;
import static com.example.formviewdemo.customform.form_view.function.FormAdapter.minWeight;

/**
 * 操作单个格子的工具类
 */
public class BoxController {

    private final FormAdapter adapter;
    private final Context context;
    private final ArrayList<Row> data;

    public BoxController(Context context, FormAdapter adapter, ArrayList<Row> data) {
        this.context = context;
        this.adapter = adapter;
        this.data = data;
    }

    /**
     * 调整宽度权重
     */
    public void adjustWidthWeight(final Box currentBox, final EditText et, final int rowPos, final int columnPos) {
        new SetWidthDialog.Builder(context)
                .setWeight(currentBox.getWeight())
                .setTitle("调整宽度权重")
                .setOnWeightChangeListener(new SetWidthDialog.OnWeightChangeListener() {
                    @Override
                    public void onResult(float newWeight) {
                        if (newWeight > (maxWeight + 0.01) || newWeight < (minWeight - 0.01)) {
                            return;
                        }
                        currentBox.setWeight(newWeight);
                        setEditTextWeight(et, newWeight);

                        if (adapter.onBoxChangeListener != null)
                            adapter.onBoxChangeListener.onWeightChange(currentBox, rowPos, columnPos);
                    }
                })
                .build()
                .show();
    }

    /**
     * 给EditText设置宽度权重
     */
    public void setEditTextWeight(EditText et, float newWeight) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, newWeight);
        layoutParams.gravity = Gravity.FILL_VERTICAL;
        et.setLayoutParams(layoutParams);
    }

    /**
     * 拆分单元格
     */
    public void split(int rowPos, int columnPos, int visibleColumnNum) {
        //检查越界
        FormUtil.checkRowIndexOutBound(rowPos, data.size());
        FormUtil.checkColumnIndexOutBound(columnPos, visibleColumnNum);

        //当前格
        Box currentBox = data.get(rowPos).getBoxList().get(columnPos);
        //当前格的右格
        Box rightBox = data.get(rowPos).getBoxList().get(columnPos + 1);
        //所在行
        ArrayList<Box> rowBean = data.get(rowPos).getBoxList();

        if (!currentBox.isExpand()) throw new IllegalStateException("不能拆分没有合并的单元格");

        //                                        思路
        //        先处理内部的组合格：从当前格子向右寻找第一个合并了右格并且被合并的格子
        //                           （isExpand和isNarrow均为true）
        //                                          ↓
        //                                        ←   →
        //               找到                                               找不到
        //                ↓                                                   ↓
        //           内部有组合格                                   内部没有组合格，全是小格子
        //                ↓                                                   ↓
        //            拆分组合格                           找当前格子右边最后一个被合并的格子(isNarrow=false)
        //                                                                     ↓
        //                                                                   ←  →
        //                                                            找到            找不到
        //                                                             ↓               ↓
        //                                                            拆分           提示并退出

        int nextBoxIndex = columnPos + 1;
        LogUtil.d("zhu", "从当前格子向右寻找第一个合并了右格并且被合并的格子");
        //从当前格子向右寻找第一个合并了右格并且被合并的格子
        for (int i = columnPos + 1; i < visibleColumnNum; i++) {
            Box box = rowBean.get(i);
            if (box.isExpand() && box.isNarrow()) {
                LogUtil.d("zhu", "找到从当前格子向右寻找第一个合并了右格并且被合并的格子啦 " + i);
                rightBox = box;
                nextBoxIndex = i;
                break;
            }
        }

        //右方没有合并了右格并且被合并的格子,就寻找当前格子右边最后一个被合并的格子
        if (nextBoxIndex == columnPos + 1 && (!rightBox.isExpand() || !rightBox.isNarrow())) {
            LogUtil.d("zhu", "找不到当前格子右边的第一个合并了右格并且被合并的格子,就寻找当前格子右边最后一个被合并的格子");
            //寻找当前格子右边最后一个被合并的格子
            for (int i = columnPos + 1; i < visibleColumnNum; i++) {
                Box box = rowBean.get(i);
                if (box.isNarrow()) {
                    rightBox = box;
                    nextBoxIndex = i;
                } else {
                    LogUtil.d("zhu", "找到当前格子右边最后一个被合并的格子" + nextBoxIndex);
                    break;
                }
            }
            if (nextBoxIndex == columnPos + 1 && !rightBox.isNarrow()) {
                LogUtil.d("zhu", "右边没有最后一个被合并的格子，退出");
                return;
            }
        }

        //找到目标格子了，开始拆分
        LogUtil.d("zhu", "找到目标格子了，开始拆分 " + nextBoxIndex);
        float weight = currentBox.getWeight() - rightBox.getWeight();
        LogUtil.d("zhu", "currentBox.getWeight() = " + currentBox.getWeight() + " rightBox.getWeight() = " + rightBox.getWeight() + " newWeight =" + weight);
        weight = weight < minWeight ? 1 : weight;
        currentBox.setWeight(weight);


        currentBox.setExpand(columnPos != nextBoxIndex - 1);

        rightBox.setNarrow(false);

        adapter.refresh();

        if (adapter.onBoxChangeListener != null)
            adapter.onBoxChangeListener.onBoxExpand(false, currentBox, rowPos, columnPos);
    }


    /**
     * 合并右单元格
     */
    public void mergeRightBox(int rowPos, int columnPos, int visibleColumnNum, boolean isTextMerge) {
        //检查越界
        FormUtil.checkRowIndexOutBound(rowPos, data.size());
        FormUtil.checkColumnIndexOutBound(columnPos, visibleColumnNum);
        if (columnPos == visibleColumnNum - 1)
            throw new IndexOutOfBoundsException("合并失败，选中格的右方没有格子了");

        Box currentBox = data.get(rowPos).getBoxList().get(columnPos);
        //思路：1. 寻找当前格子右边第一个还没被合并的格子(isNarrow=false)
        //      2.  找到  --> 合并
        //         找不到 --> 提示并退出

        // 1.寻找当前格子右边第一个还没被合并的格子
        //  默认是当前格子的右一格子
        Box nextNotNarrowBox = data.get(rowPos).getBoxList().get(columnPos + 1);
        int nextNotCombineBoxIndex = columnPos + 1;

        //遍历，从右一格子到可视的最后一个格子
        for (int i = columnPos + 1; i < visibleColumnNum; i++) {
            //如果遍历的格子没有被合并(isNarrow=false)
            if (!data.get(rowPos).getBoxList().get(i).isNarrow()) {
                //保存并退出循环
                nextNotNarrowBox = data.get(rowPos).getBoxList().get(i);
                nextNotCombineBoxIndex = i;
                break;
            }
        }

        //找不到（右方没有空余的格子），提示并退出
        if (nextNotCombineBoxIndex == columnPos + 1 && nextNotNarrowBox.isNarrow()) {
            Toast.makeText(context, "右方没有空余的格子了", Toast.LENGTH_SHORT).show();
            return;
        }

        //找到啦，开始合并
        //文字合并
        if (isTextMerge) {
            currentBox.setText(currentBox.getText() + nextNotNarrowBox.getText());
            nextNotNarrowBox.setText("");
        }

        //计算新权重
        float newWeight = currentBox.getWeight() + nextNotNarrowBox.getWeight();
        currentBox.setWeight(newWeight);

        //设置合并状态
        currentBox.setExpand(true);
        nextNotNarrowBox.setNarrow(true);

        //刷新
        adapter.refresh();

        //回调
        if (adapter.onBoxChangeListener != null)
            adapter.onBoxChangeListener.onBoxExpand(true, currentBox, rowPos, columnPos);
    }


    /**
     * 处理被选中的格子
     */
    public void handleSelect(EditText et, Box box, boolean formEnable, int selectColor) {
        if (!formEnable) return;
        //选中的格子设置背景颜色
        if (box.isSelected()) {
            et.setBackgroundColor(selectColor == 0 ? context.getColor(R.color.color_select) : selectColor);
        } else {
            et.setBackgroundColor(context.getColor(R.color.transparent));
        }
    }

    /**
     * 处理格子被合并
     */
    public void handleNarrow(EditText et, Box box, View v) {
        if (box.isNarrow()) {
            et.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
        }
    }


    /**
     * 给EditText设置文字
     */
    public void setText(EditText et, Box box, boolean formEnable, int etCursorDrawable, int etTextColor, int etTextSize) {
        //设置文字
        et.setText(box.getText());
        //设置enable
        et.setEnabled(formEnable && box.isEditable());
        //设置加粗
        et.setTypeface(box.isBold() ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);

        //设置EditText的光标颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            et.setTextCursorDrawable((etCursorDrawable != 0) ? etCursorDrawable : R.drawable.shape_main_color);
        }

        //设置EditText的文字颜色
        if (etTextColor != 0) {
            et.setTextColor(etTextColor);
        }

        //设置EditText的文字大小(单位sp)
        if (etTextSize != 0) {
            et.setTextSize(etTextSize);
        }

    }

    /**
     * 根据可见列数，设置格子是否可见
     */
    public void handleVisibleColumn(int columnNum, EditText et, View v, int visibleColumnNum) {
        et.setVisibility(columnNum < visibleColumnNum ? View.VISIBLE : View.GONE);
        v.setVisibility(columnNum < visibleColumnNum ? View.VISIBLE : View.GONE);
    }

    /**
     * 处理要加粗的格子
     */
    public void setBold(int rowPos, int columnPos, boolean bold) {
        FormUtil.checkRowIndexOutBound(rowPos, data.size());
        FormUtil.checkColumnIndexOutBound(columnPos, adapter.getVisibleColumnNum());

        Box box = data.get(rowPos).getBoxList().get(columnPos);
        box.setBold(bold);
        adapter.notifyDataSetChanged();

        if (adapter.onBoxChangeListener != null)
            adapter.onBoxChangeListener.onBoxBold(bold, box, rowPos, columnPos);
    }

    /**
     * 设置目标格的权重
     *
     * @param rowPos    目标格的行号
     * @param columnPos 目标格的列号
     * @param weight    宽度权重
     */
    public void setBoxWeight(int rowPos, int columnPos, float weight) {
        data.get(rowPos).getBoxList().get(columnPos).setWeight(weight);
        adapter.refresh();
    }
}
