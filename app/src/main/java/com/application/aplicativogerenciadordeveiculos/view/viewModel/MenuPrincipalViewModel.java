package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Veiculo;

import java.util.List;

public class MenuPrincipalViewModel extends ViewModel {
    private MutableLiveData<List<Veiculo>> listaVeiculos;
    private MutableLiveData<Boolean> mResultado;

    public MenuPrincipalViewModel() {
        this.listaVeiculos = new MutableLiveData<>();
        this.mResultado = new MutableLiveData<>();
    }

    public void limpaEstado(){
        this.listaVeiculos = new MutableLiveData<>();
        this.mResultado = new MutableLiveData<>();
    }

    public MutableLiveData<List<Veiculo>> getListaUsuarios() {
        return listaVeiculos;
    }

    public MutableLiveData<Boolean> getResultado() {
        return mResultado;
    }

    public void excluirVeiculo(Veiculo veiculo){
    }
}