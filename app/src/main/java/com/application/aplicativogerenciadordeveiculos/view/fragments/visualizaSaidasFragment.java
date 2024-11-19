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
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaEntradasBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaRelatorioBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaSaidasBinding;
import com.application.aplicativogerenciadordeveiculos.model.Entrada;
import com.application.aplicativogerenciadordeveiculos.model.Saida;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.adapter.SaidaAdapter;
import com.application.aplicativogerenciadordeveiculos.view.adapter.VeiculoAdapter;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaRelatorioViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaSaidasViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class visualizaSaidasFragment extends Fragment {

    private VisualizaSaidasViewModel mViewModel;
    InformacoesViewModel informacoesViewModel;
    FragmentVisualizaSaidasBinding binding;
    float valorTotalAbastecimentos = 0, valorTotalManutencoes = 0, valorTotalImpostos = 0, valorTotalSeguro = 0, valorTotalAluguel = 0, valorTotalOutros = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVisualizaSaidasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VisualizaSaidasViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);
        informacoesViewModel.getMlistaSaidas().observe(getViewLifecycleOwner(), observaListaSaidas);
        mViewModel.getmResultado().observe(getViewLifecycleOwner(), observaExclusaoSaida);
        informacoesViewModel.setSaidaSelecionada(null);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String valorTotalSaida = df.format(informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalSaidas());

        binding.tvItemValorTotalSaidas.setText("R$" + valorTotalSaida.replace(".", ","));

        informacoesViewModel.buscaSaidasFirebase();
        if(informacoesViewModel.getMlistaSaidas().getValue() != null){
            mViewModel.setMlistaSaidas(informacoesViewModel.getMlistaSaidas());
        }

        WebSettings webSettings = binding.wvGrafico.getSettings();
        webSettings.setJavaScriptEnabled(true);

        binding.wvGrafico.setWebViewClient(new WebViewClient());
        carregaGoogleChart(binding.wvGrafico);

        binding.bCadastrarSaidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.acao_visualizaSaidasFragment_para_cadastroSaidaFragment);
            }
        });
    }

    Observer<ArrayList<Saida>> observaListaSaidas = new Observer<ArrayList<Saida>>() {
        @Override
        public void onChanged(ArrayList<Saida> listaSaidas) {
            if (!listaSaidas.isEmpty()) {
                atualizaListagem(listaSaidas);
            }

        }
    };

    Observer<Boolean> observaExclusaoSaida = aBoolean -> {
        if(aBoolean){
            Toast.makeText(getContext(), "Exclusão Realizada com Sucesso!", Toast.LENGTH_LONG).show();
            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);
            String valorTotalSaidas = df.format(informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalSaidas());
            binding.tvItemValorTotalSaidas.setText("R$" + valorTotalSaidas.replace(".", ","));
        }else{
            Toast.makeText(getContext(), "ERRO: Não foi possível excluir o Registro de Saída, Tente Novamente!", Toast.LENGTH_LONG).show();
        }
    };

    SaidaAdapter.SaidaOnClickListener trataCliqueItem = (view, position, saida) -> {
        informacoesViewModel.setSaidaSelecionada(saida);
        Navigation.findNavController(view).navigate(R.id.acao_visualizaSaidasFragment_para_visualizaSaidaDetalhadaFragment);
    };

    SaidaAdapter.SaidaOnClickListener trataCliqueExcluirItem = (view, position, saida) -> {
        AlertDialog.Builder msgConfirmacao = new AlertDialog.Builder(getContext());
        msgConfirmacao.setTitle("Excluir");
        msgConfirmacao.setIcon(R.drawable.ic_excluir);
        msgConfirmacao.setMessage("Você deseja realmente excluir esse registro de saída de valor?" +
                "\nEssa ação não poderá ser revertida!");
        msgConfirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                informacoesViewModel.removerSaidaDaLista(position);
                mViewModel.excluirSaida(saida);
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

    SaidaAdapter.SaidaOnClickListener trataCliqueEditarItem = (view, position, saida) -> {
        informacoesViewModel.setSaidaSelecionada(saida);
        Navigation.findNavController(view).navigate(R.id.acao_visualizaSaidasFragment_para_cadastroSaidaFragment);
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
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Saídas");
            ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void atualizaListagem(ArrayList<Saida> listaSaidas) {
        if (listaSaidas != null) {
            SaidaAdapter saidaAdapter = new SaidaAdapter(listaSaidas, trataCliqueItem, trataCliqueExcluirItem, trataCliqueEditarItem);
            binding.rvVisualizaSaidas.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvVisualizaSaidas.setItemAnimator(new DefaultItemAnimator());
            binding.rvVisualizaSaidas.setAdapter(saidaAdapter);
            WebSettings webSettings = binding.wvGrafico.getSettings();
            webSettings.setJavaScriptEnabled(true);

            binding.wvGrafico.setWebViewClient(new WebViewClient());
            carregaGoogleChart(binding.wvGrafico);
        }
    }

    private void carregaGoogleChart(WebView webView) {
        ArrayList<Saida> listaSaidas = informacoesViewModel.getMlistaSaidas().getValue();
        if(listaSaidas != null){
            for (int i = 0; i < listaSaidas.size(); i++) {
                if(listaSaidas.get(i).getTipo() == 1){
                    valorTotalAbastecimentos += listaSaidas.get(i).getValor();
                } else if(listaSaidas.get(i).getTipo() == 2){
                    valorTotalManutencoes += listaSaidas.get(i).getValor();
                } else if(listaSaidas.get(i).getTipo() == 3){
                    valorTotalImpostos += listaSaidas.get(i).getValor();
                } else if(listaSaidas.get(i).getTipo() == 4){
                    valorTotalSeguro += listaSaidas.get(i).getValor();
                } else if(listaSaidas.get(i).getTipo() == 5){
                    valorTotalAluguel += listaSaidas.get(i).getValor();
                } else{
                    valorTotalOutros += listaSaidas.get(i).getValor();
                }
            }
        }
        String dataJson = "[['Abastecimentos', " + valorTotalAbastecimentos + "]," +
                " ['Manutenções', " + valorTotalManutencoes + "]," +
                " ['Impostos', " + valorTotalImpostos + "]," +
                " ['Seguro', " + valorTotalSeguro + "]," +
                " ['Aluguel', " + valorTotalAluguel + "]," +
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
                "        if("+ valorTotalAbastecimentos + " != 0.0 || " + valorTotalManutencoes + " != 0.0 || " + valorTotalImpostos + " != 0.0 || " + valorTotalSeguro + " != 0.0 || " + valorTotalAluguel + " != 0.0 || " + valorTotalOutros + " != 0.0){" +
                "        data.addRows(" + dataJson + ");\n" +
                "\n" +
                "        var options = {'title':'Comparação Tipos de Saídas',\n" +
                "                       'width':430,\n" +
                "                       'height':350," +
                "                       colors:['orange','brown','green','red','blue'],\n" +
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