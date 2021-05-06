/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distribuidos.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Dave
 */
public interface RMIBackupInterface extends Remote {
    public void reiniciarSesion(int sesion) throws RemoteException;
    
    public void colocarUsuario(int id_usuario, String ip, String nombre) throws RemoteException;
    public void colocarPedido(int id_peticion, String fecha, String hora_inicio, String hora_fin) throws RemoteException;
    public void colocarSesion(int id_sesion, int id_pedido, String isbn) throws RemoteException;
    public void colocarUsuarioSesion(int id_SU, int id_usuario, int id_sesion, String tiempo) throws RemoteException;
}
