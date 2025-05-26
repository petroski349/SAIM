package backsaim.example.saim.db.apis;

import backsaim.example.saim.db.dal.Consulta2DAL;
import backsaim.example.saim.db.entidade.Consulta2;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/consulta2")
@CrossOrigin(origins = "*")
public class Consulta2Controller {

    @PostMapping("/efetuar")
    public Map<String, Object> efetuarConsulta(@RequestBody Map<String, String> payload) {
        try {
            int medCRM = Integer.parseInt(payload.get("medCRM"));
            String medNome = payload.get("medNome");
            String medEspecialidade = payload.get("medEspecialidade");
            Date conData = Date.valueOf(payload.get("conData"));
            int horarioConsulta = Integer.parseInt(payload.get("horarioConsulta"));
            String paciNome = payload.get("paciNome");
            String paciCPF = payload.get("paciCPF");
            int paciConvenio = Integer.parseInt(payload.get("paciConvenio"));
            int numConvenio = Integer.parseInt(payload.get("numConvenio"));
            Date dataUltimaConsulta = Date.valueOf(payload.get("dataUltimaConsulta"));
            char conTipo = payload.get("conTipo").charAt(0);

            Consulta2 consulta = new Consulta2();
            consulta.setMedCRM(medCRM);
            consulta.setMedNome(medNome);
            consulta.setMedEspecialidade(medEspecialidade);
            consulta.setConData(conData);
            consulta.setHorarioConsulta(horarioConsulta);
            consulta.setPaciNome(paciNome);
            consulta.setPaciCPF(paciCPF);
            consulta.setPaciConvenio(paciConvenio);
            consulta.setNumConvenio(numConvenio);
            consulta.setDataUltimaConsulta(dataUltimaConsulta);
            consulta.setConTipo(conTipo);

            // Grava no banco via DAL
            Consulta2DAL dal = new Consulta2DAL();
            boolean sucesso = dal.gravar(consulta);

            return Map.of("success", sucesso);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }
}
