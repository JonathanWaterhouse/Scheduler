/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.ArrayList;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author user
 */
public class ScheduleDatabaseTest {
    ArrayList<String> people = new ArrayList();
    flex2DArray taskDates = new flex2DArray();
    flex2DArray assignments = new flex2DArray();
    flex2DArray holidays = new flex2DArray();
    ScheduleArray schedule = new ScheduleArray();
    
    public ScheduleDatabaseTest() {
        people.add("John"); 
        people.add("Jean");
        people.add("Bill");
        taskDates.add("taskA", "01012012", "Y");
        taskDates.add("taskA", "01022012", "N");
        taskDates.add("taskB", "01012012", "N");
        taskDates.add("taskB", "01022012", "Y");
        assignments.add("John","taskA", "Y");
        assignments.add("Jean", "taskB", "Y");
        assignments.add("John", "taskB", "N");
        assignments.add("Jean", "taskA", "N");
        holidays.add("John", "01012012", "Y");
        holidays.add("John", "01022012", "Y");
        holidays.add("Jean", "01012012", "Y");
        holidays.add("Jean", "01022012", "Y");
        flex2DArray s = new flex2DArray();
        s.add("taskA","01012012", "John");
        s.add("taskB","01022012","Jean");
        s.add("taskA","01022012", "None");
        s.add("taskB","01012012","None");
        schedule.setSchedule(s);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    /**
     * Test of setPeople method, of class Schedule.
     */
    @Test
    public void testSetPeople() {
        System.out.println("setPeople");
        Schedule instance = new Schedule();
        instance.setPeople(people);
        assertThat(instance.getPeople(),Is.is(people));
    }
    /**
     * Test of getPeople method, of class Schedule.
     */
    @Test
    public void testGetPeople() {
        System.out.println("getPeople");
        Schedule instance = new Schedule();
        ArrayList expResult = people;
        ArrayList result = instance.getPeople();
        assertThat(expResult,Is.is(result));
    }

    /**
     * Test of setTaskDates method, of class Schedule.
     */
    @Test
    public void testSetTaskDates() {
        System.out.println("setTaskDates");
        Schedule instance = new Schedule();
        instance.setTaskDates(taskDates);
        assertThat(instance.getTaskDates(),Is.is(taskDates));
    }
    /**
     * Test of getTaskDates method, of class Schedule.
     */
    @Test
    public void testGetTaskDates() {
        System.out.println("getTaskDates");
        Schedule instance = new Schedule();
        flex2DArray expResult = taskDates;
        flex2DArray result = instance.getTaskDates();
        assertThat(expResult,Is.is(result));
    }

    /**
     * Test of setAssignments method, of class Schedule.
     */
    @Test
    public void testSetAssignments() {
        System.out.println("setAssignments");
        Schedule instance = new Schedule();
        instance.setAssignments(assignments);
        assertThat(instance.getAssignments(),Is.is(assignments));
    }
    /**
     * Test of getAssignments method, of class Schedule.
     */
    @Test
    public void testGetAssignments() {
        System.out.println("getAssignments");
        Schedule instance = new Schedule();
        flex2DArray expResult = assignments;
        flex2DArray result = instance.getAssignments();
        assertThat(expResult,Is.is(result));
    }

    /**
     * Test of setHolidays method, of class Schedule.
     */
    @Test
    public void testSetHolidays() {
        System.out.println("setHolidays");
        Schedule instance = new Schedule();
        instance.setHolidays(holidays);
        assertThat(instance.getHolidays(),Is.is(holidays));
    }
     /**
     * Test of getHolidays method, of class Schedule.
     */
    @Test
    public void testGetHolidays() {
        System.out.println("getHolidays");
        Schedule instance = new Schedule();
        flex2DArray expResult = holidays;
        flex2DArray result = instance.getHolidays();
        assertThat(expResult,Is.is(result));
    }
     /**
     * Test of setschedule method, of class Schedule.
     */
    @Test
    public void testSetSchedule() {
        System.out.println("setSchedule");
        Schedule instance = new Schedule();
        instance.setSchedule(schedule);
        assertThat(instance.getSchedule(),Is.is(schedule));
    }
     /**
     * Test of getSchedule method, of class Schedule.
     */
    @Test
    public void testGetSchedule() {
        System.out.println("getSchedule");
        Schedule instance = new Schedule();
        ScheduleArray expResult = schedule;
        ScheduleArray result = instance.getSchedule();
        assertThat(expResult,Is.is(result));
    }
     /**
     * Test of enforceScheduleConsistency method, of class Schedule.
     */
    @Test
    public void testEnforceScheduleConsistency(){
        System.out.println("enforceScheduleConsistency");
        Schedule inSched = new Schedule();
        Schedule correctSched = new Schedule();;
        inSched.setPeople(people); 
        inSched.setAssignments(assignments);
        inSched.setHolidays(holidays);
        inSched.setTaskDates(taskDates);
        inSched.setSchedule(schedule);
        inSched.enforceScheduleConsistency();
        ArrayList<String> lcpeople = new ArrayList();
        flex2DArray lctaskDates = new flex2DArray();
        flex2DArray lcassignments = new flex2DArray();
        flex2DArray lcholidays = new flex2DArray();
        flex2DArray lcsch = new flex2DArray();
        ScheduleArray lcschedule = new ScheduleArray();
        lcpeople.add("John"); 
        lcpeople.add("Jean");
        lcpeople.add("Bill"); //New person
        lctaskDates.add("taskA", "01012012", "Y");
        lctaskDates.add("taskA", "01022012", "N");
        lctaskDates.add("taskB", "01012012", "N");
        lctaskDates.add("taskB", "01022012", "Y");
        lcassignments.add("John", "taskA", "Y");
        lcassignments.add("Jean", "taskB", "Y");
        lcassignments.add("John", "taskB", "N");
        lcassignments.add("Jean", "taskA", "N");
        lcassignments.add("Bill", "taskA", "N");
        lcassignments.add("Bill", "taskB", "N");
        lcholidays.add("John", "01012012", "Y");
        lcholidays.add("John", "01022012", "Y");
        lcholidays.add("Jean", "01012012", "Y");
        lcholidays.add("Jean", "01022012", "Y");
        lcholidays.add("Bill", "01012012", "Y");
        lcholidays.add("Bill", "01022012", "Y");
        lcsch.add("taskA","01012012", "John");
        lcsch.add("taskB","01022012","Jean");
        lcsch.add("taskA","01022012", "None");
        lcsch.add("taskB","01012012","None");
        lcschedule.setSchedule(lcsch);
        correctSched.setPeople(lcpeople);
        correctSched.setAssignments(lcassignments);
        correctSched.setHolidays(lcholidays);
        correctSched.setTaskDates(lctaskDates);
        correctSched.setSchedule(lcschedule);
        assertThat(correctSched,Is.is(inSched));
        
    }
}
