package backsaim.example.saim.db.servlet;

import backsaim.example.saim.db.dal.ConsultaDAL;
import backsaim.example.saim.db.entidade.Consulta;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/consulta")
public class ConsultaServlet extends HttpServlet {

    private final ConsultaDAL dal = new ConsultaDAL();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int conCod = req.getParameter("conCod") != null ? Integer.parseInt(req.getParameter("conCod")) : 0;
        int medId = Integer.parseInt(req.getParameter("medId"));
        String convenio = req.getParameter("paciConvenio");
        String tipo = req.getParameter("conTipo");
        LocalDate data = LocalDate.parse(req.getParameter("conData"));

        Consulta consulta = new Consulta(conCod, medId, convenio, tipo, data);
        boolean sucesso = (conCod == 0) ? dal.gravar(consulta) : dal.alterar(consulta);

        resp.setContentType("application/json");
        resp.getWriter().print("{\"success\": " + sucesso + "}");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int conCod = Integer.parseInt(req.getParameter("conCod"));
        Consulta consulta = dal.get(conCod);
        boolean sucesso = consulta != null && dal.apagar(consulta);

        resp.setContentType("application/json");
        resp.getWriter().print("{\"success\": " + sucesso + "}");
    }
}
