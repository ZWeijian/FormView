package com.example.formviewdemo.customform.form_view.model;

import android.view.View;

import com.example.formviewdemo.customform.form_view.FormUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Package: com.successdata.supervisionassistant.dto.mission
 * @ClassName: CustomStyle2Bean
 * @Description: 一行的数据bean
 * @Author: Zhu
 * @CreateDate: 2020/7/24 16:26
 */
public class Row implements Serializable {

    //一行的格子
    private ArrayList<Box> boxList = new ArrayList<>(15);

    //一行的id
    private String rowId;
    /**
     * 删除按钮的可见性
     */
    private int deleteIconVisibility = View.INVISIBLE;

    //新建一行的构造函数
    public Row() {
        for (int i = 0; i < 15; i++) {
            boxList.add(new Box());
        }
        rowId = FormUtil.randomUUID();
    }


    @Override
    public String toString() {
        return "LineBean{" +
                "boxList=" + boxList +
                ", lineId='" + rowId + '\'' +
                ", deleteIconVisibility=" + deleteIconVisibility +
                '}';
    }

    public int getDeleteIconVisibility() {
        return deleteIconVisibility;
    }

    public void setDeleteIconVisibility(int deleteIconVisibility) {
        this.deleteIconVisibility = deleteIconVisibility;
    }

    public ArrayList<Box> getBoxList() {
        return boxList;
    }

    public void setBoxList(ArrayList<Box> boxList) {
        this.boxList = boxList;
    }


    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }


}
