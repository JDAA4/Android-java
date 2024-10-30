package com.example.actividad7;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ListaOrdenadaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    ArrayList<String> listaInformacion;
    ArrayList<Usuarios> listaUsuarios;
    Conectar conectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_ordenada);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listViewOrdenada);
        conectar = new Conectar(this, Variables.NOMBRE_DB, null, 1);

        consultarListaUsuarios();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaInformacion);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void consultarListaUsuarios() {
        SQLiteDatabase db = conectar.getReadableDatabase();
        Usuarios usuario = null;
        listaUsuarios = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Variables.NOMBRE_TABLA + " ORDER BY " + Variables.CAMPO_NOMBRE, null);

        while (cursor.moveToNext()) {
            usuario = new Usuarios();
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
        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<>();
        for (Usuarios usuario : listaUsuarios) {
            listaInformacion.add(usuario.getNombre() + " " + usuario.getPrimerApellido());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Usuarios usuario = listaUsuarios.get(position);
        Intent intent = new Intent(ListaOrdenadaActivity.this, DetalleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}