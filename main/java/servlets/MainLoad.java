/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Piotr Berezka
 */
@WebServlet(name = "MainLoad", urlPatterns = {"/MainLoad"})
public class MainLoad extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        if(request.getSession().getAttribute("isUserLoggedIn") == null)
        {
            request.getSession().setAttribute("isUserLoggedIn", false);
        }
        if(request.getSession().getAttribute("didLogInFail") == null)
        {
            request.getSession().setAttribute("didLogInFail", true);
        }
        PrintWriter out = response.getWriter();
        if(!(boolean)request.getSession().getAttribute("didLogInFail") && (boolean)request.getSession().getAttribute("isUserLoggedIn"))
        {
            try  {
                /* TODO output your page here. You may use following sample code. */
                out.println("<div id=\"navBar\" style=\" position:fixed; height:75px; width:100%; background: rgb(6,122,167); background: linear-gradient(180deg, rgba(6,122,167,1) 0%, rgba(0,192,173,1) 80%); \">"); 
                out.println("<div style=\"border-width:5px; border-color:black; height:100%; width:1300px; margin: auto;\">");
                out.println("<a href=\"http://localhost:8080/ProjectIT/\"> <div class=\"logoButton\" style=\"margin-right: 1px; \n" +
"                                                                 \"><img style=\"height:65px; width: 250px\" src=\"surveyxw.png\"> </div> </a>");
                out.println("<a onClick=\"checkLogNavBar('ADD')\"> <div class=\"navBarButton\"><h1 class=\"buttonText\">Add survey</h1></div> </a>");
                out.println("<a onClick=\"checkLogNavBar('MY')\"> <div class=\"navBarButton\"><h1 class=\"buttonText\">My surveys</h1></div> </a>");
                out.println("<a href=\"http://localhost:8080/ProjectIT/\"> <div class=\"navBarButton\"><h1 class=\"buttonText\">Browse</h1></div> </a>");
                out.println("<a href=\"http://localhost:8080/ProjectIT/\"> <div class=\"navBarButton\"><h1 class=\"buttonText\">Help</h1></div> </a>");
                out.println("<a href=\"http://localhost:8080/ProjectIT/\"> <div class=\"navBarButton\"><h1 class=\"buttonText\">FAQ</h1></div> </a>");
                out.println("<a href=\"http://localhost:8080/ProjectIT/\"> <div class=\"alterButton\" onClick=\"logout()\"><h1>LOG OUT</h1></div> </a>");
                out.println("<a href=\"http://localhost:8080/ProjectIT/\"> <div class=\"logButton\"><h1>" + request.getSession().getAttribute("nick") + "</h1></div> </a>");
                out.println("</div>");
                out.println("</div>");
            }
            catch(Exception e)
            {
                out.print(e.getMessage());
            }
            request.getSession().setAttribute("didLogInFail", false);
        }
        else
        {
            try {
                /* TODO output your page here. You may use following sample code. */
                out.println("<script type=\"text/javascript\" src=\"js/ajax.js\"></script>");
                out.println("<div id=\"navBar\" style=\" position:fixed; height:75px; width:100%; background: rgb(6,122,167); background: linear-gradient(180deg, rgba(6,122,167,1) 0%, rgba(0,192,173,1) 80%); \">"); 
                out.println("<div style=\"border-width:5px; border-color:black; height:100%; width:1300px; margin: auto;\">");
                out.println("<a href=\"http://localhost:8080/ProjectIT/\"> <div class=\"logoButton\" style=\"margin-right: 1px; \n" +
"                                                                 \"><img style=\"height:65px; width: 250px\" src=\"surveyxw.png\"> </div> </a>");
                out.println("<a onClick=\"checkLogNavBar('ADD')\"> <div class=\"navBarButton\"><h1 class=\"buttonText\">Add survey</h1></div> </a>");
                out.println("<a onClick=\"checkLogNavBar('MY')\"> <div class=\"navBarButton\"><h1 class=\"buttonText\">My surveys</h1></div> </a>");
                out.println("<a href=\"http://localhost:8080/ProjectIT/\"> <div class=\"navBarButton\"><h1 class=\"buttonText\">Browse</h1></div> </a>");
                out.println("<a href=\"http://localhost:8080/ProjectIT/\"> <div class=\"navBarButton\"><h1 class=\"buttonText\">Help</h1></div> </a>");
                out.println("<a href=\"http://localhost:8080/ProjectIT/\"> <div class=\"navBarButton\"><h1 class=\"buttonText\">FAQ</h1></div> </a>");
                out.println("<a href=\"RegistrationPanel.html\"> <div class=\"alterButton\"><h1>JOIN NOW</h1></div> </a>");
                out.println("<a href=\"LogInPanel.html\"> <div class=\"logButton\"><h1>LOG IN</h1></div> </a>");
                out.println("</div>");
                out.println("</div>");
            }
            catch(Exception e)
            {
                out.print(e.getMessage());
            }
            request.getSession().setAttribute("didLogInFail", null);
            request.getSession().setAttribute("isUserLoggedIn", false);
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
