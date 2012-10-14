/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class holConstraint extends abstractConstraint {
    private flex2DArray holidays;
    public holConstraint(flex2DArray holidays){
        this.holidays = holidays;
    }    
    public ArrayList<String> allowed (ArrayList<String> available, String date, 
            String task, flex2DArray schedule) {
        ArrayList<String> possible = new ArrayList();
        for (String person : available){
            if(holidays.getCellContentAtKey(person, date).equals("Y")) {
                possible.add(person);
            }
        }
        return possible;    
    }
    
}
