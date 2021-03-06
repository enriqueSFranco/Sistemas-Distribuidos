/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.server.gui;

import com.distribuidos.gui.Selector;
import com.distribuidos.models.ServerConnect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pekochu
 */
public class ServerEditPreferences extends javax.swing.JFrame {
    
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ServerEditPreferences.class);
    private static final String IPV4_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    private static final Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);
    private static final String PORT_REGEX = "[+-]?[0-9][0-9]*";
    private static final Pattern PORT_PATTERN = Pattern.compile(PORT_REGEX);
    
    private BooksView booksView;
    private ServerConnect editPreferences;

    /**
     * Creates new form ClientEditPreferences
     */
    public ServerEditPreferences(BooksView booksView) {
        initComponents();
        this.booksView = booksView;
        
        this.setTitle("Preferencias de servidor");
        this.setLocationRelativeTo(booksView);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        
        getCurrentPreferences();
        jServicePort.setText(String.valueOf(editPreferences.getWorkingPort()));
    }
    
    public ServerEditPreferences(Selector selector) {
        initComponents();
        
        this.setTitle("Preferencias de servidor");
        this.setLocationRelativeTo(selector);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        
        getCurrentPreferences();
        jServicePort.setText(String.valueOf(editPreferences.getWorkingPort()));
        String backupAddress = editPreferences.getBackupAddress() == null ? "0.0.0.0"
                : editPreferences.getBackupAddress().getHostAddress();
        jBackupAddress.setText(backupAddress);
        jBackupPort.setText(String.valueOf(editPreferences.getBackupPort()));
        jClientPort.setText(String.valueOf(editPreferences.getClientPort()));

    }
    
    public ServerConnect getCurrentPreferences(){
        try{
            // Loading the YAML file from the /resources folder
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Path connectFile = Paths.get("./", "servidor.yaml");
            File file = connectFile.toFile();
            
            // Instantiating a new ObjectMapper as a YAMLFactory
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            
            if(!file.exists()){
                file.createNewFile();
                om.writeValue(file, new ServerConnect(2370, 
                        InetAddress.getByName("0.0.0.0"), 2371, 6000));
            } 
            
            this.editPreferences = om.readValue(file, ServerConnect.class);
        }catch(IOException iox){
            LOGGER.error("Error en I/O", iox);
        }
        
        return editPreferences;
    }
    
    public void writePreferences(){
        try{
            // Loading the YAML file from the /resources folder
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Path connectFile = Paths.get("./", "servidor.yaml");
            File file = connectFile.toFile();
            
            // Instantiating a new ObjectMapper as a YAMLFactory
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            file.createNewFile();
            om.writeValue(file, editPreferences);
        }catch(IOException iox){
            LOGGER.error("Error en I/O", iox);
        }
    }
    
    public static boolean isIPV4Valid(final String ip) {
        Matcher matcher = IPV4_PATTERN.matcher(ip);
        return matcher.matches();
    }
    
    public static boolean isPortValid(final String port) {
        Matcher matcher = PORT_PATTERN.matcher(port);
        return matcher.matches();
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
        jLabel5 = new javax.swing.JLabel();
        jServicePort = new javax.swing.JTextField();
        jSave = new javax.swing.JButton();
        jErrorMessage = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jBackupAddress = new javax.swing.JTextField();
        jBackupPort = new javax.swing.JTextField();
        jClientPort = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Editar valores");

        jLabel5.setText("Puerto de servicio:");

        jSave.setText("Guardar");
        jSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveActionPerformed(evt);
            }
        });

        jErrorMessage.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jErrorMessage.setForeground(new java.awt.Color(255, 0, 0));

        jLabel2.setText("Servidor de respaldo:");

        jLabel3.setText("Puerto del servidor de respaldo:");

        jLabel4.setText("Puerto del cliente:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jServicePort, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jErrorMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSave))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBackupAddress))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBackupPort))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jClientPort)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jServicePort)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jBackupAddress)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jBackupPort)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jClientPort)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jErrorMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveActionPerformed
        // TODO add your handling code here:
        String backupAddress;
        int servicePort, backupPort, clientPort;
        if(isPortValid(jServicePort.getText())){
            servicePort = Integer.valueOf(jServicePort.getText());
        }else{
            jErrorMessage.setText("Puerto de servicio invalido.");
            return;
        }
        
        if (isIPV4Valid(jBackupAddress.getText()) && isPortValid(jBackupPort.getText())) {
            backupAddress = jBackupAddress.getText();
            backupPort = Integer.valueOf(jBackupPort.getText());
        } else {
            jErrorMessage.setText("Direcci??n IP o puerto del servidor de respaldo invalido.");
            return;
        }
        
        if(isPortValid(jClientPort.getText())){
            clientPort = Integer.valueOf(jClientPort.getText());
        }else{
            jErrorMessage.setText("Puerto de cliente invalido.");
            return;
        }
        
        try{
            editPreferences.setWorkingPort(servicePort);
            editPreferences.setBackupAddress(InetAddress.getByName(backupAddress));
            editPreferences.setBackupPort(backupPort);
            editPreferences.setClientPort(clientPort);
            
            writePreferences();
            JOptionPane.showConfirmDialog(null,
                "Cambios guardados", "Informaci??n",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        }catch (UnknownHostException uhx) {
            LOGGER.error("Host desconocido", uhx);
        }
    }//GEN-LAST:event_jSaveActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if(booksView != null) JOptionPane.showConfirmDialog(null,
                "Reinicia el programa para que los cambios sean aplicados", 
                "Informaci??n",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jBackupAddress;
    private javax.swing.JTextField jBackupPort;
    private javax.swing.JTextField jClientPort;
    private javax.swing.JLabel jErrorMessage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JButton jSave;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jServicePort;
    // End of variables declaration//GEN-END:variables
}
