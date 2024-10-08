package com.example.act6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextValue;
    private Spinner spinnerFrom, spinnerTo;
    private TextView textViewResult;
    private Button btnConvert;
    private Button btnAbrirDivisas;

    // Unidades en un array de strings
    private String[] units = {
            "bit", "Byte", "Kilobyte", "Kibibyte", "Megabyte", "Mebibyte",
            "Gigabyte", "Gibibyte", "Terabyte", "Tebibyte", "Petabyte", "Pebibyte"
    };

    // Multiplicadores de conversión (basados en 1000 y 1024)
    private double[] multipliers = {
            1, 8, 8000, 8192, 8000000, 8388608,
            8000000000L, 8589934592L, 8000000000000L, 8796093022208L,
            8000000000000000L, 9007199254740992L
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        editTextValue = findViewById(R.id.editTextValue);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        textViewResult = findViewById(R.id.textViewResult);
        btnConvert = findViewById(R.id.btnConvert);
        btnAbrirDivisas = findViewById(R.id.btnDivisas); // Nuevo botón para abrir la actividad de divisas

        // Crear adaptador para los Spinners y llenarlos con las unidades
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Asignar acción al botón de convertir
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convert();
            }
        });

        // Acción para abrir la actividad de divisas
        btnAbrirDivisas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DivisasActivity.class);
                startActivity(intent);
            }
        });
    }

    // Método para realizar la conversión de unidades de datos
    private void convert() {
        // Verificar si el EditText no está vacío
        if (editTextValue.getText().toString().isEmpty()) {
            textViewResult.setText("Por favor, ingrese un valor.");
            return;
        }

        // Obtener el valor a convertir
        double value = Double.parseDouble(editTextValue.getText().toString());

        // Obtener las posiciones seleccionadas en los Spinners
        int fromIndex = spinnerFrom.getSelectedItemPosition();
        int toIndex = spinnerTo.getSelectedItemPosition();

        // Realizar la conversión
        double result = value * multipliers[fromIndex] / multipliers[toIndex];

        // Mostrar el resultado
        textViewResult.setText("Resultado: " + result);
    }
}