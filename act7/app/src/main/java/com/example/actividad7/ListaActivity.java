package com.example.actividad7;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lista;
    ArrayList<String> listausuarios;
    ArrayList<Usuarios> datosusuario;
    Conectar conectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setTitle("Ver usuarios");
        lista = (ListView) findViewById(R.id.lista);
        mostrar();
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listausuarios);
        lista.setAdapter(aa);
        lista.setOnItemClickListener(this);
    }

    private void mostrar() {
        conectar = new Conectar(this, Variables.NOMBRE_DB, null, 1);
        SQLiteDatabase bd = conectar.getReadableDatabase();
        Usuarios usuario = null;
        datosusuario = new ArrayList<Usuarios>();
        Cursor cursor = bd.rawQuery("SELECT * FROM "+ Variables.NOMBRE_TABLA, null);
        while(cursor.moveToNext()){
            usuario = new Usuarios();
            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setTelefono(cursor.getString(2));
            usuario.setPrimerApellido(cursor.getString(3));
            usuario.setEdad(cursor.getInt(4));
            usuario.setSexo(cursor.getString(5));
            usuario.setFechaNacimiento(cursor.getString(6));
            usuario.setEstatura(cursor.getFloat(7));
            datosusuario.add(usuario);
        }
        agregaralista();
        bd.close();
    }

    private void agregaralista() {
        listausuarios = new ArrayList<String>();
        for(int i = 0; i<datosusuario.size();i++){
            listausuarios.add(datosusuario.get(i).getId()+ " . "+datosusuario.get(i).getNombre());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Usuarios usuario = datosusuario.get(position);
        Intent ii = new Intent(this, DetalleActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("usuario", usuario);
        ii.putExtras(b);
        startActivity(ii);
    }
}