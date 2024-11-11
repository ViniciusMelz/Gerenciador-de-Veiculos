package com.application.aplicativogerenciadordeveiculos.view.activities;

import android.os.Bundle;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.application.aplicativogerenciadordeveiculos.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    NavController navController;

    InformacoesViewModel informacoesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        informacoesViewModel = new ViewModelProvider(this).get(InformacoesViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.visualizaRelatorioFragment, R.id.visualizaEntradasFragment, R.id.visualizaSaidasFragment).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tollbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnTrocarSenha){
            navController.navigate(R.id.acao_global_trocaSenhaFragment);
        }
        if(id == R.id.btnSair){
            FirebaseAuth.getInstance().signOut();
            informacoesViewModel.limpaEstado();
            navController.navigate(R.id.acao_global_loginFragment, null,
                    new NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build());
        }
        return true;
    }

    public void escondeBottomNavigation() {
        binding.bottomNavigation.setVisibility(View.GONE);
    }

    public void mostraBottomNavigation() {
        binding.bottomNavigation.setVisibility(View.VISIBLE);
    }
}