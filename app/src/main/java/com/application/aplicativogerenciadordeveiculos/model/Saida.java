package com.application.aplicativogerenciadordeveiculos.model;

import java.util.Date;

public class Saida {
    private Veiculo veiculo;
    private int tipo;
    private float valor;
    private String descricao;
    private int quilometragem;
    private float litrosAbastecidos;
    private float mediaCombustivel;
    private Date data;

    public Saida(Veiculo veiculo, int tipo, float valor, String descricao, int quilometragem, Date data) {
        this.veiculo = veiculo;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.quilometragem = quilometragem;
        this.data = data;
    }

    public Saida(Veiculo veiculo, int tipo, float valor, String descricao, int quilometragem, float litrosAbastecidos, float mediaCombustivel, Date data) {
        this.veiculo = veiculo;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.quilometragem = quilometragem;
        this.litrosAbastecidos = litrosAbastecidos;
        this.mediaCombustivel = mediaCombustivel;
        this.data = data;
    }

    public Saida() {
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

    public int getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public float getLitrosAbastecidos() {
        return litrosAbastecidos;
    }

    public void setLitrosAbastecidos(float litrosAbastecidos) {
        this.litrosAbastecidos = litrosAbastecidos;
    }

    public float getMediaCombustivel() {
        return mediaCombustivel;
    }

    public void setMediaCombustivel(float mediaCombustivel) {
        this.mediaCombustivel = mediaCombustivel;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    public String getTipoLiteral(){
        String retorno = "Indefinido";
        if(this.tipo == 1){
            retorno = "Abastecimento";
        } else if(this.tipo == 2){
            retorno = "Manutenção";
        } else if(this.tipo == 3){
            retorno = "Impostos";
        } else if(this.tipo == 4){
            retorno = "Seguro";
        } else if(this.tipo == 5){
            retorno = "Aluguel";
        } else{
            retorno = "Outro";
        }
        return retorno;
    }
}
