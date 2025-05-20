package back;

public class Prontuario {
    private int id;
    private String nome;
    private String rg;
    private String cpf;
    private String dataNascimento;
    private String exameFisico;
    private String exameComplementar;
    private String crm;
    private String endereco;
    private String dataEntrada;
    private String situacao;

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getExameFisico() { return exameFisico; }
    public void setExameFisico(String exameFisico) { this.exameFisico = exameFisico; }

    public String getExameComplementar() { return exameComplementar; }
    public void setExameComplementar(String exameComplementar) { this.exameComplementar = exameComplementar; }

    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(String dataEntrada) { this.dataEntrada = dataEntrada; }

    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
}
