
import back.*;
import back.DAO.*;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        staticFiles.externalLocation("src/main/resources/front");

        port(4567); // ou 8080

        // Configuração de CORS
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
            response.type("application/json"); // Garante JSON por padrão
        });

        // Tratamento de exceções global para retornar JSON
        exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.type("application/json");
            Map<String, String> error = new HashMap<>();
            error.put("status", "erro");
            error.put("mensagem", "Erro interno no servidor: " + exception.getMessage());
            exception.printStackTrace(); // Logar a exceção no console do servidor
            response.body(gson.toJson(error));
        });

        System.out.println("🚀 Servidor iniciado com sucesso na porta 4567!");

        // --- Rotas Dados Hospital e Aparência ---
        post("/api/dados-hospital", (req, res) -> {
            DadosHospital dados = gson.fromJson(req.body(), DadosHospital.class);
            DadosHospitalDAO dao = new DadosHospitalDAO();
            dao.salvar(dados);
            return gson.toJson(Map.of("status", "ok"));
        });

        post("/api/aparencia-sistema", (req, res) -> {
            AparenciaSistema dados = gson.fromJson(req.body(), AparenciaSistema.class);
            AparenciaSistemaDAO dao = new AparenciaSistemaDAO();
            dao.salvar(dados);
            return gson.toJson(Map.of("status", "ok"));
        });

        get("/api/aparencia-sistema", (req, res) -> {
            AparenciaSistemaDAO dao = new AparenciaSistemaDAO();
            AparenciaSistema dados = dao.buscarUltimaConfiguracao();
            if (dados == null) {
                dados = new AparenciaSistema(); // Valores padrão
                dados.setNome("Nome Padrão");
                dados.setCor("#1f8f8f");
                dados.setRodape("Rodapé Padrão");
            }
            return gson.toJson(dados);
        });

        get("/api/dados-hospital", (req, res) -> {
            DadosHospitalDAO dao = new DadosHospitalDAO();
            DadosHospital dados = dao.buscar();
            if (dados == null) {
                dados = new DadosHospital(); // Valores padrão
                dados.setEmail("email@padrao.com");
                dados.setTelefone("(00) 00000-0000");
                // ... outros campos padrão
            }
            return gson.toJson(dados);
        });

        // --- Rota Colaborador (Simulado) ---
        get("/api/colaborador", (req, res) -> {
            // Simula busca de usuário logado. Em um sistema real, isso viria da sessão.
            UsuarioDAO dao = new UsuarioDAO();
            Usuario u = dao.buscarPorCpf("11122233344"); // CPF fixo para teste
            if (u == null) { // Cria um usuário padrão se não existir para teste
                u = new Usuario();
                u.setId(1); // ID fixo para teste
                u.setNome("Usuário Teste");
                u.setCpf("11122233344");
                u.setSenha("senha"); // Senha não deve ser exposta
                u.setTipo("admin");
                // Considerar salvar este usuário no banco se ele não existir
            }
            // Retornar apenas dados seguros (sem senha)
            Map<String, Object> usuarioInfo = new HashMap<>();
            usuarioInfo.put("id", u.getId());
            usuarioInfo.put("nome", u.getNome());
            usuarioInfo.put("tipo", u.getTipo());
            return gson.toJson(usuarioInfo);
        });

        // --- Rotas Paciente e Prontuário ---
        get("/api/paciente", (req, res) -> {
            String cpf = req.queryParams("cpf");
            String nome = req.queryParams("nome");
            if (cpf != null) cpf = cpf.replaceAll("[^\\d]", "");

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
                return gson.toJson(Map.of("status", "erro", "mensagem", "Paciente não encontrado"));
            }

            List<Prontuario> historico = prontuarioDAO.listarPorCpf(paciente.getCpf());
            Map<String, Object> response = new HashMap<>();
            response.put("nome", paciente.getNome());
            response.put("cpf", paciente.getCpf());
            response.put("historico", historico);
            return gson.toJson(response);
        });

        post("/api/prontuario", (req, res) -> {
            Prontuario prontuario = gson.fromJson(req.body(), Prontuario.class);
            // Adicionar validações se necessário
            ProntuarioDAO dao = new ProntuarioDAO();
            dao.inserir(prontuario);
            return gson.toJson(Map.of("status", "criado", "mensagem", "Prontuário salvo com sucesso"));
        });

        get("/api/prontuario/:cpf", (req, res) -> {
            String cpf = req.params("cpf").replaceAll("[^\\d]", "");
            ProntuarioDAO dao = new ProntuarioDAO();
            return gson.toJson(dao.listarPorCpf(cpf));
        });

        get("/api/consulta", (req, res) -> {
            String cpf = req.queryParams("cpf");
            ConsultaDAO dao = new ConsultaDAO();
            return gson.toJson(dao.buscarUltimaConsultaPorCpf(cpf));
        });

        // --- Rotas Médicos (Corrigidas) ---
        post("/api/medicos", (req, res) -> {
            Medico medico = gson.fromJson(req.body(), Medico.class);
            MedicoDAO dao = new MedicoDAO();

            // Validação básica (pode ser mais robusta)
            if (medico.getNome() == null || medico.getNome().trim().isEmpty() ||
                medico.getCrm() == null || medico.getCrm().trim().isEmpty() ||
                medico.getEspecialidade() == null || medico.getEspecialidade().trim().isEmpty() ||
                medico.getUsuarioId() <= 0) { // Verifica se usuarioId foi enviado e é válido
                res.status(400);
                return gson.toJson(Map.of(
                        "status", "erro",
                        "mensagem", "Dados inválidos. Nome, CRM, Especialidade e ID do Usuário são obrigatórios."
                ));
            }

            if (dao.crmExiste(medico.getCrm())) {
                res.status(400);
                return gson.toJson(Map.of(
                        "status", "erro",
                        "mensagem", "CRM já cadastrado no sistema"
                ));
            }

            boolean sucesso = dao.cadastrarMedico(medico);
            if (sucesso) {
                return gson.toJson(Map.of(
                        "status", "sucesso",
                        "mensagem", "Médico cadastrado com sucesso"
                ));
            } else {
                res.status(500); // Mantém 500 para falha no DAO
                return gson.toJson(Map.of(
                        "status", "erro",
                        "mensagem", "Falha ao cadastrar médico no banco de dados"
                ));
            }
        });

        get("/api/medicos", (req, res) -> {
            MedicoDAO dao = new MedicoDAO();
            List<Medico> medicos = dao.listarTodos();
            return gson.toJson(medicos);
        });

        get("/api/medicos/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params("id"));
                MedicoDAO dao = new MedicoDAO();
                Medico medico = dao.buscarPorId(id);
                if (medico != null) {
                    return gson.toJson(medico);
                } else {
                    res.status(404);
                    return gson.toJson(Map.of("status", "erro", "mensagem", "Médico não encontrado"));
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(Map.of("status", "erro", "mensagem", "ID inválido"));
            }
        });

        put("/api/medicos/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params("id"));
                Medico medico = gson.fromJson(req.body(), Medico.class);
                medico.setId(id);

                // Validação básica (similar ao POST, mas sem usuarioId)
                if (medico.getNome() == null || medico.getNome().trim().isEmpty() ||
                    medico.getCrm() == null || medico.getCrm().trim().isEmpty() ||
                    medico.getEspecialidade() == null || medico.getEspecialidade().trim().isEmpty()) {
                    res.status(400);
                    return gson.toJson(Map.of(
                            "status", "erro",
                            "mensagem", "Dados inválidos. Nome, CRM e Especialidade são obrigatórios."
                    ));
                }

                MedicoDAO dao = new MedicoDAO();
                boolean sucesso = dao.atualizarMedico(medico);
                if (sucesso) {
                    return gson.toJson(Map.of("status", "sucesso", "mensagem", "Médico atualizado com sucesso"));
                } else {
                    res.status(404); // Ou 500 se a falha for no DB
                    return gson.toJson(Map.of("status", "erro", "mensagem", "Médico não encontrado ou falha na atualização"));
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(Map.of("status", "erro", "mensagem", "ID inválido"));
            }
        });

        delete("/api/medicos/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params("id"));
                MedicoDAO dao = new MedicoDAO();
                boolean sucesso = dao.desativarMedico(id);
                if (sucesso) {
                    return gson.toJson(Map.of("status", "sucesso", "mensagem", "Médico desativado com sucesso"));
                } else {
                    res.status(404); // Ou 500
                    return gson.toJson(Map.of("status", "erro", "mensagem", "Médico não encontrado ou falha na desativação"));
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(Map.of("status", "erro", "mensagem", "ID inválido"));
            }
        });

        get("/api/medicos/especialidade/:especialidade", (req, res) -> {
            String especialidade = req.params("especialidade");
            MedicoDAO dao = new MedicoDAO();
            List<Medico> medicos = dao.buscarPorEspecialidade(especialidade);
            return gson.toJson(medicos);
        });

        get("/api/medicos/crm/:crm", (req, res) -> {
            String crm = req.params("crm");
            MedicoDAO dao = new MedicoDAO();
            Medico medico = dao.buscarPorCrm(crm);
            if (medico != null) {
                return gson.toJson(medico);
            } else {
                res.status(404);
                return gson.toJson(Map.of("status", "erro", "mensagem", "Médico não encontrado"));
            }
        });

        // --- Rotas Caixa (Revisar e Corrigir) ---
        post("/api/caixa/abrir", (req, res) -> {
            Caixa caixa = gson.fromJson(req.body(), Caixa.class);
            CaixaDAO dao = new CaixaDAO();

            // Validação básica
            if (caixa.getUsuarioAberturaId() <= 0 || caixa.getSaldoInicial() == null) {
                 res.status(400);
                 return gson.toJson(Map.of("status", "erro", "mensagem", "ID do usuário e saldo inicial são obrigatórios."));
            }

            if (dao.buscarCaixaAberto() != null) {
                res.status(400);
                return gson.toJson(Map.of("status", "erro", "mensagem", "Já existe um caixa aberto"));
            }

            boolean sucesso = dao.abrirCaixa(caixa);
            if (sucesso) {
                Caixa caixaAberto = dao.buscarCaixaAberto(); // Busca o caixa recém-aberto para retornar o ID
                return gson.toJson(Map.of(
                        "status", "sucesso",
                        "mensagem", "Caixa aberto com sucesso",
                        "caixa", caixaAberto // Retorna o objeto caixa completo
                ));
            } else {
                res.status(500);
                return gson.toJson(Map.of("status", "erro", "mensagem", "Falha ao abrir caixa"));
            }
        });

        post("/api/caixa/fechar", (req, res) -> {
            Map<String, Object> body = gson.fromJson(req.body(), Map.class);
            // É crucial validar os tipos aqui, especialmente o ID do usuário
            int usuarioFechamentoId = ((Double) body.getOrDefault("usuarioFechamentoId", 0.0)).intValue();
            String observacoes = (String) body.getOrDefault("observacoes", "");

            if (usuarioFechamentoId <= 0) {
                 res.status(400);
                 return gson.toJson(Map.of("status", "erro", "mensagem", "ID do usuário de fechamento é obrigatório."));
            }

            CaixaDAO dao = new CaixaDAO();
            Caixa caixaAberto = dao.buscarCaixaAberto();

            if (caixaAberto == null) {
                res.status(400);
                return gson.toJson(Map.of("status", "erro", "mensagem", "Não há caixa aberto para fechar"));
            }

            // Calcular saldo final ANTES de fechar
            BigDecimal saldoFinalCalculado = dao.calcularSaldoAtual(caixaAberto.getId());

            boolean sucesso = dao.fecharCaixa(caixaAberto.getId(), usuarioFechamentoId, saldoFinalCalculado, observacoes);
            if (sucesso) {
                 Caixa caixaFechado = dao.buscarPorId(caixaAberto.getId()); // Busca o caixa atualizado
                 return gson.toJson(Map.of(
                         "status", "sucesso",
                         "mensagem", "Caixa fechado com sucesso",
                         "caixa", caixaFechado
                 ));
            } else {
                res.status(500);
                return gson.toJson(Map.of("status", "erro", "mensagem", "Falha ao fechar caixa"));
            }
        });

        get("/api/caixa/status", (req, res) -> {
            CaixaDAO dao = new CaixaDAO();
            Caixa caixaAberto = dao.buscarCaixaAberto();
            if (caixaAberto != null) {
                // Calcula o saldo atual no momento da consulta
                BigDecimal saldoAtual = dao.calcularSaldoAtual(caixaAberto.getId());
                caixaAberto.setSaldoAtual(saldoAtual); // Adiciona um campo transitório ou calcula no frontend
                return gson.toJson(Map.of("status", "aberto", "caixa", caixaAberto));
            } else {
                return gson.toJson(Map.of("status", "fechado"));
            }
        });

        post("/api/caixa/movimentacao", (req, res) -> {
            MovimentacaoCaixa mov = gson.fromJson(req.body(), MovimentacaoCaixa.class);
            CaixaDAO caixaDAO = new CaixaDAO();
            Caixa caixaAberto = caixaDAO.buscarCaixaAberto();

            if (caixaAberto == null) {
                res.status(400);
                return gson.toJson(Map.of("status", "erro", "mensagem", "Nenhum caixa aberto para registrar movimentação"));
            }

            // Validação básica
            if (mov.getValor() == null || mov.getValor().compareTo(BigDecimal.ZERO) <= 0 ||
                mov.getDescricao() == null || mov.getDescricao().trim().isEmpty() ||
                mov.getTipo() == null || (!mov.getTipo().equals("entrada") && !mov.getTipo().equals("saida")) ||
                mov.getUsuarioRegistroId() <= 0) {
                 res.status(400);
                 return gson.toJson(Map.of("status", "erro", "mensagem", "Dados inválidos para movimentação."));
            }

            mov.setCaixaId(caixaAberto.getId()); // Associa ao caixa aberto
            mov.setDataHora(new Timestamp(System.currentTimeMillis())); // Define data/hora atual

            boolean sucesso = caixaDAO.registrarMovimentacao(mov);
            if (sucesso) {
                return gson.toJson(Map.of("status", "sucesso", "mensagem", "Movimentação registrada com sucesso"));
            } else {
                res.status(500);
                return gson.toJson(Map.of("status", "erro", "mensagem", "Falha ao registrar movimentação"));
            }
        });

        get("/api/caixa/movimentacoes", (req, res) -> {
            CaixaDAO dao = new CaixaDAO();
            Caixa caixaAberto = dao.buscarCaixaAberto();
            List<MovimentacaoCaixa> movimentacoes = new ArrayList<>();

            if (caixaAberto != null) {
                movimentacoes = dao.listarMovimentacoes(caixaAberto.getId());
            }
            // Poderia adicionar um parâmetro para buscar movimentações de caixas fechados
            return gson.toJson(movimentacoes);
        });

        // Rota para buscar histórico de caixas (opcional)
        get("/api/caixa/historico", (req, res) -> {
            CaixaDAO dao = new CaixaDAO();
            // Adicionar paginação ou filtros se necessário
            List<Caixa> historico = dao.listarHistoricoCaixas(100); // Limita aos últimos 100
            return gson.toJson(historico);
        });
    }
}

