package backsaim.example.saim.db.servlet;

import backsaim.example.saim.db.dal.Consulta2DAL;
import backsaim.example.saim.db.entidade.Consulta2;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;

@WebServlet("/efetuar-consulta")
public class EfetuarConsultaServlet extends HttpServlet {

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
        resp.setContentType("application/json");

        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(sb.toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Consulta2 consulta = new Consulta2();
            consulta.setMedCRM(jsonNode.get("medCRM").asInt());
            consulta.setMedNome(jsonNode.get("medNome").asText());
            consulta.setMedEspecialidade(jsonNode.get("medEspecialidade").asText());
            consulta.setConData(new Date(sdf.parse(jsonNode.get("conData").asText()).getTime()));
            consulta.setHorarioConsulta(jsonNode.get("horarioConsulta").asInt());
            consulta.setPaciNome(jsonNode.get("paciNome").asText());
            consulta.setPaciCPF(jsonNode.get("paciCPF").asText());
            consulta.setPaciConvenio(jsonNode.get("paciConvenio").asInt());
            consulta.setNumConvenio(jsonNode.get("numConvenio").asInt());
            consulta.setDataUltimaConsulta(new Date(sdf.parse(jsonNode.get("dataUltimaConsulta").asText()).getTime()));
            consulta.setConTipo(jsonNode.get("conTipo").asText().charAt(0));

            Consulta2DAL dal = new Consulta2DAL();
            boolean sucesso = dal.gravar(consulta);

            PrintWriter out = resp.getWriter();
            out.print("{\"success\": " + sucesso + "}");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.print("{\"success\": false, \"error\": \"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }
}
