package com.application.aplicativogerenciadordeveiculos.model;

import java.util.Date;

public class Entrada {
    private Veiculo veiculo;
    private int tipo;
    private float valor;
    private String descricao;
    private int quilometragem;
    private Date data;

    public Entrada(Veiculo veiculo, int tipo, float valor, String descricao, Date data, int quilometragem) {
        this.veiculo = veiculo;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.quilometragem = quilometragem;
    }

    public Entrada() {
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public String getTipoLiteral(){
        String retorno = "Indefinido";
        if(this.tipo == 1){
            retorno = "Motoboy";
        } else if(this.tipo == 2){
            retorno = "Motorista de Aplicativo";
        } else if(this.tipo == 3){
            retorno = "Frete";
        } else if(this.tipo == 4){
            retorno = "Aluguel do Veículo";
        } else{
            retorno = "Outro";
        }
        return retorno;
    }
}
