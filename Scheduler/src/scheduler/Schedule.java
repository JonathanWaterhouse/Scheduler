/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

/** 
 * This class stores all the elements required to make a schedule. That includes 
 * people, people task assignments, people holidays and the task date combinations
 * with assignments to people that actually make up the total schedule proper.
 * 
 * It has several key components:
 * A) People 
 *    Stored as an array list. These should be set up first. This is the default 
 *    place to go to find which people are in the labour pool
 * B) Task Dates 
 *    Stored in a flex2DArray with tasks in the rows and dates in the columns.
 *    The dates are the dates that the task can be scheduled. The flex2DArray body
 *    has a "N" meaning that task cannot happen on the date and "Y" otherwise.
 *    The default value is "N" (ie you have to explicitly assign a task to a date)
 *    This data is used as the master source of dates and tasks for all other data
 *    structures.
 * C) Holidays
 *    Stored in a flex2DArray with people in the rows and dates in the columns.
 *    The dates are the dates that the person can be scheduled. The flex2DArray body
 *    has a "Y" meaning that person is available on the date and "N" otherwise.
 *    The default value is "Y" ie we assume that a person is not on holiday
 * D) Assignments
 *    Stored in a flex2DArray with people in the rows and tasks in the columns.
 *    The tasks are those that a person can do and have a "Y" in the array body.
 *    The default setting is that a task cannot be assigned to a person ie "N"
 * As can be seen the meaning of "Y" in an entry is "AVAILABLE"
 * E) Schedule 
 *    Stored as a schedule object which is a flex 2DArray plus some other methods.
 *    The flex2DArray has row keys task, col keys Date and the contents are the name
 *    of a person scheduled to do the task. NONE means either the task has no one
 *    scheduled to do it (eg it was impossible to find anyone and satisfy the 
 *    constraints or the task is not happening on that day)   
 * 
 * @author Jon Waterhouse
 */
public class Schedule implements Serializable {
    private static String DATABASE_FILE  = "schedule-database.obj";
    public static String SEPERATOR = "|";
    private ArrayList<String> people;
    private flex2DArray taskDates;
    private flex2DArray assignments;
    private flex2DArray holidays;
    private ScheduleArray schedule;
    /**
     * The class initiator will try and read the current values of the scheduleDatabase
     * from serialised storage. If it can't find anything then initialised values
     * will be returned.
     */
    public Schedule() {
        Store store = new Store();
        Schedule db;
        try {
            db = store.getStoredSchedule(DATABASE_FILE);
            this.holidays = db.holidays;
            this.people = db.people;
            this.assignments = db.assignments;
            this.taskDates = db.taskDates;
            this.schedule = db.schedule;
        }
        catch (IOException e){
            System.out.println("No Schedule Database object found");
            people = new ArrayList();
            taskDates = new flex2DArray();
            assignments = new flex2DArray();
            holidays = new flex2DArray();
            schedule = new ScheduleArray();
            schedule.initialise(taskDates);
            this.writeDatabase();
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class definition not found");
            people = new ArrayList();
            taskDates = new flex2DArray();
            assignments = new flex2DArray();
            holidays = new flex2DArray();
            schedule = new ScheduleArray();
            schedule.initialise(taskDates);
            this.writeDatabase();
        }
    }
    /**
     * @return the people
     */
    public ArrayList<String> getPeople() {
        return people;
    }

    /**
     * @param people the full list of people to set
     */
    public void setPeople(ArrayList<String> people) {
        this.people = people;
        this.enforceScheduleConsistency();
        this.writeDatabase();
    }

    /**
     * @return the taskDates
     */
    public flex2DArray getTaskDates() {
        return taskDates;
    }

    /**
     * @param taskDates the full array of taskDates to set
     */
    public void setTaskDates(flex2DArray taskDates) {
        this.taskDates = taskDates;
        this.enforceScheduleConsistency();
        this.writeDatabase();
    }

    /**
     * @return the assignments
     */
    public flex2DArray getAssignments() {
        return assignments;
    }

    /**
     * @param assignments the full array of assignments to set
     */
    public void setAssignments(flex2DArray assignments) {
        this.assignments = assignments;
        this.enforceScheduleConsistency();
        this.writeDatabase();
    }

    /**
     * @return the holidays
     */
    public flex2DArray getHolidays() {
        return holidays;
    }

    /**
     * @param holidays the full array of holidays to set
     */
    public void setHolidays(flex2DArray holidays) {
        this.holidays = holidays;
        this.enforceScheduleConsistency();
        this.writeDatabase();
    }

        /**
     * @return the holidays
     */
    public ScheduleArray getSchedule() {
        return schedule;
    }

    /**
     * @param holidays the full schedule array to set
     */
    public void setSchedule(ScheduleArray schedule) {
        this.schedule = schedule;
        this.enforceScheduleConsistency();
        this.writeDatabase();
    }
    
