package com.example.formviewdemo.customform.form_view.function;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.formviewdemo.R;
import com.example.formviewdemo.customform.form_view.base.BaseViewHolder;
import com.example.formviewdemo.customform.form_view.base.RecyclerAdapter;
import com.example.formviewdemo.customform.form_view.listener.OnBoxChangeListener;
import com.example.formviewdemo.customform.form_view.listener.OnColumnChangeListener;
import com.example.formviewdemo.customform.form_view.listener.OnMenuListener;
import com.example.formviewdemo.customform.form_view.listener.OnRowChangeListener;
import com.example.formviewdemo.customform.form_view.model.Box;
import com.example.formviewdemo.customform.form_view.model.Row;

import java.util.ArrayList;

/**
 * @Package: com.successdata.supervisionassistant.ui.adapter
 * @ClassName: CustomStyle2Adapter
 * @Description: 自定义模板的适配器
 * @Author: Zhu
 * @CreateDate: 2020/7/24 16:19
 */
public class FormAdapter extends RecyclerAdapter<Row> {

    //数据
    ArrayList<Row> data;

    //可见列数
    public int visibleColumnNum = 3;

    //单元格的最小宽度权重
    public static float minWeight = 1f;

    //单元格的最大宽度权重
    public static float maxWeight = 8f;

    //Adapter所属的RecyclerView
    private final RecyclerView rv;

    //是否允许编辑，默认可以
    private boolean formEnable = true;

    //表格文字是否已经变化
    private boolean isTextChange = false;

    //操作行的工具类
    private final FormController formController;

    //操作单个格子的工具类
    private final BoxController boxController;

    //菜单监听器
    public OnMenuListener onMenuListener;

    //格子变化的监听器
    public OnBoxChangeListener onBoxChangeListener;

    //列变化的监听器
    public OnColumnChangeListener onColumnChangeListener;

    //行变化的监听器
    public OnRowChangeListener onRowChangeListener;

    //列菜单的父布局
    private LinearLayout llColumnMenu;

    private Context context;

    //行菜单的按钮图标
    private int rowMenuResId;
    private Drawable rowMenuDrawable;
    private Bitmap rowMenuBitmap;

    //EditText的光标颜色
    private int etCursorDrawable;
    //EditText的文字颜色
    private int etTextColor;
    //格子被选中的颜色
    private int selectColor;
    //EditText的文字大小
    private int etTextSize;

    //背景颜色
    private int bgColor;


    public FormAdapter(Context context, ArrayList<Row> data, RecyclerView rv, ArrayList<ImageView> colMenuList, LinearLayout llColumnMenu) {
        super(data, R.layout.layout_item_form);
        this.data = data;
        this.rv = rv;
        this.context = context;
        this.llColumnMenu = llColumnMenu;
        formController = new FormController(context, this, data, colMenuList);
        boxController = new BoxController(context, this, data);
    }


    @Override
    public BaseViewHolder holder(View view, int viewType) {
        return new ViewHolder(view);
    }


    public class ViewHolder extends BaseViewHolder<Row> {
        View vTop = itemView.findViewById(R.id.top_line);
        ImageView ivRow = itemView.findViewById(R.id.iv);
        LinearLayout llRowForm = itemView.findViewById(R.id.ll_row_form);

        View v1 = itemView.findViewById(R.id.v_1);
        EditText et1 = itemView.findViewById(R.id.et_1);
        View v2 = itemView.findViewById(R.id.v_2);
        EditText et2 = itemView.findViewById(R.id.et_2);
        View v3 = itemView.findViewById(R.id.v_3);
        EditText et3 = itemView.findViewById(R.id.et_3);
        View v4 = itemView.findViewById(R.id.v_4);
        EditText et4 = itemView.findViewById(R.id.et_4);
        View v5 = itemView.findViewById(R.id.v_5);
        EditText et5 = itemView.findViewById(R.id.et_5);
        View v6 = itemView.findViewById(R.id.v_6);
        EditText et6 = itemView.findViewById(R.id.et_6);
        View v7 = itemView.findViewById(R.id.v_7);
        EditText et7 = itemView.findViewById(R.id.et_7);
        View v8 = itemView.findViewById(R.id.v_8);
        EditText et8 = itemView.findViewById(R.id.et_8);
        View v9 = itemView.findViewById(R.id.v_9);
        EditText et9 = itemView.findViewById(R.id.et_9);
        View v10 = itemView.findViewById(R.id.v_10);
        EditText et10 = itemView.findViewById(R.id.et_10);
        View v11 = itemView.findViewById(R.id.v_11);
        EditText et11 = itemView.findViewById(R.id.et_11);
        View v12 = itemView.findViewById(R.id.v_12);
        EditText et12 = itemView.findViewById(R.id.et_12);
        View v13 = itemView.findViewById(R.id.v_13);
        EditText et13 = itemView.findViewById(R.id.et_13);
        View v14 = itemView.findViewById(R.id.v_14);
        EditText et14 = itemView.findViewById(R.id.et_14);
        View v15 = itemView.findViewById(R.id.v_15);
        EditText et15 = itemView.findViewById(R.id.et_15);

