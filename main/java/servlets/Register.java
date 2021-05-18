/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.DatabaseHandler;

/**
 *
 * @author Piotr Berezka
 */
public class Register extends HttpServlet {

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
        
        String nick = request.getParameter("nick");
        String mail = request.getParameter("mail");
        String password = request.getParameter("password");
        String cPassword = request.getParameter("confirmPassword");
        
        PrintWriter out = response.getWriter();
        char[] ret = new char[]{'0','0'};
        
        try  
        {
            ResultSet rs = DatabaseHandler.query("select * from users where login = " + "'" + nick + "'");
            rs.next();
            rs.getInt("ID");
            ret[0] = '1';
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) 
        {
            
        }
        
        if(!password.equals(cPassword))
        {
            ret[1] = '1';
        }
        
        out.print(Arrays.toString(ret));
        
        if(ret[0] == '0' && ret[1] == '0')
        {
            out.print("xd");
            try
            {
              DatabaseHandler.update("insert into users (login, password, mail) " +
                                  "values ('" + nick + "', '" + password + "', '" + mail +"')");
            }
            catch (ClassNotFoundException ex) 
            {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (SQLException ex)
            {
                out.print(ex.getMessage());
            }
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
