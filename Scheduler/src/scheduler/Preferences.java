package scheduler;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class stores application preferences.
 * 
 * @author jonathan
 */
public class Preferences {

    public static String HOLIDAYS_FILE  = "holidays.obj";
    public static String ASSIGNMENTS_FILE = "assignments.obj";
    public static String PEOPLE_FILE = "people.obj";
    public static String DATES_FILE = "dates.obj";
    public static String TASKS_FILE = "tasks.obj";
    public static String SCHEDULE_FILE = "schedule.obj";
    public static String TASK_DATE_FILE = "task-date.obj";
    public static String SEPERATOR = "|";
    public static String PERSON_TYPE = "Person";
    public static String TASK_TYPE = "Task";
    public static String DATE_TYPE = "Date";
    public static String NOT_FOUND = "Not Found";
    
    /**
     * Utility method to retrieve serialised array list.
     * @param objectFile String containing serialised object filename
     * @return ArrayList from serialised object
     */
    public static ArrayList<String> retrieve(String objectFile){
    File f = new File(objectFile);
    ArrayList<String> items;
    try {
        ObjectInputStream m = new ObjectInputStream(new FileInputStream(f));
        items = (ArrayList<String>) m.readObject();   
        m.close();
    }
    catch (IOException e){
        System.out.println("No serialised ArrayList object found");
        items = new ArrayList();
    }
    catch (ClassNotFoundException e) {
        System.out.println("Class definition not found");
        items = new ArrayList();
    }
    return items;
}
    
    /**
     * Utility method to retrieve flex2DArray from serialised storage.
     * @param objectFile String containing serialised object filename
     * @return flex2DArray retrieved from serialised storage
     */
    public static flex2DArray retrieveFlex2DArray(String objectFile){
    File f = new File(objectFile);
    flex2DArray items;
    try {
        ObjectInputStream m = new ObjectInputStream(new FileInputStream(f));
        items = (flex2DArray) m.readObject();   
        m.close();
    }
    catch (IOException e){
        System.out.println("No serialised flex2DArray object found");
        items = new flex2DArray();
    }
    catch (ClassNotFoundException e) {
        System.out.println("Class definition not found");
        items = new flex2DArray();
    }
    return items;
}
    /**
     * Utility method to add new item to an array list in serialised storage
     * @param type String type indicating if we are talking about holidays, tasks or people
     * @param newItem String new text item to be added to the serialised ArrayList
     */
    public static void add(String type, String newItem){
        String objectFile;
        if (type.equals(Preferences.PERSON_TYPE)) objectFile = Preferences.PEOPLE_FILE;
        else objectFile = Preferences.PEOPLE_FILE;
        // Add the new item to the appropriate object in serialised storage
        File f = new File(objectFile);
        ObjectOutputStream oos;
        ArrayList<String> items;
        try {
            ObjectInputStream m = new ObjectInputStream(new FileInputStream(f));
            items = (ArrayList<String>) m.readObject();  
            m.close();
            items.add(newItem);
            Collections.sort(items);
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(items);
            oos.close();
        }
        catch (IOException e){
            System.out.println("No serialised object found");
            items = new ArrayList();
            items.add(newItem);
            Collections.sort(items);
            try{
                oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(items);
                oos.close();
            }
            catch (IOException ee) {System.out.println("Unable to create new FileOutputStreamfor "
                    +objectFile);}
        }
        catch (ClassNotFoundException cnfe) {System.out.println("Class definition not found");}
        Preferences.enforceScheduleConsistency();
    }
     /**
     * Utility method to remove an item from an array list in serialised storage
     * @param type Ultimately tells  us if we are dealing with a person entry. I think that this is probably not required anymore and should be removed
     * @param member String the text item to be removed from the serialised ArrayList
     */   
    public static void remove(String type, String member){
        String objectFile;
        if (type.equals(Preferences.PERSON_TYPE)) objectFile = Preferences.PEOPLE_FILE;
        else objectFile = Preferences.PEOPLE_FILE;
        File f = new File(objectFile);
        ObjectOutputStream oos;
        ArrayList<String> items;
        try {
            ObjectInputStream m = new ObjectInputStream(new FileInputStream(f));
            items = (ArrayList<String>) m.readObject();  
            m.close();
            items.remove(member);
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(items);
            oos.close();
        }
        catch (IOException e){
            System.out.println("No serialised object found");
        }
        catch (ClassNotFoundException cnfe) {System.out.println("Class definition not found");} 
        //If input is a person:
        // Update holidays by removing persons row.
        // Also update assignments by removing persons row. 
        if (type.equals(Preferences.PERSON_TYPE)){
            flex2DArray hols = Preferences.retrieveFlex2DArray(Preferences.HOLIDAYS_FILE);
            hols.deleteRow(member);
            Preferences.addFlex2DArray(Preferences.HOLIDAYS_FILE, hols);
            flex2DArray assgs = Preferences.retrieveFlex2DArray(Preferences.ASSIGNMENTS_FILE);
            assgs.deleteRow(member);
            Preferences.addFlex2DArray(Preferences.ASSIGNMENTS_FILE, assgs);
        }
        //If input is a task:
        //Update assignments by removing the column for the task (ie remove people assignments to that task
        if (type.equals(Preferences.TASK_TYPE)){
            flex2DArray assgs = Preferences.retrieveFlex2DArray(Preferences.ASSIGNMENTS_FILE);
            assgs.deleteColumn(member);
            Preferences.addFlex2DArray(Preferences.ASSIGNMENTS_FILE, assgs);
        }
        //If input is a Date
        // Update holidaysby removing the column for the date (ie remove people assignments to that date)
        if (type.equals(Preferences.HOLIDAYS_FILE)){
            flex2DArray hols = Preferences.retrieveFlex2DArray(Preferences.HOLIDAYS_FILE);
            hols.deleteColumn(member);
            Preferences.addFlex2DArray(Preferences.HOLIDAYS_FILE, hols);
        }
        Preferences.enforceScheduleConsistency();
    }
    
