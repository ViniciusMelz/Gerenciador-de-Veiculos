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

    public Veiculo inserirEntrada(Entrada entrada, int quilometragemOriginal){
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
        Veiculo veiculoAtt = new Veiculo();
        veiculoAtt = this.sincronizarDadosAposEntrada(entrada, false, quilometragemOriginal, 0);
        return veiculoAtt;
    }

    public Veiculo atualizarEntrada(Entrada entrada, int quilometragemOriginal, float valorOriginal){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Entradas").document(entrada.getIdEntrada()).
                update("idVeiculo", entrada.getVeiculo().getId(),
                        "tipo", entrada.getTipo(),
                        "valor", entrada.getValor(),
                        "descricao", entrada.getDescricao(),
                        "quilometragem", entrada.getQuilometragem(),
                        "data", entrada.getData()).addOnCompleteListener(task -> {
                    this.mResultado.postValue(true);
                    this.mEntradaEdicao.postValue(null);
                }).addOnFailureListener(e -> {
                    this.mResultado.postValue(false);
                });
        Veiculo veiculoAtt = new Veiculo();
        veiculoAtt = this.sincronizarDadosAposEntrada(entrada, true, quilometragemOriginal, valorOriginal);
        return veiculoAtt;
    }

    public Veiculo sincronizarDadosAposEntrada(Entrada entrada, boolean ehAtualizacao, int quilometragemOriginal, float valorOriginal){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        float valorTotalAtualizado;
        if(ehAtualizacao){
            valorTotalAtualizado = (entrada.getVeiculo().getValorTotalEntradas() - valorOriginal) + entrada.getValor();
        }else{
            valorTotalAtualizado = entrada.getVeiculo().getValorTotalEntradas() + entrada.getValor();
        }
        int quilometragemAtualizada = (entrada.getQuilometragem() > quilometragemOriginal ? entrada.getQuilometragem() : quilometragemOriginal);
        Veiculo veiculo = entrada.getVeiculo();
        veiculo.setQuilometragem(quilometragemAtualizada);
        veiculo.setValorTotalSaidas(valorTotalAtualizado);
        db.collection("Veículos").document(entrada.getVeiculo().getId()).
                update("quilometragem", quilometragemAtualizada,
                        "valorTotalEntradas", valorTotalAtualizado).addOnCompleteListener(task -> {
                });

        return veiculo;
    }
}