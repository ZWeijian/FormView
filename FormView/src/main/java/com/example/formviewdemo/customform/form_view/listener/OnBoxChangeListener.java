package com.example.formviewdemo.customform.form_view.listener;


import com.example.formviewdemo.customform.form_view.model.Box;

/**
 * 监听格子变化的监听器
 */
public interface OnBoxChangeListener {

    /**
     * 单元格的宽度权重发生变化
     *
     * @param box       单元格的数据
     * @param rowPos    单元格的行号
     * @param columnPos 单元格的列号
     */
    void onWeightChange(Box box, int rowPos, int columnPos);


    /**
     * 单元格进行合并或者拆分
     *
     * @param isExpand  true:格子合并  false:格子拆分
     * @param box       单元格的数据
     * @param rowPos    单元格的行号
     * @param columnPos 单元格的列号
     */
    void onBoxExpand(boolean isExpand, Box box, int rowPos, int columnPos);


    /**
     * 单元格进行加粗或者取消加粗
     *
     * @param isBold    true：字体加粗  false：字体取消加粗
     * @param box       单元格的数据
     * @param rowPos    单元格的行号
     * @param columnPos 单元格的列
     */
    void onBoxBold(boolean isBold, Box box, int rowPos, int columnPos);


}
