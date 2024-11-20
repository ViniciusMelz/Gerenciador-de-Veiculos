package com.application.aplicativogerenciadordeveiculos.model;

import java.io.Serializable;

public class Veiculo implements Serializable {

    private String id;
    private String marca;
    private String modelo;
    private int ano;
    private String placa;
    private int tipo;
    private Usuario usuarioDono;
    private int quilometragem;
    private float mediaCombustivel;
    private float valorTotalSaidas;
    private float valorTotalEntradas;

    public Veiculo(String marca, String modelo, int ano, String placa, int tipo, Usuario usuarioDono, int quilometragem) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.placa = placa;
        this.tipo = tipo;
        this.usuarioDono = usuarioDono;
        this.quilometragem = quilometragem;
    }

    public Veiculo(String id, String marca, String modelo, int ano, String placa, int tipo, Usuario usuarioDono, int quilometragem, float mediaCombustivel, float valorTotalSaidas, float valorTotalEntradas) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.placa = placa;
        this.tipo = tipo;
        this.usuarioDono = usuarioDono;
        this.quilometragem = quilometragem;
        this.mediaCombustivel = mediaCombustivel;
        this.valorTotalSaidas = valorTotalSaidas;
        this.valorTotalEntradas = valorTotalEntradas;
    }

    public Veiculo(String marca, String modelo, int ano, String placa, int tipo, Usuario usuarioDono) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.placa = placa;
        this.tipo = tipo;
        this.usuarioDono = usuarioDono;
    }

    public Veiculo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuarioDono() {
        return usuarioDono;
    }

    public void setUsuarioDono(Usuario usuario) {
        this.usuarioDono = usuario;
    }

    public String getTipoLiteral() {
        String retorno = "";
        if (this.tipo == 1) {
            retorno = "Moto";
        } else if (this.tipo == 2) {
            retorno = "Carro";
        } else if (this.tipo == 3) {
            retorno = "Caminhão";
        } else if (this.tipo == 4) {
            retorno = "Ônibus";
        } else {
            retorno = "Outro";
        }
        return retorno;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public float getMediaCombustivel() {
        return mediaCombustivel;
    }

    public void setMediaCombustivel(float mediaCombustivel) {
        this.mediaCombustivel = mediaCombustivel;
    }

    public float getValorTotalSaidas() {
        return valorTotalSaidas;
    }

    public void setValorTotalSaidas(float valorTotalSaidas) {
        this.valorTotalSaidas = valorTotalSaidas;
    }

    public float getValorTotalEntradas() {
        return valorTotalEntradas;
    }

    public void setValorTotalEntradas(float valorTotalEntradas) {
        this.valorTotalEntradas = valorTotalEntradas;
    }

    @Override
    public String toString() {
        return marca + " " + modelo;
    }
}
