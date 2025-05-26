package backsaim.example.saim.db.apis;

import backsaim.example.saim.db.dal.VisitaDAL;
import backsaim.example.saim.db.entidade.Visita;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/visitas")
@CrossOrigin(origins = "*")
public class VisitaController {

    @PostMapping("/agendar")
    public Map<String, Object> agendarVisita(@RequestBody Map<String, String> payload) {
        try {
            String nome = payload.get("nome");
            String cpf = payload.get("cpf");
            String dataEntrada = payload.get("dataEntrada");
            String dataSaida = payload.get("dataSaida");

            Visita visita = new Visita();
            visita.setVisinome(nome);
            visita.setVisicpf(cpf);
            visita.setVisidata_entrada(LocalDate.parse(dataEntrada));
            visita.setVisidata_saida((dataSaida != null && !dataSaida.isEmpty()) ? LocalDate.parse(dataSaida) : null);

            VisitaDAL dal = new VisitaDAL();

            if (dal.cpfExiste(cpf)) {
                return Map.of("success", false, "error", "CPF já cadastrado.");
            }

            boolean sucesso = dal.agendarVisita(visita);
            return Map.of("success", sucesso);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }

    @PutMapping("/alterar/{id}")
    public Map<String, Object> alterarVisita(@PathVariable int id, @RequestBody Map<String, String> payload) {
        try {
            String nome = payload.get("nome");
            String cpf = payload.get("cpf");
            String dataEntrada = payload.get("dataEntrada");
            String dataSaida = payload.get("dataSaida");

            Visita visita = new Visita(id, nome, cpf, LocalDate.parse(dataEntrada),
                    (dataSaida != null && !dataSaida.isEmpty()) ? LocalDate.parse(dataSaida) : null);

            VisitaDAL dal = new VisitaDAL();
            boolean sucesso = dal.alterar(visita);

            return Map.of("success", sucesso);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{id}")
    public Map<String, Object> excluirVisita(@PathVariable int id) {
        try {
            VisitaDAL dal = new VisitaDAL();
            Visita visita = dal.get(id);
            boolean sucesso = visita != null && dal.apagar(visita);
            return Map.of("success", sucesso);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }
}
