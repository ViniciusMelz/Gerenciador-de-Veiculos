package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Entrada;
import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class VisualizaEntradasViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Entrada>> mlistaEntradas;
    private MutableLiveData<Boolean> mResultado;

    public VisualizaEntradasViewModel() {
        this.mlistaEntradas = new MutableLiveData<>();
        this.mResultado = new MutableLiveData<>();
    }

    public void limpaEstado(){
        this.mlistaEntradas = new MutableLiveData<>();
        this.mResultado = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Entrada>> getMlistaEntradas() {
        return mlistaEntradas;
    }

    public void setMlistaEntradas(MutableLiveData<ArrayList<Entrada>> mlistaEntradas) {
        this.mlistaEntradas = mlistaEntradas;
    }

    public MutableLiveData<Boolean> getmResultado() {
        return mResultado;
    }

    public void setResultado(Boolean resultado) {
        this.mResultado.postValue(resultado);
    }

    public Veiculo excluirEntrada(Entrada entrada){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Entradas").document(entrada.getIdEntrada()).delete().addOnCompleteListener(task -> {
            mResultado.postValue(true);
        }).addOnFailureListener(e -> {
            mResultado.postValue(false);
        });

        Veiculo veiculoAtt = new Veiculo();
        veiculoAtt = sincronizarDadosAposExclusaoEntrada(entrada);
        return veiculoAtt;
    }

    public void adicionarEntradaNaLista(Entrada entrada) {
        ArrayList<Entrada> listaEntradas = getMlistaEntradas().getValue();
        if(entrada != null){
            if(listaEntradas != null) {
                listaEntradas.add(entrada);
            }else{
                ArrayList<Entrada> lista = new ArrayList<>();
                MutableLiveData<ArrayList<Entrada>> mutableLiveData = new MutableLiveData();
                mutableLiveData.postValue(lista);
                this.setMlistaEntradas(mutableLiveData);
            }
        }
        mlistaEntradas.setValue(listaEntradas);
    }

    public void removerEntradaDaLista(int posicao) {
        ArrayList<Entrada> listaEntradas = getMlistaEntradas().getValue();
        if (listaEntradas != null) {
            Entrada entrada = listaEntradas.remove(posicao);
            if (entrada != null) {
                mlistaEntradas.setValue(listaEntradas);
            }
        }
    }

    public Veiculo sincronizarDadosAposExclusaoEntrada(Entrada entrada) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        float valorTotalAtualizado = entrada.getVeiculo().getValorTotalEntradas() - entrada.getValor();
        db.collection("Veículos").document(entrada.getVeiculo().getId()).
                update("valorTotalEntradas", valorTotalAtualizado).addOnCompleteListener(task -> {
                });

        Veiculo veiculoAtt = entrada.getVeiculo();
        veiculoAtt.setValorTotalEntradas(valorTotalAtualizado);
        return veiculoAtt;
    }

}