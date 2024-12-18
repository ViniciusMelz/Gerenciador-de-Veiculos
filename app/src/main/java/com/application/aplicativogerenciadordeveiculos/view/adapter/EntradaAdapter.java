package com.application.aplicativogerenciadordeveiculos.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.databinding.ItemListRowMovimentacoesBinding;
import com.application.aplicativogerenciadordeveiculos.model.Entrada;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;

public class EntradaAdapter extends RecyclerView.Adapter<EntradaAdapter.MyViewHolder> {
    private List<Entrada> listaEntradas;
    private EntradaOnClickListener entradaOnClickListener;
    private EntradaOnClickListener entradaOnDeleteListener;
    private EntradaOnClickListener entradaOnEditListener;

    public EntradaAdapter(List<Entrada> listaEntradas,
                          EntradaOnClickListener entradaOnClickListener,
                          EntradaOnClickListener entradaOnDeleteListener,
                          EntradaOnClickListener entradaOnEditListener) {
        this.listaEntradas = listaEntradas;
        this.entradaOnClickListener = entradaOnClickListener;
        this.entradaOnDeleteListener = entradaOnDeleteListener;
        this.entradaOnEditListener = entradaOnEditListener;
    }

    @NonNull
    @Override
    public EntradaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListRowMovimentacoesBinding itemListRowMovimentacoesBinding = ItemListRowMovimentacoesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EntradaAdapter.MyViewHolder(itemListRowMovimentacoesBinding);
    }

    @Override
    public void onBindViewHolder(final EntradaAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Entrada entrada = listaEntradas.get(position);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String valorEntrada = df.format(entrada.getValor());

        String data = entrada.getData().toString();
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("EEE MMM dd HH:mm:ss O yyyy").toFormatter(Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(data, inputFormatter);
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormatada = zonedDateTime.format(formatadorData);

        holder.itemListRowBinding.tvItemData.setText(dataFormatada);
        holder.itemListRowBinding.tvItemValor.setText("R$" + valorEntrada.replace(".", ","));
        if(entrada.getDescricao().equals("")){
            holder.itemListRowBinding.tvItemDescricao.setText("Não Informado!");
        }else{
            holder.itemListRowBinding.tvItemDescricao.setText(entrada.getDescricao());
        }
        holder.itemListRowBinding.tvItemQuilometragem.setText(String.valueOf(entrada.getQuilometragem()) + " KM");

        if (entrada.getTipo() == 1) {
            holder.itemListRowBinding.ivTipoMovimentacao.setImageResource(R.drawable.ic_motoboy);
        } else if (entrada.getTipo() == 2) {
            holder.itemListRowBinding.ivTipoMovimentacao.setImageResource(R.drawable.ic_motorista);
        } else if (entrada.getTipo() == 3) {
            holder.itemListRowBinding.ivTipoMovimentacao.setImageResource(R.drawable.ic_frete);
        } else {
            holder.itemListRowBinding.ivTipoMovimentacao.setImageResource(R.drawable.ic_dinheiro);
        }

        if (entradaOnClickListener != null) {
            holder.itemListRowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    entradaOnClickListener.onClickEntrada(holder.itemView, position, entrada);
                }
            });
        }

        if (entradaOnDeleteListener != null) {
            holder.itemListRowBinding.ivMovimentacaoExcluir.setOnClickListener(onClickListener -> {
                entradaOnDeleteListener.onClickEntrada(holder.itemView, position, entrada);
            });
        }

        if (entradaOnEditListener != null) {
            holder.itemListRowBinding.ivMovimentacaoEditar.setOnClickListener(onclickListener -> {
                entradaOnEditListener.onClickEntrada(holder.itemView, position, entrada);
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaEntradas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ItemListRowMovimentacoesBinding itemListRowBinding;

        public MyViewHolder(ItemListRowMovimentacoesBinding itemListRowBinding) {
            super(itemListRowBinding.getRoot());
            this.itemListRowBinding = itemListRowBinding;
        }
    }

    public interface EntradaOnClickListener {
        public void onClickEntrada(View view, int position, Entrada entrada);
    }
}
