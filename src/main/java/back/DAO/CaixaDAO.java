package back.DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import back.Caixa;
import back.Conexao.Conexao;
import back.MovimentacaoCaixa;

public class CaixaDAO {

    // Método para abrir um novo caixa (corrigido para schema atual)
    public boolean abrirCaixa(Caixa caixa) {
        // Insere na tabela 'caixa' usando os nomes corretos das colunas
        String sql = "INSERT INTO caixa (usu_abertura_id, saldo_inicial, observacoes, status, data_abertura) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, caixa.getUsuarioAberturaId());
            stmt.setBigDecimal(2, caixa.getSaldoInicial());
            stmt.setString(3, caixa.getObservacoes());
            stmt.setString(4, "aberto"); // Status inicial
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis())); // Data de abertura atual

            int rowsAffected = stmt.executeUpdate();

            // Obter o ID gerado para o caixa (opcional, mas útil)
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        caixa.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Erro ao abrir caixa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para fechar o caixa (corrigido para schema atual)
    public boolean fecharCaixa(int caixaId, int usuarioFechamentoId, BigDecimal saldoFinalCalculado, String observacoes) {
        // Atualiza a tabela 'caixa' usando os nomes corretos
        String sql = "UPDATE caixa SET status = ?, data_fechamento = ?, usu_fechamento_id = ?, saldo_final = ?, observacoes = ? " +
                     "WHERE cai_id = ? AND status = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "fechado");
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(3, usuarioFechamentoId);
            stmt.setBigDecimal(4, saldoFinalCalculado);
            stmt.setString(5, observacoes);
            stmt.setInt(6, caixaId);
            stmt.setString(7, "aberto"); // Só pode fechar caixa que está aberto

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao fechar caixa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para registrar uma movimentação no caixa (corrigido para schema atual)
    public boolean registrarMovimentacao(MovimentacaoCaixa movimentacao) {
        // Insere na tabela 'movimentacoes_caixa' usando nomes corretos
        String sql = "INSERT INTO movimentacoes_caixa (cai_id, usu_registro_id, tipo, valor, descricao, data_hora) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, movimentacao.getCaixaId());
            stmt.setInt(2, movimentacao.getUsuarioRegistroId());
            stmt.setString(3, movimentacao.getTipo()); // 'entrada' ou 'saida'
            stmt.setBigDecimal(4, movimentacao.getValor());
            stmt.setString(5, movimentacao.getDescricao());
            stmt.setTimestamp(6, movimentacao.getDataHora() != null ? movimentacao.getDataHora() : new Timestamp(System.currentTimeMillis()));

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao registrar movimentação: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar o caixa aberto atual (corrigido para schema atual)
    public Caixa buscarCaixaAberto() {
        // Busca na tabela 'caixa' usando nomes corretos
        String sql = "SELECT * FROM caixa WHERE status = ? ORDER BY data_abertura DESC LIMIT 1";
        Caixa caixa = null;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "aberto");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                caixa = mapResultSetToCaixa(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar caixa aberto: " + e.getMessage());
            e.printStackTrace();
        }
        return caixa;
    }

     // Método para buscar um caixa por ID (corrigido)
    public Caixa buscarPorId(int caixaId) {
        String sql = "SELECT * FROM caixa WHERE cai_id = ?";
        Caixa caixa = null;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, caixaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                caixa = mapResultSetToCaixa(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar caixa por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return caixa;
    }

    // Método para listar movimentações de um caixa (corrigido para schema atual)
    public List<MovimentacaoCaixa> listarMovimentacoes(int caixaId) {
        // Busca na tabela 'movimentacoes_caixa' usando nomes corretos
        String sql = "SELECT * FROM movimentacoes_caixa WHERE cai_id = ? ORDER BY data_hora ASC";
        List<MovimentacaoCaixa> movimentacoes = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, caixaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MovimentacaoCaixa mov = mapResultSetToMovimentacao(rs);
                movimentacoes.add(mov);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar movimentações: " + e.getMessage());
            e.printStackTrace();
        }
        return movimentacoes;
    }

    // Método para calcular o saldo atual do caixa (corrigido para schema atual)
    public BigDecimal calcularSaldoAtual(int caixaId) {
        // Calcula o saldo usando nomes corretos e retorna BigDecimal
        String sqlSaldoInicial = "SELECT saldo_inicial FROM caixa WHERE cai_id = ?";
        String sqlMovimentacoes = "SELECT tipo, valor FROM movimentacoes_caixa WHERE cai_id = ?";
        BigDecimal saldoAtual = BigDecimal.ZERO;

        try (Connection conn = Conexao.conectar()) {
            // Busca saldo inicial
            try (PreparedStatement stmtInicial = conn.prepareStatement(sqlSaldoInicial)) {
                stmtInicial.setInt(1, caixaId);
                ResultSet rsInicial = stmtInicial.executeQuery();
                if (rsInicial.next()) {
                    saldoAtual = rsInicial.getBigDecimal("saldo_inicial");
                } else {
                    System.err.println("Caixa com ID " + caixaId + " não encontrado para cálculo de saldo.");
                    return BigDecimal.ZERO; // Retorna zero se o caixa não for encontrado
                }
            }

            // Soma/subtrai movimentações
            try (PreparedStatement stmtMov = conn.prepareStatement(sqlMovimentacoes)) {
                stmtMov.setInt(1, caixaId);
                ResultSet rsMov = stmtMov.executeQuery();
                while (rsMov.next()) {
                    String tipo = rsMov.getString("tipo");
                    BigDecimal valor = rsMov.getBigDecimal("valor");
                    if ("entrada".equalsIgnoreCase(tipo)) {
                        saldoAtual = saldoAtual.add(valor);
                    } else if ("saida".equalsIgnoreCase(tipo)) {
                        saldoAtual = saldoAtual.subtract(valor);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao calcular saldo atual do caixa: " + e.getMessage());
            e.printStackTrace();
            // Em caso de erro, talvez retornar null ou lançar exceção?
            // Por ora, retorna o que foi calculado até o erro ou zero.
        }
        return saldoAtual;
    }

    // Método para listar histórico de caixas (corrigido)
    public List<Caixa> listarHistoricoCaixas(int limit) {
        String sql = "SELECT * FROM caixa ORDER BY data_abertura DESC LIMIT ?";
        List<Caixa> caixas = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Caixa caixa = mapResultSetToCaixa(rs);
                caixas.add(caixa);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar histórico de caixas: " + e.getMessage());
            e.printStackTrace();
        }
        return caixas;
    }

    // Método auxiliar para mapear ResultSet para Objeto Caixa
    private Caixa mapResultSetToCaixa(ResultSet rs) throws SQLException {
        Caixa caixa = new Caixa();
        caixa.setId(rs.getInt("cai_id"));
        caixa.setUsuarioAberturaId(rs.getInt("usu_abertura_id"));
        // Trata campo nullable usu_fechamento_id
        int usuFechamentoId = rs.getInt("usu_fechamento_id");
        if (!rs.wasNull()) {
            caixa.setUsuarioFechamentoId(usuFechamentoId);
        }
        caixa.setDataAbertura(rs.getTimestamp("data_abertura"));
        caixa.setDataFechamento(rs.getTimestamp("data_fechamento"));
        caixa.setSaldoInicial(rs.getBigDecimal("saldo_inicial"));
        caixa.setSaldoFinal(rs.getBigDecimal("saldo_final"));
        caixa.setObservacoes(rs.getString("observacoes"));
        caixa.setStatus(rs.getString("status"));
        return caixa;
    }

    // Método auxiliar para mapear ResultSet para Objeto MovimentacaoCaixa
    private MovimentacaoCaixa mapResultSetToMovimentacao(ResultSet rs) throws SQLException {
        MovimentacaoCaixa mov = new MovimentacaoCaixa();
        mov.setId(rs.getInt("mov_id"));
        mov.setCaixaId(rs.getInt("cai_id"));
        mov.setUsuarioRegistroId(rs.getInt("usu_registro_id"));
        mov.setDataHora(rs.getTimestamp("data_hora"));
        mov.setTipo(rs.getString("tipo"));
        mov.setDescricao(rs.getString("descricao"));
        mov.setValor(rs.getBigDecimal("valor"));
        // Adicionar outros campos se existirem no SELECT e no modelo (categoria, anexo, etc.)
        // mov.setCategoria(rs.getString("mov_categoria"));
        // mov.setObservacoes(rs.getString("mov_observacoes"));
        // mov.setAnexo(rs.getString("mov_anexo"));
        return mov;
    }
}

