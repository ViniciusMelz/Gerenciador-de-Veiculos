package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Entrada;
import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InformacoesViewModel extends ViewModel {
    private MutableLiveData<Usuario> mUsuarioLogado;
    private MutableLiveData<ArrayList<Veiculo>> mlistaVeiculos;
    private MutableLiveData<Veiculo> mVeiculoSelecionado;
    private MutableLiveData<Entrada> mEntradaSelecionada;
    private MutableLiveData<Saida> mSaidaSelecionada;
    private MutableLiveData<ArrayList<Entrada>> mlistaEntradas;
    private MutableLiveData<ArrayList<Saida>> mlistaSaidas;

    public InformacoesViewModel() {
        this.mUsuarioLogado = new MutableLiveData<>();
        this.mlistaVeiculos = new MutableLiveData<>();
        this.mVeiculoSelecionado = new MutableLiveData<>();
        this.mEntradaSelecionada = new MutableLiveData<>();
        this.mSaidaSelecionada = new MutableLiveData<>();
        this.mlistaEntradas = new MutableLiveData<>();
        this.mlistaSaidas = new MutableLiveData<>();
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

    public MutableLiveData<ArrayList<Entrada>> getMlistaEntradas() {
        return mlistaEntradas;
    }

    public void setMlistaEntradas(MutableLiveData<ArrayList<Entrada>> mlistaEntradas) {
        this.mlistaEntradas = mlistaEntradas;
    }

    public MutableLiveData<ArrayList<Saida>> getMlistaSaidas() {
        return mlistaSaidas;
    }

    public void setMlistaSaidas(MutableLiveData<ArrayList<Saida>> mlistaSaidas) {
        this.mlistaSaidas = mlistaSaidas;
    }

    public MutableLiveData<Entrada> getmEntradaSelecionada() {
        return mEntradaSelecionada;
    }

    public void setEntradaSelecionada(Entrada entradaSelecionada) {
        this.mEntradaSelecionada.postValue(entradaSelecionada);
    }

    public MutableLiveData<Saida> getmSaidaSelecionada() {
        return mSaidaSelecionada;
    }

    public void setSaidaSelecionada(Saida saidaSelecionada) {
        this.mSaidaSelecionada.postValue(saidaSelecionada);
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

    public void limpaEstado(){
        this.mUsuarioLogado = new MutableLiveData<>();
        this.mlistaVeiculos = new MutableLiveData<>();
        this.mVeiculoSelecionado = new MutableLiveData<>();
        this.mEntradaSelecionada = new MutableLiveData<>();
        this.mSaidaSelecionada = new MutableLiveData<>();
        this.mlistaEntradas = new MutableLiveData<>();
        this.mlistaSaidas = new MutableLiveData<>();
    }

    public void setArrayListVeiculos(ArrayList<Veiculo> listaVeiculos){
        mlistaVeiculos.postValue(listaVeiculos);
    }

    public void setArrayListEntradas(ArrayList<Entrada> listaEntradas){
        mlistaEntradas.postValue(listaEntradas);
    }

    public void setArrayListSaidas(ArrayList<Saida> listaSaidas){
        mlistaSaidas.postValue(listaSaidas);
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

    public void zerarEntradaSelecionada(){
        this.mEntradaSelecionada = new MutableLiveData<>();
    }

    public void zerarSaidaSelecionada(){
        this.mSaidaSelecionada = new MutableLiveData<>();
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
                            veiculo.setUsuarioDono(new Usuario(doc.getString("email")));
                            veiculo.setId(doc.getId());
                            listaVeiculos.add(veiculo);
                        }
                        this.setArrayListVeiculos(listaVeiculos);
                    }
                });
    }

    public void buscaEntradasFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Entradas").whereEqualTo("idVeiculo", this.mVeiculoSelecionado.getValue().getId())
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        ArrayList<Entrada> listaEntradas = new ArrayList<>();
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            Entrada entrada = doc.toObject(Entrada.class);
                            entrada.setVeiculo(this.getmVeiculoSelecionado().getValue());
                            entrada.setIdEntrada(doc.getId());
                            listaEntradas.add(entrada);
                        }
                        listaEntradas.sort((entrada1, entrada2) -> {
                            Date t1 = entrada1.getData();
                            Date t2 = entrada2.getData();
                            if (t1 != null && t2 != null) {
                                return t2.compareTo(t1);
                            }
                            return 0;
                        });
                        this.setArrayListEntradas(listaEntradas);
                    }
                });
    }

    public void buscaSaidasFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Saídas").whereEqualTo("idVeiculo", this.mVeiculoSelecionado.getValue().getId())
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        ArrayList<Saida> listaSaidas = new ArrayList<>();
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            Saida saida = doc.toObject(Saida.class);
                            saida.setVeiculo(this.getmVeiculoSelecionado().getValue());
                            saida.setIdSaida(doc.getId());
                            listaSaidas.add(saida);
                        }
                        listaSaidas.sort((saida1, saida2) -> {
                            Date t1 = saida1.getData();
                            Date t2 = saida2.getData();
                            if (t1 != null && t2 != null) {
                                return t2.compareTo(t1);
                            }
                            return 0;
                        });
                        this.setArrayListSaidas(listaSaidas);
                    }
                });
    }
}
