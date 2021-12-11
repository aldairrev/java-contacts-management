/*
 * The MIT License
 *
 * Copyright 2021 aldairrev.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.aldairrev.contactsmanagement.controllers.auth;

import com.aldairrev.contactsmanagement.beans.EE_User;
import com.aldairrev.contactsmanagement.dao.AD_User;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.json.JSONObject;

/**
 *
 * @author aldairrev
 */
@WebServlet(name = "LoginController", urlPatterns = {"/admin/login"})
public class LoginController extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        if (session.getAttribute("user") != null) {
            Dotenv dotenv = Dotenv.load();
            final String redirectURL = dotenv.get("APP_URL") + "/admin/dashboard";
            response.sendRedirect(redirectURL);
        } else {
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String pass = req.getParameter("pass");

        AD_User AD = new AD_User();
        EE_User user = null;

        try {
            user = AD.login(email);
            if (!(user == null)) {
                StrongPasswordEncryptor encryp = new StrongPasswordEncryptor();
                if (encryp.checkPassword(pass, user.getPass())) {
                    HttpSession session = req.getSession(true);
                    
                    session.setAttribute("user", user);
                    
                    JSONObject resp_jo = new JSONObject();
                    JSONObject data_jo = new JSONObject();
                    data_jo.put("message", "You are logged in! Wait while we redirect you to the dashboard.");
                    resp_jo.put("success", true);
                    resp_jo.put("data", data_jo);
                    
                    PrintWriter out = resp.getWriter();
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    out.print(resp_jo.toString());
                    out.flush();
                } else {
                    JSONObject resp_jo = new JSONObject();
                    JSONObject data_jo = new JSONObject();
                    data_jo.put("message", "Oh, ship! Email or password is wrong.");
                    resp_jo.put("success", false);
                    resp_jo.put("data", data_jo);

                    PrintWriter out = resp.getWriter();
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    out.print(resp_jo.toString());
                    out.flush();
                }
            } else {
                JSONObject resp_jo = new JSONObject();
                JSONObject data_jo = new JSONObject();
                data_jo.put("message", "Oh, ship! Email or password is wrong.");
                resp_jo.put("success", false);
                resp_jo.put("data", data_jo);

                PrintWriter out = resp.getWriter();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                out.print(resp_jo.toString());
                out.flush();
            }
        } catch (SQLException ex) {
            JSONObject resp_jo = new JSONObject();
            JSONObject data_jo = new JSONObject();
            
            data_jo.put("message", "Malarkey! There was a problem, please try again.");
            resp_jo.put("success", false);
            resp_jo.put("data", data_jo);
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(resp_jo.toString());
            out.flush();
            
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Running LoginController";
    }// </editor-fold>
}
