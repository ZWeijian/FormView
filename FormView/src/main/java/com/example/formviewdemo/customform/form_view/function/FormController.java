package com.example.formviewdemo.customform.form_view.function;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.formviewdemo.customform.LogUtil;
import com.example.formviewdemo.customform.form_view.FormUtil;
import com.example.formviewdemo.customform.form_view.dialog.MenuDialog;
import com.example.formviewdemo.customform.form_view.dialog.RemindDialog;
import com.example.formviewdemo.customform.form_view.model.Box;
import com.example.formviewdemo.customform.form_view.model.Row;

import java.util.ArrayList;

import static com.example.formviewdemo.customform.form_view.function.FormAdapter.maxWeight;
import static com.example.formviewdemo.customform.form_view.function.FormAdapter.minWeight;


/**
 * 操作整个表格的行、列的工具类
 */
public class FormController {
    private final FormAdapter adapter;
    private final Context mContext;
    private final ArrayList<Row> data;

    //列菜单容器
    ArrayList<ImageView> colMenuList;
    //是否显示列菜单，默认显示
    public boolean showColumnMenu = true;

    //是否显示行菜单，默认显示
    public boolean showRowMenu = true;

    //双指缩放
    //缩放前后的X坐标
    double previousXLength;
    double currentXLength;
    //双指缩放时，宽度权重的变化速度
    final float weightSpeed = 0.1f;

    //获取到焦点的格子的行坐标和列坐标
    int focusRowPos = -1;
    int focusColumnPos = -1;

    public FormController(Context context, FormAdapter adapter, ArrayList<Row> data, ArrayList<ImageView> colMenuList) {
        this.mContext = context;
        this.adapter = adapter;
        this.data = data;
        this.colMenuList = colMenuList;
    }

    /**
     * 重置某一行的格子
     */
    public void resetRow(int rowPos) {
        FormUtil.checkRowIndexOutBound(rowPos, data.size());

        Row row = data.get(rowPos);
        for (Box box : row.getBoxList()) {
            box.setWeight(1);
            box.setNarrow(false);
            box.setExpand(false);
        }
        adapter.refresh();

        if (adapter.onRowChangeListener != null)
            adapter.onRowChangeListener.onRowReset(row, rowPos);

    }

    /**
     * 平分某一行的格子
     */
    public void equalRow(int rowPos) {
        FormUtil.checkRowIndexOutBound(rowPos, data.size());

        Row row = data.get(rowPos);
        for (Box box : row.getBoxList()) {
            box.setWeight(1);
        }
        adapter.refresh();

        if (adapter.onRowChangeListener != null)
            adapter.onRowChangeListener.onRowDivideEqually(row, rowPos);

    }


