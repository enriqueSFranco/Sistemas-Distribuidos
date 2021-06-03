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
public class Libro implements Serializable {
    private String isbn;
    private String nombre;
    private String autor;
    private String editorial;
    private float precio;
    
    private String portada;

    public Libro(String isbn, String nombre, String autor, float precio) {
        this.isbn = isbn;
        this.nombre = nombre;
        this.autor = autor;
        this.precio = precio;
    }

    public Libro(String isbn, String nombre, String autor, String editorial, float precio, String portada) {
        this.isbn = isbn;
        this.nombre = nombre;
        this.autor = autor;
        this.editorial = editorial;
        this.precio = precio;
        this.portada = portada;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAutor() {
        return autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public float getPrecio() {
        return precio;
    }

    public String getPortada() {
        return portada;
    }
    
}
