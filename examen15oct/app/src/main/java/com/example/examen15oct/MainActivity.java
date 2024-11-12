package com.example.examen15oct;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configura el user agent para osmdroid
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_main);

        // Inicializar el mapa
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        // Centrar el mapa en las coordenadas iniciales
        GeoPoint startPoint = new GeoPoint(23.692851, -102.560842);
        map.getController().setZoom(5.0);
        map.getController().setCenter(startPoint);

        // Llamada a la API para obtener terremotos y marcarlos en el mapa
        fetchEarthquakes();
    }

    // Método para añadir un marcador al mapa
    private void addMarker(GeoPoint point, String title) {
        Marker marker = new Marker(map);
        marker.setPosition(point);
        marker.setTitle(title);
        map.getOverlays().add(marker);
        map.invalidate();  // Refrescar el mapa
    }

    // Método para hacer la llamada a la API de USGS
    public void fetchEarthquakes() {
        new Thread(() -> {
            try {
                // URL de la API con parámetros de fecha y magnitud
                URL url = new URL("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2024-09-01&endtime=2024-12-31&minmagnitude=4&latitude=23.692851&longitude=-102.560842&maxradiuskm=1650");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                conn.disconnect();

                // Procesar la respuesta JSON
                JSONObject jsonResponse = new JSONObject(content.toString());
                JSONArray features = jsonResponse.getJSONArray("features");

                // Iterar sobre los terremotos y agregar los marcadores
                for (int i = 0; i < features.length(); i++) {
                    JSONObject earthquake = features.getJSONObject(i);
                    JSONObject geometry = earthquake.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");

                    double lon = coordinates.getDouble(0);
                    double lat = coordinates.getDouble(1);
                    String title = earthquake.getJSONObject("properties").getString("title");

                    // Obtener el tiempo en formato epoch y convertirlo a una fecha legible
                    long timeInMillis = earthquake.getJSONObject("properties").getLong("time");
                    String formattedDate = getFormattedDate(timeInMillis);

                    // Concatenar la fecha a la información del título
                    String markerTitle = title + "\nDate: " + formattedDate;

                    // Añadir el marcador al mapa
                    runOnUiThread(() -> addMarker(new GeoPoint(lat, lon), markerTitle));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Método para convertir el timestamp a una fecha legible
    private String getFormattedDate(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(timeInMillis);
        return sdf.format(date);
    }
}
