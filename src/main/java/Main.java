
import back.*;
import back.DAO.*;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        staticFiles.externalLocation("src/main/resources/front");

        port(4567); // ou 8080
        // CORS - Permitir requisições do navegador
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
            response.type("application/json");
        });

        System.out.println("🚀 Servidor iniciado com sucesso!");
        post("/api/dados-hospital", (req, res) -> {
            System.out.println("📥 Requisição recebida na rota /api/dados-hospital");

            res.type("application/json");

            DadosHospital dados = new Gson().fromJson(req.body(), DadosHospital.class);

            DadosHospitalDAO dao = new DadosHospitalDAO();
            dao.salvar(dados);

            return "{\"status\":\"ok\"}";
        });
        post("/api/aparencia-sistema", (req, res) -> {
            System.out.println("🔵 Rota /api/aparencia-sistema acessada");

            res.type("application/json");

            AparenciaSistema dados = new Gson().fromJson(req.body(), AparenciaSistema.class);

            AparenciaSistemaDAO dao = new AparenciaSistemaDAO();
            dao.salvar(dados);

            return "{\"status\":\"ok\"}";
        });
        get("/api/aparencia-sistema", (req, res) -> {
            res.type("application/json");

            AparenciaSistemaDAO dao = new AparenciaSistemaDAO();
            AparenciaSistema dados = dao.buscarUltimaConfiguracao(); // método a criar

            if (dados != null) {
                return new Gson().toJson(dados);
            } else {
                // Dados padrão quando não há nada no banco
                AparenciaSistema padrao = new AparenciaSistema();
                padrao.setNome("Nome da Instituição Padrão");
                padrao.setLogoBase64(""); // Caso queira colocar um logo padrão em base64
                padrao.setCor("#1f8f8f"); // Cor padrão
                padrao.setRodape("Rodapé padrão do sistema");

                return new Gson().toJson(padrao);
            }
        });

        get("/api/dados-hospital", (req, res) -> {
            res.type("application/json");

            DadosHospitalDAO dao = new DadosHospitalDAO();
            DadosHospital dados = dao.buscar(); 

            if (dados != null) {
                return new Gson().toJson(dados);
            } else {
                DadosHospital padrao = new DadosHospital();
                padrao.setEmail("email@instituicao.com");
                padrao.setTelefone("(00) 00000-0000");
                padrao.setEndereco("Endereço não cadastrado");
                padrao.setHorarioInicio("08:00");
                padrao.setHorarioFim("18:00");
                padrao.setCnpj("00.000.000/0000-00");

                return new Gson().toJson(padrao);
            }
        });

        // API de colaborador logado (fixo para testes)
        get("/api/colaborador", (req, res) -> {
            UsuarioDAO dao = new UsuarioDAO();
            Usuario u = dao.buscarPorCpf("11122233344"); // Simula usuário logado
            return new Gson().toJson(u);
        });

// API para buscar paciente + histórico
        get("/api/paciente", (req, res) -> {
            res.type("application/json");

            String cpf = req.queryParams("cpf");
            String nome = req.queryParams("nome");
            if (cpf != null) {
                cpf = cpf.replaceAll("[^\\d]", "");  // Remove pontuação
            }
            PacienteDAO pacienteDAO = new PacienteDAO();
            ProntuarioDAO prontuarioDAO = new ProntuarioDAO();

            Paciente paciente = null;

            if (cpf != null && !cpf.isEmpty()) {
                paciente = pacienteDAO.buscarPorCpf(cpf);
            } else if (nome != null && !nome.isEmpty()) {
                paciente = pacienteDAO.buscarPorNome(nome);
            }

            if (paciente == null) {
                res.status(404);
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Paciente não encontrado");
                return new Gson().toJson(erro);
            }


            List<Prontuario> historico = prontuarioDAO.listarPorCpf(paciente.getCpf());

            Map<String, Object> response = new HashMap<>();
            response.put("nome", paciente.getNome());
            response.put("cpf", paciente.getCpf());
            response.put("historico", historico);

            return new Gson().toJson(response);
        });

        post("/api/prontuario", (req, res) -> {
            res.type("application/json");
            try {
                System.out.println("📥 Recebi requisição no /api/prontuario");
                Prontuario prontuario = new Gson().fromJson(req.body(), Prontuario.class);
                ProntuarioDAO dao = new ProntuarioDAO();
                dao.inserir(prontuario);

                Map<String, String> response = new HashMap<>();
                response.put("status", "criado");
                response.put("mensagem", "Prontuário salvo com sucesso");
                return new Gson().toJson(response);

            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                Map<String, String> response = new HashMap<>();
                response.put("status", "erro");
                response.put("mensagem", "Erro ao salvar prontuário: " + e.getMessage());
                return new Gson().toJson(response);
            }
        });

        get("/api/prontuario/:cpf", (req, res) -> {
            res.type("application/json");

            String cpf = req.params("cpf");
            if (cpf != null) {
                cpf = cpf.replaceAll("[^\\d]", "");  // Remove pontuação
            }
            ProntuarioDAO dao = new ProntuarioDAO();
            return new Gson().toJson(dao.listarPorCpf(cpf));
        });

        get("/api/consulta", (req, res) -> {
            String cpf = req.queryParams("cpf");
            ConsultaDAO dao = new ConsultaDAO();
            return new Gson().toJson(dao.buscarUltimaConsultaPorCpf(cpf));
        });


    }
}
