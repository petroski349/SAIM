package backsaim.example.saim.db.dal;

import backsaim.example.saim.db.entidade.Paciente;
import backsaim.example.saim.db.util.Conexao;
import backsaim.example.saim.db.util.SingletonDB;
import backsaim.example.saim.db.util.Utils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacienteDAL implements IDAL<Paciente> {

    public boolean gravar(Paciente entidade) {
        String sql = "INSERT INTO paciente (paciNome, paciCPF, paciConvenio, paciData_Internacao, paciData_Alta, paciEndereco, paciRg, paciTelefone, paciData_Nascimento, paciCep) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SingletonDB.getConexao().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entidade.getPaciNome());
            ps.setString(2, Utils.aplicarMascaraCpf(entidade.getPaciCPF()));
            ps.setString(3, entidade.getPaciConvenio());
            ps.setDate(4, Date.valueOf(entidade.getPaciDataInternacao()));
            ps.setDate(5, Date.valueOf(entidade.getPaciDataAlta()));
            ps.setString(6, entidade.getPaciEndereco());
            ps.setString(7, entidade.getPaciRg());
            ps.setString(8, entidade.getPaciTelefone());
            ps.setDate(9, Date.valueOf(entidade.getPaciDataNascimento()));
            ps.setString(10, Utils.aplicarMascaraCep(entidade.getPaciCep()));

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alterar(Paciente paciente) {
        String sql = "UPDATE paciente SET paciNome = ?, paciCPF = ?, paciConvenio = ?, paciData_Internacao = ?, paciData_Alta = ?, paciEndereco = ?, paciRg = ?, paciTelefone = ?, paciData_Nascimento = ?, paciCep = ? WHERE paciId = ?";
        try (Connection conn = SingletonDB.getConexao().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getPaciNome());
            ps.setString(2, paciente.getPaciCPF());
            ps.setString(3, paciente.getPaciConvenio());
            ps.setDate(4, Date.valueOf(paciente.getPaciDataInternacao()));
            ps.setDate(5, Date.valueOf(paciente.getPaciDataAlta()));
            ps.setString(6, paciente.getPaciEndereco());
            ps.setString(7, paciente.getPaciRg());
            ps.setString(8, paciente.getPaciTelefone());
            ps.setDate(9, Date.valueOf(paciente.getPaciDataNascimento()));
            ps.setString(10, paciente.getPaciCep());
            ps.setInt(11, paciente.getPaciId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean apagar(Paciente entidade) {
        String sql = "DELETE FROM paciente WHERE paciId = ?";
        try (Connection conn = SingletonDB.getConexao().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidade.getPaciId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Paciente> buscarPorId(int id) {
        String sql = "SELECT * FROM pacientes WHERE paciId = ?";
        try (Connection conn = SingletonDB.getConexao().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Paciente paciente = new Paciente(
                            rs.getInt("paciId"),
                            rs.getString("paciNome"),
                            rs.getString("paciCPF"),
                            rs.getString("paciConvenio"),
                            rs.getDate("paciData_Internacao").toLocalDate(),
                            rs.getDate("paciData_Alta").toLocalDate(),
                            rs.getString("paciEndereco"),
                            rs.getString("paciRg"),
                            rs.getString("paciTelefone"),
                            rs.getDate("paciData_Nascimento").toLocalDate(),
                            rs.getString("paciCep")
                    );
                    return Optional.of(paciente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Paciente> buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM pacientes WHERE paciCPF = ?";

        try (Connection conn = SingletonDB.getConexao().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate dataInternacao = null;
                    LocalDate dataAlta = null;
                    LocalDate dataNascimento = null;

                    Date dtInternacao = rs.getDate("paciData_Internacao");
                    if (dtInternacao != null) dataInternacao = dtInternacao.toLocalDate();

                    Date dtAlta = rs.getDate("paciData_Alta");
                    if (dtAlta != null) dataAlta = dtAlta.toLocalDate();

                    Date dtNascimento = rs.getDate("paciData_Nascimento");
                    if (dtNascimento != null) dataNascimento = dtNascimento.toLocalDate();

                    Paciente paciente = new Paciente(
                            rs.getInt("paciId"),
                            rs.getString("paciNome"),
                            rs.getString("paciCPF"),
                            rs.getString("paciConvenio"),
                            dataInternacao,
                            dataAlta,
                            rs.getString("paciEndereco"),
                            rs.getString("paciRg"),
                            rs.getString("paciTelefone"),
                            dataNascimento,
                            rs.getString("paciCep")
                    );
                    return Optional.of(paciente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean cpfJaExiste(String cpf) {
        String sql = "SELECT 1 FROM paciente WHERE paciCPF = ? LIMIT 1";

        try (Connection conn = SingletonDB.getConexao().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean rgJaExiste(String rg) {
        String sql = "SELECT 1 FROM paciente WHERE paciRg = ? LIMIT 1";
        try (Connection conn = SingletonDB.getConexao().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rg);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Paciente get(int id) {
        String sql = "SELECT * FROM paciente WHERE paciId = ?";

        try (Connection conn = SingletonDB.getConexao().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                            rs.getInt("paciId"),
                            rs.getString("paciNome"),
                            rs.getString("paciCPF"),
                            rs.getString("paciConvenio"),
                            rs.getDate("paciData_Internacao").toLocalDate(),
                            rs.getDate("paciData_Alta").toLocalDate(),
                            rs.getString("paciEndereco"),
                            rs.getString("paciRg"),
                            rs.getString("paciTelefone"),
                            rs.getDate("paciData_Nascimento").toLocalDate(),
                            rs.getString("paciCep")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Paciente> get(String filtro) {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente";
        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        try (Connection conn = SingletonDB.getConexao().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pacientes.add(new Paciente(
                        rs.getInt("paciId"),
                        rs.getString("paciNome"),
                        rs.getString("paciCPF"),
                        rs.getString("paciConvenio"),
                        rs.getDate("paciData_Internacao").toLocalDate(),
                        rs.getDate("paciData_Alta").toLocalDate(),
                        rs.getString("paciEndereco"),
                        rs.getString("paciRg"),
                        rs.getString("paciTelefone"),
                        rs.getDate("paciData_Nascimento").toLocalDate(),
                        rs.getString("paciCep")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pacientes;
    }

    public List<Paciente> listarpacientes() {
        return get(null);
    }

}
