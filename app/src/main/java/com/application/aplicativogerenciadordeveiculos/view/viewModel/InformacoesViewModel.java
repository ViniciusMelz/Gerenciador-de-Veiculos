package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class InformacoesViewModel extends ViewModel {
    private MutableLiveData<Usuario> mUsuarioLogado;
    private MutableLiveData<ArrayList<Veiculo>> mlistaVeiculos;

    public InformacoesViewModel() {
        this.mUsuarioLogado = new MutableLiveData<>();
        this.mlistaVeiculos = new MutableLiveData<>();
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

    public MutableLiveData<ArrayList<Veiculo>> getListaVeiculos() {
        return mlistaVeiculos;
    }

    public void setListaVeiculos(MutableLiveData<ArrayList<Veiculo>> listaVeiculos) {
        this.mlistaVeiculos = listaVeiculos;
    }

    public void limpaEstado(){
        mUsuarioLogado = new MutableLiveData<>();
        this.mlistaVeiculos = new MutableLiveData<>();
    }

    public void adicionarVeiculosNaLista(Veiculo veiculo) {
        ArrayList<Veiculo> listaVeiculos = getListaVeiculos().getValue();
        if(veiculo != null){
            if(listaVeiculos != null) {
                listaVeiculos.add(veiculo);
            }else{
                ArrayList<Veiculo> lista = new ArrayList<>();
                MutableLiveData<ArrayList<Veiculo>> mutableLiveData = new MutableLiveData();
                mutableLiveData.postValue(lista);
                this.setListaVeiculos(mutableLiveData);
            }
        }
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
}
