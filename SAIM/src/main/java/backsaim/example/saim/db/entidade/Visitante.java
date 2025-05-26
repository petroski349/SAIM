package backsaim.example.saim.db.entidade;

import java.time.LocalDate;

public class Visitante {
    private Integer visiCod;
    private String visiNome;
    private String visiCPF;
    private String visiEmail;
    private String visiTelefone;
    private String visiEndereco;
    private String visiCep;
    private LocalDate visiDataCadastro;
    private LocalDate visiDataNascimento;


    public Visitante() {}


    public Visitante(Integer visiCod, String visiNome, String visiCPF, String visiEmail,
                     String visiTelefone, String visiEndereco, String visiCep,
                     LocalDate visiDataCadastro, LocalDate visiDataNascimento) {
        this.visiCod = visiCod;
        this.visiNome = visiNome;
        this.visiCPF = visiCPF;
        this.visiEmail = visiEmail;
        this.visiTelefone = visiTelefone;
        this.visiEndereco = visiEndereco;
        this.visiCep = visiCep;
        this.visiDataCadastro = visiDataCadastro;
        this.visiDataNascimento = visiDataNascimento;
    }

    public Integer getVisiCod() {
        return visiCod;
    }

    public void setVisiCod(Integer visiCod) {
        this.visiCod = visiCod;
    }

    public String getVisiNome() {
        return visiNome;
    }

    public void setVisiNome(String visiNome) {
        this.visiNome = visiNome;
    }

    public String getVisiCPF() {
        return visiCPF;
    }

    public void setVisiCPF(String visiCPF) {
        this.visiCPF = visiCPF;
    }

    public String getVisiEmail() {
        return visiEmail;
    }

    public void setVisiEmail(String visiEmail) {
        this.visiEmail = visiEmail;
    }

    public String getVisiTelefone() {
        return visiTelefone;
    }

    public void setVisiTelefone(String visiTelefone) {
        this.visiTelefone = visiTelefone;
    }

    public String getVisiEndereco() {
        return visiEndereco;
    }

    public void setVisiEndereco(String visiEndereco) {
        this.visiEndereco = visiEndereco;
    }

    public String getVisiCep() {
        return visiCep;
    }

    public void setVisiCep(String visiCep) {
        this.visiCep = visiCep;
    }

    public LocalDate getVisiDataCadastro() {
        return visiDataCadastro;
    }

    public void setVisiDataCadastro(LocalDate visiDataCadastro) {
        this.visiDataCadastro = visiDataCadastro;
    }

    public LocalDate getVisiDataNascimento() {
        return visiDataNascimento;
    }

    public void setVisiDataNascimento(LocalDate visiDataNascimento) {
        this.visiDataNascimento = visiDataNascimento;
    }
}
