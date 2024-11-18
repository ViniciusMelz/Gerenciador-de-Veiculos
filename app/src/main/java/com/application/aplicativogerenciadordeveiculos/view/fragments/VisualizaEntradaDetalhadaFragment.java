package com.application.aplicativogerenciadordeveiculos.view.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaEntradaDetalhadaBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaSaidaDetalhadaBinding;
import com.application.aplicativogerenciadordeveiculos.model.Entrada;
import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaEntradaDetalhadaViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaSaidaDetalhadaViewModel;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class VisualizaEntradaDetalhadaFragment extends Fragment {

    private VisualizaEntradaDetalhadaViewModel mViewModel;
    InformacoesViewModel informacoesViewModel;
    FragmentVisualizaEntradaDetalhadaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVisualizaEntradaDetalhadaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VisualizaEntradaDetalhadaViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);
        mViewModel.setEntradaSelecionada(informacoesViewModel.getmEntradaSelecionada().getValue());

        Entrada entrada = informacoesViewModel.getmEntradaSelecionada().getValue();
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String valorSaida = df.format(entrada.getValor());

        String data = entrada.getData().toString();
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("EEE MMM dd HH:mm:ss O yyyy").toFormatter(Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(data, inputFormatter);
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormatada = zonedDateTime.format(formatadorData);

        binding.tvItemTipo.setText(entrada.getTipoLiteral());
        binding.tvItemData.setText(dataFormatada);
        binding.tvItemQuilometragem.setText(entrada.getQuilometragem() + " KM");
        binding.tvItemValor.setText("R$" + valorSaida.replace(".",","));
        binding.tvItemDescricao.setText(entrada.getDescricao());

        binding.bVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.limpaEstado();
                informacoesViewModel.setEntradaSelecionada(null);
                Navigation.findNavController(view).navigate(R.id.acao_visualizaEntradaDetalhadaFragment_para_visualizaEntradasFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mViewModel.limpaEstado();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).escondeBottomNavigation();
            ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).mostraBottomNavigation();
        }
    }

}