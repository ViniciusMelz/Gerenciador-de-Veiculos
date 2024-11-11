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
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentLoginBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentTrocaSenhaBinding;
import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.TrocaSenhaViewModel;

public class TrocaSenhaFragment extends Fragment {

    private TrocaSenhaViewModel mViewModel;
    FragmentTrocaSenhaBinding binding;
    InformacoesViewModel informacoesViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTrocaSenhaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TrocaSenhaViewModel.class);

        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);

        mViewModel.getEmailInserido().observe(getViewLifecycleOwner(), observaEmailRecuperacaoSenha);
        mViewModel.isUsuarioLogado().observe(getViewLifecycleOwner(), observaSeUsuarioEhLogado);

        if(informacoesViewModel.getmUsuarioLogado().getValue() != null){
            mViewModel.setEhUsuarioLogado(true);
        }

        binding.bEnviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validador.validaTexto(binding.etEmailUsuario.getText().toString())) {
                    binding.etEmailUsuario.setError("ERRO: Informe o Email!");
                    binding.etEmailUsuario.requestFocus();
                    return;
                }
                if(!Validador.validaEmail(binding.etEmailUsuario.getText().toString())){
                    binding.etEmailUsuario.setError("ERRO: Informe um email Válido!");
                    binding.etEmailUsuario.requestFocus();
                    return;
                }

                String email = binding.etEmailUsuario.getText().toString();

                mViewModel.enviarEmailTrocaSenha(email);
            }
        });

        binding.tvVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpaCampos();
                Navigation.findNavController(view).navigate(R.id.acao_trocaSenhaFragment_para_loginFragment);
            }
        });
    }

    Observer<String> observaEmailRecuperacaoSenha = new Observer<String>() {
        @Override
        public void onChanged(String email) {
            if (email != null) {
                Toast.makeText(getContext(), "Email enviado com Sucesso!", Toast.LENGTH_LONG).show();
                Navigation.findNavController(requireView()).navigate(R.id.acao_trocaSenhaFragment_para_loginFragment);
            } else {
                String erro = mViewModel.getErroRecuperacaoSenha();
                Toast.makeText(getContext(), "ERRO: " + erro, Toast.LENGTH_LONG).show();
            }

        }
    };

    Observer<Boolean> observaSeUsuarioEhLogado = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean ehUsuarioLogado) {
            if (ehUsuarioLogado) {
                Usuario usuario = informacoesViewModel.getmUsuarioLogado().getValue();
                carregaEmail(usuario);
            }

        }
    };

    public void limpaCampos() {
        binding.etEmailUsuario.setText("");
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

    public void carregaEmail(Usuario usuario) {
        binding.etEmailUsuario.setText(usuario.getEmail());
    }
}