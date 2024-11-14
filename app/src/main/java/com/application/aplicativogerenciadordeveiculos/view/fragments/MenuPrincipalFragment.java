package com.application.aplicativogerenciadordeveiculos.view.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

        binding.bAdicionarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.acao_menuPrincipalFragment_para_cadastroVeiculoFragment);
            }
        });
    }

    Observer<ArrayList<Veiculo>> observaListaVeiculos = veiculos -> {
        atualizaListagem(veiculos);
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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Veículos").whereEqualTo("email", informacoesViewModel.getmUsuarioLogado().getValue().getEmail())
                .get().addOnCompleteListener(task -> {
                    if(!task.getResult().isEmpty()){
                        String emailDono, marca, modelo, placa;
                        int tipo, ano;
                        for(QueryDocumentSnapshot document : task.getResult()){
                            marca = document.getString("marca");
                            modelo = document.getString("modelo");
                            ano = document.getLong("ano").intValue();
                            placa = document.getString("placa");
                            tipo = document.getLong("tipo").intValue();
                            emailDono = document.getString("email");
                            Veiculo veiculo = new Veiculo(marca, modelo, ano, placa, tipo, emailDono);
                            informacoesViewModel.adicionarVeiculosNaLista(veiculo);
                        }
                    }else{
                        Toast.makeText(getContext(), "Nenhum veículo encontrado, cadastre um veículo!", Toast.LENGTH_LONG).show();
                    }
                });
        mViewModel.setMlistaVeiculos(informacoesViewModel.getListaVeiculos());
    }

    public void atualizaListagem(ArrayList<Veiculo> listaVeiculos) {
        if (listaVeiculos != null) {
            VeiculoAdapter veiculoAdapter = new VeiculoAdapter(listaVeiculos);
            binding.rvVisualizaVeiculos.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvVisualizaVeiculos.setItemAnimator(new DefaultItemAnimator());
            binding.rvVisualizaVeiculos.setAdapter(veiculoAdapter);
        }
    }
}