package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;

public class CadastroViewModel extends ViewModel {
    private MutableLiveData<Boolean> mResultado;

    public CadastroViewModel() {
        this.mResultado = new MutableLiveData<>();
    }
    public MutableLiveData<Boolean> getmResultado() {
        return mResultado;
    }

    public void limpaEstado() {
        mResultado = new MutableLiveData<>();
    }
    public void cadastrarUsuario(Usuario usuario) {

    }
}