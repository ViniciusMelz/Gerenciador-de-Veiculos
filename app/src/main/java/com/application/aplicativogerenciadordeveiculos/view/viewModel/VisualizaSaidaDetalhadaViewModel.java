package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Saida;

public class VisualizaSaidaDetalhadaViewModel extends ViewModel {
    private MutableLiveData<Saida> mSaidaSelecionada;

    public VisualizaSaidaDetalhadaViewModel() {
        this.mSaidaSelecionada = new MutableLiveData<>();
    }

    public MutableLiveData<Saida> getmSaidaSelecionada() {
        return mSaidaSelecionada;
    }

    public void setSaidaSelecionada(Saida saida) {
        this.mSaidaSelecionada.postValue(saida);
    }

    public void limpaEstado(){
        this.mSaidaSelecionada = new MutableLiveData<>();
    }
}