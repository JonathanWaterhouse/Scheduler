/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author user
 */
public class MinimumUseConstraint extends abstractConstraint {
    /**
     * from a list of input possible people decide from the constraint which people 
     * to remove from the list. This constraint keeps only the people scheduled least 
     * in the current task.This method's implementation assumes a maximum number of task
     * occurrences for a person in the schedule period of 999999999.
     * @param available the incoming ArrayList of possible people
     * @return possible ArrayList of those still possible after the constraint is applied.
     */
    public ArrayList<String> allowed(ArrayList<String> available, String date,
            String task, flex2DArray schedule){
        ArrayList<String> possible = new ArrayList();
        ArrayList out = new ArrayList();
        // Get people on date in given column
        ArrayList<String> peopleUsage = schedule.valueOccurrenceCountAtCol(task);
        String[] pUsage = new String[peopleUsage.size()];
        peopleUsage.toArray(pUsage);
        try{
            Arrays.sort(pUsage); //Get similar values of person listed contiguously
        }
        catch (NullPointerException e){
            System.out.println("MinimumUseConstraint.java: date - "+date + " task - "+task);
            for (String el : pUsage) System.out.println("MinimumUseConstraint.java: pUsage - "+el);
            e.printStackTrace();
        }
        peopleUsage.clear();
        for (String el : pUsage) peopleUsage.add(el); //Gives us a sorted list of people already scheduled for the task
        int lowestUsage = 999999999; int finalPos; int usages;
        //find the lowest usage number
        for (String pers : available) {
            int firstPos = peopleUsage.indexOf(pers);
            if (firstPos == -1) {//this person not been used yet
                //possible.add(pers);
                lowestUsage = 0;
                break;
            } 
            else{
                finalPos = peopleUsage.lastIndexOf(pers);
                usages = finalPos-firstPos+1;
                if (usages < lowestUsage) lowestUsage = usages;
            }
        }
        //Find the people corresponding to the lowest usage number
        for (String pers : available) {
            int firstPos = peopleUsage.indexOf(pers);
            if (firstPos == -1 && lowestUsage == 0) {
                possible.add(pers);
            } 
            else if (lowestUsage != 0) {
                finalPos = peopleUsage.lastIndexOf(pers);
                usages = finalPos-firstPos+1;
                if (usages == lowestUsage) possible.add(pers);
            }

        }
        return possible;
    }    
}
