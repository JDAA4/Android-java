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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText campoid, nombre, telefono, primerapellido, edad, sexo, fechaNacimiento, estatura;
    Button insertar1, insertar2, buscar1,buscarPorPrimerApellido,buscarPorEdad,buscarPorEstatura, buscar2, editar, eliminar, ver, verListaOrdenada;
    Conectar conectar;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("Control de usuarios");

        campoid = findViewById(R.id.campoid);
        nombre = findViewById(R.id.nombre);
        telefono = findViewById(R.id.telefono);
        primerapellido = findViewById(R.id.primerapellido);
        edad = findViewById(R.id.edad);
        sexo = findViewById(R.id.sexo);
        fechaNacimiento = findViewById(R.id.fecha_nacimiento);
        estatura = findViewById(R.id.estatura);

        insertar1 = findViewById(R.id.insertar1);
        insertar2 = findViewById(R.id.insertar2);
        buscar1 = findViewById(R.id.buscar1);
        buscar2 = findViewById(R.id.buscar2);
        editar = findViewById(R.id.editar);
        eliminar = findViewById(R.id.eliminar);
        ver = findViewById(R.id.ver);
        verListaOrdenada = findViewById(R.id.ver_lista_ordenada);
        buscarPorPrimerApellido = findViewById(R.id.buscar_por_primer_apellido);
        buscarPorEdad = findViewById(R.id.buscar_por_edad);
        buscarPorEstatura = findViewById(R.id.buscar_por_estatura);

        insertar1.setOnClickListener(this);
        insertar2.setOnClickListener(this);
        buscar1.setOnClickListener(this);
        buscar2.setOnClickListener(this);
        editar.setOnClickListener(this);
        eliminar.setOnClickListener(this);
        ver.setOnClickListener(this);
        verListaOrdenada.setOnClickListener(this);
        buscarPorPrimerApellido.setOnClickListener(this);
        buscarPorEdad.setOnClickListener(this);
        buscarPorEstatura.setOnClickListener(this);

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
        } else if (id == R.id.ver_lista_ordenada) {
            i = new Intent(MainActivity.this, ListaOrdenadaActivity.class);
            startActivity(i);
        }else if (id == R.id.buscar_por_primer_apellido) {
            buscarPorCampo(Variables.CAMPO_PRIMER_APELLIDO, primerapellido.getText().toString());
        } else if (id == R.id.buscar_por_edad) {
            buscarPorCampo(Variables.CAMPO_EDAD, edad.getText().toString());
        } else if (id == R.id.buscar_por_estatura) {
            buscarPorCampo(Variables.CAMPO_ESTATURA, estatura.getText().toString());
        }
    }

    private void buscarPorCampo(String campo, String valor) {
        SQLiteDatabase db = conectar.getReadableDatabase();
        ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Variables.NOMBRE_TABLA + " WHERE " + campo + "=?", new String[]{valor});

        while (cursor.moveToNext()) {
            Usuarios usuario = new Usuarios();
            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setTelefono(cursor.getString(2));
            usuario.setPrimerApellido(cursor.getString(3));
            usuario.setEdad(cursor.getInt(4));
            usuario.setSexo(cursor.getString(5));
            usuario.setFechaNacimiento(cursor.getString(6));
            usuario.setEstatura(cursor.getFloat(7));
            listaUsuarios.add(usuario);
        }
        cursor.close();

        if (listaUsuarios.size() == 1) {
            Usuarios usuario = listaUsuarios.get(0);
            Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("usuario", usuario);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (listaUsuarios.size() > 1) {
            Intent intent = new Intent(MainActivity.this, ListaBusquedaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("listaUsuarios", listaUsuarios);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertar1() {
        SQLiteDatabase db = conectar.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Variables.CAMPO_NOMBRE, nombre.getText().toString());
        valores.put(Variables.CAMPO_TELEFONO, telefono.getText().toString());
        valores.put(Variables.CAMPO_PRIMER_APELLIDO, primerapellido.getText().toString());
        valores.put(Variables.CAMPO_EDAD, Integer.parseInt(edad.getText().toString()));
        valores.put(Variables.CAMPO_SEXO, sexo.getText().toString());
        valores.put(Variables.CAMPO_FECHA_NACIMIENTO, fechaNacimiento.getText().toString());
        valores.put(Variables.CAMPO_ESTATURA, Float.parseFloat(estatura.getText().toString()));
        long id = db.insert(Variables.NOMBRE_TABLA, Variables.CAMPO_ID, valores);
        Toast.makeText(this, "id:" + id, Toast.LENGTH_LONG).show();
        nombre.setText("");
        telefono.setText("");
        primerapellido.setText("");
        edad.setText("");
        sexo.setText("");
        fechaNacimiento.setText("");
        estatura.setText("");
        db.close();
    }

    private void insertar2() {
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String insertar = "INSERT INTO " + Variables.NOMBRE_TABLA + " (" +
                Variables.CAMPO_NOMBRE + ", " +
                Variables.CAMPO_TELEFONO + ", " +
                Variables.CAMPO_PRIMER_APELLIDO + ", " +
                Variables.CAMPO_EDAD + ", " +
                Variables.CAMPO_SEXO + ", " +
                Variables.CAMPO_FECHA_NACIMIENTO + ", " +
                Variables.CAMPO_ESTATURA + ") VALUES ('" +
                nombre.getText().toString() + "','" +
                telefono.getText().toString() + "','" +
                primerapellido.getText().toString() + "'," +
                Integer.parseInt(edad.getText().toString()) + ",'" +
                sexo.getText().toString() + "','" +
                fechaNacimiento.getText().toString() + "'," +
                Float.parseFloat(estatura.getText().toString()) + ")";
        bd.execSQL(insertar);
        Toast.makeText(this, "se ingreso datos del usuario", Toast.LENGTH_LONG).show();
        bd.close();
    }

    private void buscar1() {
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {campoid.getText().toString()};
        String[] campos = {Variables.CAMPO_NOMBRE, Variables.CAMPO_TELEFONO, Variables.CAMPO_PRIMER_APELLIDO, Variables.CAMPO_EDAD, Variables.CAMPO_SEXO, Variables.CAMPO_FECHA_NACIMIENTO, Variables.CAMPO_ESTATURA};
        try {
            Cursor cursor = bd.query(Variables.NOMBRE_TABLA, campos, Variables.CAMPO_ID + "=?", parametros, null, null, null);
            cursor.moveToFirst();
            nombre.setText(cursor.getString(0));
            telefono.setText(cursor.getString(1));
            primerapellido.setText(cursor.getString(2));
            edad.setText(cursor.getString(3));
            sexo.setText(cursor.getString(4));
            fechaNacimiento.setText(cursor.getString(5));
            estatura.setText(cursor.getString(6));
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "El usuario no existe", Toast.LENGTH_LONG).show();
            nombre.setText("");
            telefono.setText("");
            primerapellido.setText("");
            edad.setText("");
            sexo.setText("");
            fechaNacimiento.setText("");
            estatura.setText("");
        }
    }

    private void buscar2() {
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {campoid.getText().toString()};

        try {
            Cursor cursor = bd.rawQuery("SELECT " + Variables.CAMPO_NOMBRE + ", " + Variables.CAMPO_TELEFONO + ", " + Variables.CAMPO_PRIMER_APELLIDO + ", " + Variables.CAMPO_EDAD + ", " + Variables.CAMPO_SEXO + ", " + Variables.CAMPO_FECHA_NACIMIENTO + ", " + Variables.CAMPO_ESTATURA + " FROM " + Variables.NOMBRE_TABLA + " WHERE " + Variables.CAMPO_ID + "=?", parametros);
            cursor.moveToFirst();
            nombre.setText(cursor.getString(0));
            telefono.setText(cursor.getString(1));
            primerapellido.setText(cursor.getString(2));
            edad.setText(cursor.getString(3));
            sexo.setText(cursor.getString(4));
            fechaNacimiento.setText(cursor.getString(5));
            estatura.setText(cursor.getString(6));
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "El usuario no existe", Toast.LENGTH_LONG).show();
            nombre.setText("");
            telefono.setText("");
            primerapellido.setText("");
            edad.setText("");
            sexo.setText("");
            fechaNacimiento.setText("");
            estatura.setText("");
        }
        bd.close();
    }

    private void editar() {
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {campoid.getText().toString()};
        ContentValues valores = new ContentValues();
        valores.put(Variables.CAMPO_NOMBRE, nombre.getText().toString());
        valores.put(Variables.CAMPO_TELEFONO, telefono.getText().toString());
        valores.put(Variables.CAMPO_PRIMER_APELLIDO, primerapellido.getText().toString());
        valores.put(Variables.CAMPO_EDAD, Integer.parseInt(edad.getText().toString()));
        valores.put(Variables.CAMPO_SEXO, sexo.getText().toString());
        valores.put(Variables.CAMPO_FECHA_NACIMIENTO, fechaNacimiento.getText().toString());
        valores.put(Variables.CAMPO_ESTATURA, Float.parseFloat(estatura.getText().toString()));
        bd.update(Variables.NOMBRE_TABLA, valores, Variables.CAMPO_ID + "=?", parametros);
        Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_LONG).show();
        bd.close();
    }

    private void eliminar() {
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {campoid.getText().toString()};
        int n = bd.delete(Variables.NOMBRE_TABLA, Variables.CAMPO_ID + "=?", parametros);
        Toast.makeText(this, "Usuarios eliminados:" + n, Toast.LENGTH_SHORT).show();
        campoid.setText("");
        nombre.setText("");
        telefono.setText("");
        primerapellido.setText("");
        edad.setText("");
        sexo.setText("");
        fechaNacimiento.setText("");
        estatura.setText("");
        bd.close();
    }
}