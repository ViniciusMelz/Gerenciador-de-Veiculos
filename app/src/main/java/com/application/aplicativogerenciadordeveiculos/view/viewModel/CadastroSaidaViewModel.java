package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CadastroSaidaViewModel extends ViewModel {
    private MutableLiveData<Boolean> mResultado;
    private MutableLiveData<Saida> mSaidaEdicao;
    private String erroCadastro;

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

    public Veiculo inserirSaida(Saida saida, int quilometragemOriginal){
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
        Veiculo veiculoAtt = new Veiculo();
        veiculoAtt = this.sincronizarDadosAposSaida(saida, false, quilometragemOriginal, 0);
        return veiculoAtt;
    }

    public Veiculo atualizarSaida(Saida saida, int quilometragemOriginal, float valorOriginal){
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
                    this.mResultado.postValue(true);
                    this.mSaidaEdicao.postValue(null);
                }).addOnFailureListener(e -> {
                    this.mResultado.postValue(false);
                });
        Veiculo veiculoAtt = new Veiculo();
        veiculoAtt = this.sincronizarDadosAposSaida(saida, true, quilometragemOriginal, valorOriginal);
        return veiculoAtt;
    }

    public Veiculo sincronizarDadosAposSaida(Saida saida, boolean ehAtualizacao, int quilometragemOriginal, float valorOriginal){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        float valorTotalAtualizado;
        if(ehAtualizacao){
            valorTotalAtualizado = (saida.getVeiculo().getValorTotalSaidas() - valorOriginal) + saida.getValor();
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

        if(ehAtualizacao){
            mediaAtualizada = saida.getVeiculo().getMediaCombustivel();
        }else if(saida.getVeiculo().getMediaCombustivel() != 0.0 && mediaCombustivel != 0.0){
            mediaAtualizada = (mediaCombustivel + saida.getVeiculo().getMediaCombustivel()) / 2;
        }else{
            mediaAtualizada = mediaCombustivel;
        }
        Veiculo veiculo = saida.getVeiculo();


        if(saida.getTipo() == 1){
            veiculo.setQuilometragem(quilometragemAtualizada);
            veiculo.setValorTotalSaidas(valorTotalAtualizado);
            veiculo.setMediaCombustivel(mediaAtualizada);
            db.collection("Veículos").document(saida.getVeiculo().getId()).
                    update("quilometragem", quilometragemAtualizada,
                            "valorTotalSaidas", valorTotalAtualizado,
                            "mediaCombustivel", mediaAtualizada).addOnCompleteListener(task -> {
                    });
        }else{
            veiculo.setQuilometragem(quilometragemAtualizada);
            veiculo.setValorTotalSaidas(valorTotalAtualizado);
            db.collection("Veículos").document(saida.getVeiculo().getId()).
                    update("quilometragem", quilometragemAtualizada,
                            "valorTotalSaidas", valorTotalAtualizado).addOnCompleteListener(task -> {
                    });
        }

        return veiculo;
    }
}