package com.example.socketsproject;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String nome;
    private String userName;

    public Usuario(String nome, String userName) {
        this.nome = nome;
        this.userName = userName;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNome() {
        return nome;
    }

    public String getUserName() {
        return userName;
    }
}
