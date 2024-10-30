package com.example.actividad7;

public class Variables {
    public static final String NOMBRE_DB = "bd_usuarios";
    public static final String NOMBRE_TABLA = "usuarios";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_TELEFONO = "telefono";
    public static final String CAMPO_PRIMER_APELLIDO = "primerapellido";
    public static final String CAMPO_EDAD = "edad";
    public static final String CAMPO_SEXO = "sexo";
    public static final String CAMPO_FECHA_NACIMIENTO = "fecha_nacimiento";
    public static final String CAMPO_ESTATURA = "estatura";

    public static final String CREAR_TABLA = "CREATE TABLE " + NOMBRE_TABLA + " (" +
            CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_NOMBRE + " TEXT, " +
            CAMPO_TELEFONO + " TEXT, " +
            CAMPO_PRIMER_APELLIDO + " TEXT, " +
            CAMPO_EDAD + " INTEGER, " +
            CAMPO_SEXO + " TEXT, " +
            CAMPO_FECHA_NACIMIENTO + " TEXT, " +
            CAMPO_ESTATURA + " REAL);";

    public static final String ELIMINAR_TABLA = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
}