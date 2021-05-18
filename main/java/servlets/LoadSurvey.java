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

/**
 *
 * @author Piotr Berezka
 */
public class LoadSurvey extends HttpServlet {

    public static String removeLastChar(String str) {
        return removeLastChars(str, 1);
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }
    
    public static String generateSingleRadio(String questionID, String answerText, int answerID) {
        return 
"                        <label class=\"radio\">" + answerText + "\n" +
"                            <input id='" + questionID + "a" + answerID + "' type=\"radio\" name=\"" + questionID + "\">\n" +
"                            <span class=\"checkmark\"></span>\n" +
"                        </label>\n";
    }
    public static String generateSingleCheckbox(String questionID, String answerText, int answerID) {
        return 
"                        <label class=\"check\">" + answerText + "\n" +
"                            <input id='" + questionID + "a" + answerID + "' type=\"checkbox\" name=\"" + questionID + "\">\n" +
"                            <span class=\"checkmarkb\"></span>\n" +
"                        </label>\n";
    }
    
    public static String generateMainDiv(int i, char questionType, String questionText) {
        return 
                "<div id='q" + (i + 1) + questionType + "' class=\"questionTile\" style=\"margin: auto; box-shadow: 0px 0px 12px 1px #888888; width: 650px; height: 100%; "
                + "padding: 20px; margin-top: 50px;\">\n" +
"                    <div id='questionText' class=\"questionText\" style='text-align: left;'>" + questionText + "</div>\n" +
"                    <div style='margin-top: 25px; text-align: left;'>";
    }
    
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
        
        int surveyID = Integer.parseInt(request.getParameter("surveyID"));
        boolean preview = Boolean.parseBoolean(request.getParameter("preview"));
        
        String outputHTML = "";
        
