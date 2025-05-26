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

@WebServlet("/agendar-visita")
public class AgendarVisitaServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String nome = req.getParameter("nome");
        String cpf = req.getParameter("cpf");
        String dataEntrada = req.getParameter("dataEntrada");
        String dataSaida = req.getParameter("dataSaida");

        VisitaDAL dal = new VisitaDAL();

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        if (dal.cpfExiste(cpf)) {
            out.print("{\"success\": false, \"error\": \"CPF já cadastrado.\"}");
            return;
        }

        Visita visita = new Visita();
        visita.setVisinome(nome);
        visita.setVisicpf(cpf);
        visita.setVisidata_entrada(LocalDate.parse(dataEntrada));

        if (dataSaida != null && !dataSaida.isEmpty())
            visita.setVisidata_saida(LocalDate.parse(dataSaida));
        else
            visita.setVisidata_saida(null);

        boolean sucesso = dal.agendarVisita(visita);
        out.print("{\"success\": " + sucesso + "}");
    }

}
