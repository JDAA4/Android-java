package com.example.actividad7;

import java.io.Serializable;
public class Usuarios implements Serializable{
    Integer Id;
    String nombre;
    String telefono;

    public Usuarios(Integer id, String nombre, String telefono){
        Id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Usuarios(){}
    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
