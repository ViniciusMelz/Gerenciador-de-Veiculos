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
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentMenuPrincipalBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaEntradasBinding;
import com.application.aplicativogerenciadordeveiculos.model.Entrada;
import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.adapter.EntradaAdapter;
import com.application.aplicativogerenciadordeveiculos.view.adapter.SaidaAdapter;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.MenuPrincipalViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaEntradasViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class visualizaEntradasFragment extends Fragment {

    private VisualizaEntradasViewModel mViewModel;
    InformacoesViewModel informacoesViewModel;
    FragmentVisualizaEntradasBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVisualizaEntradasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VisualizaEntradasViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);
        informacoesViewModel.getMlistaEntradas().observe(getViewLifecycleOwner(), observaListaEntradas);
        mViewModel.getmResultado().observe(getViewLifecycleOwner(), observaExclusaoEntrada);
        informacoesViewModel.setEntradaSelecionada(null);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String valorTotalEntrada = df.format(informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalEntradas());

        binding.tvItemValorTotalEntradas.setText("R$" + valorTotalEntrada.replace(".", ","));

        informacoesViewModel.buscaEntradasFirebase();
        if(informacoesViewModel.getMlistaEntradas().getValue() != null){
            mViewModel.setMlistaEntradas(informacoesViewModel.getMlistaEntradas());
        }

        binding.bCadastrarEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.acao_visualizaEntradasFragment_para_cadastroEntradaFragment);
            }
        });
    }

    Observer<ArrayList<Entrada>> observaListaEntradas = new Observer<ArrayList<Entrada>>() {
        @Override
        public void onChanged(ArrayList<Entrada> listaEntradas) {
            if (!listaEntradas.isEmpty()) {
                atualizaListagem(listaEntradas);
            }

        }
    };

    Observer<Boolean> observaExclusaoEntrada = aBoolean -> {
        if(aBoolean){
            Toast.makeText(getContext(), "Exclusão Realizada com Sucesso!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), "ERRO: Não foi possível excluir o Registro de Entrada, Tente Novamente!", Toast.LENGTH_LONG).show();
        }
    };

    EntradaAdapter.EntradaOnClickListener trataCliqueItem = (view, position, entrada) -> {
        informacoesViewModel.setEntradaSelecionada(entrada);
        Navigation.findNavController(view).navigate(R.id.acao_visualizaEntradasFragment_para_visualizaEntradaDetalhadaFragment);
    };

    EntradaAdapter.EntradaOnClickListener trataCliqueExcluirItem = (view, position, entrada) -> {
        AlertDialog.Builder msgConfirmacao = new AlertDialog.Builder(getContext());
        msgConfirmacao.setTitle("Excluir");
        msgConfirmacao.setIcon(R.drawable.ic_excluir);
        msgConfirmacao.setMessage("Você deseja realmente excluir esse registro de entrada de valor?" +
                "\nEssa ação não poderá ser revertida!");
        msgConfirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                informacoesViewModel.removerEntradaDaLista(position);
                mViewModel.excluirEntrada(entrada);
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

    EntradaAdapter.EntradaOnClickListener trataCliqueEditarItem = (view, position, entrada) -> {
        informacoesViewModel.setEntradaSelecionada(entrada);
        Navigation.findNavController(view).navigate(R.id.acao_visualizaEntradasFragment_para_visualizaEntradaDetalhadaFragment);
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
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Entradas");
            ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void atualizaListagem(ArrayList<Entrada> listaEntradas) {
        if (listaEntradas != null) {
            EntradaAdapter entradaAdapter = new EntradaAdapter(listaEntradas, trataCliqueItem, trataCliqueExcluirItem, trataCliqueEditarItem);
            binding.rvVisualizaEntradas.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvVisualizaEntradas.setItemAnimator(new DefaultItemAnimator());
            binding.rvVisualizaEntradas.setAdapter(entradaAdapter);
        }
    }
}