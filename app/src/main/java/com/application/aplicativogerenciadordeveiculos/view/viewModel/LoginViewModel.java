package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Usuario> mUsuarioLogado;
    private String erroLogin;

    public LoginViewModel(){
        this.mUsuarioLogado = new MutableLiveData<>();
    }

    public String getErroLogin() {
        return erroLogin;
    }

    public void setUsuarioLogado(Usuario usuario) {
        mUsuarioLogado.postValue(usuario);
    }

    public MutableLiveData<Usuario> getmUsuarioLogado() {
        return mUsuarioLogado;
    }

    public void limpaEstado() {
        mUsuarioLogado = new MutableLiveData<>();
        erroLogin = null;
    }

    public void logarUsuario(Usuario usuario) {
        erroLogin = null;
        String email = usuario.getEmail();
        String senha = usuario.getSenha();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mUsuarioLogado.postValue(usuario);
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseNetworkException e) {
                    erroLogin = "Verifique sua conexão com a internet!";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    erroLogin = "Email e/ou senha inválidos!";
                } catch (Exception e) {
                    erroLogin = "Erro ao logar usuário, tente novamente!";
                }
                mUsuarioLogado.postValue(null);
            }
        });
    }
}