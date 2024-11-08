package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Usuario> mUsuarioLogado;

    public LoginViewModel(){
        this.mUsuarioLogado = new MutableLiveData<>();
    }

    public MutableLiveData<Usuario> getmUsuarioLogado() {
        return mUsuarioLogado;
    }

    public void limpaEstado() {
        mUsuarioLogado = new MutableLiveData<>();
    }

    public void logarUsuario(Usuario usuario) {
        this.mUsuarioLogado.postValue(usuario);
    }
}