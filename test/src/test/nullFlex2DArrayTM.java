package com.waterhouse.nxmArray;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author jonathan
 */
public class nullFlex2DArrayTM extends AbstractTableModel{
    private String Type;
    public nullFlex2DArrayTM(String type){
        Type = type;
    }
    public int getColumnCount() { return 1; }
    public int getRowCount() { return 1;}
    public Object getValueAt(int row, int col) { return "Empty "+Type; }
    public String getColumnName(int col){return "";}

}
