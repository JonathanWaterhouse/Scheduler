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
import java.util.ArrayList;

/**
 * This class stores or retrieves an object in serialised form
 * @author Jon Waterhouse
 */
public class Store {
    public Schedule getStoredSchedule (String storeName)throws IOException, ClassNotFoundException {
        File f = new File(storeName);
        Schedule db;
        try{
            ObjectInputStream m = new ObjectInputStream(new FileInputStream(f));
            db = (Schedule) m.readObject();   
            m.close();
        }
        catch (IOException e){
            throw new IOException();
        }
        catch (ClassNotFoundException e) {
            throw new ClassNotFoundException();
        }
        return db;
    }
    
    public void writeSchedule (Schedule s, String DBFile) throws IOException {
        File f;
        f = new File(DBFile);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(s);
            oos.close();
        }
        catch (IOException e) {throw new IOException();}
    }
}
