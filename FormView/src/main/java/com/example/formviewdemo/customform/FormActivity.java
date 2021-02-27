package com.example.formviewdemo.customform;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.formviewdemo.R;
import com.example.formviewdemo.customform.form_view.function.FormView;
import com.example.formviewdemo.customform.form_view.listener.OnBoxChangeListener;
import com.example.formviewdemo.customform.form_view.listener.OnColumnChangeListener;
import com.example.formviewdemo.customform.form_view.listener.OnMenuListener;
import com.example.formviewdemo.customform.form_view.listener.OnRowChangeListener;
import com.example.formviewdemo.customform.form_view.model.Box;
import com.example.formviewdemo.customform.form_view.model.Row;

import java.util.ArrayList;

public class FormActivity extends AppCompatActivity {

    FormView mFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置主题颜色
        setTheme(R.style.FormStyle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        //设置状态栏颜色
        getWindow().setStatusBarColor(Color.parseColor("#06a8ff"));   //这里动态修改颜色
        mFormView = findViewById(R.id.form_view);
        initFormView();
    }

    private void initFormView() {

        //==================================创建数据=================================================

        ArrayList<Row> rows = new ArrayList<>();

        //创建第一行，一行默认有十五个空格子
        Row firstRow = new Row();
        //遍历第一行的格子
        for (int i = 0; i < firstRow.getBoxList().size(); i++) {
            //设置文字
            firstRow.getBoxList().get(i).setText(String.valueOf(i));
        }
        //添加第一行
        rows.add(firstRow);

        //添加第二行，是空白行
        rows.add(new Row());
        //添加第三行，是空白行
        rows.add(new Row());


        //==================================表格设置=================================================

        //设置表格数据
        mFormView.setData(rows);
//        //获取表格数据
//        mFormView.getData();
//
        //设置总列数，默认3列
        mFormView.setTotalColumnNum(4);
//        //获取总列数
//        mFormView.getTotalColumnNum();
//
//        //获取总行数
//        mFormView.getTotalRowNum();
//        //设置表格是否可编辑，默认可编辑
//        mFormView.setFormEnable(true);
//        //获取表格是否可编辑
//        mFormView.getFormEnable();
//
//        //设置表格的文字是否变化
//        mFormView.setTextChange(false);
//        //获取表格的文字是否变化
//        mFormView.isTextChange();
//
//        //刷新表格
//        mFormView.refresh();
//
//        //==================================操作行和列=================================================
//
//        //在第rowPos行增加一行，返回总行数
//        mFormView.addRow(2);
//        //删除第rowPos行，返回总行数
//        mFormView.deleteRow(1);
//        //表格最下面添加一行，返回总行数
//        mFormView.addRowAtTheEnd();
//        //表格最下面减少一行，返回总行数
//        mFormView.deleteRowAtTheEnd();
//
//        //从第columnPos列开始，增加range列，返回总列数
//        mFormView.addColumn(1, 2);
//        //删除一列，传入选中格的行号和列号，返回总列数
//        mFormView.deleteColumn(1, 2);
//        //表格最右方增加n列，返回总列数
//        mFormView.addColumnAtTheEnd(3);
//        //表格最右方减少n列，返回总列数
//        mFormView.deleteColumnAtTheEnd(4);
//
//
//        //选中或取消选中某一行，传入行号和是否选中
//        mFormView.selectRow(1, true);
//        //选中或取消选中某一列，传入选中格的行号和列号，以及是否选中
//        mFormView.selectColumn(1, 2, true);
//
//
//        //平分某一行，把此行的所有格子宽度设置为相同
//        mFormView.equalRow(10);
//        //重置某一行，先把此行的合并格拆分，再把此行的所有格子宽度设置为相同
//        mFormView.resetRow(10);
//
//        //是否显示行按钮，默认显示
//        mFormView.setShowRowMenu(true);
//        //获取是否显示行按钮
//        mFormView.isShowRowMenu();
//
//        //是否显示列按钮，默认显示
//        mFormView.setShowColumnMenu(true);
//        //获取是否显示列按钮
//        mFormView.isShowColumnMenu();
//
//        //设置行按钮的图标
//        mFormView.setRowMenuIcon(R.mipmap.ic_launcher);
//        mFormView.setRowMenuIcon(BitmapUtil);
//        mFormView.setRowMenuIcon(10);
//
//        //设置列按钮的图标
//        mFormView.setColMenuIcon(R.mipmap.ic_launcher);
//        mFormView.setColMenuIcon(R.mipmap.bubble);
//        mFormView.setColMenuIcon(R.mipmap.bubble);
//
//
//        //==================================操作单元格=================================================
//
//        //调整宽度权重，传入目标单元格的行号、列号以及权重
//        mFormView.setBoxWeight(1, 2, 3);
//        //合并右单元格，传入目标单元格的行号、列号以及文本是否合并
//        mFormView.mergeRightBox(1, 2, true);
//        //拆分单元格，传入目标单元格的行号、列号
//        mFormView.splitBox(1, 2);
//        //字体加粗或者取消加粗，传入目标单元格的行号、列号以及是否加粗
//        mFormView.setBoxBold(1, 2, true);
//
//        //设置单元格最大宽度权重
//        mFormView.setMaxWeight(8);
//        //获取单元格最大宽度权重
//        mFormView.getMaxWeight();
//        //设置单元格最小宽度权重
//        //mFormView.setMinWeight(mFormView.getMinWeight() + 1);
//        //获取单元格的最小宽度权重
//        mFormView.getMinWeight();
////        //设置单元格的字体颜色,传入资源id，比如R.color.text
//        mFormView.setBoxTextColor(Color.YELLOW);
////        //设置单元格的字体大小，单位sp
//        mFormView.setBoxTextSize(10);
////        //设置表格背景颜色
//        mFormView.setBgColor(Color.YELLOW);
////        //设置单元格被选中颜色
//        mFormView.setSelectColor(Color.YELLOW);


        //==================================监听器=================================================

        //设置单元格变化的监听器
        mFormView.setOnBoxChangeListener(new OnBoxChangeListener() {

            /**
             * 单元格的宽度权重发生变化
             *
             * @param box       单元格的数据
             * @param rowPos    单元格的行号
             * @param columnPos 单元格的列号
             */
            @Override
            public void onWeightChange(Box box, int rowPos, int columnPos) {
                LogUtil.d("onWeightChange box=" + box + " rowPos=" + rowPos + " columnPos=" + columnPos);
            }

            /**
             * 单元格进行合并或者拆分
             *
             * @param isExpand  true:格子合并  false:格子拆分
             * @param box       单元格的数据
             * @param rowPos    单元格的行号
             * @param columnPos 单元格的列号
             */
            @Override
            public void onBoxExpand(boolean isExpand, Box box, int rowPos, int columnPos) {
                LogUtil.d("onBoxExpand rowPos=" + rowPos + " columnPos=" + columnPos + " isExpand=" + isExpand + "   box=" + box);
            }


            /**
             * 单元格进行加粗或者取消加粗
             *
             * @param isBold    true：字体加粗  false：字体取消加粗
             * @param box       单元格的数据
             * @param rowPos    单元格的行号
             * @param columnPos 单元格的列
             */
            @Override
            public void onBoxBold(boolean isBold, Box box, int rowPos, int columnPos) {
                LogUtil.d("onBoxBold rowPos=" + rowPos + " columnPos=" + columnPos + " isBold=" + isBold + "   box=" + box);
            }
        });

        //设置行变化的监听器
        mFormView.setOnRowChangeListener(new OnRowChangeListener() {

            /**
             * 增加某一行
             *
             * @param row         新增的行
             * @param rowPos      新增行的行号
             * @param totalRowNum 增加一行后的总行数
             */
            @Override
            public void onRowAdd(Row row, int rowPos, int totalRowNum) {
                LogUtil.d("onAddRow row=" + row + " rowPos=" + rowPos + " totalRowNum=" + totalRowNum);
            }

            /**
             * 删除某一行
             *
             * @param row           删除的行
             * @param rowPos        删除行的行号
             * @param totalRowNum   删除一行后的总行数
             */
            @Override
            public void onRowDelete(Row row, int rowPos, int totalRowNum) {
                LogUtil.d("onDeleteRow  row=" + row + " rowPos=" + rowPos + " totalRowNum=" + totalRowNum);
            }


            /**
             * 平分某一行
             *
             * @param row    平分行的数据
             * @param rowPos 平分行的行号
             */
            @Override
            public void onRowDivideEqually(Row row, int rowPos) {
                LogUtil.d("onRowDivideEqually rowPos=" + rowPos + " row=" + row);
            }

            /**
             * 重置某一行
             *
             * @param row    重置行的数据
             * @param rowPos 重置行的行号
             */
            @Override
            public void onRowReset(Row row, int rowPos) {
                LogUtil.d("onRowReset rowPos=" + rowPos + " row=" + row);
            }
        });

        //设置列变化的监听器
        mFormView.setOnColumnChangeListener(new OnColumnChangeListener() {

            /**
             * 从columnPos开始，增加range列
             *
             * @param columnPos        增加列的开始位置
             * @param range            增加的列数
             * @param totalColumnNum   增加后的总列数
             */
            @Override
            public void onColumnAdd(int columnPos, int range, int totalColumnNum) {
                LogUtil.d("onColumnAdd  columnPos=" + columnPos + " range=" + range + " totalColumnNum=" + totalColumnNum);
            }

            /**
             * 从columnPos开始，删除range列
             *
             * @param deleteColumnList 删除列的数据
             * @param columnPos        删除列的开始位置
             * @param range            删除的列数
             * @param totalColumnNum   删除后的总列数
             */
            @Override
            public void onColumnDelete(ArrayList<ArrayList<Box>> deleteColumnList, int columnPos, int range, int totalColumnNum) {
                LogUtil.d("onColumnDelete  columnPos=" + columnPos + " range=" + range + " totalColumnNum=" + totalColumnNum);
            }
        });

        //设置点击行按钮、点击列按钮以及长按单元格的菜单打开监听器
        //可以监听各菜单的打开
        //也可以不用自带弹窗，使用你自己的弹窗
        mFormView.setOnMenuListener(new OnMenuListener() {

            /**
             * 用户长按单元格，需要打开单元格菜单
             *
             * @param box       选中单元格的数据
             * @param rowPos    选中单元格的行号
             * @param columnPos 选中单元格的列号
             * @param menu      推荐的菜单选项
             * @return true：使用自带的默认弹窗   false：使用你自己的弹窗
             */
            @Override
            public boolean onBoxMenu(Box box, final int rowPos, final int columnPos, String[] menu) {
                //打印数据
                StringBuilder sb = new StringBuilder("onBoxMenu  rowPos=");
                sb.append(rowPos).append(" columnPos=").append(columnPos).append(" 格子菜单有");
                for (int i = 0; i < menu.length; i++) {
                    sb.append(" ").append(i).append(".").append(menu[i]);
                }
                sb.append("  box=").append(box);
                LogUtil.d(sb.toString());

//                //创建并打开你自己的弹窗
//                MenuDialog menuDialog = new MenuDialog(FormActivity.this, menu, new MenuDialog.OnMenuListener() {
//                    @Override
//                    public void onSelect(String text, int pos) {
//                        switch (text) {
//                            case "调整宽度权重":
//                                mFormView.setBoxWeight(rowPos, columnPos, 3);
//                                break;
//                            case "合并右单元格":
//                                mFormView.mergeRightBox(rowPos, columnPos, true);
//                                break;
//                            case "拆分单元格":
//                                mFormView.splitBox(  rowPos, columnPos);
//                                break;
//                            case "文字加粗":
//                                mFormView.setBoxBold(rowPos, columnPos, true);
//                                break;
//                            case "取消加粗":
//                                mFormView.setBoxBold(rowPos, columnPos, false);
//                                break;
//                            default:
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
//                menuDialog.show();

                //return true 使用自带的默认弹窗
                //return false 使用你自己的弹窗
                return true;
            }


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
            @Override
            public boolean onColumnMenu(Box box, final int rowPos, final int columnPos, final int totalColumnNum, String[] menu) {
                //打印数据
                StringBuilder sb = new StringBuilder("onColumnMenu  rowPos=");
                sb.append(rowPos).append(" columnPos=").append(columnPos).append("  totalColumnNum=").append(totalColumnNum).append(" 列菜单有");
                for (int i = 0; i < menu.length; i++) {
                    sb.append(" ").append(i).append(".").append(menu[i]);
                }
                sb.append("  box=").append(box);
                LogUtil.d(sb.toString());

//                //打开弹窗前，选中目标列
//                mFormView.selectColumn(rowPos, columnPos, true);
//                //创建并打开你自己的弹窗
//                MenuDialog menuDialog = new MenuDialog(FormActivity.this, menu, new MenuDialog.OnMenuListener() {
//                    @Override
//                    public void onSelect(String text, int pos) {
//                        //关闭弹窗后，取消选中目标列
//                        mFormView.selectColumn(rowPos, columnPos, false);
//
//                        switch (text) {
//                            case "增加一列":
//                                mFormView.addColumn(columnPos + 1, 1);
//                                break;
//                            case "删除此列":
//                                mFormView.deleteColumn(rowPos, columnPos);
//                                break;
//                            case "最右增加一列":
//                                mFormView.addColumn(totalColumnNum, 1);
//                                break;
//                            case "最右减少一列":
//                                mFormView.deleteColumnAtTheEnd(1);
//                                break;
//                            default:
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        //关闭弹窗后，取消选中目标列
//                        mFormView.selectColumn(rowPos, columnPos, false);
//                    }
//                });
//                menuDialog.show();

                //return true 使用自带的默认弹窗
                //return false 使用你自己的弹窗
                return true;
            }

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
            @Override
            public boolean onRowMenu(Box box, final int rowPos, int columnPos, int totalRowNum, String[] menu) {
                //打印数据
                StringBuilder sb = new StringBuilder("onRowMenu  rowPos=");
                sb.append(rowPos).append(" columnPos=").append(columnPos).append("  visibleRowNum=").append(totalRowNum).append(" 行菜单有");
                for (int i = 0; i < menu.length; i++) {
                    sb.append(" ").append(i).append(".").append(menu[i]);
                }
                sb.append("  box=").append(box);
                LogUtil.d(sb.toString());

//                //打开弹窗前，选中目标行
//                mFormView.selectRow(rowPos, true);
//                //创建并打开你自己的弹窗
//                MenuDialog menuDialog = new MenuDialog(FormActivity.this, menu, new MenuDialog.OnMenuListener() {
//                    @Override
//                    public void onSelect(String text, int pos) {
//                        //关闭弹窗后，取消选中目标行
//                        mFormView.selectRow(rowPos, false);
//                        switch (text) {
//                            case "增加一行":
//                                mFormView.addRow(rowPos + 1);
//                                break;
//                            case "最下增加一行":
//                                mFormView.addRowAtTheEnd();
//                                break;
//                            case "删除此行":
//                                mFormView.deleteRow(rowPos);
//                                break;
//                            case "平分此行":
//                                mFormView.equalRow(rowPos);
//                                break;
//                            case "重置此行":
//                                mFormView.resetRow(rowPos);
//                                break;
//                            default:
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        //关闭弹窗后，取消选中目标行
//                        mFormView.selectRow(rowPos, false);
//                    }
//                });
//                menuDialog.show();

                //return true 使用自带的默认弹窗
                //return false 使用你自己的弹窗
                return true;
            }
        });
    }
}