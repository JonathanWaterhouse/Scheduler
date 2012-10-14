/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 * @author user
 */
public class AlreadyScheduledConstraint extends abstractConstraint{
    
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
