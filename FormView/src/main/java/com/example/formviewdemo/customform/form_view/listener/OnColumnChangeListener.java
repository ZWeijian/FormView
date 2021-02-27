package com.example.formviewdemo.customform.form_view.listener;


import com.example.formviewdemo.customform.form_view.model.Box;

import java.util.ArrayList;

/**
 * 监听列变化的监听器
 */
public interface OnColumnChangeListener {

    /**
     * 从columnPos开始，增加range列
     *
     * @param columnPos        增加列的开始位置
     * @param range            增加的列数
     * @param totalColumnNum   增加后的总列数
     */
    void onColumnAdd(int columnPos, int range, int totalColumnNum);


    /**
     * 从columnPos开始，删除range列
     *
     * @param deleteColumnList 删除列的数据
     * @param columnPos        删除列的开始位置
     * @param range            删除的列数
     * @param totalColumnNum   删除后的总列数
     */
    void onColumnDelete(ArrayList<ArrayList<Box>> deleteColumnList, int columnPos, int range, int totalColumnNum);


}