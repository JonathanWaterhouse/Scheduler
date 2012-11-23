package scheduler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

     /**
     * This object represents a schedule ie that object containing dates and events
     * with assignments of people to them (be aware that this is just one part of
     * a complete schedule since that contains other elements such as holidays. 
     * This class contains too an object which indicates whether an entry was a manual 
     * override. If so it may be required to treat it differently in certain 
     * circumstances. There are also methods to initialise the schedule and perform 
     * the calculations to fill the schedule consistent with any constraints.
     * @author jonathan.waterhouse@gmail.com
     */
public class ScheduleArray implements Serializable {

    private flex2DArray schedule;
    private flex2DArray scheduleOverride;
    /**
     * creates a new schedule with blank entries and columns keys formed from the
     * current values of tasks and row keys formed from the current values of dates.
     * Also creates flex2DArray scheduleOverride initialised with every element "N"
     * @param taskDates input values of current tasks and the dates they occur
     */
    public void initialise (flex2DArray taskDates){
        schedule = new flex2DArray();
        scheduleOverride = new flex2DArray();
        for(String d : taskDates.getColKeys()){
            for (String e : taskDates.getRowKeys()) {
                schedule.add(d,e,"");
                scheduleOverride.add(d,e,"N");
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
                            //TO DO Choose the least scheduled of the entries in inWorkArea
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
            System.out.println("ScheduleArray.java: Date = "+ tempdate+"\n" + "Task = "+temptask);
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
     * Get the flex2DArray containing the indicator as to whether a schedule element
     * was manually overridden ("Y" = yes, "N" = No)
     * @return flex2DArray of override indicators
     */
    public flex2DArray getScheduleOverride(){
        return scheduleOverride;
    }
    
    public void setScheduleOverride(flex2DArray schdlOverride){
        scheduleOverride = schdlOverride;
    }
    /**
     * This method is defined to allows us to check if two Schedules are equal by
     * content. It overrides the standard Java Object equals method which only 
     * checks that the object references are the same. 
     * @param scheduleCompareTo Incoming object which will be cast to scheduleArray
     * @return Boolean true if objects contents match and false if they do not
     */    
    public boolean equals(Object scheduleCompareTo){
        ScheduleArray scheduleComp = (ScheduleArray) scheduleCompareTo;
        if (schedule.equals(scheduleComp.getSchedule())
                && scheduleOverride.equals(scheduleComp.getScheduleOverride())){return true;}
        else {return false;}
    }
}
