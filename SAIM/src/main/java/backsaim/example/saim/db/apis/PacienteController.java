package backsaim.example.saim.db.apis;

import backsaim.example.saim.db.dal.PacienteDAL;
import backsaim.example.saim.db.entidade.Paciente;
import backsaim.example.saim.db.entidade.Visitante;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static backsaim.example.saim.db.util.Utils.*;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    private final PacienteDAL dal = new PacienteDAL();

    private String aplicarMascaraCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }

    private String aplicarMascaraCep(String cep) {
        if (cep == null || cep.length() != 8) {
            return cep;
        }
        return cep.substring(0, 5) + "-" + cep.substring(5, 8);
    }

    private boolean isCpfValido(String cpf) {
        if (cpf == null) return false;
        return Pattern.matches("\\d{11}", cpf);
    }

    private boolean verificarRGExistente(String rg) {
        return dal.rgJaExiste(rg);
    }

    @PostMapping("/cadastrar")
    public Map<String, Object> cadastrarPaciente(@RequestBody Map<String, String> payload) {
        try {
            String nome = payload.get("paciNome");
            String cpf = payload.get("paciCPF");
            String convenio = payload.get("paciConvenio");
            String dataInternacaoStr = payload.get("paciData_Internacao");
            String dataAltaStr = payload.get("paciData_Alta");
            String endereco = payload.get("paciEndereco");
            String rg = payload.get("paciRg");
            String telefone = payload.get("paciTelefone");
            String dataNascimentoStr = payload.get("paciData_Nascimento");
            String cep = payload.get("paciCep");

            if (nome == null || cpf == null || dataNascimentoStr == null || dataInternacaoStr == null)
                return Map.of("success", false, "error", "Campos obrigatórios não informados");

            String cpfSemMascara = cpf.replaceAll("[\\.\\-]", "");

            if (!isCpfValido(cpfSemMascara)) {
                return Map.of("success", false, "error", "CPF inválido. Deve conter 11 dígitos numéricos.");
            }

            if (dal.cpfJaExiste(cpfSemMascara)) {
                return Map.of("success", false, "error", "CPF já cadastrado.");
            }

            if (verificarRGExistente(rg)) {
                return Map.of("success", false, "error", "RG já cadastrado.");
            }

            String cpfComMascara = aplicarMascaraCpf(cpfSemMascara);

            String rgComMascara = aplicarMascaraRg(rg);
            String cepComMascara = aplicarMascaraCep(cep);

            Paciente paciente = new Paciente(
                    0,
                    nome,
                    cpfComMascara,
                    convenio,
                    LocalDate.parse(dataInternacaoStr),
                    LocalDate.parse(dataAltaStr),
                    endereco,
                    rgComMascara,
                    telefone,
                    LocalDate.parse(dataNascimentoStr),
                    cepComMascara
            );

            boolean sucesso = dal.gravar(paciente);
            return Map.of("success", sucesso);

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }


    @PostMapping("/alterar")
    public Map<String, Object> alterarPaciente(@RequestBody Map<String, String> payload) {
        try {
            String idStr = payload.get("paciId");
            if (idStr != null && !idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                String nome = payload.get("paciNome");
                String cpf = payload.get("paciCPF");
                String convenio = payload.get("paciConvenio");
                String dataInternacaoStr = payload.get("paciData_Internacao");
                String dataAltaStr = payload.get("paciData_Alta");
                String endereco = payload.get("paciEndereco");
                String rg = payload.get("paciRg");
                String telefone = payload.get("paciTelefone");
                String dataNascimentoStr = payload.get("paciData_Nascimento");
                String cep = payload.get("paciCep");

                if (nome != null && cpf != null && dataNascimentoStr != null && dataInternacaoStr != null) {

                    String cpfComMascara = aplicarMascaraCpf(cpf);
                    String rgComMascara = aplicarMascaraRg(rg);
                    String cepComMascara = aplicarMascaraCep(cep);

                    // ⬇️ Tratar datas como no cadastrar
                    LocalDate dataInternacao = (dataInternacaoStr != null && !dataInternacaoStr.isBlank())
                            ? LocalDate.parse(dataInternacaoStr)
                            : LocalDate.now();

                    LocalDate dataAlta = (dataAltaStr != null && !dataAltaStr.isBlank())
                            ? LocalDate.parse(dataAltaStr)
                            : LocalDate.now();

                    LocalDate dataNascimento = (dataNascimentoStr != null && !dataNascimentoStr.isBlank())
                            ? LocalDate.parse(dataNascimentoStr)
                            : LocalDate.now();

                    Paciente paciente = new Paciente(id, nome, cpfComMascara, convenio,
                            dataInternacao, dataAlta, endereco, rgComMascara, telefone,
                            dataNascimento, cepComMascara);

                    boolean sucesso = dal.alterar(paciente);
                    return Map.of("success", sucesso);
                } else {
                    return Map.of("success", false, "error", "Campos obrigatórios não informados");
                }
            } else {
                return Map.of("success", false, "error", "ID do paciente não informado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }


    @DeleteMapping("/excluir/{paciId}")
    public Map<String, Object> excluirPaciente(@PathVariable int paciId) {
        try {
            Paciente paciente = dal.get(paciId);
            if (paciente == null)
                return Map.of("success", false, "error", "Paciente não encontrado");

            boolean sucesso = dal.apagar(paciente);
            return Map.of("success", sucesso);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }

    @GetMapping("/listar")
    public List<Paciente> listarPacientes() {
        return dal.listarpacientes();
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Paciente> buscarPacientePorId(@PathVariable int id) {
        Paciente paciente = dal.get(id);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/buscarCPF/{cpf:.+}")
    public ResponseEntity<?> buscarPorCpf(@PathVariable String cpf) {
        System.out.println("Buscando paciente por CPF: " + cpf);
        Optional<Paciente> pacienteOpt = dal.buscarPorCpf(cpf);
        if (pacienteOpt.isPresent()) {
            return ResponseEntity.ok(pacienteOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Paciente não encontrado com CPF: " + cpf));
        }
    }

}
