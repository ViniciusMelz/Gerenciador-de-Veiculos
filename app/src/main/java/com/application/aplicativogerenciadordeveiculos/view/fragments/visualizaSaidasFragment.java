package com.application.aplicativogerenciadordeveiculos.view.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaEntradasBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaRelatorioBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaSaidasBinding;
import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.adapter.SaidaAdapter;
import com.application.aplicativogerenciadordeveiculos.view.adapter.VeiculoAdapter;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaRelatorioViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaSaidasViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

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
        informacoesViewModel.getMlistaSaidas().observe(getViewLifecycleOwner(), observaListaSaidas);
        mViewModel.getmResultado().observe(getViewLifecycleOwner(), observaExclusaoSaida);
        informacoesViewModel.setSaidaSelecionada(null);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String valorTotalSaida = df.format(informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalSaidas());

        binding.tvItemValorTotalSaidas.setText("R$" + valorTotalSaida.replace(".", ","));

        informacoesViewModel.buscaSaidasFirebase();
        if(informacoesViewModel.getMlistaSaidas().getValue() != null){
            mViewModel.setMlistaSaidas(informacoesViewModel.getMlistaSaidas());
        }

        binding.bCadastrarSaidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.acao_visualizaSaidasFragment_para_cadastroSaidaFragment);
            }
        });
    }

    Observer<ArrayList<Saida>> observaListaSaidas = new Observer<ArrayList<Saida>>() {
        @Override
        public void onChanged(ArrayList<Saida> listaSaidas) {
            if (!listaSaidas.isEmpty()) {
                atualizaListagem(listaSaidas);
            }

        }
    };

    Observer<Boolean> observaExclusaoSaida = aBoolean -> {
        if(aBoolean){
            Toast.makeText(getContext(), "Exclusão Realizada com Sucesso!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), "ERRO: Não foi possível excluir o Registro de Saída, Tente Novamente!", Toast.LENGTH_LONG).show();
        }
    };

    SaidaAdapter.SaidaOnClickListener trataCliqueItem = (view, position, saida) -> {
        informacoesViewModel.setSaidaSelecionada(saida);
        Navigation.findNavController(view).navigate(R.id.acao_visualizaSaidasFragment_para_visualizaSaidaDetalhadaFragment);
    };

    SaidaAdapter.SaidaOnClickListener trataCliqueExcluirItem = (view, position, saida) -> {
        AlertDialog.Builder msgConfirmacao = new AlertDialog.Builder(getContext());
        msgConfirmacao.setTitle("Excluir");
        msgConfirmacao.setIcon(R.drawable.ic_excluir);
        msgConfirmacao.setMessage("Você deseja realmente excluir esse registro de saída de valor?" +
                "\nEssa ação não poderá ser revertida!");
        msgConfirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                informacoesViewModel.removerSaidaDaLista(position);
                mViewModel.excluirSaida(saida);
            }
        });
        msgConfirmacao.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Exclusão Cancelada!", Toast.LENGTH_SHORT).show();
            }
        });
        msgConfirmacao.show();
    };

    SaidaAdapter.SaidaOnClickListener trataCliqueEditarItem = (view, position, saida) -> {
        informacoesViewModel.setSaidaSelecionada(saida);
        Navigation.findNavController(view).navigate(R.id.acao_visualizaSaidasFragment_para_visualizaSaidaDetalhadaFragment);
        //TODO
    };

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
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Saídas");
            ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void atualizaListagem(ArrayList<Saida> listaSaidas) {
        if (listaSaidas != null) {
            SaidaAdapter saidaAdapter = new SaidaAdapter(listaSaidas, trataCliqueItem, trataCliqueExcluirItem, trataCliqueEditarItem);
            binding.rvVisualizaSaidas.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvVisualizaSaidas.setItemAnimator(new DefaultItemAnimator());
            binding.rvVisualizaSaidas.setAdapter(saidaAdapter);
        }
    }

}