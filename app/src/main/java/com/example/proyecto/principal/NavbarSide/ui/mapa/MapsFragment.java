package com.example.proyecto.principal.NavbarSide.ui.mapa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyecto.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final String API_KEY = "AIzaSyCmmPWxATKdEsKWRngB93_yPCp6ZUn4FoE";

    private final LatLng[] zonasLima = new LatLng[]{
            new LatLng(-12.0464, -77.0428), // Lima Centro
            new LatLng(-12.1210, -77.0305), // Surco
            new LatLng(-12.0611, -76.9347), // La Molina
            new LatLng(-11.9392, -77.0651), // Callao
            new LatLng(-12.0860, -76.9750), // SJM
            new LatLng(-12.0500, -77.1000), // San Miguel
            new LatLng(-12.0336, -76.9405), // Ate
            new LatLng(-12.0268, -77.0717),  // Magdalena
            new LatLng(-11.9324, -77.0727), //Comas
            new LatLng(-11.9891,  -77.0876),//Independencia
    };
    public MapsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), API_KEY);
        }

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Iniciar en Centro de Lima
        LatLng limaCentro = new LatLng(-12.0464, -77.0428);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(limaCentro, 11));

        // Buscar restaurantes en cada zona
        for (LatLng zona : zonasLima) {
            buscarRestaurantes(zona);
        }
    }

    private void buscarRestaurantes(LatLng ubicacion) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + ubicacion.latitude + "," + ubicacion.longitude +
                "&radius=2000" +  //
                "&type=restaurant" +
                "&key=" + API_KEY;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray resultados = response.getJSONArray("results");

                    for (int i = 0; i < resultados.length(); i++) {
                        JSONObject lugar = resultados.getJSONObject(i);
                        JSONObject location = lugar.getJSONObject("geometry").getJSONObject("location");
                        double lat = location.getDouble("lat");
                        double lng = location.getDouble("lng");
                        String nombre = lugar.getString("name").toLowerCase();

                        LatLng posicion = new LatLng(lat, lng);

                        boolean esComidaRapida =
                                nombre.contains("kfc") ||
                                        nombre.contains("burger") ||
                                        nombre.contains("pollo") ||
                                        nombre.contains("pizza") ||
                                        nombre.contains("fast") ||
                                        nombre.contains("taco");

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(posicion)
                                .title(nombre);

                        if (esComidaRapida) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                        } else {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        }

                        mMap.addMarker(markerOptions);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
            }
        });
    }

}
