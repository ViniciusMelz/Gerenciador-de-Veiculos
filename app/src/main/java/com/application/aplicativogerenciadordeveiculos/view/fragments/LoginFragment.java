package com.application.aplicativogerenciadordeveiculos.view.fragments;


import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentLoginBinding;
import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    private LoginViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        mViewModel.getmUsuarioLogado().observe(getViewLifecycleOwner(), observaAutenticacaoUsuario);

        binding.bLoginEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validador.validaTexto(binding.etEmailUsuario.getText().toString())) {
                    binding.etEmailUsuario.setError("ERRO: Informe o Email!");
                    binding.etEmailUsuario.requestFocus();
                    return;
                }
                if (!Validador.validaTexto(binding.etLoginSenha.getText().toString())) {
                    binding.etLoginSenha.setError("ERRO: Informe a senha!");
                    binding.etLoginSenha.requestFocus();
                    return;
                }

                String email = binding.etEmailUsuario.getText().toString();
                String senha = binding.etLoginSenha.getText().toString();
                Usuario usuario = new Usuario(email, senha);

                mViewModel.logarUsuario(usuario);
            }
        });

        binding.tvLoginCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpaCampos();
                Navigation.findNavController(view).navigate(R.id.acao_loginFragment_para_cadastroFragment);
            }
        });
    }

    Observer<Usuario> observaAutenticacaoUsuario = new Observer<Usuario>() {
        @Override
        public void onChanged(Usuario usuario) {
            if (usuario != null) {
                Navigation.findNavController(requireView()).navigate(R.id.acao_loginFragment_para_menuPrincipalFragment);
            } else {
                String erro = mViewModel.getErroLogin();
                Toast.makeText(getContext(), "ERRO: " + erro, Toast.LENGTH_LONG).show();
            }

        }
    };

    public void limpaCampos() {
        binding.etEmailUsuario.setText("");
        binding.etLoginSenha.setText("");
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
        limpaCampos();
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).escondeBottomNavigation();
            ((MainActivity) requireActivity()).getSupportActionBar().hide();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).mostraBottomNavigation();
            ((MainActivity) requireActivity()).getSupportActionBar().show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if(usuario != null) {
            String email = usuario.getEmail();
            Usuario usuarioLogado = new Usuario(email);
            mViewModel.setUsuarioLogado(usuarioLogado);
        }
    }



}