package com.example.proyecto.principal.NavbarSide.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.proyecto.R;
import com.example.proyecto.principal.Buttom.Category.CategoriaFragment;
import com.example.proyecto.principal.Buttom.Coleccion.ColeccionesFragment;
import com.example.proyecto.principal.Buttom.Novelty.NovedadesFragment;


public class HomeFragment extends Fragment {

    private Button btnCategoria, btnNovedad, btnColeccion;
    private Button selectedButton = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnCategoria = view.findViewById(R.id.btnCategoria);
        btnNovedad = view.findViewById(R.id.btnNovedad);
        btnColeccion = view.findViewById(R.id.btnColeccion);
        // Fragmento por defecto al cargar

        if (savedInstanceState == null) {
            cargarFragmentInterno(new CategoriaFragment());
            // Establecer el estado inicial del primer botón como seleccionado
            selectedButton = btnCategoria;
            establecerEstadoBoton(selectedButton, true);
        } else {

            if (selectedButton == null) { //
                selectedButton = btnCategoria;
                establecerEstadoBoton(selectedButton, true);
            } else {
                establecerEstadoBoton(selectedButton, true);
            }
        }


        btnCategoria.setOnClickListener(v -> {
            handleButtonClick(btnCategoria);
            cargarFragmentInterno(new CategoriaFragment());
        });

        btnNovedad.setOnClickListener(v -> {
            handleButtonClick(btnNovedad);
            cargarFragmentInterno(new NovedadesFragment());
        });

        btnColeccion.setOnClickListener(v -> {
            handleButtonClick(btnColeccion);
            cargarFragmentInterno(new ColeccionesFragment());
        });

        return view;
    }

    private void handleButtonClick(Button clickedButton) {
        if (selectedButton != null && selectedButton != clickedButton) {
            establecerEstadoBoton(selectedButton, false);
        }

        selectedButton = clickedButton;
        establecerEstadoBoton(selectedButton, true);
    }

    private void establecerEstadoBoton(Button button, boolean seleccionado) {
        if (seleccionado) {
            button.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.color_seleccionado));
            button.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white));
        } else {
            button.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.color_por_defecto));
            button.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.black));
        }
    }


    private void cargarFragmentInterno(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedorInterno, fragment)
                .commit();
    }


}