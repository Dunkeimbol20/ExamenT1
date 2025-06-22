package com.example.proyecto.principal.NavbarSide.ui.perfil.Principal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto.R;


public class Perfil_PrincipalFragment extends Fragment {

    public Perfil_PrincipalFragment() {
    }
    public static Perfil_PrincipalFragment newInstance(String param1, String param2) {
        Perfil_PrincipalFragment fragment = new Perfil_PrincipalFragment();
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
        return inflater.inflate(R.layout.fragment_perfil__principal, container, false);
    }
}