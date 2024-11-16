package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroVeiculoViewModel extends ViewModel {
    private MutableLiveData<Boolean> mResultado;
    private MutableLiveData<Veiculo> mVeiculoEdicao;
    private String erroCadastro;

    public CadastroVeiculoViewModel() {
        this.mResultado = new MutableLiveData<>();
        this.mVeiculoEdicao = new MutableLiveData<>();
        erroCadastro = null;
    }

    public void limpaEstado(){
        this.mResultado = new MutableLiveData<>();
        this.mVeiculoEdicao = new MutableLiveData<>();
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

    public MutableLiveData<Veiculo> getVeiculoEdicao() {
        return mVeiculoEdicao;
    }

    public void setmVeiculoEdicao(Veiculo veiculo) {
        this.mVeiculoEdicao.setValue(veiculo);
    }

    public void inserirVeiculo(Veiculo veiculo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> veiculoMap = new HashMap<>();
        veiculoMap.put("marca", veiculo.getMarca());
        veiculoMap.put("modelo", veiculo.getModelo());
        veiculoMap.put("ano", veiculo.getAno());
        veiculoMap.put("placa", veiculo.getPlaca());
        veiculoMap.put("tipo", veiculo.getTipo());
        veiculoMap.put("email", veiculo.getUsuarioDono().getEmail());
        veiculoMap.put("quilometragem", veiculo.getQuilometragem());

        db.collection("Veículos").document().set(veiculoMap).addOnCompleteListener(taskSalvamento -> {
            if(taskSalvamento.isSuccessful()){
                mResultado.postValue(true);
            }else{
                try {
                    throw taskSalvamento.getException();
                } catch (Exception e) {
                    erroCadastro = "Erro ao cadastrar veículo, tente novamente!";
                }
                mResultado.postValue(false);
            }
        });
    }

    public void atualizarVeiculo(Veiculo veiculo){
        //metodo para atualizar veiculo no firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Veículos").document(veiculo.getId()).
                update("marca", veiculo.getMarca(),
                        "modelo", veiculo.getModelo(),
                        "ano", veiculo.getAno(),
                        "placa", veiculo.getPlaca(),
                        "tipo", veiculo.getTipo(),
                        "email", veiculo.getUsuarioDono().getEmail(),
                        "quilometragem", veiculo.getQuilometragem()).addOnCompleteListener(task -> {
                   this.mResultado.postValue(true);
                   this.mVeiculoEdicao.postValue(null);
                }).addOnFailureListener(e -> {
                   this.mResultado.postValue(false);
                });
    }
}