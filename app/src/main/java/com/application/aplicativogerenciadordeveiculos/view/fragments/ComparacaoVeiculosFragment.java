package com.application.aplicativogerenciadordeveiculos.view.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentComparacaoVeiculosBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentMenuPrincipalBinding;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.ComparacaoVeiculosViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.MenuPrincipalViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ComparacaoVeiculosFragment extends Fragment {

    private ComparacaoVeiculosViewModel mViewModel;
    FragmentComparacaoVeiculosBinding binding;
    InformacoesViewModel informacoesViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentComparacaoVeiculosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ComparacaoVeiculosViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);

        ArrayList<Veiculo> listaVeiculos = informacoesViewModel.getListaVeiculos().getValue();
        mViewModel.setmListaVeiculos(informacoesViewModel.getListaVeiculos());
        if(!listaVeiculos.isEmpty()){
            ArrayAdapter<Veiculo> adapterVeiculos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listaVeiculos);
            adapterVeiculos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spCadastroVeiculo1.setAdapter(adapterVeiculos);
            binding.spCadastroVeiculo2.setAdapter(adapterVeiculos);

            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);
            String mediaCombustivel = df.format(listaVeiculos.get(0).getMediaCombustivel());
            String valorTotalEntradas = df.format(listaVeiculos.get(0).getValorTotalEntradas());
            String valorTotalSaidas = df.format(listaVeiculos.get(0).getValorTotalSaidas());

            String modelo = listaVeiculos.get(0).getModelo().substring(0, 9) + "...";
            binding.tvLabelVeiculo1.setText(listaVeiculos.get(0).getMarca() + "\n" + modelo);
            binding.tvItemPlaca1.setText(listaVeiculos.get(0).getPlaca().toUpperCase());
            binding.tvItemTipo1.setText(listaVeiculos.get(0).getTipoLiteral());
            binding.tvItemAno1.setText(String.valueOf(listaVeiculos.get(0).getAno()));
            binding.tvItemQuilometragem1.setText(listaVeiculos.get(0).getQuilometragem() + " KM");
            binding.tvItemMediaCombustivel1.setText(mediaCombustivel.replace(".",",") + " KM/L");
            binding.tvItemValorTotalEntradas1.setText("R$ " + valorTotalEntradas.replace(".",","));
            binding.tvItemValorTotalSaidas1.setText("R$ " + valorTotalSaidas.replace(".",","));

            binding.tvLabelVeiculo2.setText(listaVeiculos.get(0).getMarca() + "\n" + modelo);
            binding.tvItemPlaca2.setText(listaVeiculos.get(0).getPlaca().toUpperCase());
            binding.tvItemTipo2.setText(listaVeiculos.get(0).getTipoLiteral());
            binding.tvItemAno2.setText(String.valueOf(listaVeiculos.get(0).getAno()));
            binding.tvItemQuilometragem2.setText(listaVeiculos.get(0).getQuilometragem() + " KM");
            binding.tvItemMediaCombustivel2.setText(mediaCombustivel.replace(".",",") + " KM/L");
            binding.tvItemValorTotalEntradas2.setText("R$ " + valorTotalEntradas.replace(".",","));
            binding.tvItemValorTotalSaidas2.setText("R$ " + valorTotalSaidas.replace(".",","));
        }

        binding.spCadastroVeiculo1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int posicaoSelecionada = binding.spCadastroVeiculo1.getSelectedItemPosition();
                DecimalFormat df = new DecimalFormat("0.00");
                df.setMaximumFractionDigits(2);
                String mediaCombustivel;
                if(listaVeiculos.get(posicaoSelecionada).getMediaCombustivel() != 0.0){
                    mediaCombustivel = df.format(listaVeiculos.get(posicaoSelecionada).getMediaCombustivel());
                    mediaCombustivel = mediaCombustivel.replace(".",",") + " KM";
                }else{
                    mediaCombustivel = "Não Informada!";
                }
                String valorTotalEntradas = df.format(listaVeiculos.get(posicaoSelecionada).getValorTotalEntradas());
                String valorTotalSaidas = df.format(listaVeiculos.get(posicaoSelecionada).getValorTotalSaidas());

                String modelo = (listaVeiculos.get(posicaoSelecionada).getModelo().length() > 10 ? listaVeiculos.get(posicaoSelecionada).getModelo().substring(0, 9) + "..." : listaVeiculos.get(posicaoSelecionada).getModelo());
                binding.tvLabelVeiculo1.setText(listaVeiculos.get(posicaoSelecionada).getMarca() + "\n" + modelo);
                binding.tvItemPlaca1.setText(listaVeiculos.get(posicaoSelecionada).getPlaca().toUpperCase());
                binding.tvItemTipo1.setText(listaVeiculos.get(posicaoSelecionada).getTipoLiteral());
                binding.tvItemAno1.setText(String.valueOf(listaVeiculos.get(posicaoSelecionada).getAno()));
                binding.tvItemQuilometragem1.setText(listaVeiculos.get(posicaoSelecionada).getQuilometragem() + " KM");
                binding.tvItemMediaCombustivel1.setText(mediaCombustivel);
                binding.tvItemValorTotalEntradas1.setText("R$ " + valorTotalEntradas.replace(".",","));
                binding.tvItemValorTotalSaidas1.setText("R$ " + valorTotalSaidas.replace(".",","));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spCadastroVeiculo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int posicaoSelecionada = binding.spCadastroVeiculo2.getSelectedItemPosition();
                DecimalFormat df = new DecimalFormat("0.00");
                df.setMaximumFractionDigits(2);
                String mediaCombustivel;
                if(listaVeiculos.get(posicaoSelecionada).getMediaCombustivel() != 0.0){
                    mediaCombustivel = df.format(listaVeiculos.get(posicaoSelecionada).getMediaCombustivel());
                    mediaCombustivel = mediaCombustivel.replace(".",",") + " KM";
                }else{
                    mediaCombustivel = "Não Informada!";
                }
                String valorTotalEntradas = df.format(listaVeiculos.get(posicaoSelecionada).getValorTotalEntradas());
                String valorTotalSaidas = df.format(listaVeiculos.get(posicaoSelecionada).getValorTotalSaidas());

                String modelo = (listaVeiculos.get(posicaoSelecionada).getModelo().length() > 9 ? listaVeiculos.get(posicaoSelecionada).getModelo().substring(0, 9) + "..." : listaVeiculos.get(posicaoSelecionada).getModelo());
                binding.tvLabelVeiculo2.setText(listaVeiculos.get(posicaoSelecionada).getMarca() + "\n" + modelo);
                binding.tvItemPlaca2.setText(listaVeiculos.get(posicaoSelecionada).getPlaca().toUpperCase());
                binding.tvItemTipo2.setText(listaVeiculos.get(posicaoSelecionada).getTipoLiteral());
                binding.tvItemAno2.setText(String.valueOf(listaVeiculos.get(posicaoSelecionada).getAno()));
                binding.tvItemQuilometragem2.setText(listaVeiculos.get(posicaoSelecionada).getQuilometragem() + " KM");
                binding.tvItemMediaCombustivel2.setText(mediaCombustivel);
                binding.tvItemValorTotalEntradas2.setText("R$ " + valorTotalEntradas.replace(".",","));
                binding.tvItemValorTotalSaidas2.setText("R$ " + valorTotalSaidas.replace(".",","));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.bVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.acao_comparacaoVeiculosFragment_para_menuPrincipalFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mViewModel.limpaEstado();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).escondeBottomNavigation();
            ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).mostraBottomNavigation();
        }
    }

}