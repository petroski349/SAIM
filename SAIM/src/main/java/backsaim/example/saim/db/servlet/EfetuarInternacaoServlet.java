package backsaim.example.saim.db.servlet;

import backsaim.example.saim.db.dal.InternacaoDAL;
import backsaim.example.saim.db.entidade.Internacao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet("/efetuar-internacao")
public class EfetuarInternacaoServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");

        int prontuarioId = Integer.parseInt(req.getParameter("prontuarioId"));
        String paciNome = req.getParameter("paciNome");
        String dataEntradaStr = req.getParameter("dataEntrada");
        String dataSaidaStr = req.getParameter("dataSaida");

        Internacao internacao = new Internacao();
        internacao.setProntuarioId(prontuarioId);
        internacao.setPaciNome(paciNome);
        internacao.setDataEntrada(LocalDate.parse(dataEntradaStr));

        if (dataSaidaStr != null && !dataSaidaStr.isEmpty())
            internacao.setDataSaida(LocalDate.parse(dataSaidaStr));
        else
            internacao.setDataSaida(null);

        InternacaoDAL dal = new InternacaoDAL();
        boolean sucesso = dal.gravar(internacao);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print("{\"success\": " + sucesso + "}");
    }


}
