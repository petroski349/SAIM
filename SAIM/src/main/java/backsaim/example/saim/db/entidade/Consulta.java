package backsaim.example.saim.db.entidade;


import java.time.LocalDate;

public class Consulta {
    private int conCod;
    private int medId;
    private String paciConvenio;
    private String conTipo;
    private LocalDate conData;

    public Consulta() {}

    public Consulta(int conCod, int medId, String paciConvenio, String conTipo, LocalDate conData) {
        this.conCod = conCod;
        this.medId = medId;
        this.paciConvenio = paciConvenio;
        this.conTipo = conTipo;
        this.conData = conData;
    }

    public int getConCod() {
        return conCod;
    }

    public void setConCod(int conCod) {
        this.conCod = conCod;
    }

    public int getMedId() {
        return medId;
    }

    public void setMedId(int medId) {
        this.medId = medId;
    }

    public String getPaciConvenio() {
        return paciConvenio;
    }

    public void setPaciConvenio(String paciConvenio) {
        this.paciConvenio = paciConvenio;
    }

    public String getConTipo() {
        return conTipo;
    }

    public void setConTipo(String conTipo) {
        this.conTipo = conTipo;
    }

    public LocalDate getConData() {
        return conData;
    }

    public void setConData(LocalDate conData) {
        this.conData = conData;
    }
}

