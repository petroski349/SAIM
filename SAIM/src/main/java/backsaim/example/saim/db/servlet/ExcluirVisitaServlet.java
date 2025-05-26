package backsaim.example.saim.db.servlet;

import backsaim.example.saim.db.dal.VisitaDAL;
import backsaim.example.saim.db.entidade.Visita;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/excluir-visita")
public class ExcluirVisitaServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        VisitaDAL dal = new VisitaDAL();
        Visita visita = dal.get(id);

        boolean sucesso = visita != null && dal.apagar(visita);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print("{\"success\": " + sucesso + "}");
    }
}

