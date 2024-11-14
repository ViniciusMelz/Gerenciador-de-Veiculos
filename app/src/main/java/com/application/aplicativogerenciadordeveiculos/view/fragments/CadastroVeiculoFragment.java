package com.application.aplicativogerenciadordeveiculos.view.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.Utils.Validador;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentCadastroVeiculoBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentLoginBinding;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.CadastroVeiculoViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroVeiculoFragment extends Fragment {

    FragmentCadastroVeiculoBinding binding;

    InformacoesViewModel informacoesViewModel;
    private CadastroVeiculoViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCadastroVeiculoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CadastroVeiculoViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);

        mViewModel.getResultado().observe(getViewLifecycleOwner(), observaCadastroVeiculo);

        Bundle args = getArguments();
        if (args != null) {
            mViewModel.setmVeiculoEdicao((Veiculo) args.getSerializable("veiculo"));
            carregaVeiculoEdicao();
        } else mViewModel.setmVeiculoEdicao(null);

        binding.bCadastrarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.spCadastroTipo.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "ERRO: Informe o tipo do veículo!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Validador.validaTexto(binding.etCadastroMarca.getText().toString().trim())) {
                    binding.etCadastroMarca.setError("ERRO: Informe a Marca!");
                    binding.etCadastroMarca.requestFocus();
                    return;
                }
                if (!Validador.validaTexto(binding.etCadastroModelo.getText().toString().trim())) {
                    binding.etCadastroModelo.setError("ERRO: Informe o Modelo!");
                    binding.etCadastroModelo.requestFocus();
                    return;
                }
                if (!Validador.validaTexto(binding.etCadastroAno.getText().toString().trim())) {
                    binding.etCadastroAno.setError("ERRO: Informe o Ano!");
                    binding.etCadastroAno.requestFocus();
                    return;
                }

                if (!Validador.validaTexto(binding.etCadastroPlaca.getText().toString().trim())) {
                    binding.etCadastroPlaca.setError("ERRO: Informe a Placa!");
                    binding.etCadastroPlaca.requestFocus();
                    return;
                }

                int tipo = binding.spCadastroTipo.getSelectedItemPosition();
                String marca = binding.etCadastroMarca.getText().toString();
                String modelo = binding.etCadastroModelo.getText().toString();
                int ano = Integer.parseInt(binding.etCadastroAno.getText().toString());
                String placa = binding.etCadastroPlaca.getText().toString();

                Veiculo veiculo = null;
                if (mViewModel.getVeiculoEdicao().getValue() == null) {
                    veiculo = new Veiculo(marca, modelo, ano, placa, tipo, informacoesViewModel.getmUsuarioLogado().getValue().getEmail());
                    mViewModel.inserirVeiculo(veiculo);
                    informacoesViewModel.adicionarVeiculosNaLista(veiculo);
                    limpaCampos();
                } else {
                    veiculo = mViewModel.getVeiculoEdicao().getValue();
                    veiculo.setMarca(marca);
                    veiculo.setModelo(modelo);
                    veiculo.setAno(ano);
                    veiculo.setPlaca(placa);
                    veiculo.setTipo(tipo);

                    mViewModel.atualizarVeiculo(veiculo);

                    Navigation.findNavController(view).popBackStack();
                }
            }
        });

        binding.bVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpaCampos();
                Navigation.findNavController(view).navigate(R.id.acao_cadastroVeiculoFragment_para_menuPrincipalFragment);
            }
        });
    }

    Observer<Boolean> observaCadastroVeiculo = aBoolean -> {
        if (aBoolean) {
            Toast.makeText(getContext(), "Veículo cadastrado com sucesso", Toast.LENGTH_LONG).show();
            limpaCampos();
            Navigation.findNavController(requireView()).popBackStack();
        } else {
            String erro = mViewModel.getErroCadastro();
            Toast.makeText(getContext(), "ERRO: " + erro, Toast.LENGTH_LONG).show();
        }
    };

    public void limpaCampos() {
        binding.spCadastroTipo.setSelection(0);
        binding.etCadastroMarca.setText("");
        binding.etCadastroModelo.setText("");
        binding.etCadastroAno.setText("");
        binding.etCadastroPlaca.setText("");
    }

    public void carregaVeiculoEdicao() {
        binding.etCadastroMarca.setText(mViewModel.getVeiculoEdicao().getValue().getMarca());
        binding.etCadastroModelo.setText(mViewModel.getVeiculoEdicao().getValue().getModelo());
        binding.etCadastroAno.setText(String.valueOf(mViewModel.getVeiculoEdicao().getValue().getAno()));
        binding.etCadastroPlaca.setText(mViewModel.getVeiculoEdicao().getValue().getPlaca());
        binding.spCadastroTipo.setSelection(mViewModel.getVeiculoEdicao().getValue().getTipo());
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
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Novo Veículo");
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