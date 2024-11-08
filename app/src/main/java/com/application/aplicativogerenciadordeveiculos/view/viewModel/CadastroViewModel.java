package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroViewModel extends ViewModel {
    private MutableLiveData<Boolean> mResultado;

    public CadastroViewModel() {
        this.mResultado = new MutableLiveData<>();
    }
    public MutableLiveData<Boolean> getResultado() {
        return mResultado;
    }

    public void limpaEstado() {
        mResultado = new MutableLiveData<>();
    }
    public void cadastrarUsuario(Usuario usuario) {
        String email = usuario.getEmail();
        String senha = usuario.getSenha();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
           mResultado.postValue(task.isSuccessful());
        });
    }
}