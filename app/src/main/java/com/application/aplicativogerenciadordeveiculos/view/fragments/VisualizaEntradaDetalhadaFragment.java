package com.application.aplicativogerenciadordeveiculos.view.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaEntradaDetalhadaViewModel;

public class VisualizaEntradaDetalhadaFragment extends Fragment {

    private VisualizaEntradaDetalhadaViewModel mViewModel;

    public static VisualizaEntradaDetalhadaFragment newInstance() {
        return new VisualizaEntradaDetalhadaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visualiza_entrada_detalhada, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VisualizaEntradaDetalhadaViewModel.class);
        // TODO: Use the ViewModel
    }

}