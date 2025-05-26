package backsaim.example.saim.db.servlet;

import backsaim.example.saim.db.dal.ConsultaDAL;
import backsaim.example.saim.db.entidade.Consulta;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet("/agendar-consulta")
public class AgendarConsultaServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String medIdStr = req.getParameter("medId");
        String convenio = req.getParameter("paciConvenio");
        String tipo = req.getParameter("conTipo");
        String data = req.getParameter("conData");

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // Validação de campos obrigatórios
        if (medIdStr == null || medIdStr.isEmpty() || data == null || data.isEmpty()) {
            out.print("{\"success\": false, \"error\": \"Campos obrigatórios não preenchidos\"}");
            return;
        }

        try {
            int medId = Integer.parseInt(medIdStr);

            Consulta consulta = new Consulta();
            consulta.setMedId(medId);
            consulta.setPaciConvenio(convenio);
            consulta.setConTipo(tipo);
            consulta.setConData(LocalDate.parse(data));

            ConsultaDAL dal = new ConsultaDAL();
            boolean sucesso = dal.gravar(consulta);

            out.print("{\"success\": " + sucesso + "}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }

}
