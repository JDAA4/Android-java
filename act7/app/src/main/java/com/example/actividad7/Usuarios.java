package com.example.actividad7;

import java.io.Serializable;

public class Usuarios implements Serializable {
    Integer Id;
    String nombre;
    String telefono;
    String primerApellido;
    Integer edad;
    String sexo;
    String fechaNacimiento;
    Float estatura;

    public Usuarios(Integer id, String nombre, String telefono, String primerApellido, Integer edad, String sexo, String fechaNacimiento, Float estatura) {
        Id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.primerApellido = primerApellido;
        this.edad = edad;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.estatura = estatura;
    }

    public Usuarios() {}

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

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Float getEstatura() {
        return estatura;
    }

    public void setEstatura(Float estatura) {
        this.estatura = estatura;
    }
}