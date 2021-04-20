package dmf.practica2.gui;

import dmf.practica2.backend.Configuracion;
import dmf.practica2.backend.Red;
import dmf.practica2.backend.Reloj;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;


/**
 *
 * @author Enrique
 */
public class GuiClock extends javax.swing.JFrame{
    
    Red red;

    /**
     * Constructor 
     */
    public GuiClock() {
        initComponents(); // iniciamos el jframe
        setTitle("Practica 2");
        setLocationRelativeTo(null); // posicionamento del jframe (centro)
        
        initHilos();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelRelojMaestro = new javax.swing.JLabel();
        labelReloj1 = new javax.swing.JLabel();
        labelReloj2 = new javax.swing.JLabel();
        labelReloj3 = new javax.swing.JLabel();
        buttonReloj2 = new javax.swing.JButton();
        buttonReloj3 = new javax.swing.JButton();
        buttonRelojMaestro = new javax.swing.JButton();
        buttonReloj1 = new javax.swing.JButton();
        buttonEnviarRelojMaestro = new javax.swing.JButton();
        buttonEnviarReloj1 = new javax.swing.JButton();
        buttonEnviarReloj3 = new javax.swing.JButton();
        buttonEnviarReloj2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 102, 204));

        labelRelojMaestro.setBackground(new java.awt.Color(0, 0, 0));
        labelRelojMaestro.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelRelojMaestro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRelojMaestro.setText("00:00:00");

        labelReloj1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelReloj1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelReloj1.setText("00:00:00");

        labelReloj2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelReloj2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelReloj2.setText("00:00:00");

        labelReloj3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelReloj3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelReloj3.setText("00:00:00");

        buttonReloj2.setBackground(new java.awt.Color(0, 153, 204));
        buttonReloj2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonReloj2.setForeground(new java.awt.Color(255, 255, 255));
        buttonReloj2.setText("Editar");
        buttonReloj2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReloj2ActionPerformed(evt);
            }
        });

        buttonReloj3.setBackground(new java.awt.Color(0, 153, 204));
        buttonReloj3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonReloj3.setForeground(new java.awt.Color(255, 255, 255));
        buttonReloj3.setText("Editar");
        buttonReloj3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReloj3ActionPerformed(evt);
            }
        });

        buttonRelojMaestro.setBackground(new java.awt.Color(0, 153, 204));
        buttonRelojMaestro.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonRelojMaestro.setForeground(new java.awt.Color(255, 255, 255));
        buttonRelojMaestro.setText("Editar");
        buttonRelojMaestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRelojMaestroActionPerformed(evt);
            }
        });

        buttonReloj1.setBackground(new java.awt.Color(0, 153, 204));
        buttonReloj1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonReloj1.setForeground(new java.awt.Color(255, 255, 255));
        buttonReloj1.setText("Editar");
        buttonReloj1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReloj1ActionPerformed(evt);
            }
        });

        buttonEnviarRelojMaestro.setBackground(new java.awt.Color(0, 153, 204));
        buttonEnviarRelojMaestro.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonEnviarRelojMaestro.setForeground(new java.awt.Color(255, 255, 255));
        buttonEnviarRelojMaestro.setText("Enviar");
        buttonEnviarRelojMaestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEnviarRelojMaestroActionPerformed(evt);
            }
        });

        buttonEnviarReloj1.setBackground(new java.awt.Color(0, 153, 204));
        buttonEnviarReloj1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonEnviarReloj1.setForeground(new java.awt.Color(255, 255, 255));
        buttonEnviarReloj1.setText("Enviar");
        buttonEnviarReloj1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEnviarReloj1ActionPerformed(evt);
            }
        });

        buttonEnviarReloj3.setBackground(new java.awt.Color(0, 153, 204));
        buttonEnviarReloj3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonEnviarReloj3.setForeground(new java.awt.Color(255, 255, 255));
        buttonEnviarReloj3.setText("Enviar");
        buttonEnviarReloj3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEnviarReloj3ActionPerformed(evt);
            }
        });

        buttonEnviarReloj2.setBackground(new java.awt.Color(0, 153, 204));
        buttonEnviarReloj2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        buttonEnviarReloj2.setForeground(new java.awt.Color(255, 255, 255));
        buttonEnviarReloj2.setText("Enviar");
        buttonEnviarReloj2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEnviarReloj2ActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Reloj Maestro:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Reloj 1");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Reloj 2");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Reloj 3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonReloj1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonEnviarReloj1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelReloj1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelRelojMaestro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonRelojMaestro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonEnviarRelojMaestro, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelReloj2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonReloj2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonEnviarReloj2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelReloj3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(buttonReloj3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonEnviarReloj3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 13, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelRelojMaestro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonRelojMaestro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonEnviarRelojMaestro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelReloj2)
                    .addComponent(labelReloj1)
                    .addComponent(labelReloj3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonReloj1)
                    .addComponent(buttonReloj2)
                    .addComponent(buttonReloj3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonEnviarReloj1)
                    .addComponent(buttonEnviarReloj2)
                    .addComponent(buttonEnviarReloj3))
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonRelojMaestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRelojMaestroActionPerformed
        rMaestro.detenerReloj();
        java.awt.EventQueue.invokeLater(() -> {
            GUIModificacion panel = new GUIModificacion(rMaestro);
            panel.setVisible(true);
        });
    }//GEN-LAST:event_buttonRelojMaestroActionPerformed

    private void buttonReloj1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReloj1ActionPerformed
        r1.detenerReloj();
        java.awt.EventQueue.invokeLater(() -> {
            GUIModificacion panel = new GUIModificacion(r1);
            panel.setVisible(true);
        });
    }//GEN-LAST:event_buttonReloj1ActionPerformed

    private void buttonReloj2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReloj2ActionPerformed
        r2.detenerReloj();
        java.awt.EventQueue.invokeLater(() -> {
            GUIModificacion panel = new GUIModificacion(r2);
            panel.setVisible(true);
        });
    }//GEN-LAST:event_buttonReloj2ActionPerformed

    private void buttonReloj3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReloj3ActionPerformed
        r3.detenerReloj();
        java.awt.EventQueue.invokeLater(() -> {
            GUIModificacion panel = new GUIModificacion(r3);
            panel.setVisible(true);
        });
    }//GEN-LAST:event_buttonReloj3ActionPerformed

    private void buttonEnviarRelojMaestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEnviarRelojMaestroActionPerformed
        red.enviarReloj(0, rMaestro);
    }//GEN-LAST:event_buttonEnviarRelojMaestroActionPerformed

    private void buttonEnviarReloj1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEnviarReloj1ActionPerformed
        red.enviarReloj(1, r1);
    }//GEN-LAST:event_buttonEnviarReloj1ActionPerformed

    private void buttonEnviarReloj3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEnviarReloj3ActionPerformed
        red.enviarReloj(3, r3);
    }//GEN-LAST:event_buttonEnviarReloj3ActionPerformed

    private void buttonEnviarReloj2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEnviarReloj2ActionPerformed
        red.enviarReloj(2, r2);
    }//GEN-LAST:event_buttonEnviarReloj2ActionPerformed
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonEnviarReloj1;
    private javax.swing.JButton buttonEnviarReloj2;
    private javax.swing.JButton buttonEnviarReloj3;
    private javax.swing.JButton buttonEnviarRelojMaestro;
    private javax.swing.JButton buttonReloj1;
    private javax.swing.JButton buttonReloj2;
    private javax.swing.JButton buttonReloj3;
    private javax.swing.JButton buttonRelojMaestro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel labelReloj1;
    private javax.swing.JLabel labelReloj2;
    private javax.swing.JLabel labelReloj3;
    private javax.swing.JLabel labelRelojMaestro;
    // End of variables declaration//GEN-END:variables
    private String hours, minutes, seconds;
    private Thread h1, h2, h3, h4, hiloRed;
    private Reloj rMaestro, r1, r2, r3;
    
    private void initHilos(){
        Calendar calendario = new GregorianCalendar();
        Random r = new Random();
        rMaestro = new Reloj(labelRelojMaestro, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), calendario.get(Calendar.SECOND));
        r1 = new Reloj(labelReloj1, r.nextInt(24), r.nextInt(60), r.nextInt(60));
        r2 = new Reloj(labelReloj2, r.nextInt(24), r.nextInt(60), r.nextInt(60));
        r3 = new Reloj(labelReloj3, r.nextInt(24), r.nextInt(60), r.nextInt(60));
        
        h1 = new Thread(rMaestro);
        h1.start();
        h2 = new Thread(r1);
        h2.start();
        h3 = new Thread(r2);
        h3.start();
        h4 = new Thread(r3);
        h4.start();
        
        Configuracion.cargarDatosDeRed();
        
        red = new Red(Configuracion.identificador, Configuracion.interfaz, Configuracion.ip, Configuracion.puerto);
        red.prepararCanal();
        hiloRed = new Thread(new Runnable() {
            @Override
            public void run() {
                red.cicloPrincipal(r1, r2, r3);
            }
        });
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
