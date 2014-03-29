/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Jonathan
 */
public class ImportSchedule {
   
    private ArrayList<String> dates;
    private ArrayList<String> tasks;
    private ArrayList<String> people;
    private flex2DArray importSched; //Has format rowKey: date, colKey: task, entry: person
    
    public ImportSchedule(String dataFile)throws FileNotFoundException, IOException{
        /*
         * Read the input file into internal storage
         * @param DataFile parameter is a fully qualified file location in a string 
         */
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(dataFile));
        String line = null;
        String[] l;
        dates = new ArrayList();
        tasks = new ArrayList();
        TreeSet p = new TreeSet();
        importSched = new flex2DArray();
        int linei = 0;
        while ((line = reader.readLine()) != null) {
            l = line.split("\t");
            //Get tasks
            if (linei == 0){
                int j = 0;
                for (String s : l) {
                    if (j != 0) {tasks.add(s.trim());}
                    j += 1;
                }
            }
            else{
                //get dates and people
                dates.add(l[0].trim());
                int j = 0;
                for (String s : l) {
                    if (j != 0) {
                        p.add(s.trim()); //People
                        importSched.add(dates.get(linei-1).trim(),tasks.get(j-1).trim(),s.trim());
                    }
                    j += 1;
                }
            }
            linei += 1;
        } 
        people = new ArrayList();
        people.addAll(p);
        reader.close();
    }
    public String audit() {
        /**
         * return the programs interpretation of the input file as a string
         * to be reviewed by the calling code
         */
        String results = new String();
        StringBuilder p  = new StringBuilder();
        StringBuilder t  = new StringBuilder();
        StringBuilder d  = new StringBuilder();
        for (String s : people){p.append(s.trim()).append("; ");}
        for (String s : tasks){t.append(s.trim()).append("; ");}
        for (String s : dates){d.append(s.trim()).append("; ");}
        int pLen = 0;
        int tLen = 0;
        int dLen = 0;
        if (p.length() <= 40) {pLen = p.length();}
        else pLen = 40;
        if (t.length() <= 40) {tLen = t.length();}
        else tLen = 40;
        if (d.length() <= 40) {dLen = d.length();}
        else dLen = 40;                
        results =  "Please check file contents seem OK. \n"
        + "People: " + p.substring(0,pLen) + ".....etc\n"
        + "Tasks: " + t.substring(0,tLen) + ".....etc\n" 
        + "Dates: " + d.substring(0,dLen) + ".....etc\n"
        + "\nDo you want to continue?";
        return results;
    }
    
    public void setSchedule() {
        /**
         * Setup all the components of the schedule
         */
        Schedule sdb = new Schedule();
        ScheduleArray sArray = new ScheduleArray();
        sdb.setPeople(people);
        //Derive task-dates.
        flex2DArray taskDates = new flex2DArray();
        for (String d : importSched.getRowKeys()){
            for (String t : importSched.getColKeys()){
                taskDates.add(t,d,"Y");
            }
        }
        sdb.setTaskDates(taskDates);
        sdb.initHolidays();
        //Derive task Assignments
        flex2DArray assignments = new flex2DArray();
        for (String p : people){
            for (String t : tasks){
                if (importSched.getValuesAtCol(t).contains(p)){
                    assignments.add(p,t,"Y");
                }
                else assignments.add(p,t,"N");
            }
        }
        sdb.setAssignments(assignments);
        sArray.setSchedule(importSched);
        sdb.setSchedule(sArray);
    }
}
