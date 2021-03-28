/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.pract1.threads;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author luisx
 */
public class ClockRunnable implements Runnable{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ClockRunnable.class);
    
    private final String threadName;
    private final javax.swing.JLabel labelReloj;
    
    private Thread t;
    private int hours, minutes, seconds;
    private double speedfactor;
    private long timeMilis;
    private Boolean currentlyEditing;
    
    public ClockRunnable(String threadName, javax.swing.JLabel labelReloj){
        this.threadName = threadName;
        this.labelReloj = labelReloj;
        LOGGER.info("Creando hilo: {}", threadName);
        
        // default values
        this.speedfactor = 1.0;
        this.timeMilis = new GregorianCalendar().getTimeInMillis();
        this.currentlyEditing = false;
    }
    
    public void start(){
        LOGGER.info("Iniciando hilo: {}", threadName);
        if (t == null) {
           t = new Thread(this, threadName);
           t.start();
        }
    }

    @Override
    public void run() {
        Thread current = Thread.currentThread();
        while(current == t) {
            timeFormat();
            
            try {
                labelReloj.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                current.sleep((long)(1000 * speedfactor));
                SwingUtilities.invokeLater(this);
            }catch(InterruptedException e) {
                LOGGER.error("Error en hilo {}", threadName);
                LOGGER.error("Error al actualizar el reloj en GUI", e);
            }
       }
        
    }

    public void setRandomClock() {
        timeMilis = (long)(timeMilis - (((Math.random() * (82800 - 3600)) + 3600) * 1000));
        LOGGER.info("Reloj aleatorio en {}", threadName);
    }

    public long getTimeMilis() {
        return timeMilis;
    }

    public void setTimeMilis(long timeMilis) {
        this.timeMilis = timeMilis;
    }

    public double getSpeedfactor() {
        return speedfactor;
    }

    public void setSpeedfactor(double speedfactor) {
        LOGGER.info("Factor: {}", speedfactor);
        this.speedfactor = speedfactor;
    }   

    public Boolean getCurrentlyEditing() {
        return currentlyEditing;
    }

    public void setCurrentlyEditing(Boolean currentlyEditing) {
        LOGGER.info(currentlyEditing ? "Deteniendo reloj" : "Resumiendo");
        this.currentlyEditing = currentlyEditing;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getThreadName() {
        return threadName;
    }
    
    
    
    /**
     * formato para la hora, minutos y segundos
     * 
     */
    public void timeFormat() {
        if(!currentlyEditing){
            timeMilis += 1000;
        }
        
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timeMilis);
        
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        seconds = calendar.get(Calendar.SECOND);
    }  
    
}
