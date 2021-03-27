package gui;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 *
 * @author Enrique
 */
public class GuiClock extends javax.swing.JFrame implements Runnable {

    /**
     * Constructor 
     */
    public GuiClock() {
        initComponents(); // iniciamos el jframe
        setTitle("Practica 1");
        h1 = new Thread(this); // objeto tipo Thread
        h1.start(); // iniciamos el hilo_1
        setLocationRelativeTo(null); // posicionamento del jframe (centro)
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelReloj1 = new javax.swing.JLabel();
        labelReloj2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        buttonReloj3 = new javax.swing.JButton();
        buttonReloj4 = new javax.swing.JButton();
        buttonReloj1 = new javax.swing.JButton();
        buttonReloj2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 102, 204));

        labelReloj1.setBackground(new java.awt.Color(0, 0, 0));
        labelReloj1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelReloj1.setText("00:00:00");

        labelReloj2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelReloj2.setText("00:00:00");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("00:00:00");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("00:00:00");

        buttonReloj3.setBackground(new java.awt.Color(0, 153, 204));
        buttonReloj3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonReloj3.setForeground(new java.awt.Color(255, 255, 255));
        buttonReloj3.setText("Editar");

        buttonReloj4.setBackground(new java.awt.Color(0, 153, 204));
        buttonReloj4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonReloj4.setForeground(new java.awt.Color(255, 255, 255));
        buttonReloj4.setText("Editar");

        buttonReloj1.setBackground(new java.awt.Color(0, 153, 204));
        buttonReloj1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonReloj1.setForeground(new java.awt.Color(255, 255, 255));
        buttonReloj1.setText("Editar");
        buttonReloj1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReloj1ActionPerformed(evt);
            }
        });

        buttonReloj2.setBackground(new java.awt.Color(0, 153, 204));
        buttonReloj2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonReloj2.setForeground(new java.awt.Color(255, 255, 255));
        buttonReloj2.setText("Editar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelReloj1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonReloj1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(137, 137, 137)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonReloj3)
                        .addGap(195, 195, 195)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonReloj4)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(buttonReloj2)
                        .addComponent(labelReloj2, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelReloj1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelReloj2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonReloj1)
                    .addComponent(buttonReloj2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonReloj4)
                    .addComponent(buttonReloj3))
                .addGap(65, 65, 65))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonReloj1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReloj1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonReloj1ActionPerformed

    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GuiClock().setVisible(true);
            }
        });
    }
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonReloj1;
    private javax.swing.JButton buttonReloj2;
    private javax.swing.JButton buttonReloj3;
    private javax.swing.JButton buttonReloj4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel labelReloj1;
    private javax.swing.JLabel labelReloj2;
    // End of variables declaration//GEN-END:variables
    private String hours, minutes, seconds;
    Thread h1;

    @Override
    public void run() {
       Thread reloj1 = Thread.currentThread();
       while(reloj1 == h1) {
           timeFormat();
           labelReloj1.setText(hours+":"+minutes+":"+seconds);
           try {
               Thread.sleep(1000);
           }catch(InterruptedException e) {
               
           }
       }
    }
    
    /**
     * formato para la hora, minutos y segundos
     * 
     */
    public void timeFormat() {
        
        Calendar calendario = new GregorianCalendar();

        hours = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? ""+calendario.get(Calendar.HOUR_OF_DAY) : "0"+calendario.get(Calendar.HOUR_OF_DAY);
        minutes = calendario.get(Calendar.MINUTE) > 9 ? ""+calendario.get(Calendar.MINUTE) : "0"+calendario.get(Calendar.MINUTE);
        seconds = calendario.get(Calendar.SECOND) > 9 ? ""+calendario.get(Calendar.SECOND) : "0"+calendario.get(Calendar.SECOND);
    }    
}
