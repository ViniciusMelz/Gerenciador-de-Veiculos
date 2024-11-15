package com.application.aplicativogerenciadordeveiculos.view.adapter;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.aplicativogerenciadordeveiculos.R;
import com.application.aplicativogerenciadordeveiculos.databinding.ItemListRowVeiculoBinding;
import com.application.aplicativogerenciadordeveiculos.model.Veiculo;

import java.text.SimpleDateFormat;
import java.util.List;

public class VeiculoAdapter extends RecyclerView.Adapter<VeiculoAdapter.MyViewHolder> {
    private List<Veiculo> listaVeiculos;
    private VeiculoOnClickListener veiculoOnClickListener;
    private VeiculoOnClickListener veiculoOnDeleteListener;
    private VeiculoOnClickListener veiculoOnEditListener;

    public VeiculoAdapter(List<Veiculo> listaVeiculos,
                          VeiculoOnClickListener veiculoOnClickListener,
                          VeiculoOnClickListener veiculoOnDeleteListener,
                          VeiculoOnClickListener veiculoOnEditListener) {
        this.listaVeiculos = listaVeiculos;
        this.veiculoOnClickListener = veiculoOnClickListener;
        this.veiculoOnDeleteListener = veiculoOnDeleteListener;
        this.veiculoOnEditListener = veiculoOnEditListener;
    }

    @NonNull
    @Override
    public VeiculoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListRowVeiculoBinding itemListRowVeiculoBinding = ItemListRowVeiculoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VeiculoAdapter.MyViewHolder(itemListRowVeiculoBinding);
    }

    @Override
    public void onBindViewHolder(final VeiculoAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Veiculo veiculo = listaVeiculos.get(position);
        holder.itemListRowBinding.tvItemPlaca.setText(veiculo.getPlaca().toUpperCase());
        holder.itemListRowBinding.tvItemMarcaModelo.setText(veiculo.getMarca() + " " + veiculo.getModelo());
        holder.itemListRowBinding.tvItemAno.setText(Integer.toString(veiculo.getAno()));

        if(veiculo.getTipo() == 1){
            holder.itemListRowBinding.ivTipoVeiculo.setImageResource(R.drawable.ic_moto);
        }else if(veiculo.getTipo() == 2){
            holder.itemListRowBinding.ivTipoVeiculo.setImageResource(R.drawable.ic_carro);
        }else if(veiculo.getTipo() == 3){
            holder.itemListRowBinding.ivTipoVeiculo.setImageResource(R.drawable.ic_caminhao);
        }else if(veiculo.getTipo() == 4){
            holder.itemListRowBinding.ivTipoVeiculo.setImageResource(R.drawable.ic_onibus);
        }else{
            holder.itemListRowBinding.ivTipoVeiculo.setImageResource(R.drawable.ic_roda);
        }

        if (veiculoOnClickListener != null) {
            holder.itemListRowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    veiculoOnClickListener.onClickVeiculo(holder.itemView, position, veiculo);
                }
            });
        }

        if (veiculoOnDeleteListener != null) {
            holder.itemListRowBinding.ivVeiculoExcluir.setOnClickListener(onClickListener -> {
                veiculoOnDeleteListener.onClickVeiculo(holder.itemView, position, veiculo);
            });
        }

        if (veiculoOnEditListener != null) {
            holder.itemListRowBinding.ivVeiculoEditar.setOnClickListener(onclickListener -> {
                veiculoOnEditListener.onClickVeiculo(holder.itemView, position, veiculo);
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaVeiculos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ItemListRowVeiculoBinding itemListRowBinding;
        public MyViewHolder(ItemListRowVeiculoBinding itemListRowBinding) {
            super(itemListRowBinding.getRoot());
            this.itemListRowBinding = itemListRowBinding;
        }
    }

    public interface VeiculoOnClickListener {
        public void onClickVeiculo(View view, int position, Veiculo veiculo);
    }
}