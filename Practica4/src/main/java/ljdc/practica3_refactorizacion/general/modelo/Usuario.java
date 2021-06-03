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
public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String ip;
    private int puerto;

    public Usuario() {
    }

    public Usuario(String ip, int puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }

    public Usuario(int id, String nombre, String ip, int puerto) {
        this.id = id;
        this.nombre = nombre;
        this.ip = ip;
        this.puerto = puerto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
    
}
