/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import ljdc.practica3_refactorizacion.general.modelo.Pedido;
import ljdc.practica3_refactorizacion.general.modelo.Sesion;
import ljdc.practica3_refactorizacion.general.modelo.Usuario;
import ljdc.practica3_refactorizacion.general.modelo.UsuarioSesion;

/**
 *
 * @author Dave
 */
public interface RMIRespaldoInterfaz extends Remote {
    
    public void reiniciarSesion(int sesion) throws RemoteException;
    public void colocarUsuario(int id_usuario, String ip, String nombre, int puerto) throws RemoteException;
    public void colocarPedido(int id_peticion, String fecha, String hora_inicio, String hora_fin) throws RemoteException;
    public void colocarSesion(int id_sesion, int id_pedido, String isbn) throws RemoteException;
    public void colocarUsuarioSesion(int id_SU, int id_usuario, int id_sesion, String tiempo) throws RemoteException;
    
    public int obtenerSesion() throws RemoteException;
    public List<Pedido> obtenerPedidos() throws RemoteException;
    public List<Sesion> obtenerSesiones() throws RemoteException;
    public List<Usuario> obtenerUsuarios() throws RemoteException;
    public List<UsuarioSesion> obtenerUsuarioSesiones() throws RemoteException;
}
