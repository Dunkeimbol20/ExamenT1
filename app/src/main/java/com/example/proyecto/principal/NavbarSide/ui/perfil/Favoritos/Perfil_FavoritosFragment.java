package com.example.proyecto.principal.NavbarSide.ui.perfil.Favoritos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto.R;


public class Perfil_FavoritosFragment extends Fragment {

    public Perfil_FavoritosFragment() {
    }


    public static Perfil_FavoritosFragment newInstance(String param1, String param2) {
        Perfil_FavoritosFragment fragment = new Perfil_FavoritosFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil__favoritos, container, false);
    }
}