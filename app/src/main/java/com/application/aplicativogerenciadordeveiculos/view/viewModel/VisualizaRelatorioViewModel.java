package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Veiculo;

public class VisualizaRelatorioViewModel extends ViewModel {
    private MutableLiveData<Veiculo> mVeiculoSelecionado;

    public VisualizaRelatorioViewModel() {
        this.mVeiculoSelecionado = new MutableLiveData<>();
    }

    public void limpaEstado(){
        this.mVeiculoSelecionado = new MutableLiveData<>();
    }

    public MutableLiveData<Veiculo> getmVeiculoSelecionado() {
        return mVeiculoSelecionado;
    }

    public void setVeiculoSelecionado(Veiculo veiculo) {
        this.mVeiculoSelecionado.postValue(veiculo);
    }
}