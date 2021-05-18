/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.DatabaseHandler;
import static servlets.LoadSurvey.removeLastChars;

/**
 *
 * @author Piotr Berezka
 */
public class ViewResults extends HttpServlet {

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
        
        String surveyID = request.getParameter("surveyID");
        
        String outputHTML = "";
        
        try 
        {
            ResultSet rs = DatabaseHandler.query("select * from surveys where SURVEY_ID = " + surveyID );
            rs.next(); 

            outputHTML += "<div id=\"surveyTitleDiv\" style=\"background: rgb(2,218,172); margin-left: 70px; line-height: 90%; margin-bottom: 25px; padding: 25px;\n" +
   "                background: linear-gradient(180deg, rgba(2,218,172,1) 0%, rgba(3,151,171,1) 100%); -webkit-background-clip: text;\n" +
   "                -webkit-text-fill-color: transparent; font-family: 'Open sans condensed', sans-serif; font-size: 120px;\">" + rs.getString("SURVEY_TITLE") + "</div>\n" +
   "\n" +
   "                <div class=\"container\" style=\"width: 100%;\n" +
   "                background: linear-gradient(180deg, rgba(2,218,172,1) 0%, rgba(3,151,171,1) 100%);\">\n" +
   "                    <div id=\"surveyDescriptionDiv\" style=\"color: white; font-family: 'Open sans condensed', \n" +
   "                         sans-serif; text-align: left; font-size: 40px; margin: auto; padding: 30px 80px 30px 80px;\">" + rs.getString("SURVEY_DESCRIPTION") + "</div>\n" +
   "                </div>\n" +
   "\n" +
   "                <div id=\"surveyPublicBoolDiv\" style=\"color: #cccccc; font-family: 'Open sans condensed', \n" +
   "                     sans-serif; font-size: 30px; margin-bottom: 50px; margin-left: 80px;\">" + 
                    (rs.getBoolean("IS_PUBLIC") == true? "a public survey" : "a private survey") + "</div>";
            
            rs.close();
            rs = DatabaseHandler.query("select * from questions where SURVEY_ID = " + surveyID + " ORDER BY QUESTION_ID");

            ArrayList<String> questionHeaders = new ArrayList<String>();

            while(rs.next()){ questionHeaders.add(rs.getString("QUESTION_TEXT") + (rs.getBoolean("OBLIGATORY") == true ? "*" : "") + rs.getString("QUESTION_TYPE")); }
         
            for(int i = 0; i < questionHeaders.size(); i++)
            {
                String questionText = questionHeaders.get(i);
                char questionType = questionText.charAt(questionHeaders.get(i).length() - 1);
                questionText = removeLastChars(questionText, 1);
                
                out.print("yes");
                
                switch(questionType)
                {
                    case 'y':
                        
                        outputHTML += 
                  "<div id='tile' class=\"questionTile\" style=\"margin: auto; margin-bottom: 50px; box-shadow: 0px 0px 12px 1px #888888; width: 650px; height: 100%; \n" +
"                 padding: 20px; margin-top: 50px;\">\n" +
"                    <div class=\"questionText\" style='text-align: left; \n" +
"                         border: none;'>" + questionText + "</div>\n" +
                                "<div style=\"margin-left: 10px;text-align: left; color: #cccccc; \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px; "
                                + "border-bottom: solid 2px #cccccc; padding-bottom: 20px;\">Yes/No question</div>" +
                  "</div>";
                        
                        rs = DatabaseHandler.query("select * from answers where SURVEY_ID = " + surveyID + " AND QUESTION_ID = " + (i + 1) + " ORDER BY ANSWER_ID");
                        
                        while(rs.next())
                        {
                            outputHTML += "<div style=\"margin-left: 10px;text-align: left; color: #cccccc; \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px; "
                                + " padding-bottom: 20px;\">" + rs.getInt("TIMES_ANSWERED") + " answered :</div>";
                            
                            outputHTML += "<div class=\"questionText\" style='text-align: left; margin-bottom: 30px;\n" +
"                         border: none;'>" + rs.getString("ANSWER") + "</div>\n";
                        }
                        
                        break;
                        
                    case 'r':
                        
                        outputHTML += 
                  "<div id='tile' class=\"questionTile\" style=\"margin: auto; margin-bottom: 50px; box-shadow: 0px 0px 12px 1px #888888; width: 650px; height: 100%; \n" +
"                 padding: 20px; margin-top: 50px;\">\n" +
"                    <div class=\"questionText\" style='text-align: left; \n" +
"                         border: none;'>" + questionText + "</div>\n" +
                                "<div style=\"margin-left: 10px;text-align: left; color: #cccccc; \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px; "
                                + "border-bottom: solid 2px #cccccc; padding-bottom: 20px;\">Radio button question</div>" +
                  "</div>";
                        
                        rs = DatabaseHandler.query("select * from answers where SURVEY_ID = " + surveyID + " AND QUESTION_ID = " + (i + 1) + " ORDER BY ANSWER_ID");
                        
                        while(rs.next())
                        {
                            outputHTML += "<div style=\"margin-left: 10px;text-align: left; color: #cccccc; \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px; "
                                + " padding-bottom: 20px;\">" + rs.getInt("TIMES_ANSWERED") + " answered :</div>";
                            
                            outputHTML += "<div class=\"questionText\" style='text-align: left; margin-bottom: 30px;\n" +
"                         border: none;'>" + rs.getString("ANSWER") + "</div>\n";
                        }
                        
                        break;
                        
                    case 'c':
                        
                        outputHTML += 
                  "<div id='tile' class=\"questionTile\" style=\"margin: auto; margin-bottom: 50px; box-shadow: 0px 0px 12px 1px #888888; width: 650px; height: 100%; \n" +
"                 padding: 20px; margin-top: 50px;\">\n" +
"                    <div class=\"questionText\" style='text-align: left; \n" +
"                         border: none;'>" + questionText + "</div>\n" +
                                "<div style=\"margin-left: 10px;text-align: left; color: #cccccc; \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px; "
                                + "border-bottom: solid 2px #cccccc; padding-bottom: 20px;\">Checkbox question</div>" +
                  "</div>";
                        
                        rs = DatabaseHandler.query("select * from answers where SURVEY_ID = " + surveyID + " AND QUESTION_ID = " + (i + 1) + " ORDER BY ANSWER_ID");
                        
                        while(rs.next())
                        {
                            outputHTML += "<div style=\"margin-left: 10px;text-align: left; color: #cccccc; \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px; "
                                + " padding-bottom: 20px;\">" + rs.getInt("TIMES_ANSWERED") + " answered :</div>";
                            
                            outputHTML += "<div class=\"questionText\" style='text-align: left; margin-bottom: 30px;\n" +
"                         border: none;'>" + rs.getString("ANSWER") + "</div>\n";
                        }
                        
                        break;
                        
                    case 't':
                        
                        outputHTML += 
                  "<div id='tile' class=\"questionTile\" style=\"margin: auto; margin-bottom: 50px; box-shadow: 0px 0px 12px 1px #888888; width: 650px; height: 100%; \n" +
"                 padding: 20px; margin-top: 50px;\">\n" +
"                    <div class=\"questionText\" style='text-align: left; \n" +
"                         border: none;'>" + questionText + "</div>\n" +
                                "<div style=\"margin-left: 10px;text-align: left; color: #cccccc; \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px; "
                                + "border-bottom: solid 2px #cccccc; padding-bottom: 20px;\">Open-ended question</div>" +
                  "</div>";
                        
                        rs = DatabaseHandler.query("select * from answers where SURVEY_ID = " + surveyID + " AND QUESTION_ID = " + (i + 1) + " ORDER BY ANSWER_ID");
                        
                        outputHTML += "<div style=\"margin-left: 10px;text-align: left; color: #cccccc; \n" +
"                         font-family: 'Open sans condensed', sans-serif; font-size: 30px; "
                                + " padding-bottom: 20px;\">answers :</div>";
                        
                        while(rs.next())
                        {
                            outputHTML += "<div class=\"questionText\" style='text-align: left; margin-bottom: 30px;\n" +
"                         border: none;'>" + rs.getString("ANSWER") + "</div>\n";
                        }
                        
                        break;
                }
                
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
