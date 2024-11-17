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
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaSaidaDetalhadaViewModel;

public class VisualizaSaidaDetalhadaFragment extends Fragment {

    private VisualizaSaidaDetalhadaViewModel mViewModel;

    public static VisualizaSaidaDetalhadaFragment newInstance() {
        return new VisualizaSaidaDetalhadaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visualiza_saida_detalhada, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VisualizaSaidaDetalhadaViewModel.class);
        // TODO: Use the ViewModel
    }

}