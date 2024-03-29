/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * This class creates a Swing dialog which contains a JTable in which the schedule
 * is displayed that schedule (apart from col 1 which contains row labels) is editable.
 * It allows a user to input some fixed manual values to an initialised schedule
 * or change calculated values. For example people may be assigned tasks on a specific
 * date who are not included within the normal pool of available people.
 * @author jon.waterhouse@gmail.com
 */
public class ChangeSchedule extends javax.swing.JDialog {
    private OutputArea output;
    /**
     * Creates new form ChangeSchedule
     */
    public ChangeSchedule(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Schedule sdb = new Schedule();
        output = new JTableOutputArea(outputJTable);
        output.print(sdb.getSchedule().getSchedule());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        outputJTable = new javax.swing.JTable(){
            public boolean isCellEditable(int row,int column){
                if(column == 0) return false;//the 1st column is not editable
                return true;
            }
        };
        updateJButton = new javax.swing.JButton();
        exitJButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Change Schedule");
        setModal(true);

        outputJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                ""
            }
        ));
        outputJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        outputJTable.setCellSelectionEnabled(true);
        outputJTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(outputJTable);

        updateJButton.setText("Update");
        updateJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateJButtonActionPerformed(evt);
            }
        });

        exitJButton.setForeground(new java.awt.Color(204, 0, 0));
        exitJButton.setText("Exit");
        exitJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(updateJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateJButton)
                    .addComponent(exitJButton)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Update the schedule with any changed values. Also keep a record of which
     * values were changed manually, to be used elsewhere.
     * @param evt 
     */
    private void updateJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateJButtonActionPerformed
        flex2DArray schedule = new flex2DArray(); //For new copy of schedule
        String d; String t; String p;
        Schedule s = new Schedule(); 
        ScheduleArray sArray = s.getSchedule();
        flex2DArray oldSchedule = sArray.getSchedule();
        flex2DArray scheduleChanges = sArray.getScheduleOverride();
        DefaultTableModel TM = (DefaultTableModel) outputJTable.getModel(); 
        for (int i = 0; i < TM.getRowCount(); i++){
            for (int j = 1; j < TM.getColumnCount(); j++){
                d = (String)TM.getValueAt(i,0);
                t = outputJTable.getColumnName(j);
                p = (String)TM.getValueAt(i,j);
                //Keep a record of what was manually input
                if (!oldSchedule.getCellContentAtKey(d,t).equals(p)) {
                    scheduleChanges.add(d,t,"Y");
                }
                schedule.add(d,t,p); 
            }
        }
        scheduleChanges.makeArrayRectangular("N");
        sArray.setSchedule(schedule);
        sArray.setScheduleOverride(scheduleChanges);
        s.setSchedule(sArray);
    }//GEN-LAST:event_updateJButtonActionPerformed

    private void exitJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitJButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_exitJButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitJButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable outputJTable;
    private javax.swing.JButton updateJButton;
    // End of variables declaration//GEN-END:variables
}