    /**
     * Save stored private variables which are part of the object Schedule
     * to serialised storage
     */
    public void writeDatabase(){
        Store store = new Store();
        File f;
        f = new File(DATABASE_FILE);
        try {
            store.writeSchedule(this, DATABASE_FILE);
        }
        catch (IOException e) {System.out.println("Unable to create new FileOutputStream for: "
                +DATABASE_FILE);}
    }
    /**
     * Utility method to ensure that the schedule remains consistent after updates
     * 1) Insertions of master data elements (people, tasks, dates) are matched by
     *    insertions of people, dates in holidays or insertions of people and tasks
     *    are matched by insertions of people and tasks in assignments
     * 2) Deletions of master data elements lead to removal of dates or people in holidays
     *    or removal of people and tasks lead to removal of people and tasks in
     *    assignments
     * 3) deletions of master data elements tasks or dates leads to removal of entries
     *    in the schedule 
     * Note that people entries in the body of the schedule are not removed even if 
     * they are not in master data since schedule can contain "external" people
     * 
     */    
    public void enforceScheduleConsistency(){
        //Are any entries in taskDates, assignments, holidays not assigned
        //a value? If so assign the default value.
        String str;
        for (String t : taskDates.getRowKeys()){
            for (String d : taskDates.getColKeys()){
                str = taskDates.getCellContentAtKey(t,d);
                if (str == null){
                    taskDates.add(t, d, "N");
                    continue;
                }
                if (!str.equals("Y") & !str.equals("N")) taskDates.add(t, d, "N");
            }
        }
        for (String p : assignments.getRowKeys()){
            for (String t : assignments.getColKeys()){
                str = assignments.getCellContentAtKey(p,t);
                if (str == null){
                    assignments.add(p, t, "N");
                    continue;
                }
                if (!str.equals("Y") & !str.equals("N")) assignments.add(p, t, "N");
            }
        }
        for (String p : holidays.getRowKeys()){
            for (String d : holidays.getColKeys()){
                str = holidays.getCellContentAtKey(p,d);
                if (str == null){
                    holidays.add(p, d, "Y");
                    continue;
                }
                if (!str.equals("Y") & !str.equals("N")) holidays.add(p, d, "Y");
            }
        }
        //If people change ensure that other pieces of schedule change accordingly
        if (people.size() > 0){
            // check all people are represented in holidays
            for (String p : people){
                if(!holidays.getRowKeys().contains(p)) {
                    for (String d : holidays.getColKeys()) holidays.add(p,d,"Y");
                }
            }
            // check all people are represented in assignments
            for (String p : people){
                if(!assignments.getRowKeys().contains(p)) {
                    for (String t : assignments.getColKeys()) assignments.add(p,t,"N");
                }
            }
            // check people entries in assignments and holidays. Delete any not in people
            TreeSet<String> compare = assignments.getRowKeys();
            for (String p : compare){
                if (!people.contains(p)) {assignments.deleteRow(p);}
            }
            // check people entries in holidays Delete any not in people
            compare = holidays.getRowKeys();
            for (String p : compare){
                if (!people.contains(p)) {holidays.deleteRow(p);}
            }
        }
        // Tidy up taskDates. 
        // Remove any tasks where there are no assignments to a date.
        // Remove any Dates where no task is scheduled.
        // This is necessary because taskDate will be the basis of further auditting.
        if (taskDates.getRowsCount() > 0){
            TreeSet<String> compare = taskDates.getRowKeys();
            for (String t : compare) {
                if (!taskDates.getValuesAtRow(t).contains("Y")) taskDates.deleteRow(t);
            }
        }
        if (taskDates.getColsCount() > 0){
            TreeSet<String> compare = taskDates.getColKeys();
            for (String d : compare) {
                if (!taskDates.getValuesAtCol(d).contains("Y")) taskDates.deleteColumn(d);
            }
        }
        // Examine taskDates to get a list of tasks. Then remove any tasks from
        // assignments or schedule that are not in that list.
        if (assignments.getRowsCount() >0){
            TreeSet<String> compare = assignments.getColKeys();
            for (String t : compare){
                if (!taskDates.getRowKeys().contains(t)) assignments.deleteColumn(t);
            }
        }
        if (schedule.getSchedule().getRowsCount() >0){
            TreeSet<String> compare = schedule.getSchedule().getColKeys();
            flex2DArray s = schedule.getSchedule();
            for (String t : compare){
                if (!taskDates.getRowKeys().contains(t)) s.deleteRow(t);
            }
            schedule.setSchedule(s);
        }
        // Ensure all current tasks are represented in assignments
        if (taskDates.getRowsCount()>0){
            for (String t : taskDates.getRowKeys()){
                if (!assignments.getColKeys().contains(t)) {
                    for (String p : assignments.getRowKeys()) assignments.add(p,t,"N");
                }
            }
        }
        // Ensure all current dates are represented in holidays
        if (taskDates.getColsCount() > 0){
            for (String d : taskDates.getColKeys()){
                if (!holidays.getColKeys().contains(d)) {
                    for (String p: taskDates.getRowKeys()) holidays.add(p,d,"Y");
                }
            }
        }
        // assignments or schedule that are not in that list.
        // Examine taskDates to get a list of dates. Remove any dates from 
        // schedule and holidays which are not in taskdates
        if (schedule.getSchedule().getColsCount() >0){
            TreeSet<String> compare = schedule.getSchedule().getRowKeys();
            flex2DArray s = schedule.getSchedule();
            for (String d : compare){
                if (!taskDates.getColKeys().contains(d)) s.deleteColumn(d);
            }
            schedule.setSchedule(s);
        }
        if (holidays.getColsCount() >0){
            TreeSet<String> compare = holidays.getColKeys();
            for (String d : compare){
                if (!taskDates.getColKeys().contains(d)) holidays.deleteColumn(d);
            }
        }
        // Ensure keys of columns are in same order in each row
        taskDates.sort();
        assignments.sort();
        holidays.sort();
        flex2DArray s = schedule.getSchedule();
        s.sort();
        schedule.setSchedule(s);
        // Serialise everything after making it consistent
        this.writeDatabase(); 
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.people != null ? this.people.hashCode() : 0);
        hash = 47 * hash + (this.taskDates != null ? this.taskDates.hashCode() : 0);
        hash = 47 * hash + (this.assignments != null ? this.assignments.hashCode() : 0);
        hash = 47 * hash + (this.holidays != null ? this.holidays.hashCode() : 0);
        hash = 47 * hash + (this.schedule != null ? this.schedule.hashCode() : 0);
        return hash;
    }
    /**
     * This method is defined to allows us to check if two ScheduleDatabases are equal by
     * content. It overrides the standard Java Object equals method which only 
     * checks that the object references are the same. It checks if the values of 
     * people, taskDates,assignments, holidays and schedule are equal.
     * @param compareTo Incoming object which will be cast to Schedule
     * @return Boolean true if objects contents match and false if they do not
     */
    @Override
    public boolean equals(Object compareTo){
        Schedule sdb = (Schedule) compareTo;
        if (people.equals(sdb.getPeople()) & taskDates.equals(sdb.getTaskDates())
                & assignments.equals(sdb.getAssignments())
                & holidays.equals(sdb.getHolidays())
                & schedule.equals(sdb.getSchedule())) {return true;}
        else {return false;}
    }
    /*
     * initialise holidays with default "Y" ie people available on all dates
     */
    public void initHolidays(){
        flex2DArray f = new flex2DArray();
        for (String p : people){
            for (String d : taskDates.getColKeys()) f.add(p, d, "Y");
        }
        holidays = f;
        this.enforceScheduleConsistency();
        this.writeDatabase();
    }
    /*
     * initialise assignments with default "N" ie people not assigned to any tasks
     */
    public void initAssignments(){
        flex2DArray f = new flex2DArray();
        for (String p : people){
            for (String t : taskDates.getRowKeys()) f.add(p, t, "N");
        }
        assignments = f;   
        this.enforceScheduleConsistency();
        this.writeDatabase();
    }
    /**
     * Output some audit results concerning the schedule into a flex2DArray. 
     * The audit output is a flex2DArray with rows indicating person and columns
     * indicating task. The cell content for a given person task is a count of
     * how many times the person was scheduled for the task in the schedule.
     * @return flex2DArray The output of the audit
     */
    public flex2DArray auditSchedule(){
        flex2DArray auditResults = new flex2DArray();
        Integer count;
        //Initialise the audit array with counts of zero
        TreeSet<String> tasks = schedule.getSchedule().getColKeys();
        TreeSet<String> dates = schedule.getSchedule().getRowKeys();
        for (String t : tasks){
            for (String d : dates){
                    String person = schedule.getSchedule().getCellContentAtKey(d, t);
                    auditResults.add(person,t,"0"); // Remember the array stores strings
            }
        }
        //Iterate through the schedule by task and date.
        for (String t : tasks){
            for (String d : dates){
                //At each cell see who is assigned
                String person = schedule.getSchedule().getCellContentAtKey(d, t);
                // If a person is assigned to a task increment the count of times assigned by 1
                try{
                    count = Integer.parseInt(auditResults.getCellContentAtKey(person, t));
                }
                catch (NullPointerException e){
                    count = 0;
                }
                catch (NumberFormatException e){
                    System.out.println("Task : "+t+ " Date : "+d+" Person : "+person);
                    System.out.println("Audit result : "+auditResults.getCellContentAtKey(person, t));
                    count = 0;
                }
                auditResults.add(person,t,Integer.toString(count+1));
            }
        }
        auditResults.makeArrayRectangular("0");
        return auditResults;
    }
}
