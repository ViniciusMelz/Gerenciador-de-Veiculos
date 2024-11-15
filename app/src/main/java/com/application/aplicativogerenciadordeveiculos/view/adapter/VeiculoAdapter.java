package com.application.aplicativogerenciadordeveiculos.view.adapter;
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

    private VeiculoAdapter.VeiculoOnClickListener VeiculoOnClickListener;

    public VeiculoAdapter(List<Veiculo> listaVeiculos/*, VeiculoAdapter.VeiculoOnClickListener VeiculoOnClickListener*/) {
        this.listaVeiculos = listaVeiculos;
        //this.VeiculoOnClickListener = VeiculoOnClickListener;
    }

    @NonNull
    @Override
    public VeiculoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListRowVeiculoBinding itemListRowEventoBinding = ItemListRowVeiculoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VeiculoAdapter.MyViewHolder(itemListRowEventoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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

        /*if (VeiculoOnClickListener != null) {
            holder.itemListRowBinding.getRoot().setOnClickListener(view -> {
                VeiculoOnClickListener.onClickVeiculo(holder.itemView, position, veiculo);
            });
        }*/
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