    /**
     * Utility method to write a flex 2DArray to serialised storage
     * @param schedFile String name of serialised storage file
     * @param sched  flex 2DArray object
     */
    public static void addFlex2DArray (String schedFile, flex2DArray sched){
        File f;
        f = new File(schedFile);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(sched);
            oos.close();
        }
        catch (IOException e) {System.out.println("Unable to create new FileOutputStream for: "
                +schedFile);}
    }
    /**
     * Utility method to update a row in a flex2DArray with user specified values.For values that
     * the user did not assign, a default is set.
     * @param Source String denoting if we are dealing with holidays (key: person, date)
     * or task assignments (key: person, task)
     * @param rowKey String denoting the row key we are doing updates for
     * @param updates ArrayList of the updates This is the values of dates or tasks
     * for which the person is available if its a task or unavailable if it's a holiday
     * @param yesNoInd String denoting the value Y or N which means available
     * @param reverseYesNoInd String denoting the value Y or N which means UNavailable
     */
    public static void updateRowInFlex2DArray (String Source, String rowKey, 
            ArrayList<String> updates,String yesNoInd, String reverseYesNoInd){
        flex2DArray f = new flex2DArray();
        //retrieve any stored data and set all values of date or task as available or unavailable
        // ie a default value This is done
        flex2DArray td = Preferences.retrieveFlex2DArray(Preferences.TASK_DATE_FILE);
        Set<String> dates = td.getColKeys();
        Set<String> tasks = td.getRowKeys();
        if (Source.equals("Holidays")) {
            f = Preferences.retrieveFlex2DArray(Preferences.HOLIDAYS_FILE);
            f.deleteRow(rowKey);
            for (String d : dates) f.add(rowKey,d,reverseYesNoInd);    
        }
        else if (Source.equals("Assignments")) {
            f = Preferences.retrieveFlex2DArray(Preferences.ASSIGNMENTS_FILE);
            f.deleteRow(rowKey);
            for (String t : tasks) f.add(rowKey,t,reverseYesNoInd);
        }
        // Add the user defined updates
        for (int i = 0; i< updates.size();i++)f.add(rowKey, updates.get(i),yesNoInd);
        if (Source.equals("Holidays"))
            Preferences.addFlex2DArray(Preferences.HOLIDAYS_FILE, f);
        else if (Source.equals("Assignments"))
            Preferences.addFlex2DArray(Preferences.ASSIGNMENTS_FILE, f);
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
    public static void enforceScheduleConsistency(){
        flex2DArray taskDates = Preferences.retrieveFlex2DArray(Preferences.TASK_DATE_FILE);
        TreeSet<String> dates = taskDates.getColKeys();
        ArrayList<String> people = Preferences.retrieve(Preferences.PEOPLE_FILE);
        flex2DArray hols = Preferences.retrieveFlex2DArray(Preferences.HOLIDAYS_FILE);
        flex2DArray assgs = Preferences.retrieveFlex2DArray(Preferences.ASSIGNMENTS_FILE);
        String content;
        //FIRST: Enforce consistency of holidays
        flex2DArray NewHols = new flex2DArray();
        try{
            for (String p : people){
                for (String d : dates){
                    NewHols.add(p, d, "Y"); //Add default values for allowed people and dates
                }
            }
            //copy just required entries from existing store holiday object
            for (String p : people){
                for (String d : dates){
                    try {
                        content = hols.getCellContentAtKey(p, d);
                        if (content!=null) NewHols.add(p,d,content);
                    }
                    catch(NullPointerException e){ //There person was not considered yet for any tesk
                        //Do nothing - no need to overwrite the defaults set in NewAssgs
                    }
                }
            }
            Preferences.addFlex2DArray(Preferences.HOLIDAYS_FILE, NewHols); //Serialise holidays
        }
        catch (NullPointerException e){
            //holidays serialised object has not yet been created            
        }
        //SECOND: Enforce consistency of assignments
        // If a task is not available on any date, delete the task
        TreeSet<String> tasksIn = taskDates.getRowKeys();
        for (String t : tasksIn) {
            if (!taskDates.getValuesAtRow(t).contains("Y")) taskDates.deleteRow(t);
        }
        Preferences.addFlex2DArray(Preferences.TASK_DATE_FILE,taskDates);
        TreeSet<String> tasks = taskDates.getRowKeys();
        // Ensure any allowed task person comb is set as either "Y" or "N"
        flex2DArray NewAssgs = new flex2DArray();
        try {
            for (String p : people){
                for (String t : tasks){
                    NewAssgs.add(p, t, "N"); //Add default values for allowed people and tasks
                }
            }
            //copy just required entries from existing store Assignments object
            for (String p : people){
                for (String t : tasks){
                    try {
                        content = assgs.getCellContentAtKey(p, t);
                        if (content!=null) NewAssgs.add(p,t,content);
                    }
                    catch(NullPointerException e){ //There person was not considered yet for any tesk
                        //Do nothing - no need to overwrite the defaults set in NewAssgs
                    }

                }
            }
            flex2DArray outAssgs = NewAssgs.maintainFlex2DArrayConsistency("N");
            Preferences.addFlex2DArray(Preferences.ASSIGNMENTS_FILE,outAssgs);
        }
        catch(NullPointerException e){
            System.out.println("Help2"); //Assignments serialised object has not yet been created
        }
    }

}
