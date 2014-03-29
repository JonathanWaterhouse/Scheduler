/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.ArrayList;

/**
 * Implements abstractConstraint with code that ensures that the task is actually
 * scheduled on this date.  
 * @author jonathan.waterhouse@gmail.com
 */
public class TaskScheduledConstraint  extends abstractConstraint {
    private flex2DArray taskDates;

    public TaskScheduledConstraint(flex2DArray taskDates){
        this.taskDates = taskDates;
    }    
    /**
     * 
     * @param available incoming ArrayList of possible people for the schedule slot
     * @param date schedule slot date
     * @param task schedule slot task
     * @param schedule the incoming schedule flex2DArray
     * @return outgoing array list of possible people for schedule slot after the 
     * constraint has been checked. This is not a reference copy but rather a deep copy.
     */
    public ArrayList<String> allowed (ArrayList<String> available, String date, 
        String task, flex2DArray schedule) 
    {
        boolean notScheduled = false;
        try {
            if (taskDates.getCellContentAtKey(task,date).equals("N")) notScheduled = true; 
            else if (taskDates.getCellContentAtKey(task,date).equals("Y")) notScheduled = false;
        }
        catch (NullPointerException e) {
            notScheduled = true;
        } 
        if (notScheduled ) {
            ArrayList<String> empty = new ArrayList();
            return empty; //Empty people list since task is not scheduled
        }
        else {
            ArrayList<String> returned = new ArrayList();
            for (String el : available) returned.add(el); 
            return returned; //A new copy not a reference
        
        }
    }
}
