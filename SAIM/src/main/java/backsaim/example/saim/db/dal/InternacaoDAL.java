package backsaim.example.saim.db.dal;

import backsaim.example.saim.db.entidade.Internacao;
import backsaim.example.saim.db.util.SingletonDB;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InternacaoDAL implements IDAL<Internacao> {

    @Override
    public boolean gravar(Internacao entidade) {
        String sql = """
            INSERT INTO internacao (PaciNome, DataEntrada, DataSaida)
            VALUES ('#1', '#2', #3);
        """;
        sql = sql.replace("#1", entidade.getPaciNome());
        sql = sql.replace("#2", entidade.getDataEntrada().toString());

        if (entidade.getDataSaida() != null)
            sql = sql.replace("#3", "'" + entidade.getDataSaida().toString() + "'");
        else
            sql = sql.replace("#3", "NULL");

        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean alterar(Internacao entidade) {
        String sql = """
            UPDATE internacao SET
                PaciNome = '#1',
                DataEntrada = '#2',
                DataSaida = #3
            WHERE Prontuario_ID = #4;
        """;
        sql = sql.replace("#1", entidade.getPaciNome());
        sql = sql.replace("#2", entidade.getDataEntrada().toString());
        sql = sql.replace("#4", String.valueOf(entidade.getProntuarioId()));

        if (entidade.getDataSaida() != null)
            sql = sql.replace("#3", "'" + entidade.getDataSaida().toString() + "'");
        else
            sql = sql.replace("#3", "NULL");

        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Internacao entidade) {
        return SingletonDB.getConexao().manipular("DELETE FROM internacao WHERE Prontuario_ID = " + entidade.getProntuarioId());
    }

    @Override
    public Internacao get(int id) {
        Internacao internacao = null;
        String sql = "SELECT * FROM internacao WHERE Prontuario_ID = " + id;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                internacao = new Internacao(
                        rs.getInt("Prontuario_ID"),
                        rs.getString("PaciNome"),
                        rs.getDate("DataEntrada").toLocalDate(),
                        rs.getDate("DataSaida") != null ? rs.getDate("DataSaida").toLocalDate() : null
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return internacao;
    }

    @Override
    public List<Internacao> get(String filtro) {
        List<Internacao> internacoes = new ArrayList<>();
        String sql = "SELECT * FROM internacao";
        if (!filtro.isEmpty())
            sql += " WHERE " + filtro;

        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Internacao internacao = new Internacao(
                        rs.getInt("Prontuario_ID"),
                        rs.getString("PaciNome"),
                        rs.getDate("DataEntrada").toLocalDate(),
                        rs.getDate("DataSaida") != null ? rs.getDate("DataSaida").toLocalDate() : null
                );
                internacoes.add(internacao);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return internacoes;
    }

    public List<Internacao> listarInternacoes() {
        List<Internacao> internacoes = new ArrayList<>();
        String sql = "SELECT * FROM internacao ORDER BY PaciNome";
        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            while (rs != null && rs.next()) {
                java.sql.Date dataSaidaSQL = rs.getDate("DataSaida");
                LocalDate dataSaida = (dataSaidaSQL != null) ? dataSaidaSQL.toLocalDate() : null;

                internacoes.add(new Internacao(
                        rs.getInt("Prontuario_ID"),
                        rs.getString("PaciNome"),
                        rs.getDate("DataEntrada").toLocalDate(),
                        dataSaida
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return internacoes;
    }



    public Internacao buscarPorProntuarioId(int prontuarioId) {

        return get(prontuarioId);
    }
}
