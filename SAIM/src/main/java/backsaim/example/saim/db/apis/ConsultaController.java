package backsaim.example.saim.db.apis;

import backsaim.example.saim.db.dal.ConsultaDAL;
import backsaim.example.saim.db.entidade.Consulta;
import backsaim.example.saim.db.entidade.Visitante;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultas")
@CrossOrigin(origins = "*")
public class ConsultaController {

    private final ConsultaDAL dal = new ConsultaDAL();

    @PostMapping("/agendar")
    public Map<String, Object> agendarConsulta(@RequestBody Map<String, String> payload) {
        try {
            int medId = Integer.parseInt(payload.get("medId"));
            String paciConvenio = payload.get("paciConvenio");
            String conTipo = payload.get("conTipo");
            String conData = payload.get("conData");

            if (conData == null || conData.isEmpty())
                return Map.of("success", false, "error", "Data da consulta é obrigatória");

            Consulta consulta = new Consulta(0, medId, paciConvenio, conTipo, LocalDate.parse(conData));
            boolean sucesso = dal.gravar(consulta);
            return Map.of("success", sucesso);

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }

    @PostMapping("/alterar")
    public Map<String, Object> alterarConsulta(@RequestBody Map<String, String> payload) {
        try {
            String codStr = payload.get("conCod");
            if (codStr == null || codStr.isEmpty())
                return Map.of("success", false, "error", "Código da consulta não informado");

            int conCod = Integer.parseInt(codStr);
            int medId = Integer.parseInt(payload.get("medId"));
            String paciConvenio = payload.get("paciConvenio");
            String conTipo = payload.get("conTipo");
            String conData = payload.get("conData");

            if (conData == null || conData.isEmpty())
                return Map.of("success", false, "error", "Data da consulta é obrigatória");

            Consulta consulta = new Consulta(conCod, medId, paciConvenio, conTipo, LocalDate.parse(conData));
            boolean sucesso = dal.alterar(consulta);

            return Map.of("success", sucesso);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }


    @DeleteMapping("/excluir/{conCod}")
    public Map<String, Object> excluirConsulta(@PathVariable int conCod) {
        try {
            Consulta consulta = dal.get(conCod);
            if (consulta == null)
                return Map.of("success", false, "error", "Consulta não encontrada");

            boolean sucesso = dal.apagar(consulta);
            return Map.of("success", sucesso);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }

    @GetMapping("/listar")
    public List<Consulta> listarconsulta() {
        return dal.listarConsultas();
    }

    @GetMapping("/{conCod}")
    public Consulta getConsultaPorId(@PathVariable int conCod) {
        return dal.get(conCod);
    }

}
