package com.application.aplicativogerenciadordeveiculos.view.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.Utils.Validador;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentCadastroBinding;
import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.CadastroViewModel;

public class CadastroFragment extends Fragment {

    private CadastroViewModel mViewModel;
    FragmentCadastroBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCadastroBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CadastroViewModel.class);

        mViewModel.getResultado().observe(getViewLifecycleOwner(), observaCadastroUsuario);
        binding.bCadastroSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validador.validaTexto(binding.etCadastroUsuarioNome.getText().toString())) {
                    binding.etCadastroUsuarioNome.setError("ERRO: Informe o nome!");
                    binding.etCadastroUsuarioNome.requestFocus();
                    return;
                }
                if (!Validador.validaEmail(binding.etCadastroUsuarioEmail.getText().toString())) {
                    binding.etCadastroUsuarioEmail.setError("ERRO: Informe um email válido!");
                    binding.etCadastroUsuarioEmail.requestFocus();
                    return;
                }
                if (!Validador.validaTexto(binding.etCadastroUsuarioSenha.getText().toString())) {
                    binding.etCadastroUsuarioSenha.setError("ERRO: Informe uma senha!");
                    binding.etCadastroUsuarioSenha.requestFocus();
                    return;
                }
                if (!Validador.validaSenha(binding.etCadastroUsuarioSenha.getText().toString())) {
                    binding.etCadastroUsuarioSenha.setError("ERRO: A senha deve conter no mínimo 6 caracteres!");
                    binding.etCadastroUsuarioSenha.requestFocus();
                    return;
                }
                if (!Validador.validaTexto(binding.etCadastroUsuarioSenhaRepeticao.getText().toString())) {
                    binding.etCadastroUsuarioSenha.setError("ERRO: Informe a repetição da senha!");
                    binding.etCadastroUsuarioSenha.requestFocus();
                    return;
                }
                if (!binding.etCadastroUsuarioSenha.getText().toString().equals(binding.etCadastroUsuarioSenhaRepeticao.getText().toString())) {
                    binding.etCadastroUsuarioSenhaRepeticao.setError("ERRO: As senhas não são iguais!");
                    binding.etCadastroUsuarioSenhaRepeticao.requestFocus();
                    return;
                }

                String nome = binding.etCadastroUsuarioNome.getText().toString();
                String email = binding.etCadastroUsuarioEmail.getText().toString();
                String senha = binding.etCadastroUsuarioSenha.getText().toString();
                Usuario usuario = new Usuario(nome, email, senha);

                mViewModel.cadastrarUsuario(usuario);
            }
        });

        binding.tvJaTemConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireView()).popBackStack();
                limpaCampos();
            }
        });
    }


    Observer<Boolean> observaCadastroUsuario = aBoolean -> {
        if (aBoolean) {
            Toast.makeText(getContext(), "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();
            limpaCampos();
            Navigation.findNavController(requireView()).popBackStack();
        } else {
            String erro = mViewModel.getErroCadastro();
            if(erro.equals("Digite uma senha com no mínimo 6 caracteres!")){
                binding.etCadastroUsuarioSenha.setError("ERRO: " + erro);
                binding.etCadastroUsuarioSenha.requestFocus();
            }else if(erro.equals("Digite um email válido!") || erro.equals("Email já em uso, escolha outro!")){
                binding.etCadastroUsuarioEmail.setError("ERRO: " + erro);
                binding.etCadastroUsuarioEmail.requestFocus();
            }else{
                Toast.makeText(getContext(), "ERRO: " + erro, Toast.LENGTH_LONG).show();
            }
        }
    };

    public void limpaCampos() {
        binding.etCadastroUsuarioNome.setText("");
        binding.etCadastroUsuarioEmail.setText("");
        binding.etCadastroUsuarioSenha.setText("");
        binding.etCadastroUsuarioSenhaRepeticao.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        // limpando os campos na tela quando "volta"
        limpaCampos();
        // escondendo a ToolBar e BottomNavigation
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).escondeBottomNavigation();
            ((MainActivity) requireActivity()).getSupportActionBar().hide();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // mostrando a ToolBar e BottomNavigation
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).mostraBottomNavigation();
            ((MainActivity) requireActivity()).getSupportActionBar().show();
        }
    }
}