package br.com.smartagro.clima;

import java.io.Serializable;

public class Cidade implements Serializable {
    private String nome;
    private String uf;
    private String id;

    public Cidade() {
        setId("");
        setNome("");
        setUf("");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
