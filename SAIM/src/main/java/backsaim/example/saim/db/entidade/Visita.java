package backsaim.example.saim.db.entidade;

import java.time.LocalDate;

public class Visita {
    private int visicod;
    private String visinome;
    private String visicpf;
    private LocalDate visidata_entrada;
    private LocalDate visidata_saida;

    public Visita() {
    }

    public Visita(int visicod, String visinome, String visicpf, LocalDate visidata_entrada, LocalDate visidata_saida) {
        this.visicod = visicod;
        this.visinome = visinome;
        this.visicpf = visicpf;
        this.visidata_entrada = visidata_entrada;
        this.visidata_saida = visidata_saida;
    }

    public int getVisicod() {
        return visicod;
    }

    public void setVisicod(int visicod) {
        this.visicod = visicod;
    }

    public String getVisinome() {
        return visinome;
    }

    public void setVisinome(String visinome) {
        this.visinome = visinome;
    }

    public String getVisicpf() {
        return visicpf;
    }

    public void setVisicpf(String visicpf) {
        this.visicpf = visicpf;
    }

    public LocalDate getVisidata_entrada() {
        return visidata_entrada;
    }

    public void setVisidata_entrada(LocalDate visidata_entrada) {
        this.visidata_entrada = visidata_entrada;
    }

    public LocalDate getVisidata_saida() {
        return visidata_saida;
    }

    public void setVisidata_saida(LocalDate visidata_saida) {
        this.visidata_saida = visidata_saida;
    }

    @Override
    public String toString() {
        return visinome + " - " + visicpf;
    }
}
