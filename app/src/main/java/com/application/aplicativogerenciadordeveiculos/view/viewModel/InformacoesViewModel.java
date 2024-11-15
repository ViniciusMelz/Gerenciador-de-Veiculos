package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class InformacoesViewModel extends ViewModel {
    private MutableLiveData<Usuario> mUsuarioLogado;
    private MutableLiveData<ArrayList<Veiculo>> mlistaVeiculos;
    private MutableLiveData<Veiculo> mVeiculoSelecionado;

    public InformacoesViewModel() {
        this.mUsuarioLogado = new MutableLiveData<>();
        this.mlistaVeiculos = new MutableLiveData<>();
        this.mVeiculoSelecionado = new MutableLiveData<>();
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

    public void zerarListaVeiculos(){
        this.mlistaVeiculos = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Veiculo>> getListaVeiculos() {
        return mlistaVeiculos;
    }

    public void setListaVeiculos(MutableLiveData<ArrayList<Veiculo>> listaVeiculos) {
        this.mlistaVeiculos = listaVeiculos;
    }

    public MutableLiveData<Veiculo> getmVeiculoSelecionado() {
        return mVeiculoSelecionado;
    }

    public void setVeiculoSelecionado(Veiculo veiculoSelecionado) {
        this.mVeiculoSelecionado.postValue(veiculoSelecionado);
    }

    public void limpaEstado(){
        mUsuarioLogado = new MutableLiveData<>();
        this.mlistaVeiculos = new MutableLiveData<>();
        this.mVeiculoSelecionado = new MutableLiveData<>();
    }

    public void setArrayListVeiculos(ArrayList<Veiculo> listaVeiculos){
        mlistaVeiculos.postValue(listaVeiculos);
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

    public void zerarVeiculoSelecionado(){
        this.mVeiculoSelecionado = new MutableLiveData<>();
    }

    public void buscarVeiculosFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Veículos").whereEqualTo("email", this.getmUsuarioLogado().getValue().getEmail())
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        ArrayList<Veiculo> listaVeiculos = new ArrayList<>();
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            Veiculo veiculo = doc.toObject(Veiculo.class);
                            listaVeiculos.add(veiculo);
                        }
                        this.setArrayListVeiculos(listaVeiculos);
                    }
                });
    }
}
