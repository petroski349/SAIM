package backsaim.example.saim.db.entidade;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class Consulta2 {
    private int medCRM;
    private String medNome;
    private String medEspecialidade;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date conData;

    private int horarioConsulta;
    private String paciNome;
    private String paciCPF;
    private int paciConvenio;
    private int numConvenio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataUltimaConsulta;

    private char conTipo;

    public Consulta2() {}

    public int getMedCRM() { return medCRM; }
    public void setMedCRM(int medCRM) { this.medCRM = medCRM; }

    public String getMedNome() { return medNome; }
    public void setMedNome(String medNome) { this.medNome = medNome; }

    public String getMedEspecialidade() { return medEspecialidade; }
    public void setMedEspecialidade(String medEspecialidade) { this.medEspecialidade = medEspecialidade; }

    public Date getConData() { return conData; }
    public void setConData(Date conData) { this.conData = conData; }

    public int getHorarioConsulta() { return horarioConsulta; }
    public void setHorarioConsulta(int horarioConsulta) { this.horarioConsulta = horarioConsulta; }

    public String getPaciNome() { return paciNome; }
    public void setPaciNome(String paciNome) { this.paciNome = paciNome; }

    public String getPaciCPF() { return paciCPF; }
    public void setPaciCPF(String paciCPF) { this.paciCPF = paciCPF; }

    public int getPaciConvenio() { return paciConvenio; }
    public void setPaciConvenio(int paciConvenio) { this.paciConvenio = paciConvenio; }

    public int getNumConvenio() { return numConvenio; }
    public void setNumConvenio(int numConvenio) { this.numConvenio = numConvenio; }

    public Date getDataUltimaConsulta() { return dataUltimaConsulta; }
    public void setDataUltimaConsulta(Date dataUltimaConsulta) { this.dataUltimaConsulta = dataUltimaConsulta; }

    public char getConTipo() { return conTipo; }
    public void setConTipo(char conTipo) { this.conTipo = conTipo; }
}
