package com.example.actividad7;

public class Variables {
    public static final String NOMBRE_DB = "bd_usuarios";
    public static final String NOMBRE_TABLA = "usuarios";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE  = "nombre";
    public static final String CAMPO_TELEFONO   = "telefono";
    public static final String CREAR_TABLA = "CREATE TABLE " + NOMBRE_TABLA + " (" + CAMPO_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ CAMPO_NOMBRE+ " TEXT, " +CAMPO_TELEFONO+ " TEXT);";
    public static final String ELIMINAR_TABLA = "DROP TABLE IF EXISTS "+ NOMBRE_TABLA;
}
