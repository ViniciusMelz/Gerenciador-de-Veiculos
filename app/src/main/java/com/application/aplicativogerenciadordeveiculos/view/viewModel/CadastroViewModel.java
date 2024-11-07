package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;

public class CadastroViewModel extends ViewModel {
    private MutableLiveData<Boolean> mResultado;

    public CadastroViewModel(MutableLiveData<Boolean> mResultado) {
        this.mResultado = mResultado;
    }

    public MutableLiveData<Boolean> getmResultado() {
        return mResultado;
    }

    public void inserirUsuario(Usuario usuario) {

    }
}