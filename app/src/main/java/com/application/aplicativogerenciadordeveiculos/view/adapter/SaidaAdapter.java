package com.application.aplicativogerenciadordeveiculos.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.databinding.ItemListRowMovimentacoesBinding;
import com.application.aplicativogerenciadordeveiculos.model.Saida;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;

public class SaidaAdapter extends RecyclerView.Adapter<SaidaAdapter.MyViewHolder> {
    private List<Saida> listaSaidas;
    private SaidaOnClickListener saidaOnClickListener;
    private SaidaOnClickListener saidaOnDeleteListener;
    private SaidaOnClickListener saidaOnEditListener;

    public SaidaAdapter(List<Saida> listaSaidas,
                        SaidaOnClickListener saidaOnClickListener,
                        SaidaOnClickListener saidaOnDeleteListener,
                        SaidaOnClickListener saidaOnEditListener) {
        this.listaSaidas = listaSaidas;
        this.saidaOnClickListener = saidaOnClickListener;
        this.saidaOnDeleteListener = saidaOnDeleteListener;
        this.saidaOnEditListener = saidaOnEditListener;
    }

    @NonNull
    @Override
    public SaidaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListRowMovimentacoesBinding itemListRowMovimentacoesBinding = ItemListRowMovimentacoesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SaidaAdapter.MyViewHolder(itemListRowMovimentacoesBinding);
    }

    @Override
    public void onBindViewHolder(final SaidaAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Saida saida = listaSaidas.get(position);
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String valorSaida = df.format(saida.getValor());

        String data = saida.getData().toString();
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("EEE MMM dd HH:mm:ss O yyyy").toFormatter(Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(data, inputFormatter);
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormatada = zonedDateTime.format(formatadorData);

        holder.itemListRowBinding.tvItemData.setText(dataFormatada);
        holder.itemListRowBinding.tvItemValor.setText("R$" + valorSaida.replace(".", ","));
        holder.itemListRowBinding.tvItemDescricao.setText(saida.getDescricao());
        holder.itemListRowBinding.tvItemQuilometragem.setText(String.valueOf(saida.getQuilometragem()) + " KM");

        if (saida.getTipo() == 1) {
            holder.itemListRowBinding.ivTipoMovimentacao.setImageResource(R.drawable.ic_abastecimento);
        } else if (saida.getTipo() == 2) {
            holder.itemListRowBinding.ivTipoMovimentacao.setImageResource(R.drawable.ic_manutencao);
        } else if (saida.getTipo() == 3 || saida.getTipo() == 4) {
            holder.itemListRowBinding.ivTipoMovimentacao.setImageResource(R.drawable.ic_imposto_seguro);
        } else {
            holder.itemListRowBinding.ivTipoMovimentacao.setImageResource(R.drawable.ic_pagamento);
        }

        if (saidaOnClickListener != null) {
            holder.itemListRowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saidaOnClickListener.onClickSaida(holder.itemView, position, saida);
                }
            });
        }

        if (saidaOnDeleteListener != null) {
            holder.itemListRowBinding.ivMovimentacaoExcluir.setOnClickListener(onClickListener -> {
                saidaOnDeleteListener.onClickSaida(holder.itemView, position, saida);
            });
        }

        if (saidaOnEditListener != null) {
            holder.itemListRowBinding.ivMovimentacaoEditar.setOnClickListener(onclickListener -> {
                saidaOnEditListener.onClickSaida(holder.itemView, position, saida);
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaSaidas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ItemListRowMovimentacoesBinding itemListRowBinding;

        public MyViewHolder(ItemListRowMovimentacoesBinding itemListRowBinding) {
            super(itemListRowBinding.getRoot());
            this.itemListRowBinding = itemListRowBinding;
        }
    }

    public interface SaidaOnClickListener {
        public void onClickSaida(View view, int position, Saida saida);
    }
}
