/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ljdc.practica3_refactorizacion.general.modelo;

import java.io.Serializable;

/**
 *
 * @author Dave
 */
public class UsuarioSesion implements Serializable {
    private int id;
    private int id_usuario;
    private int id_sesion;
    private String tiempo;

    public UsuarioSesion() {
    }

    public UsuarioSesion(int id, int id_usuario, int id_sesion, String tiempo) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.id_sesion = id_sesion;
        this.tiempo = tiempo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_sesion() {
        return id_sesion;
    }

    public void setId_sesion(int id_sesion) {
        this.id_sesion = id_sesion;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }
    
}
