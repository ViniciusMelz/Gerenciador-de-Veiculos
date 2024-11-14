package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Veiculo>> mlistaVeiculos;
    private MutableLiveData<Boolean> mResultado;

    public MenuPrincipalViewModel() {
        this.mlistaVeiculos = new MutableLiveData<>();
        this.mResultado = new MutableLiveData<>();
    }

    public void limpaEstado(){
        this.mlistaVeiculos = new MutableLiveData<>();
        this.mResultado = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Veiculo>> getListaUsuarios() {
        return mlistaVeiculos;
    }

    public MutableLiveData<Boolean> getResultado() {
        return mResultado;
    }

    public void excluirVeiculo(Veiculo veiculo){
    }

    public void setMlistaVeiculos(MutableLiveData<ArrayList<Veiculo>> mlistaVeiculos) {
        this.mlistaVeiculos = mlistaVeiculos;
    }

    public MutableLiveData<ArrayList<Veiculo>> getListaVeiculos() {
        return mlistaVeiculos;
    }

    public void adicionarVeiculosNaLista(Veiculo veiculo) {
        ArrayList<Veiculo> listaVeiculos = getListaVeiculos().getValue();
        listaVeiculos.add(veiculo);
        mlistaVeiculos.setValue(listaVeiculos);
    }

    public void removerVeiculoDaLista(int posicao) {
        ArrayList<Veiculo> listaVeiculos = getListaVeiculos().getValue();
        if (listaVeiculos != null) {
            Veiculo veiculo = listaVeiculos.remove(posicao);
            if (veiculo != null) {
                mlistaVeiculos.setValue(listaVeiculos);
            }
        }
    }

    public MutableLiveData<Boolean> getmResultado() {
        return mResultado;
    }

    public void setmResultado(MutableLiveData<Boolean> mResultado) {
        this.mResultado = mResultado;
    }
}