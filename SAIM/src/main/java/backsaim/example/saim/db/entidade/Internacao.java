package backsaim.example.saim.db.entidade;

import java.time.LocalDate;

public class Internacao {
    private int prontuarioId;
    private String paciNome;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;

    public Internacao() {}

    public Internacao(int prontuarioId, String paciNome, LocalDate dataEntrada, LocalDate dataSaida) {
        this.prontuarioId = prontuarioId;
        this.paciNome = paciNome;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }

    public int getProntuarioId() {
        return prontuarioId;
    }

    public void setProntuarioId(int prontuarioId) {
        this.prontuarioId = prontuarioId;
    }

    public String getPaciNome() {
        return paciNome;
    }

    public void setPaciNome(String paciNome) {
        this.paciNome = paciNome;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }
}
