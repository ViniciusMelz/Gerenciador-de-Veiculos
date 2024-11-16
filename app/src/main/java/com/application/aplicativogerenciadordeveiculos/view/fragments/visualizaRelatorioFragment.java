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
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaEntradasBinding;
import com.application.aplicativogerenciadordeveiculos.databinding.FragmentVisualizaRelatorioBinding;
import com.application.aplicativogerenciadordeveiculos.view.activities.MainActivity;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.InformacoesViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaEntradasViewModel;
import com.application.aplicativogerenciadordeveiculos.view.viewModel.VisualizaRelatorioViewModel;

import java.text.DecimalFormat;

public class visualizaRelatorioFragment extends Fragment {

    private VisualizaRelatorioViewModel mViewModel;
    InformacoesViewModel informacoesViewModel;
    FragmentVisualizaRelatorioBinding binding;

    String valorTotalEntrada, valorTotalSaidas, valorQuilometragem, valorMediaCombustivel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVisualizaRelatorioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VisualizaRelatorioViewModel.class);
        informacoesViewModel = new ViewModelProvider(getActivity()).get(InformacoesViewModel.class);
        mViewModel.setVeiculoSelecionado(informacoesViewModel.getmVeiculoSelecionado().getValue());

        WebSettings webSettings = binding.wvGrafico.getSettings();
        webSettings.setJavaScriptEnabled(true);

        binding.wvGrafico.setWebViewClient(new WebViewClient());

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        valorTotalEntrada = df.format(informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalEntradas());
        valorTotalSaidas = df.format(informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalSaidas());
        valorQuilometragem = String.valueOf(informacoesViewModel.getmVeiculoSelecionado().getValue().getQuilometragem());
        valorMediaCombustivel = df.format(informacoesViewModel.getmVeiculoSelecionado().getValue().getMediaCombustivel());

        binding.tvItemValorEntradas.setText("R$" + valorTotalEntrada.replace(".", ","));
        binding.tvItemValorSaidas.setText("R$" + valorTotalSaidas.replace(".",","));
        binding.tvItemQuilometragem.setText(valorQuilometragem + " KM");
        binding.tvItemMediaCombustivel.setText(valorMediaCombustivel.replace(".",",") + " KM/L");

        carregaGoogleChart(binding.wvGrafico);

        binding.bVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.setVeiculoSelecionado(null);
                informacoesViewModel.setVeiculoSelecionado(null);
                Navigation.findNavController(view).navigate(R.id.acao_visualizaRelatorioFragment_para_menuPrincipalFragment);
            }
        });
    }

    private void carregaGoogleChart(WebView webView) {
        String dataJson = "[['Total Entradas', "+ informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalEntradas() +"], ['Total Saidas', "+ informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalSaidas() +"]]";
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
                "        if("+ informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalEntradas() +" != 0.0 || "+ informacoesViewModel.getmVeiculoSelecionado().getValue().getValorTotalSaidas() +" != 0.0){" +
                "        data.addRows(" + dataJson + ");\n" +
                "\n" +
                "        var options = {'title':'Comparação Entradas/Saídas',\n" +
                "                       'width':430,\n" +
                "                       'height':350," +
                "                       colors:['green','red'],\n" +
                "                       pieHole: 0.5," +
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
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Relátorio");
            ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

}