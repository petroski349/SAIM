package com.example.saim.Models.Service;

import org.springframework.stereotype.Service;

@Service
public class SingletonDB
{
    private static Conexao conexao = null;

    private SingletonDB() {
    }

    public static Conexao getConexao() {
        if (conexao == null) {
            conexao = new Conexao();
            boolean conectado = conexao.conectar(
                    "jdbc:postgresql://localhost:5432/",
                    "engII",
                    "postgres",
                    "Postgres123"
            );
            if (!conectado) {
                System.err.println("❌ Falha ao conectar ao banco de dados!");
                conexao = null;
            }
        }
        return conexao;
    }
}



