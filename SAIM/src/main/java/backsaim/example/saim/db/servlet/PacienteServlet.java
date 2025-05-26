package backsaim.example.saim.db.servlet;

import backsaim.example.saim.db.dal.PacienteDAL;
import backsaim.example.saim.db.entidade.Paciente;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/pacientes")
public class PacienteServlet extends HttpServlet {

    private final PacienteDAL dal = new PacienteDAL();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        int paciId = req.getParameter("paciId") != null && !req.getParameter("paciId").isEmpty()
                ? Integer.parseInt(req.getParameter("paciId"))
                : 0;

        String paciNome = req.getParameter("paciNome");
        String paciCPF = req.getParameter("paciCPF");
        String paciConvenio = req.getParameter("paciConvenio");
        String paciDataInternacaoStr = req.getParameter("paciData_Internacao");
        String paciDataAltaStr = req.getParameter("paciData_Alta");
        String paciEndereco = req.getParameter("paciEndereco");
        String paciRg = req.getParameter("paciRg");
        String paciTelefone = req.getParameter("paciTelefone");
        String paciDataNascimentoStr = req.getParameter("paciData_Nascimento");
        String paciCep = req.getParameter("paciCep");

        LocalDate paciDataInternacao = paciDataInternacaoStr != null ? LocalDate.parse(paciDataInternacaoStr) : null;
        LocalDate paciDataAlta = paciDataAltaStr != null ? LocalDate.parse(paciDataAltaStr) : null;
        LocalDate paciDataNascimento = paciDataNascimentoStr != null ? LocalDate.parse(paciDataNascimentoStr) : null;

        Paciente paciente = new Paciente(
                paciId,
                paciNome,
                paciCPF,
                paciConvenio,
                paciDataInternacao,
                paciDataAlta,
                paciEndereco,
                paciRg,
                paciTelefone,
                paciDataNascimento,
                paciCep
        );

        boolean sucesso = (paciId == 0) ? dal.gravar(paciente) : dal.alterar(paciente);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("{\"success\": " + sucesso + "}");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int paciId = Integer.parseInt(req.getParameter("paciId"));
        Paciente paciente = dal.get(paciId);
        boolean sucesso = paciente != null && dal.apagar(paciente);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("{\"success\": " + sucesso + "}");
    }
}
