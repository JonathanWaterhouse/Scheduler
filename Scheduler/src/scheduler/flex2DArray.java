package scheduler;
import java.io.Serializable;
import java.util.*;
/**
 * This class is an implementation of abstractFlex2DArray
 * @author jonathan
 */
public class flex2DArray implements abstractFlex2DArray, Serializable {
    /**
     * rowsMap is a LinkedHashMap map keyed on a string and containing another
     * LinkedHashMap
     */
    private LinkedHashMap<String,LinkedHashMap> rowsMap;
    /**
     * This class allows storeage of a 2D array where rows have flexible length
     * (but all rows are expected to be the same length or problems may occur)
     * and each array element is keyed on 2 strings, one representing the row and
     * the second the column. The implementation s based on linked hash maps and so
     * ordering should remain at that which was initialised. It is assumed that
     * keys and contents are STRINGS!! 
     */

    public flex2DArray (){
        rowsMap = new LinkedHashMap();

    }
    /**
     * This method will add an entry to the two dimensional array.
     * The element will be added to the end of the appropriate row if it contains
     * an existing row key and a new column key. If the row key is new, it will
     * be added as the last row of the array with the appropriate column key. If
     * the row and column key already exist then the ordering is unaffected - the
     * value associated with row and column keys will be just overwritten.
     * @param rowLabel String The row key
     * @param colLabel String The col key
     * @param cellContent String The value to add at (rowLabel, colLabel)
     */
    public void add(String rowLabel, String colLabel, String cellContent){
        if (rowsMap.containsKey(rowLabel)){
            LinkedHashMap e = rowsMap.get(rowLabel);
            e.put(colLabel,cellContent);
            rowsMap.put(rowLabel, e);
        }
        else {
            LinkedHashMap e = new LinkedHashMap();
            e.put(colLabel, cellContent);
            rowsMap.put(rowLabel, e);
        }
    }
    /**
     * 
     * @param rowLabel of flex2DArray to interrogate
     * @param colLabel of flex2DArray to interrogate
     * @return contents of the cell (Returns null if no entry found)
     */
    public String getCellContentAtKey(String rowLabel, String colLabel){
        String s = (String) rowsMap.get(rowLabel).get(colLabel);
        if (s == null){ //Linked hashmap returns null if entry not found
            System.out.println("Unable to find a value for row '"+rowLabel+
                    "' and column '"+colLabel+"'");
        }
        return s;
    }

