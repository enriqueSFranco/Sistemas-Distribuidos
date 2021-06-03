/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import ljdc.practica3_refactorizacion.general.modelo.Libro;

/**
 *
 * @author Dave
 */
public interface RMIServidorInterfaz extends Remote {
    
    //Usada para verificar si un servidor está levantado
    public void ping() throws RemoteException;
    
    //Usada por los clientes para registrarse en la base
    public void regitrarse(int puerto, String nombre) throws RemoteException;
    
    //Usada por los usuarios para pedir un libro aleatorio
    public Libro pedirLibro(int puerto, String tiempo) throws RemoteException;
    
}
