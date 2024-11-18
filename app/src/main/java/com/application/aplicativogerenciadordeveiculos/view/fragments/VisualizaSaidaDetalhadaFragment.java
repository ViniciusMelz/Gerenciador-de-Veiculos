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
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaRelatorioBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaSaidaDetalhadaBinding;
import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaRelatorioViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaSaidaDetalhadaViewModel;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class VisualizaSaidaDetalhadaFragment extends Fragment {

    private VisualizaSaidaDetalhadaViewModel mViewModel;
    InformacoesViewModel informacoesViewModel;
    FragmentVisualizaSaidaDetalhadaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVisualizaSaidaDetalhadaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VisualizaSaidaDetalhadaViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);
        mViewModel.setSaidaSelecionada(informacoesViewModel.getmSaidaSelecionada().getValue());

        Saida saida = informacoesViewModel.getmSaidaSelecionada().getValue();
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String valorSaida = df.format(saida.getValor());
        String valorMedia = "";
        if(saida.getMediaCombustivel() == 0.0){
            valorMedia = df.format(saida.getMediaCombustivel());
        }

        String data = saida.getData().toString();
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("EEE MMM dd HH:mm:ss O yyyy").toFormatter(Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(data, inputFormatter);
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormatada = zonedDateTime.format(formatadorData);

        binding.tvItemTipo.setText(saida.getTipoLiteral());
        binding.tvItemData.setText(dataFormatada);
        binding.tvItemQuilometragem.setText(saida.getQuilometragem() + " KM");
        binding.tvItemValor.setText("R$" + valorSaida.replace(".",","));
        binding.tvItemDescricao.setText(saida.getDescricao());
        if(saida.getTipo() == 1){
            binding.tvItemLitros.setText(String.valueOf(saida.getLitrosAbastecidos()).replace(".",",") + " L");
            if(saida.getMediaCombustivel() == 0.0){
                binding.tvItemMedia.setText(valorMedia.replace(".",",") + " KM/L");
            }else{
                binding.tvItemMedia.setText("Não Informado!");
            }
        }else{
            binding.tvItemLitros.setText("");
            binding.tvItemMedia.setText("");
            binding.tvLabelLitros.setText("");
            binding.tvLabelMedia.setText("");
        }

        binding.bVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.limpaEstado();
                informacoesViewModel.setSaidaSelecionada(null);
                Navigation.findNavController(view).navigate(R.id.acao_visualizaSaidaDetalhadaFragment_para_visualizaSaidasFragment);
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