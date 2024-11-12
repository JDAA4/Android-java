package com.example.actividad7;

import android.content.Intent;
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

public class ListaBusquedaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    ArrayList<String> listaInformacion;
    ArrayList<Usuarios> listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_busqueda);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listViewBusqueda);

        Bundle objeto = getIntent().getExtras();
        if (objeto != null) {
            listaUsuarios = (ArrayList<Usuarios>) objeto.getSerializable("listaUsuarios");
            obtenerLista();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaInformacion);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<>();
        for (Usuarios usuario : listaUsuarios) {
            listaInformacion.add(usuario.getId() + " - " + usuario.getNombre() + " " + usuario.getPrimerApellido());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Usuarios usuario = listaUsuarios.get(position);
        Intent intent = new Intent(ListaBusquedaActivity.this, DetalleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}