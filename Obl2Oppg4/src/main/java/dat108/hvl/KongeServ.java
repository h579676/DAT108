package dat108.hvl;

import static dat108.hvl.Konger.getNorske;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/konge")
public class KongeServ extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        String stringAar = request.getParameter("stringAar");

        Konge konge = null;
        int aar;
        int feilKode = 0;

        String tittel = "Ingen resultat";

        if (stringAar.matches("[0-9]+")) {

            aar = Integer.parseInt(stringAar);

           List<Konge> kongeListe = getNorske().stream()
                    .filter(f -> f.getKongeFraAar() < aar && f.getKongeTilAar() >= aar)
                    .collect(Collectors.toList());

           if (kongeListe.size() == 1) {
               konge = kongeListe.get(0);
               tittel = konge.getNavn();
           } else {
               feilKode = 1;
           }
        } else {
            feilKode = 2;
        }

        response.setContentType("text/html; charset=ISO-8859-1");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head");
        out.println("<meta charset=\"ISO-8859-1\">");
        out.println("<title>");
        out.println(tittel);
        out.println("</title>");
        out.println("<head>");
        out.println("<body>");
        out.println("<center>");

        if (konge != null) {
            out.println("<img src=" + konge.getBilde() + " alt=" + konge.getNavn() + " height=400/>");
            out.println("<h1>Konge i år " + stringAar + " var " + konge.getNavn() + ", født " + konge.getFodtAar() + ",</h1>");
            out.println("<h1>konge fra " + konge.getKongeFraAar() + " til " + konge.getKongeTilAar() + ".</h1>");
        }
        if (feilKode == 1) {
            out.println("<h1>Ingen treff på år: " + stringAar + "</h1>");
        }
        if (feilKode == 2) {
            out.println("<h1>" + stringAar + " er ikke et år!</h1>");
        }

        out.println("<h2><a href=\"http://localhost:8080/obl2/\">Nytt søk</a><h2>");
        out.println("</center>");
        out.println("<body>");
        out.println("</html>");
    }
}