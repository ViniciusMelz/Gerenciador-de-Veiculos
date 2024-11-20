package com.application.aplicativogerenciadordeveiculos.view.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentMenuPrincipalBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaEntradasBinding;
import com.application.aplicativogerenciadordeveiculos.model.Entrada;
import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.adapter.EntradaAdapter;
import com.application.aplicativogerenciadordeveiculos.view.adapter.SaidaAdapter;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.MenuPrincipalViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaEntradasViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class visualizaEntradasFragment extends Fragment {

    private VisualizaEntradasViewModel mViewModel;
    InformacoesViewModel informacoesViewModel;
    FragmentVisualizaEntradasBinding binding;
    float valorTotalMotoboy = 0, valorTotalMotorista = 0, valorTotalFrete = 0, valorTotalAluguelDoVeiculo = 0, valorTotalOutros = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVisualizaEntradasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VisualizaEntradasViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);
        informacoesViewModel.getMlistaEntradas().observe(getViewLifecycleOwner(), observaListaEntradas);
        mViewModel.getmResultado().observe(getViewLifecycleOwner(), observaExclusaoEntrada);
        informacoesViewModel.setEntradaSelecionada(null);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String valorTotalEntrada = df.format(informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalEntradas());

        binding.tvItemValorTotalEntradas.setText("R$" + valorTotalEntrada.replace(".", ","));

        informacoesViewModel.buscaEntradasFirebase();
        if(informacoesViewModel.getMlistaEntradas().getValue() != null){
            mViewModel.setMlistaEntradas(informacoesViewModel.getMlistaEntradas());
        }

        WebSettings webSettings = binding.wvGrafico.getSettings();
        webSettings.setJavaScriptEnabled(true);

        binding.wvGrafico.setWebViewClient(new WebViewClient());
        carregaGoogleChart(binding.wvGrafico);

        binding.bCadastrarEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.acao_visualizaEntradasFragment_para_cadastroEntradaFragment);
            }
        });
    }

    Observer<ArrayList<Entrada>> observaListaEntradas = new Observer<ArrayList<Entrada>>() {
        @Override
        public void onChanged(ArrayList<Entrada> listaEntradas) {
            if (!listaEntradas.isEmpty()) {
                atualizaListagem(listaEntradas);
            }

        }
    };

    Observer<Boolean> observaExclusaoEntrada = aBoolean -> {
        if(aBoolean){
            Toast.makeText(getContext(), "Exclusão Realizada com Sucesso!", Toast.LENGTH_LONG).show();
            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);
            String valorTotalEntrada = df.format(informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalEntradas());
            binding.tvItemValorTotalEntradas.setText("R$" + valorTotalEntrada.replace(".", ","));
        }else{
            Toast.makeText(getContext(), "ERRO: Não foi possível excluir o Registro de Entrada, Tente Novamente!", Toast.LENGTH_LONG).show();
        }
    };

    EntradaAdapter.EntradaOnClickListener trataCliqueItem = (view, position, entrada) -> {
        informacoesViewModel.setEntradaSelecionada(entrada);
        Navigation.findNavController(view).navigate(R.id.acao_visualizaEntradasFragment_para_visualizaEntradaDetalhadaFragment);
    };

    EntradaAdapter.EntradaOnClickListener trataCliqueExcluirItem = (view, position, entrada) -> {
        AlertDialog.Builder msgConfirmacao = new AlertDialog.Builder(getContext());
        msgConfirmacao.setTitle("Excluir");
        msgConfirmacao.setIcon(R.drawable.ic_excluir);
        msgConfirmacao.setMessage("Você deseja realmente excluir esse registro de entrada de valor?" +
                "\nEssa ação não poderá ser revertida!");
        msgConfirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                informacoesViewModel.removerEntradaDaLista(position);
                Veiculo veiculoAtt;
                veiculoAtt = mViewModel.excluirEntrada(entrada);
                informacoesViewModel.getmVeiculoSelecionado().getValue().setValorTotalEntradas(veiculoAtt.getValorTotalEntradas());
                atualizaListagem(informacoesViewModel.getMlistaEntradas().getValue());
            }
        });
        msgConfirmacao.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Exclusão Cancelada!", Toast.LENGTH_SHORT).show();
            }
        });
        msgConfirmacao.show();
    };

    EntradaAdapter.EntradaOnClickListener trataCliqueEditarItem = (view, position, entrada) -> {
        informacoesViewModel.setEntradaSelecionada(entrada);
        Navigation.findNavController(view).navigate(R.id.acao_visualizaEntradasFragment_para_cadastroEntradaFragment);
    };

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
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Entradas");
            ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void atualizaListagem(ArrayList<Entrada> listaEntradas) {
        if (listaEntradas != null) {
            EntradaAdapter entradaAdapter = new EntradaAdapter(listaEntradas, trataCliqueItem, trataCliqueExcluirItem, trataCliqueEditarItem);
            binding.rvVisualizaEntradas.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvVisualizaEntradas.setItemAnimator(new DefaultItemAnimator());
            binding.rvVisualizaEntradas.setAdapter(entradaAdapter);
            WebSettings webSettings = binding.wvGrafico.getSettings();
            webSettings.setJavaScriptEnabled(true);

            binding.wvGrafico.setWebViewClient(new WebViewClient());
            carregaGoogleChart(binding.wvGrafico);
        }
    }

    private void carregaGoogleChart(WebView webView) {
        valorTotalMotoboy = valorTotalMotorista = valorTotalFrete = valorTotalAluguelDoVeiculo = valorTotalOutros = 0;
        if(informacoesViewModel.getMlistaEntradas().getValue() != null){
            for (int i = 0; i < informacoesViewModel.getMlistaEntradas().getValue().size(); i++) {
                if(informacoesViewModel.getMlistaEntradas().getValue().get(i).getTipo() == 1){
                    valorTotalMotoboy += informacoesViewModel.getMlistaEntradas().getValue().get(i).getValor();
                } else if(informacoesViewModel.getMlistaEntradas().getValue().get(i).getTipo() == 2){
                    valorTotalMotorista += informacoesViewModel.getMlistaEntradas().getValue().get(i).getValor();
                } else if(informacoesViewModel.getMlistaEntradas().getValue().get(i).getTipo() == 3){
                    valorTotalFrete += informacoesViewModel.getMlistaEntradas().getValue().get(i).getValor();
                } else if(informacoesViewModel.getMlistaEntradas().getValue().get(i).getTipo() == 4){
                    valorTotalAluguelDoVeiculo += informacoesViewModel.getMlistaEntradas().getValue().get(i).getValor();
                } else{
                    valorTotalOutros += informacoesViewModel.getMlistaEntradas().getValue().get(i).getValor();
                }
            }
        }
        String dataJson = "[['Motoboy', " + valorTotalMotoboy + "]," +
                " ['Motorista de Aplicativo', " + valorTotalMotorista + "]," +
                " ['Frete', " + valorTotalFrete + "]," +
                " ['Aluguel do Veículo', " + valorTotalAluguelDoVeiculo + "]," +
                " ['Outras Entradas', " + valorTotalOutros + "]]";
        String htmlData = "<html>\n" +
                "  <head>\n" +
                "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "\n" +
                "      google.charts.load('current', {'packages':['corechart']});\n" +
                "\n" +
                "      google.charts.setOnLoadCallback(drawChart);\n" +
                "\n" +
                "      function drawChart() {\n" +
                "\n" +
                "        var data = new google.visualization.DataTable();\n" +
                "        data.addColumn('string', 'Topping');\n" +
                "        data.addColumn('number', 'Slices');\n" +
                "        if("+ valorTotalMotoboy + " != 0.0 || " + valorTotalMotorista + " != 0.0 || " + valorTotalFrete + " != 0.0 || " + valorTotalAluguelDoVeiculo + " != 0.0 || " + valorTotalOutros + " != 0.0){" +
                "        data.addRows(" + dataJson + ");\n" +
                "\n" +
                "        var options = {'title':'Comparação Tipos de Entradas',\n" +
                "                       'width':430,\n" +
                "                       'height':350," +
                "                       colors:['green','blue','red','brown','orange'],\n" +
                "                       pieHole: 0.4," +
                "                       backgroundColor: '#D3D3D3'};\n" +
                "\n" +
                "        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));\n" +
                "        chart.draw(data, options);\n" +
                "        }else{" +
                "           var chart = document.getElementById('chart_div');" +
                "           chart.style = 'text-align: center; padding-top: 10px; font-weight: bold;';" +
                "           chart.innerText = 'SEM ENTRADAS E SAÍDAS REGISTRADAS! CADASTRE ALGUMA ENTRADA OU SAÍDA PARA VISUALIZAR O GRÁFICO!'" +
                "        }" +
                "      }\n" +
                "    </script>\n" +
                "  </head>\n" +
                "\n" +
                "  <body style='overflow: hidden; background-color: #D3D3D3;'>\n" +
                "    <div id=\"chart_div\" style='overflow: hidden; background-color: #D3D3D3;'></div>\n" +
                "  </body>\n" +
                "</html>";

        webView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);
    }
}