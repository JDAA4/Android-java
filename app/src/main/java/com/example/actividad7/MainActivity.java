package com.example.actividad7;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText campoid, nombre, telefono;
    Button insertar1, insertar2, buscar1, buscar2, editar, eliminar, ver;
    Conectar conectar;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
       setTitle("Control de usuarios");
       campoid = (EditText) findViewById(R.id.campoid);
       nombre = (EditText) findViewById(R.id.nombre);
       telefono = (EditText) findViewById(R.id.telefono);
       insertar1 = (Button) findViewById(R.id.insertar1);
       insertar2 = (Button) findViewById(R.id.insertar2);
       buscar1 = (Button) findViewById(R.id.buscar1);
       buscar2 = (Button) findViewById(R.id.buscar2);
       editar = (Button) findViewById(R.id.editar);
       eliminar = (Button) findViewById(R.id.eliminar);
       ver = (Button) findViewById(R.id.ver);

       insertar1.setOnClickListener(this);
       insertar2.setOnClickListener(this);
       buscar1.setOnClickListener(this);
       buscar2.setOnClickListener(this);
       editar.setOnClickListener(this);
       eliminar.setOnClickListener(this);
       ver.setOnClickListener(this);

       conectar = new Conectar(this, Variables.NOMBRE_DB, null, 1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.insertar1) {
            insertar1();
        } else if (id == R.id.insertar2) {
            insertar2();
        } else if (id == R.id.buscar1) {
            buscar1();
        } else if (id == R.id.buscar2) {
            buscar2();
        } else if (id == R.id.editar) {
            editar();
        } else if (id == R.id.eliminar) {
            eliminar();
        } else if (id == R.id.ver) {
            i = new Intent(MainActivity.this, ListaActivity.class);
            startActivity(i);
        }
    }
    private void insertar1() {
        SQLiteDatabase db = conectar.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Variables.CAMPO_NOMBRE, nombre.getText().toString());
        valores.put(Variables.CAMPO_TELEFONO, telefono.getText().toString());
        long id = db.insert(Variables.NOMBRE_TABLA, Variables.CAMPO_ID, valores);
        Toast.makeText(this,"id:" + id, Toast.LENGTH_LONG).show();
        nombre.setText("");
        telefono.setText("");
        db.close();
    }

    private void insertar2() {
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String insertar = "INSERT INTO " + Variables.NOMBRE_TABLA + " (" + Variables.CAMPO_NOMBRE + ", " + Variables.CAMPO_TELEFONO + ") VALUES ('" + nombre.getText().toString() + "','" + telefono.getText().toString() + "')";
        bd.execSQL(insertar);
        Toast.makeText(this, "se ingreso datos del usuario", Toast.LENGTH_LONG).show();
        bd.close();
    }

    private void buscar1(){
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {campoid.getText().toString()};
        String[] campos = {Variables.CAMPO_NOMBRE, Variables.CAMPO_TELEFONO};
        try {
            Cursor cursor = bd.query(Variables.NOMBRE_TABLA, campos, Variables.CAMPO_ID + "=?", parametros, null, null, null);
            cursor.moveToFirst();
            nombre.setText(cursor.getString(0));
            telefono.setText(cursor.getString(1));
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "El usuario no existe", Toast.LENGTH_LONG).show();
            nombre.setText("");
            telefono.setText("");
            e.printStackTrace();
        }
        bd.close();
    }

    private void buscar2(){
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {campoid.getText().toString()};

        try {
            Cursor cursor = bd.rawQuery("SELECT " + Variables.CAMPO_NOMBRE + ", " + Variables.CAMPO_TELEFONO + " FROM " + Variables.NOMBRE_TABLA + " WHERE " + Variables.CAMPO_ID + "=?", parametros);
            cursor.moveToFirst();
            nombre.setText(cursor.getString(0));
            telefono.setText(cursor.getString(1));
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "El usuario no existe", Toast.LENGTH_LONG).show();
            nombre.setText("");
            telefono.setText("");
            e.printStackTrace();
        }
        bd.close();
    }

    private void editar(){
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {campoid.getText().toString()};
        ContentValues valores = new ContentValues();
        valores.put(Variables.CAMPO_NOMBRE, telefono.getText().toString());
        valores.put(Variables.CAMPO_TELEFONO, telefono.getText().toString());
        bd.update(Variables.NOMBRE_TABLA, valores, Variables.CAMPO_ID + "=?", parametros);
        Toast.makeText(this, "Los datos del usuario han sido actualizados", Toast.LENGTH_SHORT).show();
        bd.close();
    }

    private void eliminar(){
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros ={campoid.getText().toString()};
        int n = bd.delete(Variables.NOMBRE_TABLA, Variables.CAMPO_ID + "=?", parametros);
        Toast.makeText(this, "Usuarios eliminados:" + n, Toast.LENGTH_SHORT).show();
        nombre.setText("");
        telefono.setText("");
        campoid.setText("");
        bd.close();
    }
}