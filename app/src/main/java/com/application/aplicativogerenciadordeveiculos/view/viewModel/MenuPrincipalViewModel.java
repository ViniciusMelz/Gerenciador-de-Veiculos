package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Veiculo>> mlistaVeiculos;
    private MutableLiveData<Boolean> mResultado;

    public MenuPrincipalViewModel() {
        this.mlistaVeiculos = new MutableLiveData<>();
        this.mResultado = new MutableLiveData<>();
    }

    public void limpaEstado(){
        this.mlistaVeiculos = new MutableLiveData<>();
        this.mResultado = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getResultado() {
        return mResultado;
    }

    public void excluirVeiculo(Veiculo veiculo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Veículos").document(veiculo.getId()).delete().addOnCompleteListener(task -> {
           mResultado.postValue(true);
        }).addOnFailureListener(e -> {
            mResultado.postValue(false);
        });
        db.collection("Saídas").whereEqualTo("idVeiculo", veiculo.getId()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot) {
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> {
                                        });
                            }
                        }
                    }
                });
        db.collection("Entradas").whereEqualTo("idVeiculo", veiculo.getId()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot) {
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> {
                                        });
                            }
                        }
                    }
                });
    }

    public void setmListaVeiculos(MutableLiveData<ArrayList<Veiculo>> mlistaVeiculos) {
        this.mlistaVeiculos = mlistaVeiculos;
    }

    public MutableLiveData<ArrayList<Veiculo>> getListaVeiculos() {
        return mlistaVeiculos;
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
                this.setmListaVeiculos(mutableLiveData);
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

    public MutableLiveData<Boolean> getmResultado() {
        return mResultado;
    }

    public void setmResultado(MutableLiveData<Boolean> mResultado) {
        this.mResultado = mResultado;
    }
}