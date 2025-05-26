package backsaim.example.saim.db.dal;

import backsaim.example.saim.db.entidade.Consulta;
import backsaim.example.saim.db.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAL implements IDAL<Consulta> {

    @Override
    public boolean gravar(Consulta entidade) {
        String sql = """
            INSERT INTO consulta (MedID, PaciConvenio, ConTipo, ConData)
            VALUES (#1, '#2', '#3', '#4');
        """;
        sql = sql.replace("#1", String.valueOf(entidade.getMedId()))
                .replace("#2", entidade.getPaciConvenio())
                .replace("#3", entidade.getConTipo())
                .replace("#4", entidade.getConData().toString());

        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean alterar(Consulta entidade) {
        String sql = """
            UPDATE consulta SET
                MedID = #1, PaciConvenio = '#2', ConTipo = '#3', ConData = '#4'
            WHERE ConCod = #5;
        """;
        sql = sql.replace("#1", String.valueOf(entidade.getMedId()))
                .replace("#2", entidade.getPaciConvenio())
                .replace("#3", entidade.getConTipo())
                .replace("#4", entidade.getConData().toString())
                .replace("#5", String.valueOf(entidade.getConCod()));

        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Consulta entidade) {
        return SingletonDB.getConexao().manipular("DELETE FROM consulta WHERE ConCod = " + entidade.getConCod());
    }

    @Override
    public Consulta get(int id) {
        String sql = "SELECT * FROM consulta WHERE ConCod = " + id;
        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            if (rs != null && rs.next()) {
                return new Consulta(
                        rs.getInt("ConCod"),
                        rs.getInt("MedID"),
                        rs.getString("PaciConvenio"),
                        rs.getString("ConTipo"),
                        rs.getDate("ConData").toLocalDate()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Consulta> get(String filtro) {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT * FROM consulta";
        if (filtro != null && !filtro.isEmpty())
            sql += " WHERE " + filtro;

        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            while (rs != null && rs.next()) {
                consultas.add(new Consulta(
                        rs.getInt("ConCod"),
                        rs.getInt("MedID"),
                        rs.getString("PaciConvenio"),
                        rs.getString("ConTipo"),
                        rs.getDate("ConData").toLocalDate()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consultas;
    }

    public List<Consulta> listarConsultas() {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT * FROM consulta ORDER BY ConData, ConCod";
        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            while (rs != null && rs.next()) {
                consultas.add(new Consulta(
                        rs.getInt("ConCod"),
                        rs.getInt("MedID"),
                        rs.getString("PaciConvenio"),
                        rs.getString("ConTipo"),
                        rs.getDate("ConData").toLocalDate()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consultas;
    }
}
