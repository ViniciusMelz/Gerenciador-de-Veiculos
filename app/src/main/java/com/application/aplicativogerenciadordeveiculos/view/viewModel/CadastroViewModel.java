package com.application.aplicativogerenciadordeveiculos.view.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.aplicativogerenciadordeveiculos.model.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class CadastroViewModel extends ViewModel {
    private MutableLiveData<Boolean> mResultado;
    private String erroCadastro;

    public CadastroViewModel() {
        this.mResultado = new MutableLiveData<>();
    }
    public MutableLiveData<Boolean> getResultado() {
        return mResultado;
    }

    public String getErroCadastro() {
        return erroCadastro;
    }

    public void limpaEstado() {
        mResultado = new MutableLiveData<>();
        erroCadastro = null;
    }
    public void cadastrarUsuario(Usuario usuario) {
        erroCadastro = null;
        String nome = usuario.getNome();
        String email = usuario.getEmail();
        String senha = usuario.getSenha();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mResultado.postValue(true);
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthWeakPasswordException e) {
                    erroCadastro = "Digite uma senha com no mínimo 6 caracteres!";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    erroCadastro = "Digite um email válido!";
                } catch (FirebaseAuthUserCollisionException e) {
                    erroCadastro = "Email já em uso, escolha outro!";
                } catch (FirebaseNetworkException e) {
                    erroCadastro = "Verifique sua conexão com a internet!";
                } catch (Exception e) {
                    erroCadastro = "Erro ao cadastrar usuário, tente novamente!";
                }
                mResultado.postValue(false);
            }
        });

        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", email);
        usuarioMap.put("nome", nome);

        db.collection("Usuários").document(email).set(usuarioMap).addOnCompleteListener(taskSalvamento -> {
            if(!taskSalvamento.isSuccessful()){
                try {
                    throw taskSalvamento.getException();
                } catch (Exception e) {
                    erroCadastro = "Erro ao cadastrar usuário, tente novamente!";
                }
                mResultado.postValue(false);
            }
        });
    }
}