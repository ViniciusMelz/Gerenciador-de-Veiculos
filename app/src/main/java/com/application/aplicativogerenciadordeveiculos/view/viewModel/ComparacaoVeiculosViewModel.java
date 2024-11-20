package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Veiculo;

import java.util.ArrayList;

public class ComparacaoVeiculosViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Veiculo>> mlistaVeiculos;

    public ComparacaoVeiculosViewModel() {
        this.mlistaVeiculos = new MutableLiveData<>();
    }

    public void limpaEstado(){
        this.mlistaVeiculos = new MutableLiveData<>();
    }

    public void setmListaVeiculos(MutableLiveData<ArrayList<Veiculo>> mlistaVeiculos) {
        this.mlistaVeiculos = mlistaVeiculos;
    }

    public MutableLiveData<ArrayList<Veiculo>> getListaVeiculos() {
        return mlistaVeiculos;
    }
}