package com.example.formviewdemo.customform.form_view.function;


import com.example.formviewdemo.customform.form_view.model.Box;
import com.example.formviewdemo.customform.form_view.model.Row;

import java.util.ArrayList;

public class CustomFormUtil {

    /**
     * 生成长按单元格的菜单
     */
    public static String[] getBoxMenu(Box currentBox, int rowPos, int columnPos, int visibleColumnNum, ArrayList<Box> rowBean) {
        //弹出的菜单内容
        ArrayList<String> menu = new ArrayList<>();

        //合并右单元格：格子的右方存在没被合并的可见格子
        for (int i = columnPos + 1; i < visibleColumnNum; i++) {
            if (!rowBean.get(i).isNarrow()) {
                menu.add("合并右单元格");
                break;
            }
        }

        //拆分单元格：当前格子已经合并了右格
        if (currentBox.isExpand()) menu.add("拆分单元格");

        //调整宽度权重:每个格子都有
        menu.add("调整宽度权重");

        return menu.toArray(new String[0]);
    }


    /**
     * 获取行的弹出菜单
     */
    public static String[] getRowMenu(ArrayList<Box> rowBean, int rowCount) {
        //弹出的菜单内容
        ArrayList<String> menu = new ArrayList<>();

        //增加一行
        menu.add("增加一行");

        //删除此行：至少有两行
        if (rowCount > 2) menu.add("删除此行");

        //平分此行：此行的宽度权重不完全相同
        float weight = rowBean.get(0).getWeight();
        for (int i = 1; i < rowBean.size(); i++) {
            if (weight != rowBean.get(i).getWeight()) {
                menu.add("平分此行");
                break;
            }
        }

        //重置此行：此行的宽度权重不完全相同，或者存在合并格
        if (menu.contains("平分此行")) {
            menu.add("重置此行");
        } else {
            for (Box box : rowBean) {
                if (box.isExpand()) {
                    menu.add("重置此行");
                    break;
                }
            }
        }

        //减少一行：至少有两行
//        if (rowCount > 2) menu.add("减少一行");


        return menu.toArray(new String[0]);
    }


    /**
     * 获取列的弹出菜单
     */
    public static String[] getColumnMenu(int visibleColumnNum) {
        //弹出的菜单内容
        ArrayList<String> menu = new ArrayList<>();

        //删除此列：至少要有1列
        if (visibleColumnNum > 1) menu.add("删除此列");

        //最右增加一列：最多15列
        if (visibleColumnNum < 15) menu.add("最右增加一列");

        //最右减小一列：至少要有1列
        if (visibleColumnNum > 1) menu.add("最右减小一列");

        return menu.toArray(new String[0]);
    }


    /**
     * 这行是否有合并的格子
     */
    public static boolean isRowExpand(Row row) {
        for (Box box : row.getBoxList()) {
            if (box.isExpand() || box.isNarrow()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 此行的所有EditText的权重是否完全相同
     */
    public static boolean isRowWeightAllEqual(Row row, int visibleColumnNum) {
        //取这一行第一个格子的权重作为基准，跟后面格子的权重比较
        float weight = row.getBoxList().get(0).getWeight();
        for (int i = 1; i < visibleColumnNum; i++) {
            //如果不相等，返回false
            if (weight != row.getBoxList().get(i).getWeight()) {
                return false;
            }
        }
        return true;
    }

}
