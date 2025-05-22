package com.example.saim.Models.Entitys;

import java.sql.Date;
import java.time.LocalDateTime;


public class Usuario {
    long id;
    String nome;
    String email;
    String senha;
    LocalDateTime dataRegistro;
    char tipo;

    public Usuario(long id, char tipo, LocalDateTime dataRegistro, String senha, String email, String nome) {
        this.id = id;
        this.tipo = tipo;
        this.dataRegistro = dataRegistro;
        this.senha = senha;
        this.email = email;
        this.nome = nome;
    }

    public Usuario() {}

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}



