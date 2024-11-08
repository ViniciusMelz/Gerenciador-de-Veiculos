package com.application.aplicativogerenciadordeveiculos.view.activities;

import android.os.Bundle;

import com.application.aplicativogerenciadordeveiculos.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.application.aplicativogerenciadordeveiculos.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.visualizaRelatorioFragment, R.id.visualizaEntradasFragment, R.id.visualizaSaidasFragment).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }

    // método para esconder o botomNavigation
    public void escondeBottomNavigation() {
        binding.bottomNavigation.setVisibility(View.GONE);
    }

    // método para mostrar o bottomNavigation
    public void mostraBottomNavigation() {
        binding.bottomNavigation.setVisibility(View.VISIBLE);
    }
}