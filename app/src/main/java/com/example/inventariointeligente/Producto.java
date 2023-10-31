package com.example.inventariointeligente;

import java.io.Serializable;

public class Producto implements Serializable {
    private String nombre;
    private int cantidad;
    private int precioUnidad;
    private String imagenUrl;

    public Producto() {}

    public Producto(String nombre, int cantidad, int precioUnidad, String imagenUrl) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnidad = precioUnidad;
        this.imagenUrl = imagenUrl;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(int precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
