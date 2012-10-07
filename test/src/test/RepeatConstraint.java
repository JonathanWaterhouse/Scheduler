/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class RepeatConstraint extends abstractConstraint {
    /**
     * from a list of input possible people decide from the constraint which people 
     * to remove from the list
     * @param available the incoming ArrayList of possible people
     * @return possible ArrayList of those still possible after the constraint is applied.
     */
    public ArrayList<String> allowed(ArrayList<String> available, String date,
            String task, flex2DArray schedule){
        ArrayList<String> possible = new ArrayList();
        int earlier = 2;
        ArrayList<String> prevValues = new ArrayList();
        prevValues = schedule.getValuesAtEarlierRowsSameColumn(date, task, earlier);
        for (String a : available) {
            if (!prevValues.contains(a)) possible.add(a);
        }
        return possible;
    }
}
