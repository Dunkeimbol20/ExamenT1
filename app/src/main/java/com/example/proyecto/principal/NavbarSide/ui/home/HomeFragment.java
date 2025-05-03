package com.example.proyecto.principal.NavbarSide.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.proyecto.R;
import com.example.proyecto.principal.Buttom.Category.CategoriaFragment;
import com.example.proyecto.principal.Buttom.Coleccion.ColeccionesFragment;
import com.example.proyecto.principal.Buttom.Novelty.NovedadesFragment;


public class HomeFragment extends Fragment {

    private Button btnCategoria, btnNovedad, btnColeccion;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnCategoria = view.findViewById(R.id.btnCategoria);
        btnNovedad = view.findViewById(R.id.btnNovedad);
        btnColeccion = view.findViewById(R.id.btnColeccion);

        // Fragmento por defecto al cargar
        cargarFragmentInterno(new CategoriaFragment());

        // Manejo de clics
        btnCategoria.setOnClickListener(v -> cargarFragmentInterno(new CategoriaFragment()));
        btnNovedad.setOnClickListener(v -> cargarFragmentInterno(new NovedadesFragment()));
        btnColeccion.setOnClickListener(v -> cargarFragmentInterno(new ColeccionesFragment()));
        btnCategoria.setSelected(true);
        btnNovedad.setSelected(true);
        btnColeccion.setSelected(true);
        return view;
    }

    private void cargarFragmentInterno(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedorInterno, fragment)
                .commit();
    }
}