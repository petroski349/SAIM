
import back.AparenciaSistema;
import back.DAO.AparenciaSistemaDAO;
import back.DAO.ProntuarioDAO;
import back.DadosHospital;
import back.DAO.DadosHospitalDAO;
import back.Prontuario;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
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

            return new Gson().toJson(dados);
        });
        post("/api/prontuario", (req, res) -> {
            res.type("application/json");

            // Converte JSON recebido em objeto Java
            Prontuario prontuario = new Gson().fromJson(req.body(), Prontuario.class);

            // Chama o DAO para inserir no banco
            ProntuarioDAO dao = new ProntuarioDAO();
            dao.inserir(prontuario);

            // Retorna resposta
            Map<String, String> response = new HashMap<>();
            response.put("status", "criado");
            return new Gson().toJson(response);

        });

        // Endpoint para atualizar prontuário (com base no CPF)
        put("/api/prontuario", (req, res) -> {
            res.type("application/json");

            // Converte JSON recebido
            Prontuario prontuario = new Gson().fromJson(req.body(), Prontuario.class);

            // Atualiza o registro no banco
            ProntuarioDAO dao = new ProntuarioDAO();
            dao.atualizar(prontuario);

            // Retorna status
            Map<String, String> response = new HashMap<>();
            response.put("status", "atualizado");
            return new Gson().toJson(response);
        });
        get("/api/dados-hospital", (req, res) -> {
            res.type("application/json");

            DadosHospitalDAO dao = new DadosHospitalDAO();
            DadosHospital dados = dao.buscar(); // Certifique-se de ter esse método

            if (dados != null) {
                return new Gson().toJson(dados);
            } else {
                return "null";
            }
        });


    }
}
