/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.cliente.gui;

import com.distribuidos.gui.Selector;
import com.distribuidos.models.ClientConnect;
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
public class ClientEditPreferences extends javax.swing.JFrame {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ClientEditPreferences.class);
    private static final String IPV4_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    private static final Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);
    private static final String PORT_REGEX = "[+-]?[0-9][0-9]*";
    private static final Pattern PORT_PATTERN = Pattern.compile(PORT_REGEX);

    private MainView mainView;
    private ClientConnect editPreferences;

    /**
     * Creates new form ClientEditPreferences
     */
    public ClientEditPreferences(MainView mainView) {
        initComponents();
        this.mainView = mainView;

        this.setTitle("Preferencias del cliente");
        this.setLocationRelativeTo(mainView);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        getCurrentPreferences();
        
        String mainAddress = editPreferences.getMainServer() == null ? "0.0.0.0"
                : editPreferences.getMainServer().getHostAddress();
        jMainAddress.setText(mainAddress);
        jMainPort.setText(String.valueOf(editPreferences.getMainServerPort()));

        String backupAddress = editPreferences.getBackupServer() == null ? "0.0.0.0"
                : editPreferences.getMainServer().getHostAddress();
        jBackupAddress.setText(backupAddress);
        jBackupPort.setText(String.valueOf(editPreferences.getBackupServerPort()));
    }
    
    public ClientEditPreferences(Selector selector) {
        initComponents();

        this.setTitle("Preferencias del cliente");
        this.setLocationRelativeTo(selector);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        getCurrentPreferences();
        String mainAddress = editPreferences.getMainServer() == null ? "0.0.0.0"
                : editPreferences.getMainServer().getHostAddress();
        jMainAddress.setText(mainAddress);
        jMainPort.setText(String.valueOf(editPreferences.getMainServerPort()));

        String backupAddress = editPreferences.getBackupServer() == null ? "0.0.0.0"
                : editPreferences.getMainServer().getHostAddress();
        jBackupAddress.setText(backupAddress);
        jBackupPort.setText(String.valueOf(editPreferences.getBackupServerPort()));
    }

    public ClientConnect getCurrentPreferences() {
        try {
            // Loading the YAML file from the /resources folder
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Path connectFile = Paths.get("./", "cliente.yaml");
            File file = connectFile.toFile();

            // Instantiating a new ObjectMapper as a YAMLFactory
            ObjectMapper om = new ObjectMapper(new YAMLFactory());

            if (!file.exists()) {
                file.createNewFile();
                om.writeValue(file, new ClientConnect(
                        InetAddress.getByName("0.0.0.0"), 
                        InetAddress.getByName("0.0.0.0"), 
                        2370, 2371)
                );
            }

            this.editPreferences = om.readValue(file, ClientConnect.class);
        } catch (IOException iox) {
            LOGGER.error("Error en I/O", iox);
        }

        return editPreferences;
    }

    public void writePreferences() {
        try {
            // Loading the YAML file from the /resources folder
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Path connectFile = Paths.get("./", "cliente.yaml");
            File file = connectFile.toFile();

            // Instantiating a new ObjectMapper as a YAMLFactory
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            file.createNewFile();
            om.writeValue(file, editPreferences);
        } catch (IOException iox) {
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
        jLabel2 = new javax.swing.JLabel();
        jMainAddress = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jMainPort = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jBackupAddress = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jBackupPort = new javax.swing.JTextField();
        jSave = new javax.swing.JButton();
        jErrorMessage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Editar valores");

        jLabel2.setText("Dirección IP del servidor principal:");

        jLabel3.setText("Puerto del servidor principal:");

        jLabel4.setText("Dirección IP del servidor de respaldo:");

        jLabel5.setText("Puerto del servidor de respaldo:");

        jSave.setText("Guardar");
        jSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveActionPerformed(evt);
            }
        });

        jErrorMessage.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jErrorMessage.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jMainAddress))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jMainPort))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBackupAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBackupPort))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jErrorMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSave)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jMainAddress)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jMainPort)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jBackupAddress)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jBackupPort)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jErrorMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveActionPerformed
        // TODO add your handling code here:
        String mainAddress, backupAddress;
        int mainPort, backupPort;
        if (isIPV4Valid(jMainAddress.getText()) && isPortValid(jMainPort.getText())) {
            mainAddress = jMainAddress.getText();
            mainPort = Integer.valueOf(jMainPort.getText());
        } else {
            jErrorMessage.setText("Dirección IP o puerto del servidor principal invalido.");
            return;
        }

        if (isIPV4Valid(jBackupAddress.getText()) && isPortValid(jBackupPort.getText())) {
            backupAddress = jBackupAddress.getText();
            backupPort = Integer.valueOf(jBackupPort.getText());
        } else {
            jErrorMessage.setText("Dirección IP o puerto del servidor de respaldo invalido.");
            return;
        }

        try {
            InetAddress main = InetAddress.getByName(mainAddress);
            editPreferences.setMainServer(main);
            editPreferences.setMainServerPort(mainPort);
            InetAddress backup = InetAddress.getByName(backupAddress);
            editPreferences.setBackupServer(backup);
            editPreferences.setBackupServerPort(backupPort);

            writePreferences();
            JOptionPane.showConfirmDialog(null,
                "Cambios guardados", "Información",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        } catch (UnknownHostException uhx) {
            LOGGER.error("Host desconocido", uhx);
        }

    }//GEN-LAST:event_jSaveActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if(mainView != null) JOptionPane.showConfirmDialog(null,
                "Reinicia el programa para que los cambios sean aplicados", 
                "Información",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jBackupAddress;
    private javax.swing.JTextField jBackupPort;
    private javax.swing.JLabel jErrorMessage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jMainAddress;
    private javax.swing.JTextField jMainPort;
    private javax.swing.JButton jSave;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