        try 
        {
//            surveyID = 222;
            
            ResultSet rs = DatabaseHandler.query("select * from surveys where SURVEY_ID = " + surveyID );
            rs.next();
            if(!preview)
            {
                outputHTML += "<div id=\"surveyTitleDiv\" style=\"background: rgb(2,218,172); margin-left: 70px; line-height: 90%; text-align: left; "
                        + "margin-bottom: 25px; padding: 25px;\n" +
"                background: linear-gradient(180deg, rgba(2,218,172,1) 0%, rgba(3,151,171,1) 100%); -webkit-background-clip: text;\n" +
"                -webkit-text-fill-color: transparent; font-family: 'Open sans condensed', sans-serif; font-size: 120px;\">" + rs.getString("SURVEY_TITLE") + "</div>\n" +
"                <div class=\"container\" style=\"width: 100%;\n" +
"                background: linear-gradient(180deg, rgba(2,218,172,1) 0%, rgba(3,151,171,1) 100%);\">\n" +
"                    <div id=\"surveyDescriptionDiv\" style=\"color: white; text-align: left;  font-family: 'Open sans condensed', \n" +
"                         sans-serif; text-align: left; font-size: 40px; "
                        + "margin: auto; padding: 30px 80px 30px 80px;\">" + rs.getString("SURVEY_DESCRIPTION") + "</div>\n" +
"                </div>";
            }
            else
            {
                outputHTML += 
            "<div>\n" +
"                <div style=\"background: rgb(2,218,172); margin-left: 70px; line-height: 90%; margin-bottom: 25px; padding: 25px; text-align: left; \n" +
"                background: linear-gradient(180deg, rgba(2,218,172,1) 0%, rgba(3,151,171,1) 100%); -webkit-background-clip: text;\n" +
"                -webkit-text-fill-color: transparent; font-family: 'Open sans condensed', sans-serif; font-size: 120px;\">\n" +
"                    Success!</br>You have successfully created a survey.</br>Here's a preview...\n" +
"                </div>\n" +
"            </div> ";
            }
            rs.close();
            rs = DatabaseHandler.query("select * from questions where SURVEY_ID = " + surveyID + " ORDER BY QUESTION_ID");
            
            ArrayList<String> questionHeaders = new ArrayList<String>();
            
            while(rs.next()){ questionHeaders.add(rs.getString("QUESTION_TEXT") + (rs.getBoolean("OBLIGATORY") == true ? "*" : "") + rs.getString("QUESTION_TYPE")); }
            
            for(int i = 0; i < questionHeaders.size(); i++)
            {
                String questionText = questionHeaders.get(i);
                char questionType = questionText.charAt(questionHeaders.get(i).length() - 1);
                questionText = removeLastChars(questionText, 1);
                
//                out.print(questionType + " | " + questionText);
                
                switch(questionType)
                {
                    case 'y':
                    case 'r':
                        outputHTML += generateMainDiv(i, questionType, questionText);
                        
                        rs = DatabaseHandler.query("select * from answers where SURVEY_ID = " + surveyID + " AND QUESTION_ID = " + (i + 1) + " ORDER BY ANSWER_ID");
                        
                        while(rs.next())
                        {
                            outputHTML += generateSingleRadio(("q" + (i + 1) + questionType), rs.getString("ANSWER"), rs.getInt("ANSWER_ID"));
                        }

                        outputHTML +=
"                    </div>\n" +
"                </div>";
                        
                        break;
                        
                    case 'c':
                        outputHTML += generateMainDiv(i, questionType, questionText);
                        
                        rs = DatabaseHandler.query("select * from answers where SURVEY_ID = " + surveyID + " AND QUESTION_ID = " + (i + 1) + " ORDER BY ANSWER_ID");
                        
                        while(rs.next())
                        {
                            outputHTML += generateSingleCheckbox(("q" + (i + 1) + questionType), rs.getString("ANSWER"), rs.getInt("ANSWER_ID"));
                        }
                        
                        outputHTML +=
"                    </div>\n" +
"                </div>";
                        
                        break;
                        
                    case 't':
                        outputHTML += generateMainDiv(i, questionType, questionText);
                        
                        outputHTML += 
                                "<textarea id='" + ("q" + (i + 1) + questionType) + "text' class=\"questionText\" \n" +
"                                  maxlength='150' oninput=\"autoresize(this)\" placeholder=\"Your answer here...\"></textarea>";
                        
                        outputHTML +=
"                    </div>\n" +
"                </div>";
                        break;
                }
                
            }
            if(preview)
            {
                outputHTML +="<div id='endingMsg' style=\"margin: auto; width: 85%; height: 100%; "
                        + "padding: 20px; margin-top: 50px; margin-bottom: 50px;\">\n" +                     
"                    <div style='margin-top: 25px; text-align: center;'>" +
                        "<div id='questionText' style=\"background: rgb(2,218,172); margin-left: 0px; line-height: 90%; margin-bottom: 25px; padding: 25px;\n" +
"                background: linear-gradient(180deg, rgba(2,218,172,1) 0%, rgba(3,151,171,1) 100%); -webkit-background-clip: text;\n" +
"                -webkit-text-fill-color: transparent; font-family: 'Open sans condensed', sans-serif; font-size: 40px;\">http://localhost:8080/ProjectIT/LoadSurvey.html?surveyID=" + surveyID + "&preview=false</div>\n" +
                        
                        "<div id='questionText' style=\"color: #cccccc; font-family: 'Open sans condensed', "
                        + "sans-serif; font-size: 30px; text-align: center;\">Your survey link</div>\n" +
                "</div>\n" +
"                </div>";
                
                outputHTML += 
                "<div  class=\"container\" style=\"margin-bottom: 100px;\">\n" +
"                    <div class=\"addQuestionButton\" onClick=\"javascript:location.href='index.html'\" style=\"padding-left: 20%; "
                        + "padding-right: 20%; margin-bottom: 100px;\">    Finish preview   </div>\n" +
"                </div>" ;
            }
            else
            {
                outputHTML += 
                "<div style=\"height: 50px;\"></div>\n" +
                "<div  class=\"container\" style=\"margin-bottom: 100px;\">\n" +
"                    <div class=\"addQuestionButton\" onClick=\"finishAnswering();\" style=\"padding-left: 20%; padding-right: 20%; "
                        + "margin-bottom: 100px;\">    Submit answers   </div>\n" +
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
