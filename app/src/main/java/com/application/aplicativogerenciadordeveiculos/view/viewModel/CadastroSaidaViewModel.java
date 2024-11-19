package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class CadastroSaidaViewModel extends ViewModel {
    private MutableLiveData<Boolean> mResultado;
    private MutableLiveData<Saida> mSaidaEdicao;
    private String erroCadastro;
    private int quilometragemOriginal;

    public CadastroSaidaViewModel() {
        this.mResultado = new MutableLiveData<>();
        this.mSaidaEdicao = new MutableLiveData<>();
        erroCadastro = null;
    }

    public void limpaEstado(){
        this.mResultado = new MutableLiveData<>();
        this.mSaidaEdicao = new MutableLiveData<>();
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

    public MutableLiveData<Saida> getSaidaEdicao() {
        return mSaidaEdicao;
    }

    public void setmSaidaEdicao(Saida saida) {
        this.mSaidaEdicao.setValue(saida);
    }

    public void inserirSaida(Saida saida){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> saidaMap = new HashMap<>();
        saidaMap.put("idVeiculo", saida.getVeiculo().getId());
        saidaMap.put("tipo", saida.getTipo());
        saidaMap.put("valor", saida.getValor());
        saidaMap.put("descricao", saida.getDescricao());
        saidaMap.put("quilometragem", saida.getQuilometragem());
        saidaMap.put("data", saida.getData());
        saidaMap.put("litrosAbastecidos", saida.getLitrosAbastecidos());
        saidaMap.put("mediaCombustivel", saida.getMediaCombustivel());

        db.collection("Saídas").document().set(saidaMap).addOnCompleteListener(taskSalvamento -> {
            if(taskSalvamento.isSuccessful()){
                this.sincronizarDadosAposSaida(saida, false);
                mResultado.postValue(true);
            }else{
                try {
                    throw taskSalvamento.getException();
                } catch (Exception e) {
                    erroCadastro = "Erro ao cadastrar saída, tente novamente!";
                }
                mResultado.postValue(false);
            }
        });
    }

    public void atualizarSaida(Saida saida){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Saídas").document(saida.getIdSaida()).
                update("idVeiculo", saida.getVeiculo().getId(),
                        "tipo", saida.getTipo(),
                        "valor", saida.getValor(),
                        "descricao", saida.getDescricao(),
                        "quilometragem", saida.getQuilometragem(),
                        "data", saida.getData(),
                        "litrosAbastecidos", saida.getLitrosAbastecidos(),
                        "mediaCombustivel", saida.getMediaCombustivel()).addOnCompleteListener(task -> {
                    this.sincronizarDadosAposSaida(saida, true);
                    this.mResultado.postValue(true);
                    this.mSaidaEdicao.postValue(null);
                }).addOnFailureListener(e -> {
                    this.mResultado.postValue(false);
                });
    }

    public void sincronizarDadosAposSaida(Saida saida, boolean ehAtualizacao){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Saídas")
                .whereEqualTo("idVeiculo", saida.getVeiculo().getId()).whereEqualTo("tipo", 1)
                .orderBy("quilometragem", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        quilometragemOriginal = queryDocumentSnapshots.getDocuments().get(0).getLong("quilometragem").intValue();
                    }else{
                        quilometragemOriginal = saida.getVeiculo().getQuilometragem();
                    }
                }).addOnFailureListener(e -> {
                    quilometragemOriginal = saida.getVeiculo().getQuilometragem();
                });
        float valorTotalAtualizado;
        if(ehAtualizacao){
            valorTotalAtualizado = (saida.getVeiculo().getValorTotalSaidas() - this.getSaidaEdicao().getValue().getValor()) + saida.getValor();
        }else{
            valorTotalAtualizado = saida.getVeiculo().getValorTotalSaidas() + saida.getValor();
        }

        int quilometragemAtualizada = (saida.getQuilometragem() > quilometragemOriginal ? saida.getQuilometragem() : quilometragemOriginal);
        float mediaCombustivel, mediaAtualizada;
        if(saida.getMediaCombustivel() == 0.0){
            mediaCombustivel = (saida.getQuilometragem() > quilometragemOriginal ? saida.getQuilometragem() - quilometragemOriginal : quilometragemOriginal - saida.getQuilometragem()) / saida.getLitrosAbastecidos();
        }else{
            mediaCombustivel = saida.getMediaCombustivel();
        }

        if(saida.getVeiculo().getMediaCombustivel() != 0.0){
            mediaAtualizada = (mediaCombustivel + saida.getVeiculo().getMediaCombustivel()) / 2;
        }else{
            mediaAtualizada = mediaCombustivel;
        }

        quilometragemAtualizada = (quilometragemAtualizada > saida.getVeiculo().getQuilometragem() ? quilometragemAtualizada : saida.getVeiculo().getQuilometragem());
        db.collection("Veículos").document(saida.getVeiculo().getId()).
                update("quilometragem", quilometragemAtualizada,
                        "valorTotalSaidas", valorTotalAtualizado,
                        "mediaCombustivel", mediaAtualizada).addOnCompleteListener(task -> {
                });
    }
}