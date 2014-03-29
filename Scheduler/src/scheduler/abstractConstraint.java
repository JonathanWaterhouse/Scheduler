package scheduler;

import java.util.ArrayList;

/**
 * This object represents a constraint on a schedule which has one method - that 
 * method is called by the scheduling program which provides a list of incoming 
 * allowed people - this method then reviews the incoming list in the light of 
 * the schedule that it represents and removes any people that are inconsistent
 * with this constraint.
 * @author jonathan.waterhouse@gmail.com
 */
public abstract class abstractConstraint {
/**
 * 
 * @param inPeople Incoming available people to be acted upon by the constraint
 * @param date The date we are calculating the constraint on for each incoming person
 * @param task The task we are calculating the constraint on for each incoming person
 * @param schedule the incoming schedule flex2DArry
 * @return Outgoing people withe some of incoming entries removed by virtue of the constraint
 */
    public abstract ArrayList<String> allowed(ArrayList<String> inPeople,
            String date, String task, flex2DArray schedule);
    
}
