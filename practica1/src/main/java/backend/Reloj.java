/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

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
public class Reloj implements Runnable{

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

    private JLabel lblReloj;
    private int horas;
    private int minutos;
    private int segundos;
    private int delay = 1000;
    
    private boolean activo = true;
    private boolean pausado = false;
    
    public Reloj (JLabel lblReloj, int horas, int minutos, int segundos){
        this.lblReloj = lblReloj;
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
        if (this.lblReloj != null)
            this.lblReloj.setText( obtenerHora() );
    }
    
    public void detenerReloj(){
        this.pausado = true;
        System.out.println("Detener");
    }
    
    public void reanudarReloj(){
        this.pausado = false;
        System.out.println("Reanudar");
    }
    
    public void editarReloj(int horas, int minutos, int segundos, int delay){
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
        this.delay = delay;
        if (this.lblReloj != null)
            this.lblReloj.setText( obtenerHora() );
    }
    
    public String obtenerHora(){
        return  ( getHoras()    > 9 ? ""+getHoras()   :"0"+getHoras())   + ":" +
                ( getMinutos()  > 9 ? ""+getMinutos() :"0"+getMinutos()) + ":" +
                ( getSegundos() > 9 ? ""+getSegundos():"0"+getSegundos());
    }
    
    @Override
    public void run() {
        int i = 0;
        while(activo){
            try {
                if(!pausado){
                    try {
                        Thread.sleep(this.getDelay());
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
                        System.out.println("Continuando "+horas+":"+minutos+":"+segundos);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    Thread.sleep(100);
                    //System.out.println("En espera");
                }
                
            } catch (Exception ex) {
                Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }
    
}
