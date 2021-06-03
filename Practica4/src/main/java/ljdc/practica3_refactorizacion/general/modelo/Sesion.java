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
public class Sesion implements Serializable {
    private int id;
    private int id_pedido;
    private String isbn;

    public Sesion() {
    }

    public Sesion(int id_sesion, int id_pedido, String isbn) {
        this.id = id_sesion;
        this.id_pedido = id_pedido;
        this.isbn = isbn;
    }

    public int getId_sesion() {
        return id;
    }

    public void setId_sesion(int id_sesion) {
        this.id = id_sesion;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
