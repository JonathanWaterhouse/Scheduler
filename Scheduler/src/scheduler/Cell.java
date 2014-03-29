package scheduler;

/**
 * This object represents the basic unit of the flex2DArray the cell which consists
 * of row and column keys and a content.
 * @author jonathan.waterhouse@gmail.com
 */
public class Cell {
/**
 * Holds the contents of a cell of a flex2DArray and its X and Y keys
 */
    private String rKey, cKey, val;
    public Cell(String rowKey, String colKey, String value){
        rKey = rowKey; cKey = colKey; val = value;
    }
    public String getRowKey() {return rKey;}
    public String getColKey() {return cKey;}
    public String getValue() {return val;}
}