    /**
     * 
     * @return count of rows in the flex2DArray
     */
    public int getRowsCount(){
        return rowsMap.size();
    }
    /**
     * Get Total number of columns
     * @return Number of columns where first column is the row key so need to add 1
     * to the length of the LinkedHashMap storing the column values for a given row.
     */
    public int getColsCount(){
        Collection<LinkedHashMap> c = rowsMap.values();
        int len = 0;
        for (LinkedHashMap l : c){
            len = l.size();
            break; // only need first element to getCellContentAtKey length
        }
        return len+1;
    }
    /**
     * Made to work with a TableMap in Swing. We want the row key to be displayed
     * in the first column of a JTable
     * @param colIndex int containing coulmun index starting at 0
     * @return string containing the column identifier. Blank if column 0.
     */
    public String getcolLabelAtIndex(int colIndex){
        Collection<LinkedHashMap> c = rowsMap.values();
        String colLabel = "";
        if (colIndex == 0) colLabel = "";
        else {
            for (LinkedHashMap l : c){
                Set keys = l.keySet();
                Iterator it = keys.iterator();
                int i = 1;
                while (it.hasNext()){
                    colLabel = (String) it.next();
                    if (i == colIndex){
                        break;
                    }
                    i++;
                }
                break; // only need first element to getCellContentAtKey length
            }
        }
        return colLabel;
    }
    /**
     * Return the cell content at a given numeric position in the array
     * @param rowIndex Index of row
     * @param colIndex Index of column wheer first column contains row key so need to add 1
     * @return String containing cell contents (returns null if no content)
     */
    public String getCellContentAtIndex(int rowIndex, int colIndex){
        Collection<LinkedHashMap> c = rowsMap.values();
        Set keys = rowsMap.keySet();
        String cellContent = "";
        boolean finish = false;
        int row = 0;
        for (LinkedHashMap l : c){  // rows
            Collection<String> values = l.values();
            if (colIndex == 0){ // Here we want to choose the RowsMap key and not the value
                Iterator it = keys.iterator();
                int keyIndex = 0;
                while (it.hasNext()){
                    cellContent = (String) it.next();
                    if (keyIndex == rowIndex) {
                        finish = true;
                        break;
                    }
                    keyIndex++;
                }
            }
            else { // Here we want to populate cell contents from the embedded LinkedHashMap values.
                int col = 1;
                for (String cell : values){ // columns
                    if (col == colIndex & row == rowIndex){
                        cellContent = cell;
                        finish = true;
                        break;
                    }
                    col++;
                }
            }
            if (finish) break;
            row++;
        }
        return cellContent;
    }
    /**
     * This method will produce sorted keys for each row of the flex2DArray. 
     * Example: If row 1 has keys Key A, row 2 Key B and row 3 Key C 
     * then the treeset returned will have Key A, Key B, Key C
     * @return Treeset of the column keys
     */
    public TreeSet<String> getRowKeys(){
        TreeSet rowKeys = new TreeSet(); //A sorted set
        for (String key : rowsMap.keySet()) rowKeys.add(key);
        return rowKeys;
    }
    /**
     * This method will produce sorted keys for each columns in each row of the flex2DArray. 
     * Example: If row 1 has keys Key A, Key B, Key C and row 2 has keys key A, Key D
     * Then the treeset returned will have Key A, Key B, Key C, Key D
     * @return TreeSet - Of column keys
     */
    public TreeSet<String> getColKeys(){
        TreeSet colKeys = new TreeSet(); //A sorted set
        for (LinkedHashMap<String,String> l : rowsMap.values()){
            for (String colKey : l.keySet()){
                colKeys.add(colKey);
            }
        }
        return colKeys;
    }
    /**
     * get all the values in the body of the 2D Array
     * @return TreeSet Containing the different unique array content values
     */
    public TreeSet<String> getcellContent(){
        Set v = new TreeSet();
        for (LinkedHashMap<String,String> l : rowsMap.values()){
            for (String s : l.values()){
                v.add(s);
            }
        }
        return (TreeSet<String>) v;
    }
    /**
     * Return set of values at a particular column of the array
     * @param colKey Key of the array column
     * @return TreeSet of values at the particular column of the array
     */
    public TreeSet<String> getValuesAtCol(String colKey){
        Set<String> values = new TreeSet();
        for (LinkedHashMap<String,String> l : rowsMap.values()){
            values.add(l.get(colKey));
        }
        return (TreeSet<String>) values;
    }
    /**
     * Return set of values at a particular row of the array
     * @param rowKey Key of the array column
     * @return Set of values at the particular row of the array
     */
    public TreeSet<String> getValuesAtRow(String rowKey){
        Set<String> values = new TreeSet();
        try {
            Collection<String> c = rowsMap.get(rowKey).values();
            for (String val : c) values.add(val);
            return (TreeSet<String>) values;
        }
        catch (NullPointerException e){return (TreeSet<String>) values;}
    }
    /**
     * Creates and returns an iterator over the elements of the flex2DArray.
     * Iteration proceeds by row and then column values within the row ie, row1
     * col1 followed by row1 col2 etc.
     * @return Iterator
     */
    public Iterator iterator(){
        return new internalIterator();
    }
    /**
     * Get all col keys where a specific row has a specified entry. An
     * example: An array where row keys are holiday dates and column keys are people.
     * Values are blank (available) or "Y" Unavailable.
     * We want to return all people available on a given date.
     * @param rowKey the key of the row we are interested in
     * @param value the value that must be set in the row for us to return the col key
     * @return Set The col keys selected on the basis of value in row specified by rowKey
     */
    public TreeSet<String> getColKeysForRow(String rowKey, String value){
        Set keys = new TreeSet();
        LinkedHashMap<String,String> row = rowsMap.get(rowKey);
        Set<String> colKeys = row.keySet();
        for (String ck : colKeys){
            if (row.get(ck).equals(value)) keys.add(ck);
        }
        return (TreeSet<String>) keys;
    }
    /**
     * Get all row keys where a specific column has a specified entry. An
     * example: An array where row keys are people, col keys are duties and the
     * value is "Y" or "N" depending on whether the person will do the duty.
     * We might want to return all people who will do a given duty
     * @param colKey the key of the column we are interested in
     * @param value the value that must be set in the column for us to return the row key
     * @return Set The row keys selected on the basis of value in column specified by colKey
     */
    public TreeSet<String> getRowKeysForCol(String colKey, String value){
        Set keys = new TreeSet();
        for (String rowKey : rowsMap.keySet()){
            if (rowsMap.get(rowKey).get(colKey).equals(value)) keys.add(rowKey);
        }
        return (TreeSet<String>) keys;
    }
    private class internalIterator implements Iterator,Serializable{
        private Iterator rowIterator;
        private Iterator colIterator;
        private String thisColKey = "";
        private String thisRowKey = "";
        private LinkedHashMap thisRow;
        private internalIterator(){
            rowIterator = rowsMap.keySet().iterator();
            if (rowIterator.hasNext()){
                thisRowKey = (String) rowIterator.next();
                thisRow = (LinkedHashMap) rowsMap.get(thisRowKey);
                colIterator = thisRow.keySet().iterator();
            }
        }
        /**
         * Standard way to indicate if any more elements to process in the array
         * @return boolean false if no more elements to process, true otherwise
         */
        public boolean hasNext(){
            if (!rowIterator.hasNext() & !colIterator.hasNext()){return false;}
            else {return true;}
        }
        /**
         * Standard way to return next element
         * @return Cell Next element as a cell object
         */
        public Object next(){
            // The standard way to use this iterator precludes us ever getting the
            // case where there is no next column AND no next row entry (since
            // we test for iterator.hasNext())
            if (colIterator.hasNext()){
                thisColKey = (String) colIterator.next(); //thisRowKey is unchanged
            }
            else {
                if (rowIterator.hasNext()){
                    thisRowKey = (String) rowIterator.next();
                    thisRow = (LinkedHashMap) rowsMap.get(thisRowKey);
                    colIterator = thisRow.keySet().iterator();
                    thisColKey = (String) colIterator.next();
                }
            }
            String cellContent = (String) thisRow.get(thisColKey);
            return new Cell(thisRowKey,thisColKey,cellContent);
        }
        /**
         * Must be implemented to implement iterator but we do not need it here
         * so it is empty
         */
        public void remove(){}
    }
    /*
     * @param The string character to be used as a field separator in the output
     * @return a linked list with one line of the flex2DArry and each field
     * separated by separator
     */
    public LinkedList<String> print(String seperator){
        LinkedList<String> l = new LinkedList();
        int row = 0, col= 0;
        String line;
        //Find length of longest entry in flex2DArray
        Integer lenKey = 0; Integer lenData = 0; String cc; String ccc;
        for (String r : rowsMap.keySet()){
            for (Object c : rowsMap.get(r).keySet()){
                if (r.length() > lenKey) lenKey = r.length(); //Row key
                cc = (String) c;
                if (cc.length() > lenKey) lenKey = cc.length(); // Col Key
                ccc = (String) rowsMap.get(r).get(c); // Data
                if (ccc.length() > lenData) lenData = ccc.length();
            }
        }
        System.out.println("Max key length : " + lenKey);
        System.out.println("Max data length : " + lenData);
        if (lenKey == 0) {
            l.add("No data to display.");
            return l;
        }
        // retrieve contents
        String fString = "%"+lenKey.toString()+"s";;
        String header = String.format(fString," " + seperator);
        for (String rowKey : rowsMap.keySet()){
            //Print headers
            if (row == 0){
                for(Object colKey : this.getColKeys()) {
                    String s = (String) colKey;
                    header += String.format(fString,s) +seperator;
                }
                if (header.endsWith(seperator)) {
                    header = header.substring(0,header.length()-1);
                }
                l.add(header);
            }
            row +=1;
            fString = "%"+lenKey.toString()+"s";
            line = String.format(fString, rowKey) +seperator; //Print key of row we will process next
            for (Object colKey : rowsMap.get(rowKey).keySet()){
                String s = this.getCellContentAtKey(rowKey, (String) colKey);
                line += String.format(fString,s)+seperator;
            }
            if (line.endsWith(seperator)) line = line.substring(0,line.length()-1);
            l.add(line);
            line = "";
        }
        return l;
    }
    
