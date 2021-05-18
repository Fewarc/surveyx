/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.DatabaseHandler;

/**
 *
 * @author Piotr Berezka
 */
public class SubmitSurvey extends HttpServlet {

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
        
        PrintWriter out = response.getWriter();
        
        String surveyTitle = request.getParameter("title");
        String surveyDescription = request.getParameter("description");
        String surveyIsPublic = request.getParameter("public");
        
        try 
        {
            String nick = (String)request.getSession().getAttribute("nick");
            DatabaseHandler.update("insert into surveys (SURVEY_TITLE, SURVEY_DESCRIPTION, CREATION_DATE, CREATION_TIME, IS_PUBLIC, TIMES_SUBMITTED, CREATOR, ON_GOING) " +
                                  "values ('" + surveyTitle + "', '" + surveyDescription + "', " + "CURRENT_DATE" + ", " + 
                                          "CURRENT_TIME" + ", '" + surveyIsPublic + "', " + '0' + ", '" + nick + "', " + "'true'" + ")");
            
            ResultSet rs = DatabaseHandler.query("select * from surveys where SURVEY_TITLE = '" + surveyTitle + 
                                                "' AND SURVEY_DESCRIPTION ='" + surveyDescription + "'");
            rs.next();
            int surveyID = rs.getInt("SURVEY_ID");
            out.print(surveyID);
            request.getSession().setAttribute("operatingSurveyID", surveyID);
        }
        catch(Exception e)
        {
            out.print(e.getMessage());
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
