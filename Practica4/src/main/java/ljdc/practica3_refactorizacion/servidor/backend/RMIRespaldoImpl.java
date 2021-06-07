/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.servidor.backend;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import ljdc.practica3_refactorizacion.general.modelo.Pedido;
import ljdc.practica3_refactorizacion.general.modelo.Sesion;
import ljdc.practica3_refactorizacion.general.modelo.Usuario;
import ljdc.practica3_refactorizacion.general.modelo.UsuarioSesion;
import ljdc.practica3_refactorizacion.rmi.RMIRespaldoInterfaz;
import ljdc.practica3_refactorizacion.servidor.backend.basedatos.ConexionBD;
import ljdc.practica3_refactorizacion.servidor.interfaces.GUIServidorInterfaz;

/**
 *
 * @author Dave
 */
public class RMIRespaldoImpl extends UnicastRemoteObject implements RMIRespaldoInterfaz {
    
    private GUIServidorInterfaz gui;

    public RMIRespaldoImpl(GUIServidorInterfaz gui)  throws RemoteException {
        this.gui = gui;
    }

    @Override
    public void reiniciarSesion(int sesion) throws RemoteException {
        ConexionBD con = new ConexionBD();
        con.cambiarSesion(sesion);
        this.gui.actualizarSesion(sesion);
        this.gui.actualizarLista(con.obtenerLibrosRestantes());
        this.gui.limpiarPortada();
    }

    @Override
    public void colocarUsuario(int id_usuario, String ip, String nombre, int puerto) throws RemoteException {
        ConexionBD con = new ConexionBD();
        con.colocarUsuario(id_usuario, ip, nombre, puerto);
    }

    @Override
    public void colocarPedido(int id_peticion, String fecha, String hora_inicio, String hora_fin) throws RemoteException {
        ConexionBD con = new ConexionBD();
        con.colocarPedido(id_peticion, fecha, hora_inicio, hora_fin);
        this.gui.actualizarSesion(con.obtenerNumeroSesion());
        this.gui.actualizarLista(con.obtenerLibrosRestantes());
    }

    @Override
    public void colocarSesion(int id_sesion, int id_pedido, String isbn) throws RemoteException {
        ConexionBD con = new ConexionBD();
        con.colocarSesion(id_sesion, id_pedido, isbn);
        this.gui.actualizarSesion(con.obtenerNumeroSesion());
        this.gui.actualizarLista(con.obtenerLibrosRestantes());
    }

    @Override
    public void colocarUsuarioSesion(int id_SU, int id_usuario, int id_sesion, String tiempo) throws RemoteException {
        ConexionBD con = new ConexionBD();
        con.colocarUsuarioSesion(id_SU, id_usuario, id_sesion, tiempo);
        this.gui.actualizarSesion(con.obtenerNumeroSesion());
        this.gui.actualizarLista(con.obtenerLibrosRestantes());
    }

    @Override
    public int obtenerSesion() throws RemoteException {
        ConexionBD con = new ConexionBD();
        return con.obtenerNumeroSesion();
    }

    @Override
    public List<Pedido> obtenerPedidos() throws RemoteException {
        ConexionBD con = new ConexionBD();
        return con.obtenerTodosLosPedidos();
    }

    @Override
    public List<Sesion> obtenerSesiones() throws RemoteException {
        ConexionBD con = new ConexionBD();
        return con.obtenerTodasLasSesiones();
    }

    @Override
    public List<Usuario> obtenerUsuarios() throws RemoteException {
        ConexionBD con = new ConexionBD();
        return con.obtenerTodosLosUsuarios();
    }

    @Override
    public List<UsuarioSesion> obtenerUsuarioSesiones() throws RemoteException {
        ConexionBD con = new ConexionBD();
        return con.obtenerTodasLasUsuarioSesiones();
    }
    
}
