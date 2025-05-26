package backsaim.example.saim.db.apis;

import backsaim.example.saim.db.dal.InternacaoDAL;
import backsaim.example.saim.db.entidade.Consulta;
import backsaim.example.saim.db.entidade.Internacao;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/internacoes")
public class InternacaoController {

    private final InternacaoDAL dal = new InternacaoDAL();

    @PostMapping("/efetuar")
    public String efetuarInternacao(@RequestBody Map<String, String> dados) {
        try {
            int prontuarioId = Integer.parseInt(dados.get("prontuarioId"));
            String paciNome = dados.get("paciNome");
            LocalDate dataEntrada = LocalDate.parse(dados.get("dataEntrada"));

            LocalDate dataSaida = null;
            if (dados.get("dataSaida") != null && !dados.get("dataSaida").isEmpty()) {
                dataSaida = LocalDate.parse(dados.get("dataSaida"));
            }

            Internacao internacao = new Internacao(prontuarioId, paciNome, dataEntrada, dataSaida);
            boolean sucesso = dal.gravar(internacao);
            return sucesso ? "Internação registrada com sucesso." : "Falha ao registrar internação.";

        } catch (Exception e) {
            return "Erro ao processar dados: " + e.getMessage();
        }
    }

    @PutMapping("/alterar")
    public String alterarInternacao(@RequestBody Internacao internacao) {
        try {
            boolean sucesso = dal.alterar(internacao);
            return sucesso ? "Internação alterada com sucesso." : "Falha ao alterar internação.";
        } catch (Exception e) {
            return "Erro ao processar dados: " + e.getMessage();
        }
    }

    @DeleteMapping("/excluir/{prontuarioId}")
    public Map<String, Object> excluirInternacao(@PathVariable int prontuarioId) {
        try {
            Internacao internacao = dal.get(prontuarioId);
            if (internacao == null)
                return Map.of("success", false, "error", "Internação não encontrada");

            boolean sucesso = dal.apagar(internacao);
            return Map.of("success", sucesso);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }


    @GetMapping("/listar")
    public List<Internacao> listarInternacoes() {
        return dal.listarInternacoes();
    }

    @GetMapping("/buscar/{prontuarioId}")
    public Internacao buscarPorProntuarioId(@PathVariable int prontuarioId) {
        return dal.buscarPorProntuarioId(prontuarioId);
    }
}
