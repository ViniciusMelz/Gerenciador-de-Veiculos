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
import android.widget.Toolbar;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentCadastroBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentMenuPrincipalBinding;
import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.adapter.VeiculoAdapter;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.MenuPrincipalViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalFragment extends Fragment {

    private MenuPrincipalViewModel mViewModel;
    FragmentMenuPrincipalBinding binding;
    InformacoesViewModel informacoesViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuPrincipalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MenuPrincipalViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);
        informacoesViewModel.getListaVeiculos().observe(getViewLifecycleOwner(), observaListaVeiculos);
        mViewModel.getmResultado().observe(getViewLifecycleOwner(), observaExclusaoVeiculos);

        informacoesViewModel.buscarVeiculosFirebase();
        if(informacoesViewModel.getListaVeiculos().getValue() != null){
            mViewModel.setmListaVeiculos(informacoesViewModel.getListaVeiculos());
        }

        binding.bAdicionarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.acao_menuPrincipalFragment_para_cadastroVeiculoFragment);
            }
        });
    }

    Observer<ArrayList<Veiculo>> observaListaVeiculos = new Observer<ArrayList<Veiculo>>() {
        @Override
        public void onChanged(ArrayList<Veiculo> listaVeiculos) {
            if (!listaVeiculos.isEmpty()) {
                atualizaListagem(listaVeiculos);
            }

        }
    };

    Observer<Boolean> observaExclusaoVeiculos = aBoolean -> {
        if(aBoolean){
            Toast.makeText(getContext(), "Exclusão Realizada com Sucesso!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), "ERRO: Não foi possível excluir o Veículo, Tente Novamente!", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mViewModel.limpaEstado();
    }

    VeiculoAdapter.VeiculoOnClickListener trataCliqueItem = (view, position, veiculo) -> {
        informacoesViewModel.setVeiculoSelecionado(veiculo);
        Navigation.findNavController(view).navigate(R.id.acao_menuPrincipalFragment_para_visualizaRelatorioFragment3);
    };

    VeiculoAdapter.VeiculoOnClickListener trataCliqueExcluirItem = (view, position, veiculo) -> {
        AlertDialog.Builder msgConfirmacao = new AlertDialog.Builder(getContext());
        msgConfirmacao.setTitle("Excluir");
        msgConfirmacao.setIcon(R.drawable.ic_excluir);
        msgConfirmacao.setMessage("Você deseja realmente excluir o veículo " + veiculo.getMarca() + " " + veiculo.getModelo() + "?" +
                "\nEssa ação não poderá ser revertida!");
        msgConfirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                informacoesViewModel.removerVeiculoDaLista(position);
                mViewModel.excluirVeiculo(veiculo);
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

    VeiculoAdapter.VeiculoOnClickListener trataCliqueEditarItem = (view, position, veiculo) -> {
        informacoesViewModel.setVeiculoSelecionado(veiculo);
        Navigation.findNavController(view).navigate(R.id.acao_menuPrincipalFragment_para_cadastroVeiculoFragment);
    };

    @Override
    public void onResume() {
        super.onResume();
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).escondeBottomNavigation();
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Seus veículos");
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

    public void atualizaListagem(ArrayList<Veiculo> listaVeiculos) {
        if (listaVeiculos != null) {
            VeiculoAdapter veiculoAdapter = new VeiculoAdapter(listaVeiculos, trataCliqueItem, trataCliqueExcluirItem, trataCliqueEditarItem);
            binding.rvVisualizaVeiculos.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvVisualizaVeiculos.setItemAnimator(new DefaultItemAnimator());
            binding.rvVisualizaVeiculos.setAdapter(veiculoAdapter);
        }
    }
}