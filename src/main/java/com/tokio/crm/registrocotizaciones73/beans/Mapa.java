package com.tokio.crm.registrocotizaciones73.beans;

public class Mapa {
    int id;
    String descripcion;

    public Mapa(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Mapa() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
