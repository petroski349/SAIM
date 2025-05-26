package backsaim.example.saim.db.entidade;

import java.time.LocalDate;

public class Paciente {
    private int paciId;
    private String paciNome;
    private String paciCPF;
    private String paciConvenio;
    private LocalDate paciDataInternacao;
    private LocalDate paciDataAlta;
    private String paciEndereco;
    private String paciRg;
    private String paciTelefone;
    private LocalDate paciDataNascimento;
    private String paciCep;

    public Paciente() {}

    public Paciente(int paciId, String paciNome, String paciCPF, String paciConvenio,
                    LocalDate paciDataInternacao, LocalDate paciDataAlta, String paciEndereco,
                    String paciRg, String paciTelefone, LocalDate paciDataNascimento, String paciCep) {
        this.paciId = paciId;
        this.paciNome = paciNome;
        this.paciCPF = paciCPF;
        this.paciConvenio = paciConvenio;
        this.paciDataInternacao = paciDataInternacao;
        this.paciDataAlta = paciDataAlta;
        this.paciEndereco = paciEndereco;
        this.paciRg = paciRg;
        this.paciTelefone = paciTelefone;
        this.paciDataNascimento = paciDataNascimento;
        this.paciCep = paciCep;
    }

    // Getters e Setters

    public int getPaciId() {
        return paciId;
    }

    public void setPaciId(int paciId) {
        this.paciId = paciId;
    }

    public String getPaciNome() {
        return paciNome;
    }

    public void setPaciNome(String paciNome) {
        this.paciNome = paciNome;
    }

    public String getPaciCPF() {
        return paciCPF;
    }

    public void setPaciCPF(String paciCPF) {
        this.paciCPF = paciCPF;
    }

    public String getPaciConvenio() {
        return paciConvenio;
    }

    public void setPaciConvenio(String paciConvenio) {
        this.paciConvenio = paciConvenio;
    }

    public LocalDate getPaciDataInternacao() {
        return paciDataInternacao;
    }

    public void setPaciDataInternacao(LocalDate paciDataInternacao) {
        this.paciDataInternacao = paciDataInternacao;
    }

    public LocalDate getPaciDataAlta() {
        return paciDataAlta;
    }

    public void setPaciDataAlta(LocalDate paciDataAlta) {
        this.paciDataAlta = paciDataAlta;
    }

    public String getPaciEndereco() {
        return paciEndereco;
    }

    public void setPaciEndereco(String paciEndereco) {
        this.paciEndereco = paciEndereco;
    }

    public String getPaciRg() {
        return paciRg;
    }

    public void setPaciRg(String paciRg) {
        this.paciRg = paciRg;
    }

    public String getPaciTelefone() {
        return paciTelefone;
    }

    public void setPaciTelefone(String paciTelefone) {
        this.paciTelefone = paciTelefone;
    }

    public LocalDate getPaciDataNascimento() {
        return paciDataNascimento;
    }

    public void setPaciDataNascimento(LocalDate paciDataNascimento) {
        this.paciDataNascimento = paciDataNascimento;
    }

    public String getPaciCep() {
        return paciCep;
    }

    public void setPaciCep(String paciCep) {
        this.paciCep = paciCep;
    }

    @Override
    public String toString() {
        return paciNome + " (" + paciCPF + ")";
    }
}
