package com.example.actividad7;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleActivity extends AppCompatActivity {

    TextView txtid, txttelefono, txtnombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setTitle("Detalle usuario");
        txtid = (TextView) findViewById(R.id.txtid);
        txtnombre = (TextView) findViewById(R.id.txtnombre);
        txttelefono = (TextView) findViewById(R.id.txttelefono);
        Bundle objeto = getIntent().getExtras();
        Usuarios usu = null;
                if(objeto != null){
                    usu = (Usuarios) objeto.getSerializable("usuario");
                    txtid.setText(usu.getId().toString());
                    txtnombre.setText(usu.getNombre().toString());
                    txttelefono.setText(usu.getTelefono().toString());
                }
    }
}