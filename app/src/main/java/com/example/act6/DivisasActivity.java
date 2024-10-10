package com.example.act6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "DivisasActivity";
    private EditText editTextValor;
    private Spinner spinnerMonedaOrigen, spinnerMonedaDestino;
    private TextView textViewResultado, textViewNombreMonedaOrigen, textViewNombreMonedaDestino;
    private Button btnConvertir, btnRegresar;

    // Arrays con códigos y nombres de monedas
    private String[] codigosMonedas = {"EUR", "USD", "JPY", "BGN", "CZK", "DKK", "GBP", "HUF", "PLN", "RON", "SEK", "CHF", "ISK", "NOK", "HRK", "RUB", "TRY", "AUD", "BRL", "CAD", "CNY", "HKD", "IDR", "ILS", "INR", "KRW", "MXN", "MYR", "NZD", "PHP", "SGD", "THB", "ZAR"};

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
        btnRegresar = findViewById(R.id.btnRegresar);  // Nuevo botón

        // Llenar los spinners con los códigos de monedas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, codigosMonedas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonedaOrigen.setAdapter(adapter);
        spinnerMonedaDestino.setAdapter(adapter);

        // Acción al presionar el botón "Convertir"
        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String monedaOrigen = spinnerMonedaOrigen.getSelectedItem().toString();
                obtenerTasasDeCambio(monedaOrigen);  // Llamar a la API con la moneda de origen seleccionada
            }
        });

        // Acción al presionar el botón "Regresar"
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DivisasActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  // Cierra esta actividad para que no quede en el historial
            }
        });
    }

    // Método para obtener tasas de cambio desde la API
    private void obtenerTasasDeCambio(String monedaOrigen) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://v6.exchangerate-api.com/v6/10a18377c174c005ee31e837/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ExchangeRateService service = retrofit.create(ExchangeRateService.class);
        Call<ExchangeRateResponse> call = service.getRates(monedaOrigen);

        call.enqueue(new Callback<ExchangeRateResponse>() {
            @Override
            public void onResponse(Call<ExchangeRateResponse> call, Response<ExchangeRateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ExchangeRateResponse rates = response.body();

                    // Obtener la tasa de la moneda de destino seleccionada
                    String monedaDestino = spinnerMonedaDestino.getSelectedItem().toString();
                    Float tasaDestino = rates.getConversionRates().get(monedaDestino);

                    if (tasaDestino != null) {
                        Log.d(TAG, "Tasa de cambio recibida: " + tasaDestino);
                        convertirDivisa(tasaDestino);  // Pasar la tasa al método de conversión
                    } else {
                        textViewResultado.setText("Tasa de cambio no disponible para " + monedaDestino);
                        Log.e(TAG, "Tasa de cambio no disponible para " + monedaDestino);
                    }
                } else {
                    Log.e(TAG, "Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ExchangeRateResponse> call, Throwable t) {
                textViewResultado.setText("Error al cargar tasas de cambio.");
                Log.e(TAG, "Error al cargar tasas de cambio: " + t.getMessage());
            }
        });
    }

    // Método para convertir divisa usando la tasa de cambio obtenida
    private void convertirDivisa(float tasaDestino) {
        if (editTextValor.getText().toString().isEmpty()) {
            textViewResultado.setText("Por favor, ingrese un valor.");
            Log.d(TAG, "Valor de entrada vacío.");
            return;
        }

        float valor = Float.parseFloat(editTextValor.getText().toString());

        // Realizar la conversión
        float resultado = valor * tasaDestino;
        textViewResultado.setText("Resultado: " + resultado);
        Log.d(TAG, "Resultado de conversión: " + resultado);
    }

    // Interfaz Retrofit para la API
    public interface ExchangeRateService {
        @retrofit2.http.GET("latest/{monedaOrigen}")
        Call<ExchangeRateResponse> getRates(@retrofit2.http.Path("monedaOrigen") String monedaOrigen);
    }

    // Modelo de respuesta para la API
    public class ExchangeRateResponse {
        private HashMap<String, Float> conversion_rates;

        public HashMap<String, Float> getConversionRates() {
            return conversion_rates;
        }
    }
}
