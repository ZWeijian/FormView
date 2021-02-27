package com.example.formviewdemo.customform.form_view.listener;


import com.example.formviewdemo.customform.form_view.model.Row;

/**
 * 监听行变化的监听器
 */
public interface OnRowChangeListener {

    /**
     * 增加某一行
     *
     * @param row         新增的行
     * @param rowPos      新增行的行号
     * @param totalRowNum 增加一行后的总行数
     */
    void onRowAdd(Row row, int rowPos, int totalRowNum);

    /**
     * 删除某一行
     *
     * @param row           删除的行
     * @param rowPos        删除行的行号
     * @param totalRowNum   删除一行后的总行数
     */
    void onRowDelete(Row row, int rowPos, int totalRowNum);

    /**
     * 平分某一行
     *
     * @param row    平分行的数据
     * @param rowPos 平分行的行号
     */
    void onRowDivideEqually(Row row, int rowPos);


    /**
     * 重置某一行
     *
     * @param row    重置行的数据
     * @param rowPos 重置行的行号
     */
    void onRowReset(Row row, int rowPos);

}
