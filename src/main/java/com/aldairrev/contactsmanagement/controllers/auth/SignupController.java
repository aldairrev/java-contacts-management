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
import org.jasypt.util.password.StrongPasswordEncryptor;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author aldairrev
 */
@WebServlet(name = "SignupController", urlPatterns = {"/admin/signup"})
public class SignupController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String firstname = req.getParameter("firstname");
        String surname = req.getParameter("surname");
        String email = req.getParameter("email");
        String pass = req.getParameter("pass");

        StrongPasswordEncryptor encriptador = new StrongPasswordEncryptor();
        String password_encrypt = encriptador.encryptPassword(pass);

        EE_User user = new EE_User(firstname, surname, email, password_encrypt);

        AD_User AD = new AD_User();
        boolean isCreated = false;
        try {
            isCreated = AD.create(user);

            JSONObject resp_jo = new JSONObject();
            JSONObject data_jo = new JSONObject();

            if (isCreated) {
                data_jo.put("message", "Hooray! Your new account has been successfully created.");
                resp_jo.put("success", true);
                resp_jo.put("data", data_jo);
            } else {
                data_jo.put("message", "Ay, caramba! The email (" + email + ") is already registered.");
                resp_jo.put("success", false);
                resp_jo.put("data", data_jo);
            }

            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(resp_jo.toString());
            out.flush();
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
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
