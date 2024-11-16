package com.application.aplicativogerenciadordeveiculos.model;

import java.util.Date;

public class Entradas {
    private Veiculo veiculo;
    private int tipo;
    private float valor;
    private String descricao;
    private int odometro;
    private Date data;

    public Entradas(Veiculo veiculo, int tipo, float valor, String descricao, Date data, int odometro) {
        this.veiculo = veiculo;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.odometro = odometro;
    }

    public Entradas() {
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

    public int getOdometro() {
        return odometro;
    }

    public void setOdometro(int odometro) {
        this.odometro = odometro;
    }
}
