package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Entrada;
import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CadastroEntradaViewModel extends ViewModel {
    private MutableLiveData<Boolean> mResultado;
    private MutableLiveData<Entrada> mEntradaEdicao;
    private String erroCadastro;

    public CadastroEntradaViewModel() {
        this.mResultado = new MutableLiveData<>();
        this.mEntradaEdicao = new MutableLiveData<>();
        erroCadastro = null;
    }

    public void limpaEstado(){
        this.mResultado = new MutableLiveData<>();
        this.mEntradaEdicao = new MutableLiveData<>();
        erroCadastro = null;
    }

    public String getErroCadastro() {
        return erroCadastro;
    }

    public void setErroCadastro(String erroCadastro) {
        this.erroCadastro = erroCadastro;
    }

    public MutableLiveData<Boolean> getResultado() {
        return mResultado;
    }

    public MutableLiveData<Entrada> getEntradaEdicao() {
        return mEntradaEdicao;
    }

    public void setmEntradaEdicao(Entrada entrada) {
        this.mEntradaEdicao.setValue(entrada);
    }

    public void inserirEntrada(Entrada entrada){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> entradaMap = new HashMap<>();
        entradaMap.put("idVeiculo", entrada.getVeiculo().getId());
        entradaMap.put("tipo", entrada.getTipo());
        entradaMap.put("valor", entrada.getValor());
        entradaMap.put("descricao", entrada.getDescricao());
        entradaMap.put("quilometragem", entrada.getQuilometragem());
        entradaMap.put("data", entrada.getData());

        db.collection("Entradas").document().set(entradaMap).addOnCompleteListener(taskSalvamento -> {
            if(taskSalvamento.isSuccessful()){
                this.sincronizarDadosAposEntrada(entrada, false);
                mResultado.postValue(true);
            }else{
                try {
                    throw taskSalvamento.getException();
                } catch (Exception e) {
                    erroCadastro = "Erro ao cadastrar entrada, tente novamente!";
                }
                mResultado.postValue(false);
            }
        });
    }

    public void atualizarEntrada(Entrada entrada){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Entradas").document(entrada.getIdEntrada()).
                update("idVeiculo", entrada.getVeiculo().getId(),
                        "tipo", entrada.getTipo(),
                        "valor", entrada.getValor(),
                        "descricao", entrada.getDescricao(),
                        "quilometragem", entrada.getQuilometragem(),
                        "data", entrada.getData()).addOnCompleteListener(task -> {
                    this.sincronizarDadosAposEntrada(entrada, true);
                    this.mResultado.postValue(true);
                    this.mEntradaEdicao.postValue(null);
                }).addOnFailureListener(e -> {
                    this.mResultado.postValue(false);
                });
    }

    public void sincronizarDadosAposEntrada(Entrada entrada, boolean atualizacao){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int quilometragemAtualizada = (entrada.getQuilometragem() > entrada.getVeiculo().getQuilometragem() ? entrada.getQuilometragem() : entrada.getVeiculo().getQuilometragem());
        float valorTotalAtualizado;
        if(atualizacao){
            valorTotalAtualizado = (entrada.getVeiculo().getValorTotalEntradas() - this.getEntradaEdicao().getValue().getValor()) + entrada.getValor();
        }else{
            valorTotalAtualizado = entrada.getVeiculo().getValorTotalEntradas() + entrada.getValor();
        }
        db.collection("Veículos").document(entrada.getVeiculo().getId()).
                update("quilometragem", quilometragemAtualizada,
                        "valorTotalEntradas", valorTotalAtualizado).addOnCompleteListener(task -> {
                });
    }
}