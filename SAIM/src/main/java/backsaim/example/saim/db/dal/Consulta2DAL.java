package backsaim.example.saim.db.dal;

import backsaim.example.saim.db.entidade.Consulta2;
import backsaim.example.saim.db.util.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Consulta2DAL {

    public boolean gravar(Consulta2 consulta) {
        String sql = """
            INSERT INTO med_consulta (
                medcrm, mednome, medespecialidade, condata, horarioconsulta,
                pacinome, pacicpf, paciconvenio, numconvenio,
                dataultimaconsulta, contipo
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection conn = SingletonDB.getConexao().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consulta.getMedCRM());
            stmt.setString(2, consulta.getMedNome());
            stmt.setString(3, consulta.getMedEspecialidade());
            stmt.setDate(4, consulta.getConData());
            stmt.setInt(5, consulta.getHorarioConsulta());
            stmt.setString(6, consulta.getPaciNome());
            stmt.setString(7, consulta.getPaciCPF());
            stmt.setInt(8, consulta.getPaciConvenio());
            stmt.setInt(9, consulta.getNumConvenio());
            stmt.setDate(10, consulta.getDataUltimaConsulta());
            stmt.setString(11, String.valueOf(consulta.getConTipo()));

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
