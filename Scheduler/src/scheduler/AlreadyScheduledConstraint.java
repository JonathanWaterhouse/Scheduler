/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Implements abstractConstraint with code that ensures that the person is not
 * already scheduled on this date for another task.
 * @author jonathan.waterhouse@gmail.com
 */
public class AlreadyScheduledConstraint extends abstractConstraint{
     /**
     * @param available incoming ArrayList of possible people for the schedule slot
     * @param date schedule slot date
     * @param task schedule slot task
     * @param schedule the incoming schedule flex2DArray
     * @return outgoing array list of possible people for schedule slot after the 
     * constraint has been checked. This is not a reference copy but rather a deep copy.
     */
    public ArrayList<String> allowed(ArrayList<String> available, String date,
        String task, flex2DArray schedule){
        ArrayList<String> possible = new ArrayList();
        TreeSet<String> prevValues = new TreeSet();
        prevValues = schedule.getValuesAtRow(date);
        for (String a : available) {
            if (!prevValues.contains(a)) possible.add(a);
        }
        return possible;
    }
    
}
