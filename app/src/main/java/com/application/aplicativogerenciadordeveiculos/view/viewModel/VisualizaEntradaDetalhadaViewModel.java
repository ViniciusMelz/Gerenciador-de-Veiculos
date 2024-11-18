package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Entrada;
import com.application.aplicativogerenciadordeveiculos.model.Saida;

public class VisualizaEntradaDetalhadaViewModel extends ViewModel {
    private MutableLiveData<Entrada> mEntradaSelecionada;

    public VisualizaEntradaDetalhadaViewModel() {
        this.mEntradaSelecionada = new MutableLiveData<>();
    }

    public MutableLiveData<Entrada> getmEntradaSelecionada() {
        return mEntradaSelecionada;
    }

    public void setEntradaSelecionada(Entrada entrada) {
        this.mEntradaSelecionada.postValue(entrada);
    }

    public void limpaEstado(){
        this.mEntradaSelecionada = new MutableLiveData<>();
    }
}