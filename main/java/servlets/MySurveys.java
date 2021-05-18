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
public class MySurveys extends HttpServlet {

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
        
        String outputHTML = "";
        
        String creator = (String)request.getSession().getAttribute("nick");
        
        try 
        {
            ResultSet rs = DatabaseHandler.query("select * from surveys where CREATOR = '" + creator + "'");
            
            while(rs.next())
            {
                outputHTML += "<div class=\"questionTile\" style=\"margin: auto; box-shadow: 0px 0px 12px 1px #888888; width: 650px; height: 100%; \n" +
"                 padding: 20px; margin-top: 50px;\">\n" +
"                    <div class=\"questionText\" style='text-align: left; \n" +
"                         border: none;'>" + rs.getString("SURVEY_TITLE") + "</div>\n" +
"                    <div style=\"margin-left: 10px;text-align: left; color: #cccccc; \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px; border-bottom: solid 2px #cccccc; padding-bottom: 20px;\">"
                        + rs.getString("SURVEY_DESCRIPTION") + "</div>\n" +
"                    <div style=\"margin-left: 10px;text-align: left; color: #cccccc; \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px;\">total submissions : " + rs.getInt("TIMES_SUBMITTED") + "</div>\n" +
"                    <div style=\"margin-left: 10px;text-align: left; color: #cccccc; \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px;\">Status: " + (rs.getBoolean("ON_GOING") == true? "on-going" : "closed") + "</div>\n" +
"                         <div style=\"height: 30px;\"></div>\n" +
"                    <div id='" + rs.getInt("SURVEY_ID") + "' class=\"addQuestionButton\" onClick=\"viewResults(this);\" style=\" text-align: left;  \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px;\">view results</div>\n" +
"                         <div style=\"height: 10px;\"></div>\n" +
"                </div>";
            }
            out.print(outputHTML);
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
