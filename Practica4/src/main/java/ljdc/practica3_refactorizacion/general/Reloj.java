/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.general;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Dave
 */
public class Reloj implements Runnable, Serializable {

    private int horas;
    private int minutos;
    private int segundos;
    private long milisegundos_sistema;
    private int delay = 1000;
    private int deltaTiempo = 0;
    
    private boolean activo = true;
    private boolean pausado = false;
    
    private JLabel lbl_reloj = null;
    
    public Reloj (JLabel lbl_reloj, int horas, int minutos, int segundos){
        this.lbl_reloj = lbl_reloj;
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
        this.lbl_reloj.setText(this.obtenerHora());
    }
    
    @Override
    public void run() {
        int i = 0;
        long mil;
        while(activo){
            try {
                if(!pausado){
                    try {
                        //this.milisegundos_sistema = System.currentTimeMillis();
                        //Thread.sleep((long)delay - deltaTiempo);
                        
                        //for(this.milisegundos_sistema = 0; this.milisegundos_sistema < delay - deltaTiempo ; this.milisegundos_sistema++){
                        //mil = System.currentTimeMillis();
                        for(this.milisegundos_sistema = 0; this.milisegundos_sistema < delay * 0.66 ; this.milisegundos_sistema++){
                            Thread.sleep(1);
                            //TimeUnit.MICROSECONDS.sleep(1000000);
                        }
                        //System.out.println("Del:"+ (System.currentTimeMillis()-mil));
                        //if (this.milisegundos_sistema == 1000){
                        //    this.milisegundos_sistema = 0;
                        //    this.segundos++;
                        //}
                        
                        if(deltaTiempo != 0)
                            deltaTiempo = 0;
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
                        if (!pausado && activo){
                            if (lbl_reloj != null)
                                this.lbl_reloj.setText(this.obtenerHora());
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    Thread.sleep(100);
                }
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void detenerReloj(){
        this.pausado = true;
    }
    
    public void reanudarReloj(){
        this.pausado = false;
    }
    
    public void editarReloj(int horas, int minutos, int segundos, int delay){
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
        this.delay = delay;
        if (this.lbl_reloj != null)
            this.lbl_reloj.setText(this.obtenerHora());
        
    }
    
    public final String obtenerHora(){
        return  ( getHoras()    > 9 ? ""+getHoras()   :"0"+getHoras())   + ":" +
                ( getMinutos()  > 9 ? ""+getMinutos() :"0"+getMinutos()) + ":" +
                ( getSegundos() > 9 ? ""+getSegundos():"0"+getSegundos());
    }

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

    public void setDeltaTiempo(int deltaTiempo) {
        this.deltaTiempo = deltaTiempo;
    }

    public int getDeltaTiempo() {
        return deltaTiempo;
    }

    public long getMilisegundos() {
        //return System.currentTimeMillis() - milisegundos_sistema;
        return milisegundos_sistema;
    }

    public void setMilisegundos(long milisegundos_sistema) {
        this.milisegundos_sistema = milisegundos_sistema;
    }
    
}