        TextWatcher tw1, tw2, tw3, tw4, tw5, tw6, tw7, tw8, tw9, tw10, tw11, tw12, tw13, tw14, tw15;

        //存放这一行15个EditText的容器
        private final ArrayList<EditText> etList = new ArrayList<>(15);
        //存放这一行15个分割线的容器
        private final ArrayList<View> lineList = new ArrayList<>(15);
        //存放这一行15个TextWatcher的容器
        private final ArrayList<TextWatcher> twList = new ArrayList<>(15);


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void build(Row row, final int rowPos) {
            //装载容器
            loadList();

            //如果这一行所有格子都不为1且权重相同，就全部重置为1
            formController.isNeedEqualRowWeight(row, visibleColumnNum, formEnable, rowPos);

            //遍历此行的所有格子
            for (int i = 0; i < etList.size(); i++) {
                //这个格子的数据
                Box box = row.getBoxList().get(i);
                //这个格子的EditText
                EditText et = etList.get(i);
                //这个格子右边的竖线
                View v = lineList.get(i);

                //RecyclerView的复用机制会使EditText的数据混乱，所以要绑定数据，进行处理
                formController.handleEditText(rowPos, et, twList.get(i), i, visibleColumnNum, ivRow, formController.showRowMenu, formController.showColumnMenu, llColumnMenu);
                //给EditText设置文字
                boxController.setText(et, box, formEnable, etCursorDrawable, etTextColor, etTextSize);
                //根据可见列数，设置格子是否可见
                boxController.handleVisibleColumn(i, et, v, visibleColumnNum);
                //设置双指缩放监听
                formController.setScale(et, row, boxController, etList);
                //设置长按菜单
                formController.setLongClick(et, rowPos, etList, lineList, visibleColumnNum, boxController);
                //给每个EditText设置宽度权重
                boxController.setEditTextWeight(et, box.getWeight());
                //处理被合并的格子
                boxController.handleNarrow(et, box, v);
                //处理被选中的格子
                boxController.handleSelect(et, box, formEnable, selectColor);
                //设置行按钮图标
                setRowMenuIcon(ivRow);
            }
            setTextChange(false);
            vTop.setVisibility(rowPos == 0 ? View.VISIBLE : View.GONE);

            ivRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    formController.openRowMenu(rowPos, visibleColumnNum);
                }
            });

            if (bgColor != 0) {
                llRowForm.setBackgroundColor(bgColor);
            }

        }

        //装载容器
        private void loadList() {
            if (etList.size() == 0) {
                //装载EditText
                etList.add(et1);
                etList.add(et2);
                etList.add(et3);
                etList.add(et4);
                etList.add(et5);
                etList.add(et6);
                etList.add(et7);
                etList.add(et8);
                etList.add(et9);
                etList.add(et10);
                etList.add(et11);
                etList.add(et12);
                etList.add(et13);
                etList.add(et14);
                etList.add(et15);

                //装载分割线
                lineList.add(v1);
                lineList.add(v2);
                lineList.add(v3);
                lineList.add(v4);
                lineList.add(v5);
                lineList.add(v6);
                lineList.add(v7);
                lineList.add(v8);
                lineList.add(v9);
                lineList.add(v10);
                lineList.add(v11);
                lineList.add(v12);
                lineList.add(v13);
                lineList.add(v14);
                lineList.add(v15);

                //装载TextWatcher
                twList.add(tw1);
                twList.add(tw2);
                twList.add(tw3);
                twList.add(tw4);
                twList.add(tw5);
                twList.add(tw6);
                twList.add(tw7);
                twList.add(tw8);
                twList.add(tw9);
                twList.add(tw10);
                twList.add(tw11);
                twList.add(tw12);
                twList.add(tw13);
                twList.add(tw14);
                twList.add(tw15);
            }
        }
    }

    /**
     * 设置行按钮的图标
     */
    private void setRowMenuIcon(ImageView iv) {
        if (rowMenuResId != 0) {
            iv.setImageResource(rowMenuResId);
        } else if (rowMenuBitmap != null) {
            iv.setImageBitmap(rowMenuBitmap);
        } else if (rowMenuDrawable != null) {
            iv.setImageDrawable(rowMenuDrawable);
        } else {
            iv.setImageResource(R.mipmap.row_menu_icon);
        }
    }

    /**
     * 刷新
     */
    public void refresh() {
        rv.post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 打开列菜单
     */
    public void openColumnMenu() {
        formController.openColumnMenu(visibleColumnNum);
    }

    /**
     * 表格的文字是否发生了变化
     */
    public boolean isTextChange() {
        return isTextChange;
    }

    public void setTextChange(boolean textChange) {
        isTextChange = textChange;
    }

    /**
     * 最右方增加num列
     *
     * @param startPos 增加列的开始位置
     * @param range    增加列数
     * @return visibleColumnNum 增加后的可见列数
     */
    public int addColumn(int startPos, int range) {
        rv.requestFocus();
        visibleColumnNum = formController.addColumn(startPos, range, visibleColumnNum);
        return visibleColumnNum;
    }

    /**
     * 最右方减少num列
     *
     * @return visibleColumnNum 减少后的可见列数
     */
    public int deleteColumnAtTheEnd(int num) {
        rv.requestFocus();
        visibleColumnNum = formController.deleteColumnAtTheEnd(num, visibleColumnNum);
        return visibleColumnNum;
    }

    /**
     * 删除列
     *
     * @param rowPos    选中格的行号
     * @param columnPos 选中格的列号
     */
    public int deleteColumn(int rowPos, int columnPos) {
        rv.requestFocus();
        formController.deleteColumn(rowPos, columnPos, visibleColumnNum);
        return getVisibleColumnNum();
    }

    /**
     * 最下面添加一行
     */
    public int addRowAtTheEnd() {
        rv.requestFocus();
        formController.addRow(data.size());
        return data.size();
    }

    /**
     * 新增一行
     */
    public int addRow(int rowPos) {
        rv.requestFocus();
        formController.addRow(rowPos);
        return data.size();
    }

    /**
     * 减少最下面一行
     */
    public int deleteRowAtTheEnd() {
        rv.requestFocus();
        formController.deleteRowAtTheEnd();
        return data.size();
    }

    /**
     * 删除某一行
     */
    public int deleteRow(int rowPos) {
        rv.requestFocus();
        formController.deleteRow(rowPos);
        return data.size();
    }

    /**
     * 获取可见行数
     */
    public int getVisibleRowNum() {
        return data.size();
    }

    /**
     * 获取可见列数
     */
    public int getVisibleColumnNum() {
        return visibleColumnNum;
    }

    /**
     * 设置可见列数
     */
    public void setVisibleColumnNum(int visibleColumnNum) {
        this.visibleColumnNum = visibleColumnNum;
    }

    /**
     * 设置表格是否可编辑
     */
    public void setFormEnable(boolean formEnable) {
        this.formEnable = formEnable;
    }

    /**
     * 获取表格是否可编辑
     */
    public boolean getFormEnable() {
        return formEnable;
    }


    /**
     * 设置格子变化监听器
     */
    public void setOnBoxChangeListener(OnBoxChangeListener onBoxChangeListener) {
        this.onBoxChangeListener = onBoxChangeListener;
    }


    /**
     * 设置列变化监听器
     */
    public void setOnColumnChangeListener(OnColumnChangeListener onColumnChangeListener) {
        this.onColumnChangeListener = onColumnChangeListener;
    }

    /**
     * 设置行变化监听器
     */
    public void setOnRowChangeListener(OnRowChangeListener onRowChangeListener) {
        this.onRowChangeListener = onRowChangeListener;
    }

    /**
     * 是否显示列菜单，默认显示
     */
    public void setShowColumnMenu(boolean setShowColumnMenu, boolean isRefresh) {
        formController.showColumnMenu = setShowColumnMenu;
        if (isRefresh) {
            notifyDataSetChanged();
        }
    }

    /**
     * 获取显示列菜单
     */
    public boolean isShowColumnMenu() {
        return formController.showColumnMenu;
    }


    /**
     * 是否显示行菜单，默认显示
     */
    public void setShowRowMenu(boolean showRowMenu, boolean isRefresh) {
        formController.showRowMenu = showRowMenu;
        if (isRefresh) {
            notifyDataSetChanged();
        }
    }

    /**
     * 获取显示行菜单
     */
    public boolean isShowRowMenu() {
        return formController.showRowMenu;
    }

    /**
     * 打印所有格子
     */
    public void printBoxList() {
        formController.printBoxList(visibleColumnNum);
    }

    /**
     * 使某一列选中或者取消选中
     */
    public void selectColumn(int rowPos, int columnPos, boolean select) {
        formController.selectColumn(rowPos, columnPos, select);
    }


    /**
     * 使某一行选中或者取消选中
     */
    public void selectRow(int rowPos, boolean select) {
        formController.selectRow(rowPos, select);
    }

    /**
     * 设置菜单监听，使用自定义弹窗
     */
    public void setOnMenuListener(OnMenuListener onMenuListener) {
        this.onMenuListener = onMenuListener;
    }

    /**
     * 重置某一行的格子
     */
    public void resetRow(int rowPos) {
        formController.resetRow(rowPos);
    }

    /**
     * 平分某一行的格子
     */
    public void equalRow(int rowPos) {
        formController.equalRow(rowPos);
    }


    /**
     * 合并目标格的右格
     *
     * @param rowPos      目标格的行号
     * @param columnPos   目标格的列号
     * @param isTextMerge 文字是否合并
     */
    public void mergeRightBox(int rowPos, int columnPos, boolean isTextMerge) {
        boxController.mergeRightBox(rowPos, columnPos, visibleColumnNum, isTextMerge);
    }

    /**
     * 拆分目标格
     *
     * @param rowPos    合并格的行号
     * @param columnPos 合并格的列号
     */
    public void splitBox(int rowPos, int columnPos) {
        boxController.split(rowPos, columnPos, visibleColumnNum);
    }

    /**
     * 设置目标格的权重
     *
     * @param rowPos    目标格的行号
     * @param columnPos 目标格的列号
     * @param weight    宽度权重
     */
    public void setBoxWeight(int rowPos, int columnPos, float weight) {
        boxController.setBoxWeight(rowPos, columnPos, weight);
    }

    /**
     * 设置目标格的加粗
     *
     * @param rowPos    目标格的行号
     * @param columnPos 目标格的列号
     * @param bold      是否加粗
     */
    public void setBoxBold(int rowPos, int columnPos, boolean bold) {
        boxController.setBold(rowPos, columnPos, bold);
    }


    // 行菜单的按钮图标
    public void setRowMenuResId(int rowMenuResId, boolean isRefresh) {
        this.rowMenuResId = rowMenuResId;
        this.rowMenuDrawable = null;
        this.rowMenuBitmap = null;
        if (isRefresh) {
            notifyDataSetChanged();
        }
    }

    public void setRowMenuDrawable(Drawable rowMenuDrawable) {
        this.rowMenuResId = 0;
        this.rowMenuDrawable = rowMenuDrawable;
        this.rowMenuBitmap = null;
        notifyDataSetChanged();
    }

    public void setRowMenuBitmap(Bitmap rowMenuBitmap) {
        this.rowMenuResId = 0;
        this.rowMenuDrawable = null;
        this.rowMenuBitmap = rowMenuBitmap;
        notifyDataSetChanged();
    }


    //设置EditText的光标颜色
    public void setEtCursorColor(int color, boolean isRefresh) {
        this.etCursorDrawable = color;
        if (isRefresh) {
            notifyDataSetChanged();
        }
    }

    //设置EditText的文字颜色
    public void setEtTextColor(int etTextColor, boolean isRefresh) {
        this.etTextColor = etTextColor;
        if (isRefresh) {
            notifyDataSetChanged();
        }
    }

    //设置EditText的文字大小
    public void setEtTextSize(int etTextSize, boolean isRefresh) {
        this.etTextSize = etTextSize;
        if (isRefresh) {
            notifyDataSetChanged();
        }
    }

    //设置背景
    public void setBgColor(int color) {
        this.bgColor = color;
    }

    /**
     * 设置单元格被选中的颜色
     */
    public void setSelectColor(int color, boolean isRefresh) {
        this.selectColor = color;
        if (isRefresh) {
            notifyDataSetChanged();
        }
    }

}

