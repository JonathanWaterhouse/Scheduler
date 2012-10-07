package test;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

/**
 *
 * @author jonathan
 */
public class Schedule {
    /**
     * Initialises a blank schedule.
     */ 
    private flex2DArray schedule;
    public Schedule(flex2DArray taskDates){
        schedule = new flex2DArray();
        for(String d : taskDates.getColKeys()){
            for (String e : taskDates.getRowKeys()) {
                schedule.add(d,e," ");
            }
        }
    }
    /**
     * Initialises Schedule from serialised object.
     * @param Filename 
     */
    public Schedule(String Filename){
        schedule = Preferences.retrieveFlex2DArray(Filename);
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
        try{
            for (String d : dates){
                for (String t : tasks){
                    tempdate = d; temptask = t;
                    if (d.equals("2012-02-01 Wed")){
                        System.out.println(t);
                    }
                    if (!schedule.getCellContentAtKey(d, t).equals(" ")) continue;
                    for (String in : people) inWorkArea.add(in); //real copy not just a reference
                    //Need to loop over constraints here
                    for (abstractConstraint c: constraints ){
                        outWorkArea = c.allowed(inWorkArea, d,t, schedule);
                        //Prepare for next constraint loop
                        inWorkArea.clear();
                        for (String in : outWorkArea) inWorkArea.add(in);
                        outWorkArea.clear();
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
            }
        }
        catch (Exception e){
            System.out.println("Date = "+ tempdate+"/nTask = "+temptask);
        }
        Preferences.addFlex2DArray(Preferences.SCHEDULE_FILE, schedule);
    } 
    
    public flex2DArray getSchedule(){
        return schedule;
    }
}
