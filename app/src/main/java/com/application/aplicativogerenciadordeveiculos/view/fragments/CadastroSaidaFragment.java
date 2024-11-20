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
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentCadastroSaidaBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentCadastroVeiculoBinding;
import com.application.aplicativogerenciadordeveiculos.model.Entrada;
import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.CadastroEntradaViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.CadastroSaidaViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.CadastroVeiculoViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Locale;

public class CadastroSaidaFragment extends Fragment {

    FragmentCadastroSaidaBinding binding;
    private CadastroSaidaViewModel mViewModel;

    InformacoesViewModel informacoesViewModel;

    private String labelTela = "CADASTRO DE SAÍDA";
    private String labelHeader = "Nova Saída";
    private String labelBotao = "Cadastrar Saída";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCadastroSaidaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CadastroSaidaViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);
        binding.etCadastroMediaCombustivel.setVisibility(View.GONE);
        binding.etCadastroLitros.setVisibility(View.GONE);

        if(informacoesViewModel.getmSaidaSelecionada().getValue() != null){
            mViewModel.setmSaidaEdicao(informacoesViewModel.getmSaidaSelecionada().getValue());
            informacoesViewModel.zerarEntradaSelecionada();
            labelTela = "EDIÇÃO DE SAÍDA";
            labelHeader = "Editar Saída";
            labelBotao = "Editar Saída";
            carregaSaidaEdicao();
        }else{
            mViewModel.setmSaidaEdicao(null);
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date dataAtual = new Date();
            String dataAtualFormatada = formatador.format(dataAtual);
            binding.etCadastroData.setText(dataAtualFormatada);
        }

        mViewModel.getResultado().observe(getViewLifecycleOwner(), observaCadastroSaida);

        binding.spCadastroTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(binding.spCadastroTipo.getSelectedItemPosition() == 1){
                    binding.etCadastroMediaCombustivel.setVisibility(View.VISIBLE);
                    binding.etCadastroLitros.setVisibility(View.VISIBLE);
                }else{
                    binding.etCadastroMediaCombustivel.setVisibility(View.GONE);
                    binding.etCadastroLitros.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.bCadastrarSaida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.spCadastroTipo.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "ERRO: Informe o tipo da Saída!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Validador.validaTexto(binding.etCadastroData.getText().toString().trim())) {
                    binding.etCadastroData.setError("ERRO: Informe a Data!");
                    binding.etCadastroData.requestFocus();
                    return;
                }
                if (!Validador.ehDataValida(binding.etCadastroData.getText().toString())) {
                    binding.etCadastroData.setError("ERRO: Informe uma Data Válida!");
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
                if (Float.parseFloat(binding.etCadastroValor.getText().toString()) < 0) {
                    binding.etCadastroValor.setError("ERRO: Informe um Valor Válido!");
                    binding.etCadastroValor.requestFocus();
                    return;
                }
                if(binding.spCadastroTipo.getSelectedItemPosition() == 1){
                    if (!Validador.validaTexto(binding.etCadastroLitros.getText().toString().trim())) {
                        binding.etCadastroLitros.setError("ERRO: Informe a Quantidade de Litros Abastecidos!");
                        binding.etCadastroLitros.requestFocus();
                        return;
                    }
                    if (Float.parseFloat(binding.etCadastroLitros.getText().toString()) <= 0) {
                        binding.etCadastroValor.setError("ERRO: Informe uma Quantidade Válida de Litros Abastecidos!");
                        binding.etCadastroValor.requestFocus();
                        return;
                    }
                    if(binding.etCadastroMediaCombustivel.getText().length() != 0){
                        if (Float.parseFloat(binding.etCadastroMediaCombustivel.getText().toString()) <= 0) {
                            binding.etCadastroMediaCombustivel.setError("ERRO: Informe uma Média de Combustível Válida!");
                            binding.etCadastroMediaCombustivel.requestFocus();
                            return;
                        }
                    }
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
                float media = 0, litrosAbastecidos = 0;
                if(binding.spCadastroTipo.getSelectedItemPosition() == 1){
                    if(binding.etCadastroMediaCombustivel.getText().length() != 0) {
                        media = Float.parseFloat(binding.etCadastroMediaCombustivel.getText().toString());
                    }
                    litrosAbastecidos = Float.parseFloat(binding.etCadastroLitros.getText().toString());
                }

                Saida saida = null;
                if (mViewModel.getSaidaEdicao().getValue() == null) {
                    saida = new Saida(informacoesViewModel.getmVeiculoSelecionado().getValue(), tipo, valor, descricao, quilometragem, litrosAbastecidos, media, data );
                    int quilometragemOriginal = informacoesViewModel.getmVeiculoSelecionado().getValue().getQuilometragem();
                    if(saida.getTipo() == 1){
                        for(int i = 0; i < informacoesViewModel.getMlistaSaidas().getValue().size(); i++){
                            if(informacoesViewModel.getMlistaSaidas().getValue().get(i).getTipo() == 1){
                                quilometragemOriginal = informacoesViewModel.getMlistaSaidas().getValue().get(i).getQuilometragem();
                                break;
                            }
                        }
                    }
                    mViewModel.inserirSaida(saida, quilometragemOriginal);
                    informacoesViewModel.adicionarSaidaNaLista(saida);
                    limpaCampos();
                } else {
                    float valorOriginal = mViewModel.getSaidaEdicao().getValue().getValor();
                    saida = mViewModel.getSaidaEdicao().getValue();
                    saida.setVeiculo(informacoesViewModel.getmVeiculoSelecionado().getValue());
                    saida.setTipo(tipo);
                    saida.setData(data);
                    saida.setQuilometragem(quilometragem);
                    saida.setValor(valor);
                    saida.setDescricao(descricao);
                    saida.setMediaCombustivel(media);
                    saida.setLitrosAbastecidos(litrosAbastecidos);

                    Veiculo veiculoAtt = new Veiculo();
                    veiculoAtt = mViewModel.atualizarSaida(saida, informacoesViewModel.getmVeiculoSelecionado().getValue().getQuilometragem(), valorOriginal);
                    informacoesViewModel.getmVeiculoSelecionado().getValue().setValorTotalSaidas(veiculoAtt.getValorTotalSaidas());
                    informacoesViewModel.getmVeiculoSelecionado().getValue().setQuilometragem(veiculoAtt.getQuilometragem());
                    informacoesViewModel.getmVeiculoSelecionado().getValue().setMediaCombustivel(veiculoAtt.getMediaCombustivel());
                    Toast.makeText(getContext(), "Saída Atualizada com Sucesso!", Toast.LENGTH_LONG).show();
                    Navigation.findNavController(view).popBackStack();
                }
            }
        });

        binding.bVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpaCampos();
                Navigation.findNavController(view).navigate(R.id.acao_cadastroSaidaFragment_para_visualizaSaidasFragment);
            }
        });
    }

    Observer<Boolean> observaCadastroSaida = aBoolean -> {
        if (aBoolean) {
            Toast.makeText(getContext(), "Saída cadastrada com sucesso", Toast.LENGTH_LONG).show();
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
        binding.etCadastroMediaCombustivel.setText("");
        binding.etCadastroLitros.setText("");
    }

    public void carregaSaidaEdicao() {
        String data = mViewModel.getSaidaEdicao().getValue().getData().toString();
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("EEE MMM dd HH:mm:ss O yyyy").toFormatter(Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(data, inputFormatter);
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormatada = zonedDateTime.format(formatadorData);
        binding.etCadastroData.setText(dataFormatada);
        binding.etCadastroValor.setText(String.valueOf(mViewModel.getSaidaEdicao().getValue().getValor()));
        binding.etCadastroDescricao.setText(mViewModel.getSaidaEdicao().getValue().getDescricao());
        binding.spCadastroTipo.setSelection(mViewModel.getSaidaEdicao().getValue().getTipo());
        binding.etCadastroQuilometragem.setText(String.valueOf(mViewModel.getSaidaEdicao().getValue().getQuilometragem()));
        binding.etCadastroLitros.setText(String.valueOf(mViewModel.getSaidaEdicao().getValue().getLitrosAbastecidos()));
        if(mViewModel.getSaidaEdicao().getValue().getMediaCombustivel() != 0.0){
            binding.etCadastroMediaCombustivel.setText(String.valueOf(mViewModel.getSaidaEdicao().getValue().getMediaCombustivel()));
        }
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
            binding.bCadastrarSaida.setText(labelBotao);

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