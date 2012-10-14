/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.ArrayList;

/**
 *
 * @author jonathan
 */
public abstract class abstractConstraint {
/**
 * 
 * @param inPeople Incoming available people to be acted upon by the constraint
 * @param date The date we are calculating the constraint on for each incoming person
 * @param task The task we are calculating the constraint on for each incoming person
 * @return Outgoing people withe some of incoming entries removed by virtue of the constraint
 */
    public abstract ArrayList<String> allowed(ArrayList<String> inPeople,
            String date, String task, flex2DArray schedule);
    
}
