/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Jon Waterhouse
 */
public class JTextAreaOutput implements OutputArea {
    
    private Object outputArea;
    /**
     * Constructor
     * @param outputArea and object containing a reference to the screen object
     * to be filled with data
     */ 
    public JTextAreaOutput(Object outputArea){
        this.outputArea = outputArea;
    }
    
    @Override
    public void print(flex2DArray printText){
        javax.swing.JTextArea outputTextArea;
        try {
            outputTextArea = (javax.swing.JTextArea) this.outputArea;
            outputTextArea.replaceRange("",0,outputTextArea.getLineEndOffset(outputTextArea.getLineCount()-1));
            //flex2DArray f = s.getSchedule();
            LinkedList<String> L = printText.print("|");
            for (String st : L) {outputTextArea.append(st+System.getProperty( "line.separator" ));}
        } catch (BadLocationException ex) {
            Logger.getLogger(Ui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
