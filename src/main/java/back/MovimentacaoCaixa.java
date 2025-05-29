package back;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class MovimentacaoCaixa {
    private int id;
    private int caixaId;
    private int usuarioRegistroId; // Alterado de funcionarioId para usuarioRegistroId
    private Timestamp dataHora; // Alterado de data para dataHora
    private String tipo; // Alterado para 'entrada' ou 'saida' em vez de 'E' ou 'S'
    private BigDecimal valor;
    private String descricao;
    
    // Construtores
    public MovimentacaoCaixa() {}

    public MovimentacaoCaixa(int caixaId, int usuarioRegistroId, String tipo,
                             BigDecimal valor, String descricao) {
        this.caixaId = caixaId;
        this.usuarioRegistroId = usuarioRegistroId;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.dataHora = new Timestamp(System.currentTimeMillis());
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCaixaId() {
        return caixaId;
    }

    public void setCaixaId(int caixaId) {
        this.caixaId = caixaId;
    }

    public int getUsuarioRegistroId() {
        return usuarioRegistroId;
    }

    public void setUsuarioRegistroId(int usuarioRegistroId) {
        this.usuarioRegistroId = usuarioRegistroId;
    }

    public Timestamp getDataHora() {
        return dataHora;
    }

    public void setDataHora(Timestamp dataHora) {
        this.dataHora = dataHora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
