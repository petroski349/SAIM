package backsaim.example.saim.db.servlet;

import backsaim.example.saim.db.dal.VisitaDAL;
import backsaim.example.saim.db.entidade.Visita;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet("/alterar-visita")
public class AlterarVisitaServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String nome = req.getParameter("nome");
        String cpf = req.getParameter("cpf");
        String dataEntrada = req.getParameter("dataEntrada");
        String dataSaida = req.getParameter("dataSaida");

        VisitaDAL dal = new VisitaDAL();
        Visita visita = new Visita(id, nome, cpf, LocalDate.parse(dataEntrada),
                (dataSaida != null && !dataSaida.isEmpty()) ? LocalDate.parse(dataSaida) : null);

        boolean sucesso = dal.alterar(visita);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print("{\"success\": " + sucesso + "}");
    }
}

