package backsaim.example.saim.db.apis;

import backsaim.example.saim.db.dal.VisitanteDAL;
import backsaim.example.saim.db.entidade.Visitante;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/visitantes")
@CrossOrigin(origins = "*")
public class VisitanteController {

    private final VisitanteDAL dal = new VisitanteDAL();

    private String aplicarMascaraCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }

    private boolean isCpfValido(String cpf) {
        return Pattern.matches("\\d{11}", cpf);
    }

    @PostMapping("/cadastrar")
    public Map<String, Object> cadastrarVisitante(@RequestBody Map<String, String> payload) {
        try {
            String nome = payload.get("visiNome");
            String cpf = payload.get("visiCPF");
            String email = payload.get("visiEmail");
            String telefone = payload.get("visiTelefone");
            String endereco = payload.get("visiEndereco");
            String cep = payload.get("visiCep");
            String dataCadastroStr = payload.get("visiData_Cadastro");
            String dataNascimentoStr = payload.get("visiData_Nascimento");

            if (nome == null || nome.isBlank())
                return Map.of("success", false, "error", "Nome é obrigatório");
            if (cpf == null || cpf.isBlank())
                return Map.of("success", false, "error", "CPF é obrigatório");
            if (dataCadastroStr == null || dataCadastroStr.isBlank())
                return Map.of("success", false, "error", "Data de cadastro é obrigatória");
            if (dataNascimentoStr == null || dataNascimentoStr.isBlank())
                return Map.of("success", false, "error", "Data de nascimento é obrigatória");

            if (!isCpfValido(cpf)) {
                return Map.of("success", false, "error", "CPF inválido. Deve conter 11 dígitos numéricos.");
            }

            if (dal.cpfjaExiste(cpf)) {
                return Map.of("success", false, "error", "CPF já cadastrado.");
            }

            String cpfComMascara = aplicarMascaraCpf(cpf);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataCadastro = LocalDate.parse(dataCadastroStr, formatter);
            LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, formatter);

            Visitante visitante = new Visitante(
                    0,
                    nome,
                    cpfComMascara,
                    email,
                    telefone,
                    endereco,
                    cep,
                    dataCadastro,
                    dataNascimento
            );

            boolean sucesso = dal.gravar(visitante);
            if (sucesso) {
                return Map.of("success", true, "visitante", visitante);
            } else {
                return Map.of("success", false, "error", "Falha ao gravar visitante");
            }

        } catch (DateTimeParseException e) {
            return Map.of("success", false, "error", "Formato de data inválido. Use yyyy-MM-dd");
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }


    @PostMapping("/alterar")
    public Map<String, Object> alterarVisitante(@RequestBody Map<String, String> payload) {
        try {
            String codStr = payload.get("visiCod");
            if (codStr == null || codStr.isEmpty())
                return Map.of("success", false, "error", "Código do visitante não informado");

            String nome = payload.get("visiNome");
            String cpf = payload.get("visiCPF");
            String email = payload.get("visiEmail");
            String telefone = payload.get("visiTelefone");
            String endereco = payload.get("visiEndereco");
            String cep = payload.get("visiCep");
            String dataCadastroStr = payload.get("visiData_Cadastro");
            String dataNascimentoStr = payload.get("visiData_Nascimento");

            if (nome == null || cpf == null || dataCadastroStr == null || dataNascimentoStr == null)
                return Map.of("success", false, "error", "Campos obrigatórios não informados");

            if (!isCpfValido(cpf)) {
                return Map.of("success", false, "error", "CPF inválido. Deve conter 11 dígitos numéricos.");
            }

            int cod = Integer.parseInt(codStr);


            String cpfComMascara = aplicarMascaraCpf(cpf);

            Visitante visitante = new Visitante(
                    cod,
                    nome,
                    cpfComMascara,
                    email,
                    telefone,
                    endereco,
                    cep,
                    LocalDate.parse(dataCadastroStr),
                    LocalDate.parse(dataNascimentoStr)
            );

            boolean sucesso = dal.alterar(visitante);
            return Map.of("success", sucesso);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> alterarVisitante(@PathVariable int id, @RequestBody Visitante visitante) {
        if (visitante.getVisiCod() == null || !visitante.getVisiCod().equals(id)) {
            return ResponseEntity.badRequest().body("ID no caminho e no corpo não coincidem");
        }

        boolean atualizado = dal.alterar(visitante);

        if (atualizado) {
            return ResponseEntity.ok().body(Map.of("success", true, "message", "Visitante alterado com sucesso"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Visitante não encontrado ou dados iguais"));
        }
    }


    @DeleteMapping("/excluir/{visiCod}")
    public Map<String, Object> excluirVisitante(@PathVariable int visiCod) {
        try {
            Visitante visitante = dal.get(visiCod);
            if (visitante == null)
                return Map.of("success", false, "error", "Visitante não encontrado");

            boolean sucesso = dal.apagar(visitante);
            return Map.of("success", sucesso);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }

    @GetMapping("/listar")
    public List<Visitante> listarVisitantes() {
        return dal.listar();
    }
}
