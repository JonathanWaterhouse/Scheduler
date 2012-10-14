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
public class TaskScheduledConstraint  extends abstractConstraint {
    private flex2DArray taskDates;
    public TaskScheduledConstraint(flex2DArray taskDates){
        this.taskDates = taskDates;
    }    
    
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
