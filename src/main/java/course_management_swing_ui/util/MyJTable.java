package course_management_swing_ui.util;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview A class is used to display and edit regular two-dimensional tables of cells. It also has methods to control
 * the editable state of each cell.
 */
public class MyJTable extends JTable {
    private boolean[][] currentSetting;
    private final boolean[][] columnSetting;

    public MyJTable(TableModel dm) {
        super(dm);
        currentSetting = new boolean[dm.getRowCount()][dm.getColumnCount()];
        columnSetting = new boolean[1][dm.getColumnCount()];
    }

    /**
     * Set listed columns to editable or not
     * @requires columns!=null
     * @effects <pre>
     *     for all column i in columns
     *      store value to currentSetting[i] and columnSetting[0]
     * </pre>
     */
    public void setEditableColumns(boolean value, int... columns) {
        if (checkDataChanged()) {
            reset();
        }
        if (columns != null) {
            for (int col : columns) {
                for (int i = 0; i < currentSetting.length; i++) {
                    currentSetting[i][col] = value;
                    columnSetting[0][col] = value;
                }
            }
        }
    }

    /**
     * Set listed columns to editable or not
     * @requires row==this.TableModel.row /\ column==this.TableModel.column
     * @effects <pre>
     *      store value to currentSetting[row][column]
     * </pre>
     */
    public void setEditableCell(boolean value, int row, int column) {
        if (checkDataChanged()) {
            reset();
        }
        if (row >= 0 && row < currentSetting.length && column >= 0 && column < columnSetting[0].length) {
            currentSetting[row][column] = value;
        } else {
            System.out.println("Exception roi :(((");
        }
    }

    /**
     * Check whether the Table Model is changed
     */
    private boolean checkDataChanged() {
        return currentSetting.length != getRowCount();
    }

    /**
     * always called when the table model is changed
     * @effects <pre>
     *     set currentSetting = new 2d array[][]
     *     copy value of first row of columnSetting to all element in currentSetting
     * </pre>
     */
    private void reset() {
        TableModel tm = getModel();
        boolean[][] newArr = new boolean[tm.getRowCount()][tm.getColumnCount()];

        // fill newArr with columnSetting
        for (int i = 0; i < newArr.length; i++) {
            // copy row[0][] of columnSetting to each index of newArr[i][]
            System.arraycopy(columnSetting, 0, newArr, i, 1);
        }
        currentSetting = newArr;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == getModel().getColumnCount() - 1) {
            return Boolean.class;
        } else {
            return super.getColumnClass(column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (checkDataChanged()) {
            reset();
        }
        return currentSetting[row][column];
    }
}
