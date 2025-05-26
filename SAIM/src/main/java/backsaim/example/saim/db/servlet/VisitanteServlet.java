package backsaim.example.saim.db.servlet;

import backsaim.example.saim.db.dal.VisitanteDAL;
import backsaim.example.saim.db.entidade.Visitante;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/visitante")
public class VisitanteServlet extends HttpServlet {

    private final VisitanteDAL dal = new VisitanteDAL();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        int visiCod = req.getParameter("visiCod") != null && !req.getParameter("visiCod").isEmpty()
                ? Integer.parseInt(req.getParameter("visiCod"))
                : 0;

        String visiNome = req.getParameter("visiNome");
        String visiCPF = req.getParameter("visiCPF");
        String visiEmail = req.getParameter("visiEmail");
        String visiTelefone = req.getParameter("visiTelefone");
        String visiEndereco = req.getParameter("visiEndereco");
        String visiCep = req.getParameter("visiCep");
        LocalDate visiDataCadastro = LocalDate.parse(req.getParameter("visiDataCadastro"));
        LocalDate visiDataNascimento = LocalDate.parse(req.getParameter("visiDataNascimento"));

        Visitante visitante = new Visitante(
                visiCod,
                visiNome,
                visiCPF,
                visiEmail,
                visiTelefone,
                visiEndereco,
                visiCep,
                visiDataCadastro,
                visiDataNascimento
        );

        boolean sucesso = (visiCod == 0) ? dal.gravar(visitante) : dal.alterar(visitante);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("{\"success\": " + sucesso + "}");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int visiCod = Integer.parseInt(req.getParameter("visiCod"));
        Visitante visitante = dal.get(visiCod);
        boolean sucesso = visitante != null && dal.apagar(visitante);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("{\"success\": " + sucesso + "}");
    }
}
