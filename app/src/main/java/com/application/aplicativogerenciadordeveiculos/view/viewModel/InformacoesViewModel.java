package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;

public class InformacoesViewModel extends ViewModel {
    private MutableLiveData<Usuario> mUsuarioLogado;

    public InformacoesViewModel() {
        this.mUsuarioLogado = new MutableLiveData<>();
    }

    public MutableLiveData<Usuario> getmUsuarioLogado() {
        return mUsuarioLogado;
    }

    public void inicializaUsuarioLogado(Usuario usuario) {
        this.mUsuarioLogado.setValue(usuario);
    }

    public Usuario obtemUsuarioLogado() {
        return this.mUsuarioLogado.getValue();
    }

    public void limpaEstado(){
        mUsuarioLogado = new MutableLiveData<>();
    }
}
