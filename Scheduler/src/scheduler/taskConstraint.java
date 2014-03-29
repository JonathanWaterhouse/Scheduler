/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.ArrayList;

/**
 * Implements abstractConstraint with code that ensures that the person is actually
 * available for this task.
 * @author jonathan.waterhouse@gmail.com
 */
public class taskConstraint extends abstractConstraint {
    private flex2DArray assignments;
    public taskConstraint(flex2DArray assignments){
        this.assignments = assignments;
    }
    
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
        for (String person : available){
            if(assignments.getCellContentAtKey(person, task).equals("Y")) {
                possible.add(person);
            }
        }
        return possible;
    }
    
}
