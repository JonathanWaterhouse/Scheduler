/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

/**
 * This interface defines the output mechanism for display of schedule components
 * It allows the precise object into which output is done to be populated without 
 * the caller knowing the details of the implementation.
 * @author user
 */
public interface OutputArea {
    
    void print(flex2DArray printText);
    
}
