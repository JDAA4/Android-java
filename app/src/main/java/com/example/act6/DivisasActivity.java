package com.example.act6;

import android.os.Bundle;
import android.util.Log; // Importar la clase Log
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.HashMap;

public class DivisasActivity extends AppCompatActivity {

    private static final String TAG = "DivisasActivity"; // Etiqueta para los logs
    private EditText editTextValor;
    private Spinner spinnerMonedaOrigen, spinnerMonedaDestino;
    private TextView textViewResultado, textViewNombreMonedaOrigen, textViewNombreMonedaDestino;
    private Button btnConvertir;

    // Arrays con códigos y nombres de monedas
    private String[] codigosMonedas = {"EUR", "USD", "JPY", "BGN", "CZK", "DKK", "GBP", "HUF", "PLN", "RON", "SEK", "CHF", "ISK", "NOK", "HRK", "RUB", "TRY", "AUD", "BRL", "CAD", "CNY", "HKD", "IDR", "ILS", "INR", "KRW", "MXN", "MYR", "NZD", "PHP", "SGD", "THB", "ZAR"};
    private String[] nombresMonedas = {"Euro", "Dólar Estadounidense", "Yen Japonés", "Lev Búlgaro", "Corona Checa", "Corona Danesa", "Libra Esterlina", "Florín Húngaro", "Zloty Polaco", "Leu Rumano", "Corona Sueca", "Franco Suizo", "Corona Islandesa", "Corona Noruega", "Kuna Croata", "Rublo Ruso", "Lira Turca", "Dólar Australiano", "Real Brasileño", "Dólar Canadiense", "Yuan Chino", "Dólar Hongkonés", "Rupia Indonesia", "Nuevo Shekel Israelí", "Rupia India", "Won Surcoreano", "Peso Mexicano", "Ringgit Malasio", "Dólar Neozelandés", "Peso Filipino", "Dólar de Singapur", "Baht Tailandés", "Rand Sudafricano"};

    private HashMap<String, Float> tasaDeCambio = new HashMap<>(); // Map para almacenar tasas de cambio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divisas);

        // Inicializar vistas
        editTextValor = findViewById(R.id.editTextValor);
        spinnerMonedaOrigen = findViewById(R.id.spinnerMonedaOrigen);
        spinnerMonedaDestino = findViewById(R.id.spinnerMonedaDestino);
        textViewResultado = findViewById(R.id.textViewResultado);
        textViewNombreMonedaOrigen = findViewById(R.id.textViewNombreMonedaOrigen);
        textViewNombreMonedaDestino = findViewById(R.id.textViewNombreMonedaDestino);
        btnConvertir = findViewById(R.id.btnConvertir);

        // Llenar los spinners con los códigos de monedas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, codigosMonedas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonedaOrigen.setAdapter(adapter);
        spinnerMonedaDestino.setAdapter(adapter);

        // Cambiar nombre de moneda cuando se seleccione en el spinner
        spinnerMonedaOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textViewNombreMonedaOrigen.setText(nombresMonedas[position]);
                Log.d(TAG, "Moneda de origen seleccionada: " + codigosMonedas[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerMonedaDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textViewNombreMonedaDestino.setText(nombresMonedas[position]);
                Log.d(TAG, "Moneda de destino seleccionada: " + codigosMonedas[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Cargar tasas de cambio desde la API
        obtenerTasasDeCambio();

        // Acción al presionar el botón "Convertir"
        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertirDivisa();
            }
        });
    }

    // Método para obtener tasas de cambio desde la API
    private void obtenerTasasDeCambio() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.floatrates.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FloatRatesService service = retrofit.create(FloatRatesService.class);
        Call<HashMap<String, ExchangeRate>> call = service.getRates();

        call.enqueue(new Callback<HashMap<String, ExchangeRate>>() {
            @Override
            public void onResponse(Call<HashMap<String, ExchangeRate>> call, Response<HashMap<String, ExchangeRate>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HashMap<String, ExchangeRate> rates = response.body();

                    // Guardar tasas de cambio en el HashMap
                    for (String codigo : codigosMonedas) {
                        if (rates.containsKey(codigo.toLowerCase())) {
                            tasaDeCambio.put(codigo, rates.get(codigo.toLowerCase()).getRate());
                            Log.d(TAG, "Tasa de cambio para " + codigo + ": " + rates.get(codigo.toLowerCase()).getRate());
                        }
                    }
                } else {
                    Log.e(TAG, "Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, ExchangeRate>> call, Throwable t) {
                textViewResultado.setText("Error al cargar tasas de cambio.");
                Log.e(TAG, "Error al cargar tasas de cambio: " + t.getMessage());
            }
        });
    }

    // Método para convertir divisa
    private void convertirDivisa() {
        if (editTextValor.getText().toString().isEmpty()) {
            textViewResultado.setText("Por favor, ingrese un valor.");
            Log.d(TAG, "Valor de entrada vacío.");
            return;
        }

        float valor = Float.parseFloat(editTextValor.getText().toString());
        String monedaOrigen = spinnerMonedaOrigen.getSelectedItem().toString();
        String monedaDestino = spinnerMonedaDestino.getSelectedItem().toString();

        Log.d(TAG, "Convertir " + valor + " desde " + monedaOrigen + " a " + monedaDestino);

        // Verificar si tenemos las tasas de cambio
        if (tasaDeCambio.containsKey(monedaOrigen) && tasaDeCambio.containsKey(monedaDestino)) {
            float tasaOrigen = tasaDeCambio.get(monedaOrigen);
            float tasaDestino = tasaDeCambio.get(monedaDestino);
            if (!tasaDeCambio.containsKey("MXN")) {
                Log.e(TAG, "La tasa para MXN no está disponible.");
            }


            // Convertir de origen a destino
            float resultado = valor * (tasaDestino / tasaOrigen);
            textViewResultado.setText("Resultado: " + resultado);
            Log.d(TAG, "Resultado de conversión: " + resultado);
        } else {
            textViewResultado.setText("Tasas de cambio no disponibles.");
            Log.e(TAG, "Tasas de cambio no disponibles para " + monedaOrigen + " o " + monedaDestino);
        }
    }

    // Interfaz Retrofit para la API
    public interface FloatRatesService {
        @retrofit2.http.GET("daily/mxn.json")
        Call<HashMap<String, ExchangeRate>> getRates();
    }

    // Clase ExchangeRate para el modelo de datos
    public class ExchangeRate {
        private String code;
        private String name;
        private float rate;

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public float getRate() {
            return rate;
        }
    }
}