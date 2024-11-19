package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Entrada;
import com.application.aplicativogerenciadordeveiculos.model.Saida;

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

    public void inserirSaida(Saida saida){
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> veiculoMap = new HashMap<>();
        veiculoMap.put("marca", veiculo.getMarca());
        veiculoMap.put("modelo", veiculo.getModelo());
        veiculoMap.put("ano", veiculo.getAno());
        veiculoMap.put("placa", veiculo.getPlaca());
        veiculoMap.put("tipo", veiculo.getTipo());
        veiculoMap.put("email", veiculo.getUsuarioDono().getEmail());
        veiculoMap.put("quilometragem", veiculo.getQuilometragem());
        veiculoMap.put("mediaCombustivel", veiculo.getMediaCombustivel());
        veiculoMap.put("valorTotalSaidas", veiculo.getValorTotalSaidas());
        veiculoMap.put("valorTotalEntradas", veiculo.getValorTotalEntradas());

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
        });*/
    }

    public void atualizarSaida(Saida saida){
        /*
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
                });*/
    }
}