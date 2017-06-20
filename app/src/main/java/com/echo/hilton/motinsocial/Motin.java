package com.echo.hilton.motinsocial;

/**
 * Created by hilton on 20/06/17.
 */

public class Motin {
    public String nome;
    public String demanda;
    public String servico;
    public String email;

    public Motin(String nome, String demanda, String servico, String email) {
        this.nome = nome;
        this.demanda = demanda;
        this.servico = servico;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDemanda() {
        return demanda;
    }

    public void setDemanda(String demanda) {
        this.demanda = demanda;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }
}
