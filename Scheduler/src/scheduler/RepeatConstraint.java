
package scheduler;

import java.util.ArrayList;

/**
 * Implements abstractConstraint with code that ensures that the same person was
 * not scheduled on the same task in the last three scheduled occurrences of the 
 * event.
 * @author jonathan.waterhouse@gmail.com
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
        int earlier = 3;
        ArrayList<String> prevValues = new ArrayList();
        prevValues = schedule.getValuesAtEarlierRowsSameColumn(date, task, earlier);
        for (String a : available) {
            if (!prevValues.contains(a)) possible.add(a);
        }
        return possible;
    }
}
