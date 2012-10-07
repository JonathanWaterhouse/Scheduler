/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
/**
 * This interface defines the necessary state variables and methods to describe
 * a flexible 2D array where each cell is defined by a string row and column key
 * @author jonathan
 */
public interface abstractFlex2DArray {

    public void add(String rowLabel, String colLabel, String cellContent);
    public String getCellContentAtKey(String rowLabel, String colLabel);

    public int getRowsCount();
    /**
     * Get Total number of columns
     * @return Number of columns where first column is the row key so need to add 1
     * to the length of the LinkedHashMap storing the column values for a given row.
     */
    public int getColsCount();
    /**
     * Made to work with a TableMap in Swing. We want the row key to be displayed
     * in the first column of a JTable
     * @param colIndex int containing coulmun index starting at 0
     * @return string contaning the column identifier. Blank if column 0.
     */
    public String getcolLabelAtIndex(int colIndex);
    /**
     * Return the cell content at a given numeric position in the array
     * @param rowIndex Index of row
     * @param colIndex Index of column wheer first column contains row key so need to add 1
     * @return String
     */
    public String getCellContentAtIndex(int rowIndex, int colIndex);
    /**
     * Allows to getCellContentAtKey row keys
     * @return a Set containg the row keys
     */
    public Set getRowKeys();
    /**
     * Allows to getCellContentAtKey column keys
     * @return a Set containg the column keys
     */
    public Set getColKeys();
    /**
     * Allows to getCellContentAtKey the unique elements stored in the Flex2DArray
     * @return a Set containing the array elements
     */
    public Set getcellContent();
    /**
     * Allows to getCellContentAtKey values stored in a particular column
     * @return Set containing the values
     */
    public Set getValuesAtCol(String colKey);
    /**
     * Allows to getCellContentAtKey values stored in a particular row
     * @return Set Values stored in the row
     */
    public Set getValuesAtRow(String rowKey);
    /**
     * Provides a way to iterate over abstractFlex2DArray elements
     * @return Iterator
     */
    public Iterator iterator();
    /**
     * Get all col keys where a specific row has a specified entry. An
     * example: An array where row keys are dates and column keys are people.
     * Values are blank (available) or "Y" Unavailable.
     * We want to return all people available on a given date.
     * @param rowKey the key of the row we are interested in
     * @param value the value that must be set in the row for us to return the col key
     * @return Set The col keys selected on the basis of value in row specified by rowKey
     */
    public Set getColKeysForRow(String rowKey, String value);
    /**
     * Get all row keys where a specific column has a specified entry. An
     * example: An array where row keys are people, col keys are duties and the
     * value is "Y" or "N" depending on whether the person will do the duty.
     * We might want to return all people who will do a given duty
     * @param colKey the key of the column we are interested in
     * @param value the value that must be set in the column for us to return the row key
     * @return Set The row keys selected on the basis of value in column specified by colKey
     */
    public Set getRowKeysForCol(String colKey, String value);
    /**
     * Output row and column keys and cell contents as a linked list of strings 
     * which can easily be printed.
     * @param seperator field seperator in output eg comma for csv output
     * @return LinkedList printable "Lines" of the flex2DArray
     */
    public LinkedList<String> print(String seperator);
    /**
     * Delete a row keyed on input rowKey
     * @param rowKey String the row key of row to delete
     */
    public void deleteRow(String rowKey);
    /**
     * Delete a column keyed on input colKey
     * @param colkey String the col key of column to delete
     */
    public void deleteColumn(String colkey);
    /**
     * Find a value in an earlier row (specified by an integer) but in the same column. 
     * @param curRowKey String key of current row
     * @param colKey String key of column
     * @param earlier int the number of rows earlier we need to look at (1 is previous row)
     * @return ArrayList of values between current and current minus earlier rows
     */
    public ArrayList<String> getValuesAtEarlierRowsSameColumn(String curRowKey, String colKey, int earlier);
}
