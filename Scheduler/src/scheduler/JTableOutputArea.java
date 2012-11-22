/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class JTableOutputArea implements OutputArea {
    
    private Object outputArea;
    
    public JTableOutputArea (Object outputArea){
        this.outputArea = outputArea;
    }
    
    public void print(flex2DArray f){
        //DefaultTableModel TM = new DefaultTableModel(f.getColKeys().toArray(),f.getRowsCount());
        DefaultTableModel TM = new DefaultTableModel();
        // Add first column containing row keys
        TM.addColumn("",f.getRowKeys().toArray()); 
        // Now get the column headers correct - All this to get the headers in an array with blank in first one
        int totCols = f.getColsCount()+1;
        TM.setColumnCount(totCols);
        String[] colHeaders = new String[totCols];
        TreeSet<String> cols = f.getColKeys();
        colHeaders[0] = "";
        int i = 1;
        for (String cKey : cols){
            colHeaders[i] = cKey;
            i++;
        }
        TM.setColumnIdentifiers(colHeaders);
        //Set the data content of the jTable
        int r = 0; int c = 1;
        TreeSet<String> rows = f.getRowKeys();
        for (String rKey : rows){
            for (String cKey : cols){
                TM.setValueAt(f.getCellContentAtKey(rKey,cKey), r, c);
                c++;
            }
            r++;
            c=1;
        }
        //Now attach table model to the JTable
        javax.swing.JTable outputJTable = (javax.swing.JTable) outputArea;
        //Now Size the jTable entries in first row (0 index) which is populated with row keys and has no header
        int maxSize = 0;
        int curSize = 0;
        for (i = 0; i < TM.getRowCount(); i++) {
            String cellContent = (String) TM.getValueAt(i,0);
            if (maxSize < cellContent.length()) maxSize =cellContent.length();
        }
        outputJTable.setModel(TM);
        outputJTable.getColumnModel().getColumn(0).setPreferredWidth(maxSize*7);
        //Now Size the jTable entries in the other rows
        maxSize = 0; i = 1;
        for (String col : cols){
            maxSize = col.length();
            for (String entry : f.getValuesAtCol(col)){
                if (entry.length() > maxSize) maxSize = entry.length();
            }
            outputJTable.getColumnModel().getColumn(i).setPreferredWidth(maxSize*10);
            i++;
        }
    }
}
