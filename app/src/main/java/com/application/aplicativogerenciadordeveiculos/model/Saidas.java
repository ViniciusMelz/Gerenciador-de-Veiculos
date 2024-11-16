package com.application.aplicativogerenciadordeveiculos.model;

import java.util.Date;

public class Saidas {
    private Veiculo veiculo;
    private int tipo;
    private float valor;
    private String descricao;
    private int odometro;
    private float litrosAbastecidos;
    private float mediaCombustivel;
    private Date data;

    public Saidas(Veiculo veiculo, int tipo, float valor, String descricao, int odometro, Date data) {
        this.veiculo = veiculo;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.odometro = odometro;
        this.data = data;
    }

    public Saidas(Veiculo veiculo, int tipo, float valor, String descricao, int odometro, float litrosAbastecidos, float mediaCombustivel, Date data) {
        this.veiculo = veiculo;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.odometro = odometro;
        this.litrosAbastecidos = litrosAbastecidos;
        this.mediaCombustivel = mediaCombustivel;
        this.data = data;
    }

    public Saidas() {
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

    public int getOdometro() {
        return odometro;
    }

    public void setOdometro(int odometro) {
        this.odometro = odometro;
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
}
