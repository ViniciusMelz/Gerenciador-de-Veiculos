package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.ViewModel;

import androidx.lifecycle.MutableLiveData;

import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class VisualizaSaidasViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Saida>> mlistaSaidas;
    private MutableLiveData<Boolean> mResultado;

    public VisualizaSaidasViewModel() {
        this.mlistaSaidas = new MutableLiveData<>();
        this.mResultado = new MutableLiveData<>();
    }

    public void limpaEstado(){
        this.mlistaSaidas = new MutableLiveData<>();
        this.mResultado = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Saida>> getMlistaSaidas() {
        return mlistaSaidas;
    }

    public void setMlistaSaidas(MutableLiveData<ArrayList<Saida>> mlistaSaidas) {
        this.mlistaSaidas = mlistaSaidas;
    }

    public MutableLiveData<Boolean> getmResultado() {
        return mResultado;
    }

    public void setResultado(Boolean resultado) {
        this.mResultado.postValue(resultado);
    }

    public void excluirSaida(Saida saida){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Saídas").document(saida.getIdSaida()).delete().addOnCompleteListener(task -> {
            sincronizarDadosAposExclusaoSaida(saida);
            mResultado.postValue(true);
        }).addOnFailureListener(e -> {
            mResultado.postValue(false);
        });
    }

    public void adicionarSaidaNaLista(Saida saida) {
        ArrayList<Saida> listaSaidas = getMlistaSaidas().getValue();
        if(saida != null){
            if(listaSaidas != null) {
                listaSaidas.add(saida);
            }else{
                ArrayList<Saida> lista = new ArrayList<>();
                MutableLiveData<ArrayList<Saida>> mutableLiveData = new MutableLiveData();
                mutableLiveData.postValue(lista);
                this.setMlistaSaidas(mutableLiveData);
            }
        }
        mlistaSaidas.setValue(listaSaidas);
    }

    public void removerSaidaDaLista(int posicao) {
        ArrayList<Saida> listaSaidas = getMlistaSaidas().getValue();
        if (listaSaidas != null) {
            Saida saida = listaSaidas.remove(posicao);
            if (saida != null) {
                mlistaSaidas.setValue(listaSaidas);
            }
        }
    }

    public void sincronizarDadosAposExclusaoSaida(Saida saida) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        float valorTotalAtualizado = saida.getVeiculo().getValorTotalSaidas() - saida.getValor();
        db.collection("Veículos").document(saida.getVeiculo().getId()).
                update("valorTotalSaidas", valorTotalAtualizado).addOnCompleteListener(task -> {
                });
    }
}