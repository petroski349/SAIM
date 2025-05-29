package back;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Caixa {
    private int id; // Corresponde a cai_id
    private int usuarioAberturaId; // Corresponde a usu_abertura_id
    private Integer usuarioFechamentoId; // Corresponde a usu_fechamento_id (pode ser null)
    private Timestamp dataAbertura; // Corresponde a data_abertura
    private Timestamp dataFechamento; // Corresponde a data_fechamento (pode ser null)
    private BigDecimal saldoInicial; // Corresponde a saldo_inicial
    private BigDecimal saldoFinal; // Corresponde a saldo_final (pode ser null)
    private String observacoes; // Corresponde a observacoes
    private String status; // Corresponde a status ('aberto', 'fechado')

    // Campo transitório para saldo atual (não persistido diretamente)
    private transient BigDecimal saldoAtual;

    // Construtores
    public Caixa() {}

    public Caixa(int usuarioAberturaId, BigDecimal saldoInicial) {
        this.usuarioAberturaId = usuarioAberturaId;
        this.saldoInicial = saldoInicial;
        this.status = "aberto"; // Status inicial padrão
        this.dataAbertura = new Timestamp(System.currentTimeMillis()); // Data de abertura atual
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioAberturaId() {
        return usuarioAberturaId;
    }

    public void setUsuarioAberturaId(int usuarioAberturaId) {
        this.usuarioAberturaId = usuarioAberturaId;
    }

    public Integer getUsuarioFechamentoId() {
        return usuarioFechamentoId;
    }

    public void setUsuarioFechamentoId(Integer usuarioFechamentoId) {
        this.usuarioFechamentoId = usuarioFechamentoId;
    }

    public Timestamp getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Timestamp dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Timestamp getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Timestamp dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }
}

