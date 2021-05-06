/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.server.gui;

import com.distribuidos.cliente.backend.Reloj;
import com.distribuidos.cliente.gui.ClientEditPreferences;
import com.distribuidos.models.ServerConnect;
import com.distribuidos.server.backend.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pekochu
 */
public class BackupBooksView extends javax.swing.JFrame {
    
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BackupBooksView.class);
    
    private Server server;
    private Thread clockThread;
    private Reloj reloj;
    private ServerConnect preferences;
    private int servicePort = 2371;

    /**
     * Creates new form BooksView
     */
    public BackupBooksView() {
        initComponents();
        
        this.setTitle("Modo: servidor de respaldo");
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        
        getPreferences();
        
        try{
            server = new Server(servicePort);
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
    
    public void getPreferences(){
        javax.swing.JMenu jMenuPreferences = new javax.swing.JMenu();
        jMenuBar1.removeAll();
        
        javax.swing.JMenuItem preferencesItem;
        
        preferencesItem = new javax.swing.JMenuItem();
        preferencesItem.setText("Editar preferencias");
        preferencesItem.addActionListener((e) -> {
            java.awt.EventQueue.invokeLater(() -> {
                new BackupServerEditPreferences(this).setVisible(true);
            });
        });
        
        jMenuPreferences.setText("Edición");
        jMenuPreferences.add(preferencesItem);
        jMenuBar1.add(jMenuPreferences);        
        
        try{
            // Loading the YAML file from the /resources folder
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Path connectFile = Paths.get("./", "servidor_backup.yaml");
            File file = connectFile.toFile();
            
            // Instantiating a new ObjectMapper as a YAMLFactory
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            
            if(!file.exists()){
                file.createNewFile();
                om.writeValue(file, new ServerConnect(2371));
            } 
            
            this.preferences = om.readValue(file, ServerConnect.class);
            if(preferences.getWorkingPort() != 0) 
                servicePort = preferences.getWorkingPort();
        }catch(IOException iox){
            LOGGER.error("Error en I/O", iox);
        }
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Modo: SERVIDOR DE RESPALDO");

        jClock.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jClock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jClock.setText("00:00:00");

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

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
                .addContainerGap(211, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jClock;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
