/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.pract1.backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Dave
 */
public class RelojTimer /*implements Runnable*/{

    private JLabel lblReloj;
    private int horas;
    private int minutos;
    private int segundos;
    private int delay = 1000;
    private double speedfactor = 1.0;
    
    private Timer temporizador;
    
    private boolean activo = true;
    private boolean pausado = false;
    
    public RelojTimer (JLabel lblReloj, int horas, int minutos, int segundos){
        this.lblReloj = lblReloj;
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
        if (this.lblReloj != null)
            this.lblReloj.setText( obtenerHora() );
        this.temporizador = new Timer((int)(delay*speedfactor), (ActionEvent e) -> {
            aumentoDeUnidad();
        });
    }
    
    public void detenerReloj(){
        getTemporizador().stop();
        System.out.println("Detener");
    }
    
    public void reanudarReloj(){
        getTemporizador().restart();
        System.out.println("Reanudar");
    }
    
    public void editarReloj(int horas, int minutos, int segundos, int delay){
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
        this.delay = delay;
        if (this.lblReloj != null)
            this.lblReloj.setText( obtenerHora() );
        this.temporizador.setDelay(delay);
    }
    
    public String obtenerHora(){
        return  ( getHoras()    > 9 ? ""+getHoras()   :"0"+getHoras())   + ":" +
                ( getMinutos()  > 9 ? ""+getMinutos() :"0"+getMinutos()) + ":" +
                ( getSegundos() > 9 ? ""+getSegundos():"0"+getSegundos());
    }
    
    public void aumentoDeUnidad(){
        this.segundos++;
        if(this.getSegundos() == 60){
            this.segundos = 0;
            this.minutos++;
        }
        if(this.getMinutos() == 60){
            this.minutos = 0;
            this.horas++;
        }
        if(this.getHoras() == 24) {
            this.horas = 0;
        }
        if (this.lblReloj != null && !pausado && activo)
            this.lblReloj.setText( obtenerHora() );
    }
//    
//    @Override
//    public void run() {
//        t.start();
//    }

    /**
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }
    
    /**
     * @return the horas
     */
    public int getHoras() {
        return horas;
    }

    /**
     * @return the minutos
     */
    public int getMinutos() {
        return minutos;
    }

    /**
     * @return the segundos
     */
    public int getSegundos() {
        return segundos;
    }

    /**
     * @return the t
     */
    public Timer getTemporizador() {
        return temporizador;
    }
    
    /**
     * @return the speedfactor
     */
    public double getSpeedfactor() {
        return speedfactor;
    }

    public void setSpeedfactor(double speedfactor) {
        this.speedfactor = speedfactor;
    }  
    
}
