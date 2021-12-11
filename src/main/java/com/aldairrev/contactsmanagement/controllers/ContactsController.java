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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

/**
 *
 * @author aldairrev
 */
@WebServlet(name = "ContactsController", urlPatterns = {"/admin/contacts"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class ContactsController extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Dotenv dotenv = Dotenv.load();

        String firstname = "A";
        String surname = "A";
        String email = "A";
        String address = "A";

        String photo_name = "";
        try {
            String uploadPath = getServletContext().getRealPath("") + File.separator + "public/images";

            List<FileItem> multiparts = new ServletFileUpload(
                    new DiskFileItemFactory()).parseRequest(req);

            for (FileItem item : multiparts) {
                if (!item.isFormField()) {
                    String name = new File(item.getName()).getName();
                    System.out.println("UPLOAD PATH");
                    System.out.println(uploadPath);

                    int photo_index_ext = name.lastIndexOf('.');
                    String photo_ext = name.substring(photo_index_ext + 1);

                    photo_name = timestamp.getTime() + "." + photo_ext;
                    item.write(new File(uploadPath + File.separator + photo_name));
                } else {
                    System.out.println("item.isFormField()");
                    String name = new File(item.getName()).getName();
                    System.out.println(name);
                    
                }
            }
            firstname = req.getParameter("firstname");
                    surname = req.getParameter("surname");
                    email = req.getParameter("email");
                    address = req.getParameter("address");
            
            
        } catch (Exception ex) {
            System.out.println("File Upload Failed due to " + ex);
        }

        String photo = dotenv.get("APP_URL") + "/public/images/" + photo_name;

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
        super.doPut(req, resp); //To change body of generated methods, choose Tools | Templates.
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
        return "Running DashboardController";
    }// </editor-fold>

}
