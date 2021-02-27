package com.example.formviewdemo.customform.form_view.listener;


import com.example.formviewdemo.customform.form_view.model.Box;

public interface OnMenuListener {

    /**
     * 用户长按单元格，需要打开单元格菜单
     *
     * @param box       选中单元格的数据
     * @param rowPos    选中单元格的行号
     * @param columnPos 选中单元格的列号
     * @param menu      推荐的菜单选项
     * @return true：使用自带的默认弹窗   false：使用你自己的弹窗
     */
    boolean onBoxMenu(Box box, int rowPos, int columnPos, String[] menu);


    /**
     * 用户点击列按钮，需要打开列菜单
     * 建议打开弹窗前，选中目标列 mFormView.selectColumn(rowPos, columnPos, true);
     * 关闭弹窗后，取消选中目标列 mFormView.selectColumn(rowPos, columnPos, false);
     *
     * @param box              目标单元格的数据
     * @param rowPos           目标单元格的行号
     * @param columnPos        目标单元格的列号
     * @param totalColumnNum   可见列数
     * @param menu             推荐的菜单选项
     * @return true：使用自带的默认弹窗   false：使用你自己的弹窗
     */
    boolean onColumnMenu(Box box, int rowPos, int columnPos, int totalColumnNum, String[] menu);


    /**
     * 用户点击行按钮，需要打开行菜单
     * 建议打开弹窗前，选中目标行 mFormView.selectRow(rowPos, true);
     * 关闭弹窗后，取消选中目标行 mFormView.selectRow(rowPos, false);
     *
     * @param box           目标单元格的数据
     * @param rowPos        目标单元格的行号
     * @param columnPos     目标单元格的列号
     * @param totalRowNum   总行数
     * @param menu          推荐的菜单选项
     * @return true：使用自带的默认弹窗   false：使用你自己的弹窗
     */
    boolean onRowMenu(Box box, int rowPos, int columnPos, int totalRowNum, String[] menu);

}
