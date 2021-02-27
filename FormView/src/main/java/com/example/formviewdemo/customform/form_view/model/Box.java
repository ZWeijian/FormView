package com.example.formviewdemo.customform.form_view.model;


import com.example.formviewdemo.customform.form_view.FormUtil;

import java.io.Serializable;

import static com.example.formviewdemo.customform.form_view.function.FormAdapter.maxWeight;
import static com.example.formviewdemo.customform.form_view.function.FormAdapter.minWeight;


/**
 * 一个格子
 */
public class Box implements Serializable {

    Box() {
        this.weight = 1;
        this.isNarrow = false;
        this.isExpand = false;
        this.editable = true;
        this.isSelected = false;
        this.bold = false;
        this.id = FormUtil.randomUUID();
    }

    //格子显示的文字
    private String text;

    //格子的权重默认为1
    private float weight;

    //是否被左边的格子合并
    private boolean isNarrow;

    //是否合并了右边的格子
    private boolean isExpand;

    //是否允许修改
    private boolean editable;

    //文字是否加粗
    private boolean bold;

    //一个格子的id
    private String id;

    //是否被选中
    private boolean isSelected;

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isNarrow() {
        return isNarrow;
    }

    public void setNarrow(boolean narrow) {
        isNarrow = narrow;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        //处理越界
        weight = Math.max(weight, minWeight);
        weight = Math.min(weight, maxWeight);
        this.weight = weight;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "Box{" +
                "text='" + text + '\'' +
                ", weight=" + weight +
                ", isNarrow=" + isNarrow +
                ", isExpand=" + isExpand +
                ", editable=" + editable +
                ", bold=" + bold +
                ", id='" + id + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    public static Box getNewBox() {
        return new Box();
    }
}