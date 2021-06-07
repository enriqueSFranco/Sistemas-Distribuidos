/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Dave
 */
public interface RMISincronizacionInterfaz extends Remote {
    
    /**
     * 
     * @return El tiempo en milisegundos del reloj
     */
    public long obtenerTiempo(long tiempo) throws RemoteException;
    public void cambiarTiempo(long ajuste) throws RemoteException;
    public void cambiarTiempo(int horas, int minutos, int segundos, int delta) throws RemoteException;
    
}
