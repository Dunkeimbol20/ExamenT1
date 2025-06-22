package com.example.proyecto.data.remote.client;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Actualizado con tu subdominio y una supuesta carpeta /api/
    public static final String BASE_URL = "https://lightskyblue-cod-920464.hostingersite.com/api/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static com.example.proyecto.data.remote.api.ApiService getApiService() {
        return getClient().create(com.example.proyecto.data.remote.api.ApiService.class);
    }
}