    public void deleteRow(String rowKey){
        rowsMap.remove(rowKey);
    }
    
    public void deleteColumn(String colKey){
        for (String rowKey : rowsMap.keySet()) rowsMap.get(rowKey).remove(colKey);
    }
    
    public ArrayList<String> getValuesAtEarlierRowsSameColumn(String curRowKey, String colKey, int earlier){
        ArrayList<String> out = new ArrayList();
        LinkedList<String> colVals = new LinkedList(); //Preserves ordering
        int i = 0;
        //Get all col Values
        for (LinkedHashMap row : rowsMap.values()){
            colVals.add((String)row.get(colKey));
            i+=1;
        }
        int curRowPos = 0;
        i = 0;
        //Get row position of curRowKey
        for (String rowKey : rowsMap.keySet()){
            if (rowKey.equals(curRowKey)) curRowPos = i;
            i+=1;
        }
        // iterate backward through colKeys LinkedList between curRowPos and (curRowPos - earlier)
        for (int j=curRowPos-1; j>curRowPos-earlier; j--){
            if (j<0) break; //We are beyond start of Linked List
            out.add(colVals.get(j));
        }
        return out;
    }
    /**
     * Sort flex2DArray by col and row keys. This method's use ensures that we
     * can rely on each row having its column keys in the same position which is
     * necessary for the print() method to work properly.
     */
    public void sort(){
        flex2DArray copyF2D = new flex2DArray(); //currently no entries
        // Get Total Column Keys across all rows
        TreeSet<String> colKeys = new TreeSet(); //TreeSet is sorted implementation of set 
        for (LinkedHashMap<String,String> l : this.rowsMap.values()){
            for (String colKey : l.keySet()){
                colKeys.add(colKey);
            }
        }
        // Get Sorted Row Keys
        TreeSet<String> rowKeys = new TreeSet(); //TreeSet is sorted implementation of set
        for (String key : this.rowsMap.keySet()) rowKeys.add(key);
        //Read the starting flex2DArray for all rown and column keys. If a row is 
        //missing something then we just add a default entry 
        String s;
        String value;
        for (String rowKey : rowKeys){
            for (String colKey : colKeys){
                s = (String) this.rowsMap.get(rowKey).get(colKey);
                if (s == null) {copyF2D.add(rowKey, colKey, s);}
                else {copyF2D.add(rowKey, colKey, s);}
            }
        }
        this.rowsMap = copyF2D.rowsMap;
        return;
    }
    /**
     * This method is defined to allows us to check if two flex 2DArrays are equal by
     * content. It overrides the standard Java Object equals method which only 
     * checks that the object references are the same. Note that this implementation 
     * does not check the ordering of row key , col key and value tuples.It checks that
     * row and column lengths are the same, that the set of keys in rows and columns are equal
     * and that when iterating through col and row keys that the returned values are identical.
     * @param compareTo Incoming object which will be cast to flex2DArray
     * @return Boolean true if objects contents match and false if they do not
     */
    public boolean equals(Object compareTo){
       flex2DArray comp = (flex2DArray) compareTo;
       //check row and col lengths equal
       if (this.getRowsCount() == comp.getRowsCount() &
                this.getColsCount()== comp.getColsCount()){
           //check row and col keys equal
           //Note that the treeset implementation of "equals" compares contents
           if(this.getRowKeys().equals(comp.getRowKeys())& 
                   this.getColKeys().equals(comp.getColKeys())){
               // the set of keys is equal 
               for (String row : this.getRowKeys()){
                   for (String col : this.getColKeys()){
                       if (!this.getCellContentAtKey(row, col).equals(comp.getCellContentAtKey(row, col))){
                           return false; //We found an element which is not equal
                       }
                   }
               }
               return true; //If we got to here all elements are equal
           }
           return false;
       }
       else return false;
    }
}
