package com.example.formviewdemo.customform.form_view.function;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.formviewdemo.R;
import com.example.formviewdemo.customform.form_view.listener.OnBoxChangeListener;
import com.example.formviewdemo.customform.form_view.listener.OnColumnChangeListener;
import com.example.formviewdemo.customform.form_view.listener.OnMenuListener;
import com.example.formviewdemo.customform.form_view.listener.OnRowChangeListener;
import com.example.formviewdemo.customform.form_view.model.Row;

import java.util.ArrayList;

import static com.example.formviewdemo.customform.form_view.function.FormAdapter.maxWeight;


public class FormView extends LinearLayout {

    RecyclerView rv;

    //RecyclerView的适配器
    private FormAdapter adapter;

    //数据
    ArrayList<Row> mRows = new ArrayList<>();

    //控制列的菜单
    ArrayList<ImageView> colMenuList = new ArrayList<>();

    //上下文
    private final Context context;

    public FormView(Context context) {
        this(context, null);
    }

    public FormView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FormView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(context);
        initAttr(context, attrs);
    }

    private void initAttr(final Context context, @Nullable final AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FormView);

        //表格背景颜色
        int bgColor = ta.getColor(R.styleable.FormView_form_bg_color, Color.TRANSPARENT);
        adapter.setBgColor(bgColor);

        //表格是否可编辑
        adapter.setFormEnable(ta.getBoolean(R.styleable.FormView_form_enable, true));

        //表格默认列数
        int totalColumnNum = ta.getInt(R.styleable.FormView_form_total_column_num, 3);
        adapter.setVisibleColumnNum(totalColumnNum);

        //是否显示行按钮
        boolean showRowMenu = ta.getBoolean(R.styleable.FormView_form_show_row_menu, true);
        adapter.setShowRowMenu(showRowMenu, false);

        //是否显示列按钮
        boolean showColumnMenu = ta.getBoolean(R.styleable.FormView_form_show_column_menu, true);
        adapter.setShowColumnMenu(showColumnMenu, false);

        //行按钮的图标
        int rowMenuIcon = ta.getResourceId(R.styleable.FormView_form_row_menu_icon, R.mipmap.row_menu_icon);
        adapter.setRowMenuResId(rowMenuIcon, false);

        //列按钮的图标
        int columnMenuIcon = ta.getResourceId(R.styleable.FormView_form_column_menu_icon, R.mipmap.col_menu_icon);
        setColMenuIcon(columnMenuIcon);

        //字体大小
        int textSize = ta.getDimensionPixelSize(R.styleable.FormView_form_text_size, 18);
        adapter.setEtTextSize(textSize, false);

        //字体颜色
        int textColor = ta.getColor(R.styleable.FormView_form_text_color, Color.BLACK);
        adapter.setEtTextColor(textColor, false);

        //光标颜色
        int cursorColor = ta.getResourceId(R.styleable.FormView_form_cursor_drawable, R.drawable.shape_main_color);
        adapter.setEtCursorColor(cursorColor, false);

        //单元格的最大宽度权重
        float maxWeight = ta.getFloat(R.styleable.FormView_form_max_width_weight, 8f);
        setMaxWeight(maxWeight);

        //单元格被选中的颜色
        int selectColor = ta.getColor(R.styleable.FormView_form_text_color, 0);
        adapter.setSelectColor(selectColor, false);

        ta.recycle();
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        //加载布局
        LayoutInflater.from(context).inflate(R.layout.layout_form_view, this, true);

        //初始化控制列的菜单
        initColMenu();

        //初始化RecyclerView
        initRecyclerView();
    }

    /**
     * 初始化控制列的菜单
     */
    private void initColMenu() {
        colMenuList.add((ImageView) findViewById(R.id.iv_column_1));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_2));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_3));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_4));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_5));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_6));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_7));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_8));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_9));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_10));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_11));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_12));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_13));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_14));
        colMenuList.add((ImageView) findViewById(R.id.iv_column_15));

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.openColumnMenu();
            }
        };


        for (ImageView iv : colMenuList) {
            iv.setOnClickListener(onClickListener);
        }

    }


    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FormAdapter(context, mRows, rv, colMenuList, (LinearLayout) findViewById(R.id.ll_column_menu));
        rv.setAdapter(adapter);
        rv.setNestedScrollingEnabled(false);
    }


    //========================================暴露给用户使用的方法===================================================

    /**
     * 设置数据
     */
    public void setData(ArrayList<Row> rows) {
        adapter.setData(rows);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获得数据
     */
    public ArrayList<Row> getData() {
        return (ArrayList<Row>) adapter.getData();
    }

    /**
     * 刷新
     */
    public void refresh() {
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置格子变化监听器
     */
    public void setOnBoxChangeListener(OnBoxChangeListener onBoxChangeListener) {
        adapter.setOnBoxChangeListener(onBoxChangeListener);
    }

    /**
     * 设置行变化的监听
     */
    public void setOnRowChangeListener(OnRowChangeListener onRowChangeListener) {
        adapter.setOnRowChangeListener(onRowChangeListener);
    }


    /**
     * 设置列变化的监听
     */
    public void setOnColumnChangeListener(OnColumnChangeListener onColumnChangeListener) {
        adapter.setOnColumnChangeListener(onColumnChangeListener);
    }


    /**
     * 设置可见列数，不设置则默认为3
     */
    public void setTotalColumnNum(int visibleColumnNum) {
        if (visibleColumnNum <= 0 || visibleColumnNum > 15)
            throw new IndexOutOfBoundsException("FormView 可见列数不能小于等于0或者大于15");
        adapter.setVisibleColumnNum(visibleColumnNum);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取当前可见列数
     */
    public int getTotalColumnNum() {
        return adapter.getVisibleColumnNum();
    }

    /**
     * 获取当前可见行数
     */
    public int getTotalRowNum() {
        return adapter.getVisibleRowNum();
    }

    /**
     * 设置表格是否可编辑，默认可以编辑
     */
    public void setFormEnable(boolean formEnable) {
        adapter.setFormEnable(formEnable);
        adapter.refresh();
    }

    /**
     * 获取表格是否可编辑
     */
    public boolean getFormEnable() {
        return adapter.getFormEnable();
    }

    /**
     * 表格的文字是否发生了变化
     */
    public boolean isTextChange() {
        return adapter.isTextChange();
    }

    /**
     * 设置表格的文字是否发生了变化
     */
    public void setTextChange(boolean textChange) {
        adapter.setTextChange(textChange);
    }

    /**
     * 是否显示列按钮，默认显示
     */
    public void setShowColumnMenu(boolean showColumnMenu) {
        adapter.setShowColumnMenu(showColumnMenu, true);
    }

    /**
     * 获取是否显示列按钮
     */
    public boolean isShowColumnMenu() {
        return adapter.isShowColumnMenu();
    }

    /**
     * 是否显示行按钮，默认显示
     */
    public void setShowRowMenu(boolean showRowMenu) {
        adapter.setShowRowMenu(showRowMenu, true);
    }

    /**
     * 获取是否显示行按钮
     */
    public boolean isShowRowMenu() {
        return adapter.isShowRowMenu();
    }


    /**
     * 添加一行
     *
     * @param rowPos 添加的行号
     * @return 返回增加后的总行数
     */
    public int addRow(int rowPos) {
        return adapter.addRow(rowPos);
    }


    /**
     * 最下面添加一行
     *
     * @return 增加后的总行数
     */
    public int addRowAtTheEnd() {
        return adapter.addRowAtTheEnd();
    }

    /**
     * 减少最下面一行
     *
     * @return 减少后的总行数
     */
    public int deleteRowAtTheEnd() {
        return adapter.deleteRowAtTheEnd();
    }

    /**
     * 减少最下面一行
     *
     * @return 减少后的总行数
     */
    public int deleteRow(int rowPos) {
        return adapter.deleteRow(rowPos);
    }

    /**
     * 最右方减少range列
     *
     * @return columnNum 减少后的可见列数
     */
    public int deleteColumnAtTheEnd(int range) {
        return adapter.deleteColumnAtTheEnd(range);
    }

    /**
     * 删除列
     *
     * @param rowPos    选中格的行号
     * @param columnPos 选中格的列号
     */
    public int deleteColumn(int rowPos, int columnPos) {
        return adapter.deleteColumn(rowPos, columnPos);
    }

    /**
     * 表格最右方增加range列
     *
     * @return columnNum 增加后的可见列数
     */
    public int addColumnAtTheEnd(int range) {
        return adapter.addColumn(getTotalColumnNum(), range);
    }

    /**
     * 从columnPos开始，增加range列
     *
     * @param columnPos 增加列的开始坐标
     * @param range     增加列数
     * @return 增加后的可见列数
     */
    public int addColumn(int columnPos, int range) {
        return adapter.addColumn(columnPos, range);
    }


    /**
     * 打印所有格子
     */
    public void printBoxList() {
        adapter.printBoxList();
    }

    /**
     * 滑动到某位置
     */
    public void scrollToPosition(int pos) {
        rv.scrollToPosition(pos);
    }

    /**
     * 设置点击行按钮、点击列按钮以及长按单元格的菜单打开监听器
     * 可以监听各菜单的打开
     * 也可以不用自带弹窗，使用你自己的弹窗
     */
    public void setOnMenuListener(OnMenuListener onMenuListener) {
        adapter.setOnMenuListener(onMenuListener);
    }

    /**
     * 使某一列选中或者取消选中
     */
    public void selectColumn(int rowPos, int columnPos, boolean select) {
        adapter.selectColumn(rowPos, columnPos, select);
    }


    /**
     * 使某一行选中或者取消选中
     */
    public void selectRow(int rowPos, boolean select) {
        adapter.selectRow(rowPos, select);
    }

    /**
     * 重置某一行的格子
     */
    public void resetRow(int rowPos) {
        adapter.resetRow(rowPos);
    }

    /**
     * 平分某一行的格子
     */
    public void equalRow(int rowPos) {
        adapter.equalRow(rowPos);
    }


    /**
     * 目标格合并目标格的右格
     *
     * @param rowPos      目标格的行号
     * @param columnPos   目标格的列号
     * @param isTextMerge 文字是否合并 true：右格的文字合并到左格，右格文字清空  false：文字不合并
     */
    public void mergeRightBox(int rowPos, int columnPos, boolean isTextMerge) {
        adapter.mergeRightBox(rowPos, columnPos, isTextMerge);
    }

    /**
     * 拆分目标格
     *
     * @param rowPos    合并格的行号
     * @param columnPos 合并格的列号
     */
    public void splitBox(int rowPos, int columnPos) {
        adapter.splitBox(rowPos, columnPos);
    }

    /**
     * 设置目标格的权重
     *
     * @param rowPos    目标格的行号
     * @param columnPos 目标格的列号
     * @param weight    宽度权重
     */
    public void setBoxWeight(int rowPos, int columnPos, float weight) {
        adapter.setBoxWeight(rowPos, columnPos, weight);
    }


    /**
     * 设置目标格是否加粗
     *
     * @param rowPos    目标格的行号
     * @param columnPos 目标格的列号
     * @param bold      true加粗 ，false取消加粗
     */
    public void setBoxBold(int rowPos, int columnPos, boolean bold) {
        adapter.setBoxBold(rowPos, columnPos, bold);
    }

    /**
     * 设置单元格的最大宽度权重
     */
    public void setMaxWeight(float maxWeight) {
        if (maxWeight <= 0) throw new IllegalStateException("宽度权重不能小于等于零 maxWeight=" + maxWeight);
        if (maxWeight <= FormAdapter.minWeight)
            throw new IllegalStateException("最大宽度权重必须大于最小宽度权重 maxWeight=" + maxWeight + "   minWeight=" + FormAdapter.minWeight);
        FormAdapter.maxWeight = maxWeight;
    }

    /**
     * 获取单元格的最大宽度权重
     */
    public float getMaxWeight() {
        return maxWeight;
    }

//    /**
//     * 设置单元格的最小宽度权重
//     */
//    public void setMinWeight(float minWeight) {
//        if (minWeight <= 0) throw new IllegalStateException("宽度权重需要是正数 minWeight=" + minWeight);
//        if (minWeight >= maxWeight)
//            throw new IllegalStateException("最小宽度权重小于最大宽度权重  minWeight=" + minWeight + "   maxWeight=" + maxWeight);
//        FormAdapter.minWeight = minWeight;
//    }

    /**
     * 获取单元格的最小宽度权重
     */
    public float getMinWeight() {
        return FormAdapter.minWeight;
    }


    /*
     * 设置列按钮的图标
     */
    public void setColMenuIcon(@DrawableRes int resId) {
        for (ImageView imageView : colMenuList) {
            imageView.setImageResource(resId);
        }
    }

    public void setColMenuIcon(Drawable drawable) {
        for (ImageView imageView : colMenuList) {
            imageView.setImageDrawable(drawable);
        }
    }

    public void setColMenuIcon(Bitmap bitmap) {
        for (ImageView imageView : colMenuList) {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 设置行按钮的图标
     */
    public void setRowMenuIcon(@DrawableRes int resId) {
        adapter.setRowMenuResId(resId, true);
    }

    public void setRowMenuIcon(Drawable drawable) {
        adapter.setRowMenuDrawable(drawable);
    }

    public void setRowMenuIcon(Bitmap bitmap) {
        adapter.setRowMenuBitmap(bitmap);
    }


//    /**
//     * 设置单元格的光标颜色
//     */
//    public void setBoxCursor(@DrawableRes int color) {
//        adapter.setEtCursorColor(color, true);
//    }

    /**
     * 设置单元格的字体颜色
     */
    public void setBoxTextColor(@ColorInt int color) {
        adapter.setEtTextColor(color, true);
    }

    /**
     * 设置单元格的字体大小，单位sp
     */
    public void setBoxTextSize(int textSize) {
        adapter.setEtTextSize(textSize, true);
    }

    /*
     * 设置表格背景颜色
     */
    public void setBgColor(@ColorInt int color) {
        adapter.setBgColor(color);
    }

    /**
     * 设置单元格被选中的颜色
     */
    public void setSelectColor(@ColorInt int color) {
        adapter.setSelectColor(color, true);
    }

}
