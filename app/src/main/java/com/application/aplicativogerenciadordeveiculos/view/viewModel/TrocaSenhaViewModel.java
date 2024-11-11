package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class TrocaSenhaViewModel extends ViewModel {
    private MutableLiveData<String> mEmailRecuperacaoSenha;
    private String erroRecuperacaoSenha;
    private MutableLiveData<Boolean> mEhUsuarioLogado;

    public TrocaSenhaViewModel(){
        this.mEmailRecuperacaoSenha = new MutableLiveData<>();
        this.mEhUsuarioLogado = new MutableLiveData<>();
    }

    public MutableLiveData<String> getEmailInserido() {
        return mEmailRecuperacaoSenha;
    }

    public String getErroRecuperacaoSenha() {
        return erroRecuperacaoSenha;
    }

    public MutableLiveData<Boolean> isUsuarioLogado() {
        return mEhUsuarioLogado;
    }

    public void setEhUsuarioLogado(Boolean ehUsuarioLogado) {
        mEhUsuarioLogado.postValue(ehUsuarioLogado);
    }

    public void limpaEstado() {
        mEmailRecuperacaoSenha = new MutableLiveData<>();
        erroRecuperacaoSenha = null;
        mEhUsuarioLogado = new MutableLiveData<>();
    }

    public void enviarEmailTrocaSenha(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mEmailRecuperacaoSenha.postValue(email);
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseNetworkException e) {
                    erroRecuperacaoSenha = "Verifique sua conexão com a internet!";
                } catch (FirebaseAuthEmailException e) {
                    erroRecuperacaoSenha = "Email Inválido!";
                } catch (FirebaseAuthInvalidUserException e) {
                    erroRecuperacaoSenha = "Email Não Encontrado!";
                } catch (Exception e) {
                    erroRecuperacaoSenha = "Ocorreu um erro ao enviar o email, tente novamente!";
                }
                mEmailRecuperacaoSenha.postValue(null);
            }
        });
    }
}