package com.application.aplicativogerenciadordeveiculos.view.fragments;

import androidx.lifecycle.Observer;
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
import android.widget.Toast;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.Utils.Validador;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentCadastroEntradaBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentCadastroSaidaBinding;
import com.application.aplicativogerenciadordeveiculos.model.Entrada;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.CadastroEntradaViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.CadastroSaidaViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Locale;

public class CadastroEntradaFragment extends Fragment {

    FragmentCadastroEntradaBinding binding;
    private CadastroEntradaViewModel mViewModel;
    InformacoesViewModel informacoesViewModel;

    private String labelTela = "CADASTRO DE ENTRADA";
    private String labelHeader = "Nova Entrada";
    private String labelBotao = "Cadastrar Entrada";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCadastroEntradaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CadastroEntradaViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);

        if(informacoesViewModel.getmEntradaSelecionada().getValue() != null){
            mViewModel.setmEntradaEdicao(informacoesViewModel.getmEntradaSelecionada().getValue());
            informacoesViewModel.zerarEntradaSelecionada();
            labelTela = "EDIÇÃO DE ENTRADA";
            labelHeader = "Editar Entrada";
            labelBotao = "Editar Entrada";
            carregaEntradaEdicao();
        }else{
            mViewModel.setmEntradaEdicao(null);
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date dataAtual = new Date();
            String dataAtualFormatada = formatador.format(dataAtual);
            binding.etCadastroData.setText(dataAtualFormatada);
        }

        mViewModel.getResultado().observe(getViewLifecycleOwner(), observaCadastroEntrada);

        binding.bCadastrarEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.spCadastroTipo.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "ERRO: Informe o tipo da Entrada!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Validador.validaTexto(binding.etCadastroData.getText().toString().trim())) {
                    binding.etCadastroData.setError("ERRO: Informe a Data!");
                    binding.etCadastroData.requestFocus();
                    return;
                }
                if (!Validador.ehDataValida(binding.etCadastroData.getText().toString())) {
                    binding.etCadastroData.setError("ERRO: Informe uma Data Válida");
                    binding.etCadastroData.requestFocus();
                    return;
                }
                if (!Validador.validaTexto(binding.etCadastroQuilometragem.getText().toString().trim())) {
                    binding.etCadastroQuilometragem.setError("ERRO: Informe a Quilometragem!");
                    binding.etCadastroQuilometragem.requestFocus();
                    return;
                }
                if (!Validador.validaTexto(binding.etCadastroValor.getText().toString().trim())) {
                    binding.etCadastroValor.setError("ERRO: Informe o Valor!");
                    binding.etCadastroValor.requestFocus();
                    return;
                }
                if (Float.parseFloat(binding.etCadastroValor.getText().toString()) <= 0) {
                    binding.etCadastroValor.setError("ERRO: Informe um Valor Válido!");
                    binding.etCadastroValor.requestFocus();
                    return;
                }

                int tipo = binding.spCadastroTipo.getSelectedItemPosition();
                DateTimeFormatter formatadorDeEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime dataConvertida = LocalDateTime.parse(binding.etCadastroData.getText().toString(), formatadorDeEntrada);
                ZonedDateTime dataComZona = dataConvertida.atZone(ZoneId.of("GMT-03:00"));
                DateTimeFormatter formatadorDeSaida = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", java.util.Locale.ENGLISH);
                String dataFormatada = dataComZona.format(formatadorDeSaida);
                SimpleDateFormat formatadorFinal = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                Date data = new Date();
                try {
                    data = formatadorFinal.parse(dataFormatada);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int quilometragem = Integer.parseInt(binding.etCadastroQuilometragem.getText().toString());
                float valor = Float.parseFloat(binding.etCadastroValor.getText().toString());
                String descricao = binding.etCadastroDescricao.getText().toString();

                Entrada entrada = null;
                if (mViewModel.getEntradaEdicao().getValue() == null) {
                    entrada = new Entrada(informacoesViewModel.getmVeiculoSelecionado().getValue(), tipo, valor, descricao, data, quilometragem );
                    mViewModel.inserirEntrada(entrada);
                    informacoesViewModel.adicionarEntradaNaLista(entrada);
                    limpaCampos();
                } else {
                    entrada = mViewModel.getEntradaEdicao().getValue();
                    entrada.setVeiculo(informacoesViewModel.getmVeiculoSelecionado().getValue());
                    entrada.setTipo(tipo);
                    entrada.setData(data);
                    entrada.setQuilometragem(quilometragem);
                    entrada.setValor(valor);
                    entrada.setDescricao(descricao);

                    mViewModel.atualizarEntrada(entrada);
                    Toast.makeText(getContext(), "Entrada Atualizada com Sucesso!", Toast.LENGTH_LONG).show();
                    Navigation.findNavController(view).popBackStack();
                }
            }
        });

        binding.bVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpaCampos();
                Navigation.findNavController(view).navigate(R.id.acao_cadastroEntradaFragment_para_visualizaEntradasFragment);
            }
        });
    }

    Observer<Boolean> observaCadastroEntrada = aBoolean -> {
        if (aBoolean) {
            Toast.makeText(getContext(), "Entrada cadastrada com sucesso!", Toast.LENGTH_LONG).show();
            limpaCampos();
            Navigation.findNavController(requireView()).popBackStack();
        } else {
            String erro = mViewModel.getErroCadastro();
            Toast.makeText(getContext(), "ERRO: " + erro, Toast.LENGTH_LONG).show();
        }
    };

    public void limpaCampos() {
        binding.spCadastroTipo.setSelection(0);
        binding.etCadastroData.setText("");
        binding.etCadastroValor.setText("");
        binding.etCadastroQuilometragem.setText("");
        binding.etCadastroDescricao.setText("");
    }

    public void carregaEntradaEdicao() {
        String data = mViewModel.getEntradaEdicao().getValue().getData().toString();
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("EEE MMM dd HH:mm:ss O yyyy").toFormatter(Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(data, inputFormatter);
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormatada = zonedDateTime.format(formatadorData);
        binding.etCadastroData.setText(dataFormatada);
        binding.etCadastroValor.setText(String.valueOf(mViewModel.getEntradaEdicao().getValue().getValor()));
        binding.etCadastroDescricao.setText(mViewModel.getEntradaEdicao().getValue().getDescricao());
        binding.spCadastroTipo.setSelection(mViewModel.getEntradaEdicao().getValue().getTipo());
        binding.etCadastroQuilometragem.setText(String.valueOf(mViewModel.getEntradaEdicao().getValue().getQuilometragem()));
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
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle(labelHeader);
            ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            binding.tvLabelTela.setText(labelTela);
            binding.bCadastrarEntrada.setText(labelBotao);

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