    /**
     * 选中行，并询问是否删除
     */
    public void showDeleteRowDialog(final int rowPos) {
        //至少要有两行
        if (data.size() <= 2) return;

        RemindDialog showDeleteRowDialog = new RemindDialog.Builder(mContext)
                .setMsg("是否删除选中行？")
                .setUser(new RemindDialog.AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (confirmed) {
                            deleteRow(rowPos);
                        } else {
                            selectRow(rowPos, false);
                        }
                    }
                })
                .setOutSizeListener(new RemindDialog.OutSizeListener() {
                    @Override
                    public void onResult(Bundle bundle) {
                        selectRow(rowPos, false);
                    }
                })
                .showCancel(false)
                .build();
        showDeleteRowDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                selectRow(rowPos, true);
            }
        });
        showDeleteRowDialog.show();
    }

    /**
     * 使某一行选中或者取消选中
     */
    public void selectRow(int rowPos, boolean select) {
        //越界检查
        FormUtil.checkRowIndexOutBound(rowPos, data.size());

        for (Box box : data.get(rowPos).getBoxList()) {
            box.setSelected(select);
        }
        adapter.refresh();
    }


    /**
     * 选中列，并询问是否删除
     */
    public void showDeleteColumnDialog(final int rowPos, final int columnPos, final int visibleColumnNum) {
        if (visibleColumnNum <= 1) {
            Toast.makeText(mContext, "至少要有一列", Toast.LENGTH_SHORT).show();
            return;
        }
        RemindDialog deleteColumnDialog = new RemindDialog.Builder(mContext)
                .setMsg("是否删除选中列？")
                .setUser(new RemindDialog.AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (confirmed) {
                            deleteColumn(rowPos, columnPos, visibleColumnNum);
                        } else {
                            //选中行 列 的接口应该开放给外面
                            selectColumn(rowPos, columnPos, false);
                        }
                    }
                })
                .setOutSizeListener(new RemindDialog.OutSizeListener() {
                    @Override
                    public void onResult(Bundle bundle) {
                        selectColumn(rowPos, columnPos, false);
                    }
                })
                .showCancel(false)
                .build();

        deleteColumnDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                selectColumn(rowPos, columnPos, true);
            }
        });
        deleteColumnDialog.show();
    }

    /**
     * 使某一列选中或者取消选中
     */
    public void selectColumn(int rowPos, int columnPos, boolean select) {
        FormUtil.checkRowIndexOutBound(rowPos, data.size());
        FormUtil.checkColumnIndexOutBound(columnPos, adapter.visibleColumnNum);

        //用户选中的格子
        Box currentBox = data.get(rowPos).getBoxList().get(columnPos);
        ////用户选中的格子所在的行
        ArrayList<Box> row = data.get(rowPos).getBoxList();
        //是否选中
        if (select) {
            if (currentBox.isExpand()) {
                //选中的是合并格
                //总列数
                int maxRow = data.get(0).getBoxList().size();

                //计算选中合并格所占的范围
                int range = 0;
                for (int i = columnPos + 1; i < maxRow; i++) {
                    if (row.get(i).isNarrow()) {
                        range++;
                    } else {
                        break;
                    }
                }

                //计算出合并格结尾的坐标
                int endRowPos = columnPos + range;

                //把每一行的范围内的格子设置为选中
                for (Row rowBean : data) {
                    for (int i = columnPos; i <= endRowPos; i++) {
                        rowBean.getBoxList().get(i).setSelected(select);
                    }
                }

            } else {
                //选中的是非合并格
                for (Row rowBean : data) {
                    //选中这一列的非合并格
                    Box box = rowBean.getBoxList().get(columnPos);
                    if (!box.isExpand() && !box.isNarrow()) {
                        box.setSelected(select);
                    }
                }
            }
        } else {
            //取消闪亮
            for (Row style2Bean : data) {
                for (Box box1 : style2Bean.getBoxList()) {
                    box1.setSelected(false);
                }
            }
        }
        adapter.refresh();
    }


    /**
     * 如果一行的所有EditText的权重都相同且不为1，就全部重置为1
     */
    public void isNeedEqualRowWeight(Row row, int visibleColumnNum, boolean formEditable, int rowPos) {
        if (formEditable && row.getBoxList().get(0).getWeight() != 1 && FormUtil.isRowWeightAllEqual(row, visibleColumnNum)) {
            LogUtil.d("zhu", "如果一行的所有EditText的权重都相同且不为1，就全部重置为1");
            equalRow(rowPos);
        }
    }


    /**
     * 打印所有格子
     */
    public void printBoxList(int visibleColumnNum) {
        StringBuilder sb = new StringBuilder("打印所有格子：");
        for (int i = 0; i < data.size(); i++) {
            sb.append("\n");
            for (int j = 0; j < visibleColumnNum; j++) {
                Box box = data.get(i).getBoxList().get(j);
                sb.append("(权重：").append(box.getWeight())
                        .append(",是否合并:").append(box.isExpand())
                        .append(",是否被合并:").append(box.isNarrow())
                        .append(")");
            }
        }
        LogUtil.d("zhu", sb.toString());
    }


    /**
     * 添加一行
     * 格式跟上一行一样
     *
     * @param rowPos 增加行号
     */
    public void addRow(int rowPos) {
        //越界检查
        if (rowPos < 0) throw new IllegalStateException("增加行的位置不能小于0，行号:" + rowPos);
        //如果超过范围，则取最后一个
        if (rowPos >= data.size()) rowPos = data.size();

        //新行
        Row newRow = new Row();

        //rowPos=0，第一行的格式不用设置，跟全新一样
        //rowPos>0，格式跟上一行一样
        if (rowPos > 0) {
            for (int i = 0; i < newRow.getBoxList().size(); i++) {
                Box newBox = newRow.getBoxList().get(i);

                //如果rowPos在行范围内，取rowPos的上一行。越界取最后一行。
                Box lastBox = data.get(rowPos - 1).getBoxList().get(i);

                newBox.setWeight(lastBox.getWeight());
                newBox.setExpand(lastBox.isExpand());
                newBox.setNarrow(lastBox.isNarrow());
            }
        }

        data.add(rowPos, newRow);
        adapter.notifyItemInserted(rowPos);

        if (adapter.onRowChangeListener != null)
            adapter.onRowChangeListener.onRowAdd(data.get(rowPos), rowPos, data.size());

    }

    /**
     * 删除最下面一行
     */
    public void deleteRowAtTheEnd() {
        if (data.size() == 2) {
            Toast.makeText(mContext, "至少要有2行", Toast.LENGTH_SHORT).show();
            return;
        }
        data.remove(data.size() - 1);
        adapter.refresh();
    }

    /**
     * 删除某一行
     */
    public void deleteRow(int rowPos) {
        //越界检查
        FormUtil.checkRowIndexOutBound(rowPos, data.size());

        Row deleteRow = data.remove(rowPos);
        adapter.refresh();

        if (adapter.onRowChangeListener != null)
            adapter.onRowChangeListener.onRowDelete(deleteRow, rowPos, data.size());

    }

    /**
     * 从startPos开始增加range个空白列
     * 如果想在第0列和第1列之间新增2个空白列，那么startPos = 1，range = 2，visibleColumnNum += 2
     *
     * @param startPos         从第startPos列开始增加range列
     * @param range            增加的列数
     * @param visibleColumnNum 增加后的可见列数
     */
    public int addColumn(int startPos, int range, int visibleColumnNum) {
        //越界判断
        int maxColumnNum = data.get(0).getBoxList().size();
        if (visibleColumnNum + range > maxColumnNum) {
            visibleColumnNum = maxColumnNum;
            range = maxColumnNum - visibleColumnNum;
        }

        if (range == 0) return visibleColumnNum;
        if (range < 0) throw new IllegalStateException("列数不能小于0");

        visibleColumnNum += range;

        // 1.2  从startPos开始增加range个空白列
        //
        // 思路   1.2.1  从startPos开始，每一列都向右覆盖一列
        //        1.2.2  把startPos右方的range列置为空白

        // 遍历每行
        for (int i = 0; i < data.size(); i++) {
            //遍历的行
            ArrayList<Box> row = data.get(i).getBoxList();

            //1.2.1  从startPos开始，每一列都向右覆盖一列
            for (int j = maxColumnNum - range - 1; j >= startPos; j--) {
                int k = j + range;
                LogUtil.d("zhu", "覆盖第" + j + "列到第" + k + "列");
                row.set(k, row.get(j));
            }

            //1.2.2  从startPos开始的range列置为空白
            for (int j = startPos; j < startPos + range; j++) {
                LogUtil.d("zhu", "给第" + j + "列赋空值");
                row.set(j, Box.getNewBox());
            }
        }

        adapter.refresh();

        if (adapter.onColumnChangeListener != null)
            adapter.onColumnChangeListener.onColumnAdd(startPos, range, visibleColumnNum);

        return visibleColumnNum;
    }


    /**
     * 最右方减少num列
     *
     * @return columnNum 减少后的可见列数
     */
    public int deleteColumnAtTheEnd(int num, int visibleColumnNum) {
        if (num < 0) throw new IllegalStateException("列数不能小于0");
        visibleColumnNum -= num;
        if (visibleColumnNum <= 1) {
            visibleColumnNum = 1;
            Toast.makeText(mContext, "至少要有1列", Toast.LENGTH_SHORT).show();
        }

        adapter.refresh();
        return visibleColumnNum;
    }


    /**
     * 删除列
     *
     * @param rowPos           选中格的行号
     * @param columnPos        选中格的列号
     * @param visibleColumnNum 可见列数
     */
    public void deleteColumn(int rowPos, int columnPos, int visibleColumnNum) {
        //越界检查
        if (visibleColumnNum <= 1) {
            Toast.makeText(mContext, "至少要有一列", Toast.LENGTH_SHORT).show();
            return;
        }

        FormUtil.checkRowIndexOutBound(rowPos, data.size());
        FormUtil.checkColumnIndexOutBound(columnPos, visibleColumnNum);

        //总列数
        int maxColumn = data.get(0).getBoxList().size();
        //选中格
        Box currentBox = data.get(rowPos).getBoxList().get(columnPos);
        //选中格所处行
        ArrayList<Box> rowBean = data.get(rowPos).getBoxList();
        //记录删除的列
        ArrayList<ArrayList<Box>> deleteColumnList = new ArrayList<>();

        // 1 如果选中格是合并格
        if (currentBox.isExpand()) {
            // 1.1 计算选中合并格所占的范围
            int range = 0;
            for (int i = columnPos + 1; i < maxColumn; i++) {
                if (rowBean.get(i).isNarrow()) {
                    range++;
                } else {
                    break;
                }
            }
            int endColumnPos = columnPos + range;
            LogUtil.d("选中格是合并格 columnPos=" + columnPos + "  range=" + range + " endColumnPos=" + endColumnPos);

            // 1.2 删除从columnPos到endColumnPos的列
            // 思路   1.2.1 endColumnPos右面的每一列都向左range列覆盖
            //       1.2.2 然后最右侧的range列赋空值

            // 遍历每行
            for (int i = 0; i < data.size(); i++) {
                //遍历的行
                ArrayList<Box> row = data.get(i).getBoxList();
                //记录本行删除的格子
                ArrayList<Box> deleteBoxList = new ArrayList<>();

                //1.2.1 endColumnPos右面的每一列都向左range列覆盖
                for (int j = columnPos, k = endColumnPos + 1; k < maxColumn; j++, k++) {
                    deleteBoxList.add(row.get(j));
                    LogUtil.d("zhu", "覆盖第" + k + "列到第" + j + "列");
                    row.set(j, row.get(k));
                }

                //1.2.2.最右侧的range列赋空值
                for (int j = maxColumn - range - 1; j < maxColumn; j++) {
                    LogUtil.d("zhu", "给第" + j + "列赋空值");
                    row.set(j, Box.getNewBox());
                }
                deleteColumnList.add(deleteBoxList);
            }

            //移除最后 ( range + 1 ) 列
            adapter.deleteColumnAtTheEnd(range + 1);
            adapter.refresh();

            if (adapter.onColumnChangeListener != null)
                adapter.onColumnChangeListener.onColumnDelete(deleteColumnList, columnPos, range + 1, adapter.getVisibleColumnNum());

            return;
        }

        // 2 如果选中格不是合并格
        //开始删除该列
        // 2.1 对于该列中没有合并过或者被合并的格子，直接删除
        // 2.2 对于该列中合并过或者被合并的格子，断开合并，重新计算该行单元格的权重，再删除

        //遍历每行
        for (int rowNum = 0; rowNum < data.size(); rowNum++) {
            //遍历到的行
            ArrayList<Box> RowBean = data.get(rowNum).getBoxList();
            //此行的选中列的格子
            Box box = RowBean.get(columnPos);
            //记录此行删除的格子
            ArrayList<Box> deleteBoxList = new ArrayList<>();
            deleteBoxList.add(box);
            deleteColumnList.add(deleteBoxList);

            //补丁：如果这一行的第i列被合并了，并且前面的列没有合并的，还原他
            for (int i = 0; i < maxColumn; i++) {
                if (RowBean.get(i).isNarrow()) {
                    RowBean.get(i).setNarrow(false);
                }
                if (RowBean.get(i).isExpand())
                    break;
            }

            //如果此行的这一列的格子被合并了
            if (box.isNarrow()) {
                LogUtil.d("如果某一行的这一列的格子被合并了");
                //左边的所有合并过的格子都要重新计算权重
                for (int i = columnPos - 1; i >= 0; i--) {
                    //如果该格子合并过
                    if (RowBean.get(i).isExpand()) {
                        //重新计算权重
                        float newWeight = RowBean.get(i).getWeight() - box.getWeight();
                        if (newWeight < minWeight) {
                            newWeight = minWeight;
                        }
                        RowBean.get(i).setWeight(newWeight);
                    }
                }
                //和左边断开合并
                box.setNarrow(false);
                RowBean.get(columnPos - 1).setExpand(false);
            }

            //如果此行的这一列的格子合并了别人，和右边断开合并
            if (box.isExpand()) {
                LogUtil.d("第" + rowNum + "行第" + columnPos + "列的格子合并了别人，和右边断开合并");
                box.setExpand(false);

                //把右边所有连续的isExpand=false且isNarrow=true的格子都清除
                for (int i = columnPos + 1; i < maxColumn; i++) {
                    //要处理的格子
                    Box b = RowBean.get(i);
                    if (!b.isExpand() && b.isNarrow()) {
                        b.setNarrow(false);
                    } else {
                        break;
                    }
                }

                for (int i = columnPos + 1; i < maxColumn - 1; i++) {
                    Box b = RowBean.get(i);
                    Box nextBox = RowBean.get(i + 1);
                    if (nextBox.isNarrow() && !b.isExpand()) {
                        nextBox.setNarrow(false);
                    } else {
                        break;
                    }
                }

                if (RowBean.get(columnPos + 1).isExpand() && RowBean.get(columnPos + 1).isNarrow()) {
                    RowBean.get(columnPos + 1).setNarrow(false);
                }
            }


            //处理内容：从要删除的列，向右到末尾，后面一列覆盖前面一列
            for (int i = columnPos + 1; i < maxColumn; i++) {
                //从后往前覆盖
                RowBean.set(i - 1, RowBean.get(i));
            }

            //最后一列赋新值
            RowBean.set(maxColumn - 1, Box.getNewBox());

            //重新计算此行的所有权重
            for (int i = 0; i < RowBean.size(); i++) {
                if (RowBean.get(i).isExpand()) {
                    //合并了别人，重新计算
                    float newWeight = 1;
                    for (int j = i + 1; j < RowBean.size(); j++) {
                        if (RowBean.get(j).isNarrow()) {
                            newWeight++;
                        } else {
                            break;
                        }
                    }
                    RowBean.get(i).setWeight(newWeight);
                } else {
                    //没有合并别人的，一律设置为1
                    RowBean.get(i).setWeight(1);
                }
            }
        }
        adapter.deleteColumnAtTheEnd(1);
        adapter.refresh();

        if (adapter.onColumnChangeListener != null)
            adapter.onColumnChangeListener.onColumnDelete(deleteColumnList, columnPos, 1, adapter.getVisibleColumnNum());

    }

    //RecyclerView的复用机制会使EditText的数据混乱，所以要绑定数据，进行处理
    public void handleEditText(final int rowPos, final EditText et, TextWatcher watcher, final int columnPos, final int visibleColumnNum, final ImageView ivRow, final boolean showRowMenu, final boolean showColumnMenu, final LinearLayout llColumnMenu) {
        //1、移除原来的文字改变监听，避免列表刷新或者view复用所造成TextWatcher被多次调用
        if (watcher != null) {
            et.removeTextChangedListener(watcher);
        }
        if (et.getTag() instanceof TextWatcher) {
            et.removeTextChangedListener((TextWatcher) et.getTag());
        }

        //2 创建TextWatcher
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                data.get(rowPos).getBoxList().get(columnPos).setText(s.toString());
                adapter.setTextChange(true);
            }
        };

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (rowPos >= data.size()) return;

                focusRowPos = rowPos;
                focusColumnPos = columnPos;

                //记录此行中的可见格子权重
                ArrayList<Float> rowWeightList = new ArrayList<>();
                //此行的格子是否被合并
                ArrayList<Boolean> narrowList = new ArrayList<>();

                for (int i = 0; i < visibleColumnNum; i++) {
                    Box box = data.get(rowPos).getBoxList().get(i);
                    rowWeightList.add(box.getWeight());
                    narrowList.add(box.isNarrow());
                }


                //处理行菜单
                ivRow.setVisibility(hasFocus && showRowMenu ? View.VISIBLE : View.INVISIBLE);

                //处理列菜单
                if (showColumnMenu) {
                    llColumnMenu.setVisibility(View.VISIBLE);
                    ColumnMenuUtil.showColMenu(mContext, hasFocus ? columnPos : -1, rowWeightList, narrowList, colMenuList);
                } else {
                    llColumnMenu.setVisibility(View.GONE);
                }


            }
        });
        //3 先移除，后添加
        et.addTextChangedListener(watcher);
        et.setTag(watcher);
        //4 设置值
        et.setText(data.get(rowPos).getBoxList().get(columnPos).getText());

    }


    /**
     * 设置长按事件
     */
    public void setLongClick(final EditText et, final int rowPos, ArrayList<EditText> etList, ArrayList<View> lineList, final int visibleColumnNum, final BoxController boxController) {
        //rowPos 当前格子的行号
        //columnPos  当前格子的列号(et1的currentBoxIndex为0)
        final int columnPos = etList.indexOf(et);

        et.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LogUtil.d("当前格子的行号" + rowPos + "   当前格子的列号" + columnPos);

                //当前的格子
                final Box currentBox = data.get(rowPos).getBoxList().get(columnPos);
                //当前格子的右一格子
                Box rightBox = data.get(rowPos).getBoxList().get(columnPos + 1);

                String[] menu = FormUtil.getBoxMenu(currentBox, rowPos, columnPos, visibleColumnNum, data.get(rowPos).getBoxList());

                if (adapter.onMenuListener != null && !adapter.onMenuListener.onBoxMenu(currentBox, rowPos, columnPos, menu)) {
                    return false;
                }

                MenuDialog menuDialog = new MenuDialog(mContext, menu, new MenuDialog.OnMenuListener() {
                    @Override
                    public void onSelect(String text, int pos) {
                        switch (text) {
                            case "调整宽度权重":
                                boxController.adjustWidthWeight(currentBox, et, rowPos, columnPos);
                                break;
                            case "合并右单元格":
                                boxController.mergeRightBox(rowPos, columnPos, visibleColumnNum, true);
                                break;
                            case "拆分单元格":
                                boxController.split(rowPos, columnPos, visibleColumnNum);
                                break;
                            case "文字加粗":
                                boxController.setBold(rowPos, columnPos, true);
                                break;
                            case "取消加粗":
                                boxController.setBold(rowPos, columnPos, false);
                                break;
                            default:
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                menuDialog.show();

                return false;
            }
        });
    }

    /**
     * 设置双指缩放
     */
    @SuppressLint("ClickableViewAccessibility")
    public void setScale(final EditText et, final Row row, final BoxController boxController, final ArrayList<EditText> etList) {
        et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //解决滑动冲突，双指操作交给EditText处理，其他操作交给父容器（ScrollView）处理
                et.getParent().requestDisallowInterceptTouchEvent(event.getPointerCount() == 2);

                int pCount = event.getPointerCount();// 触摸设备时手指的数量
                int action = event.getAction();// 获取触屏动作。比如：按下、移动和抬起等手势动作、

                if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN && pCount == 2) {
                    //手指按下时，获取两个手指x坐标的水平距离绝对值
                    previousXLength = Math.abs(event.getX(0) - event.getX(1));

                } else if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP && pCount == 2) {
                    //手指抬起时，刷新
                    adapter.refresh();

                } else if (action == MotionEvent.ACTION_MOVE && pCount == 2) {
                    //手指移动时，获取两个手指x坐标的水平距离绝对值
                    currentXLength = Math.abs(event.getX(0) - event.getX(1));

                    //得到两只手指从按下位置到移动位置的水平距离变化值 ΔX
                    double deltaX = currentXLength - previousXLength;
                    LogUtil.d("deltaX =" + deltaX);

                    //幅度太小不处理
                    if (deltaX < 2 && deltaX > -2) return false;

                    //计算新的权重
                    float newWeight = 1;
                    float oldWeight = row.getBoxList().get(etList.indexOf(et)).getWeight();

                    if (deltaX > 0) {
                        //deltaX > 0 宽度的权重增加
                        newWeight = oldWeight + weightSpeed;
                        //越界检查
                        newWeight = Math.min(newWeight, maxWeight);
                    } else if (deltaX < 0) {
                        //deltaX < 0 宽度的权重减少
                        newWeight = oldWeight - weightSpeed;
                        //越界检查
                        newWeight = Math.max(newWeight, minWeight);
                    }

                    //给当前格子设置新权重
                    boxController.setEditTextWeight(et, newWeight);
                    ((LinearLayout.LayoutParams) et.getLayoutParams()).weight = newWeight;
                    row.getBoxList().get(etList.indexOf(et)).setWeight(newWeight);

                    //如果此行没有被合并的格子,给此列的其他没有被改变的格子设置同样的权重
                    if (!FormUtil.isRowExpand(row)) {
                        for (int i = 0; i < data.size(); i++) {
                            if (!FormUtil.isRowExpand(data.get(i))) {
                                data.get(i).getBoxList().get(etList.indexOf(et)).setWeight(newWeight);
                                adapter.notifyItemChanged(i);
                            }
                        }
                    }

                    previousXLength = currentXLength;
                }

                return false;
            }
        });
    }

    public int getFocusRowPos() {
        return focusRowPos;
    }

    public int getFocusColumnPos() {
        return focusColumnPos;
    }

    /**
     * 展开列菜单
     */
    public void openColumnMenu(final int visibleColumnNum) {
        final int rowPos = focusRowPos;
        final int columnPos = focusColumnPos;
        String[] menu = FormUtil.getColumnMenu(visibleColumnNum);

        if (adapter.onMenuListener != null && !adapter.onMenuListener.onColumnMenu(data.get(rowPos).getBoxList().get(columnPos), rowPos, columnPos, visibleColumnNum, menu)) {
            return;
        }

        selectColumn(rowPos, columnPos, true);
        MenuDialog menuDialog = new MenuDialog(mContext, menu, new MenuDialog.OnMenuListener() {
            @Override
            public void onSelect(String text, int pos) {
                selectColumn(rowPos, columnPos, false);
                switch (text) {
                    case "增加一列":
                        adapter.addColumn(columnPos + 1, 1);
                        break;
                    case "删除此列":
                        showDeleteColumnDialog(rowPos, columnPos, visibleColumnNum);
                        break;
                    case "最右增加一列":
                        adapter.addColumn(visibleColumnNum, 1);
                        break;
                    case "最右减少一列":
                        adapter.deleteColumnAtTheEnd(1);
                        break;
                    default:
                }
            }

            @Override
            public void onCancel() {
                selectColumn(rowPos, columnPos, false);
            }
        });
        menuDialog.show();


    }

    /**
     * 展开行菜单
     */
    public void openRowMenu(final int rowPos, int visibleColumnNum) {
        String[] menu = FormUtil.getRowMenu(data.get(rowPos).getBoxList(), data.size());

        if (adapter.onMenuListener != null && !adapter.onMenuListener.onRowMenu(data.get(rowPos).getBoxList().get(focusColumnPos), rowPos, focusColumnPos, data.size(), menu)) {
            return;
        }

        selectRow(rowPos, true);
        MenuDialog menuDialog = new MenuDialog(mContext, menu, new MenuDialog.OnMenuListener() {
            @Override
            public void onSelect(String text, int pos) {
                selectRow(rowPos, false);
                switch (text) {
                    case "增加一行":
                        adapter.addRow(rowPos + 1);
                        break;
                    case "最下增加一行":
                        adapter.addRowAtTheEnd();
                        break;
                    case "删除此行":
                        showDeleteRowDialog(rowPos);
                        break;
                    case "平分此行":
                        equalRow(rowPos);
                        break;
                    case "重置此行":
                        resetRow(rowPos);
                        break;
                    default:
                }
            }

            @Override
            public void onCancel() {
                selectRow(rowPos, false);
            }
        });
        menuDialog.show();

    }


}
