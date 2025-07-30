package com.example.cadastroaluno.src;
public class Usuario {
    private String nome, senha, tipo;
    public Usuario(String nome, String senha, String tipo){
        this.nome = nome;
        this.senha = senha;
        this.tipo  = tipo;
    }
    public String getNome(){
        return nome;
    }
    public String getSenha(){
        return senha;
    }
}