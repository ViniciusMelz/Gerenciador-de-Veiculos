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
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaEntradasBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaRelatorioBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaSaidasBinding;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaRelatorioViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaSaidasViewModel;

public class visualizaSaidasFragment extends Fragment {

    private VisualizaSaidasViewModel mViewModel;
    InformacoesViewModel informacoesViewModel;
    FragmentVisualizaSaidasBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVisualizaSaidasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VisualizaSaidasViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        //mViewModel.limpaEstado();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Saídas");
            ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

}