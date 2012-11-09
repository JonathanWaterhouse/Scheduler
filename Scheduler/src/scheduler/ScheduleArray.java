package scheduler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

/**
 *
 * @author jonathan
 */
public class ScheduleArray implements Serializable {
    /**
     * Initialises a blank schedule.
     */ 
    private flex2DArray schedule;
    /**
     * creates a new schedule with blank entries and columns keys formed from the
     * current values of tasks and row keys formed from the current values of dates
     * @param taskDates input values of current tasks and the dates they occur
     * @return 
     */
    public void initialise (flex2DArray taskDates){
        schedule = new flex2DArray();
        for(String d : taskDates.getColKeys()){
            for (String e : taskDates.getRowKeys()) {
                schedule.add(d,e,"");
            }
        }
    }
    /**
     * Calculate a schedule that satisfies constraints
     */
    public void calculate(ArrayList<abstractConstraint> constraints,TreeSet<String> dates,
        TreeSet<String> tasks, ArrayList<String> people){
        ArrayList<String> inWorkArea = new ArrayList();
        ArrayList<String> outWorkArea = new ArrayList();
        String tempdate = "", temptask = "";
        Random random = new Random();
        StringBuffer entry;
        try{
            for (String d : dates){
                for (String t : tasks){ // Loop over all possible tasks and dates
                    tempdate = d; temptask = t;
                    entry = new StringBuffer("");
                    try {
                        entry.append(schedule.getCellContentAtKey(d, t));
                    }
                    catch (NullPointerException e) {entry.append("");}
                    //We do not change existing entries
                    if (entry.toString().equals("null")
                            ||entry.toString().equals("NONE")
                            ||entry.length() == 0) {
                        for (String in : people) inWorkArea.add(in); //real copy not just a reference
                        //Need to loop over constraints here
                        for (abstractConstraint c: constraints ){
                            outWorkArea = c.allowed(inWorkArea, d,t, schedule);
                            //Prepare for next constraint loop
                            inWorkArea.clear();
                            for (String in : outWorkArea) inWorkArea.add(in);
                            outWorkArea.clear();
                            if (inWorkArea.isEmpty()) break;
                        }
                        if (inWorkArea.isEmpty()) {
                            schedule.add(d,t,"NONE");
                        }
                        else{
                            int randomInt = random.nextInt(inWorkArea.size());
                            schedule.add(d,t,inWorkArea.get(randomInt));
                            inWorkArea.clear(); 
                        }
                    }
                    else { // schedule entry is not null and does not have length zero
                        continue;
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("Date = "+ tempdate+"\n" + "Task = "+temptask);
            e.printStackTrace();
        }
        Schedule s = new Schedule();
        s.setSchedule(this);
    } 
    
    public flex2DArray getSchedule(){
        return schedule;
    }
    
    public void setSchedule(flex2DArray schdl){
        schedule = schdl;
    }
    /**
     * This method is defined to allows us to check if two Schedules are equal by
     * content. It overrides the standard Java Object equals method which only 
     * checks that the object references are the same. 
     * @param compareTo Incoming object which will be cast to flex2DArray
     * @return Boolean true if objects contents match and false if they do not
     */    
    public boolean equals(Object compareTo){
        ScheduleArray comp = (ScheduleArray) compareTo;
        if (schedule.equals(comp.getSchedule())){return true;}
        else {return false;}
    }
}
