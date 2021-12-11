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
package com.aldairrev.contactsmanagement.controllers;

import com.aldairrev.contactsmanagement.beans.EE_Contact;
import com.aldairrev.contactsmanagement.dao.AD_Contact;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.dbcp2.Utils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author aldairrev
 */
@WebServlet(name = "ContactsController", urlPatterns = {"/admin/contacts"})

public class ContactsController extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        AD_Contact AD = new AD_Contact();

        try {
            ArrayList<EE_Contact> contacts = new ArrayList<>();
            contacts = AD.selectAll();

            JSONObject resp_jo = new JSONObject();
            JSONObject data_jo = new JSONObject();
            JSONArray contacts_ja = new JSONArray();

            resp_jo.put("success", true);

            contacts.forEach((contact) -> {
                JSONObject contact_jo = new JSONObject();
                contact_jo.put("id", contact.getId());
                contact_jo.put("firstname", contact.getFirstname());
                contact_jo.put("surname", contact.getSurname());
                contact_jo.put("email", contact.getEmail());
                contact_jo.put("photo", contact.getPhoto());
                contact_jo.put("address", contact.getAddress());

                contacts_ja.put(contact_jo);
            });

            data_jo.put("contacts", contacts_ja);
            resp_jo.put("data", data_jo);

            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(resp_jo.toString());
            out.flush();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String firstname = req.getParameter("firstname");
        String surname = req.getParameter("surname");
        String email = req.getParameter("email");
        String address = req.getParameter("address");
        String photo = req.getParameter("photo");

        AD_Contact AD = new AD_Contact();
        boolean status;

        try {
            EE_Contact contact = new EE_Contact(firstname, surname, email, address, photo);
            System.out.println("CREATE CONTACT");
            System.out.println(contact.toString());

            status = AD.create(contact);
            if (status) {
                JSONObject resp_jo = new JSONObject();
                JSONObject data_jo = new JSONObject();
                data_jo.put("message", "Contact created successfully! Now you can see it your list.");
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
                data_jo.put("message", "Ops! Contact can't be created, please try again!");
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
            data_jo.put("message", "Ops! Contact can't be created, please try again!");
            resp_jo.put("success", false);
            resp_jo.put("data", data_jo);

            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(resp_jo.toString());
            out.flush();

            Logger.getLogger(ContactsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(req.getInputStream()));

        String data = br.readLine();

        String[] params = data.split("&");

        int id = 0;
        String firstname = null;
        String surname = null;
        String email = null;
        String photo = null;
        String address = null;

        for (String param : params) {
            String key = param.split("=")[0];
            String val = URLDecoder.decode(param.split("=")[1], StandardCharsets.UTF_8.toString());
            System.out.println(val);
            switch (key) {
                case "id":
                    id = Integer.parseInt(val);
                    break;
                case "firstname":
                    firstname = val;
                    break;
                case "surname":
                    surname = val;
                    break;
                case "email":
                    email = val;
                    break;
                case "photo":
                    photo = val;
                    break;
                case "address":
                    address = val;
                    break;
            }
        }

        AD_Contact AD = new AD_Contact();
        boolean status;

        try {
            EE_Contact contact = new EE_Contact(id, firstname, surname, email, address, photo);
            System.out.println("UPDATE CONTACT");
            System.out.println(contact.toString());

            status = AD.update(contact);
            if (status) {
                JSONObject resp_jo = new JSONObject();
                JSONObject data_jo = new JSONObject();
                data_jo.put("message", "Contact updated successfully!");
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
                data_jo.put("message", "Ops! Contact can't be updated, please try again!");
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
            data_jo.put("message", "Ops! Contact can't be updated, please try again!");
            resp_jo.put("success", false);
            resp_jo.put("data", data_jo);

            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(resp_jo.toString());
            out.flush();

            Logger.getLogger(ContactsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Running ContactsController";
    }// </editor-fold>
}
