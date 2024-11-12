package com.example.actividad7;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleActivity extends AppCompatActivity {
    TextView txtid, txttelefono, txtnombre, txtprimerapellido, txtedad, txtsexo, txtfechaNacimiento, txtestatura;

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
        txtid = findViewById(R.id.txtid);
        txtnombre = findViewById(R.id.txtnombre);
        txttelefono = findViewById(R.id.txttelefono);
        txtprimerapellido = findViewById(R.id.txtprimerapellido);
        txtedad = findViewById(R.id.txtedad);
        txtsexo = findViewById(R.id.txtsexo);
        txtfechaNacimiento = findViewById(R.id.txtfechaNacimiento);
        txtestatura = findViewById(R.id.txtestatura);

        Bundle objeto = getIntent().getExtras();
        Usuarios usu = null;
        if (objeto != null) {
            usu = (Usuarios) objeto.getSerializable("usuario");
            txtid.setText(usu.getId().toString());
            txtnombre.setText(usu.getNombre().toString());
            txttelefono.setText(usu.getTelefono().toString());
            txtprimerapellido.setText(usu.getPrimerApellido().toString());
            txtedad.setText(String.valueOf(usu.getEdad()));
            txtsexo.setText(usu.getSexo().toString());
            txtfechaNacimiento.setText(usu.getFechaNacimiento().toString());
            txtestatura.setText(String.valueOf(usu.getEstatura()));
        }
    }
}