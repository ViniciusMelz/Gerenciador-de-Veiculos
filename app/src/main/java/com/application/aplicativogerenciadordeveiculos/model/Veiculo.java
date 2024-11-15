package com.application.aplicativogerenciadordeveiculos.model;

import java.io.Serializable;

public class Veiculo implements Serializable {
    private String marca;
    private String modelo;
    private int ano;
    private String placa;
    private int tipo;
    private Usuario usuarioDono;

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
}
