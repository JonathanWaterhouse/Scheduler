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
public class taskConstraint extends abstractConstraint {
    private flex2DArray assignments;
    public taskConstraint(flex2DArray assignments){
        this.assignments = assignments;
    }
    /**
     * from a list of input possible people decide from the constraint which people 
     * to remove from the list
     * @param available the incoming ArrayList of possible people
     * @return possible ArrayList of those still possible after the constraint is applied.
     */
    public ArrayList<String> allowed(ArrayList<String> available, String date,
            String task, flex2DArray schedule){
        ArrayList<String> possible = new ArrayList();
        for (String person : available){
            if(assignments.getCellContentAtKey(person, task).equals("Y")) {
                possible.add(person);
            }
        }
        return possible;
    }
    
}
