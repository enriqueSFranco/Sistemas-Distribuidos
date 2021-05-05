/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.server.gui;

import com.distribuidos.cliente.backend.Reloj;
import com.distribuidos.server.backend.Server;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pekochu
 */
public class BooksView extends javax.swing.JFrame {
    
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BooksView.class);
    
    private Server server;
    private Thread clockThread;
    private Reloj reloj;

    /**
     * Creates new form BooksView
     */
    public BooksView() {
        initComponents();
        
        this.setTitle("Modo: servidor principal");
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        
        try{
            server = new Server();
        }catch(RemoteException rx){
            LOGGER.error("No se pudo crear el objeto remoto", rx);
            JOptionPane.showConfirmDialog(null,
                    rx.getMessage(), "No se pudo crear el objeto remoto",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            
            System.exit(1);
            return;
        }
        
        try {
            server.startRMI();
        } catch (RemoteException rx) {
            LOGGER.error("Error al iniciar el servicio RMI", rx);
        }
        
        initClock();
    }
    
    public void initClock(){
        Calendar calendario = new GregorianCalendar();
        reloj = new Reloj(jClock, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), calendario.get(Calendar.SECOND));
        
        clockThread = new Thread(reloj);
        clockThread.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jClock = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Modo: SERVIDOR PRINCIPAL");

        jClock.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jClock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jClock.setText("00:00:00");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(jClock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jClock)
                .addContainerGap(232, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jClock;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
