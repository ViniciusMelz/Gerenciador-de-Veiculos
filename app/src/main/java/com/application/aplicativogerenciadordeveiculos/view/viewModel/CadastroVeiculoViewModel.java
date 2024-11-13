package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Veiculo;

public class CadastroVeiculoViewModel extends ViewModel {
    private MutableLiveData<Boolean> mResultado;
    private MutableLiveData<Veiculo> mVeiculoEdicao;

    public CadastroVeiculoViewModel() {
        this.mResultado = new MutableLiveData<>();
        this.mVeiculoEdicao = new MutableLiveData<>();
    }

    public void limpaEstado(){
        this.mResultado = new MutableLiveData<>();
        this.mVeiculoEdicao = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getResultado() {
        return mResultado;
    }

    public MutableLiveData<Veiculo> getVeiculoEdicao() {
        return mVeiculoEdicao;
    }

    public void setUsuarioEdicao(Veiculo veiculo) {
        this.mVeiculoEdicao.setValue(veiculo);
    }

    public void inserirVeiculo(Veiculo veiculo){
    }

    public void atualizarVeiculo(Veiculo veiculo){
    }
}