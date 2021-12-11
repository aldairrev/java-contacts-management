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
package com.aldairrev.contactsmanagement.dao;

import com.aldairrev.contactsmanagement.beans.EE_Contact;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aldairrev
 */
public class AD_Contact {

    public ArrayList selectAll() throws SQLException {
        ArrayList<EE_Contact> contacts = new ArrayList<>();
        Connection conn = null;

        try {
            conn = ConnectionPool.getInstance().getConnection();
            if (conn != null) {
                String SQL = "SELECT * FROM contacts";
                PreparedStatement pst;
                pst = conn.prepareStatement(SQL);

                ResultSet rst;
                rst = pst.executeQuery();
                System.out.println(rst.toString());
                while (rst.next()) {
                    EE_Contact contact = new EE_Contact();
                    contact.setId(rst.getInt("id"));
                    contact.setFirstname(rst.getString("firstname"));
                    contact.setSurname(rst.getString("surname"));
                    contact.setEmail(rst.getString("email"));
                    contact.setPhoto(rst.getString("photo"));
                    contact.setAddress(rst.getString("address"));
                    
                    contacts.add(contact);
                }
            } else {
                System.out.println("Error Creating a Contact");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionPool.getInstance().closeConnection(conn);
        }
        return contacts;
    }

    public boolean create(EE_Contact contact) throws SQLException {
        boolean state = false;
        Connection conn = null;

        try {
            conn = ConnectionPool.getInstance().getConnection();
            if (conn != null) {
                String SQL = "INSERT INTO contacts(firstname, surname, email, photo, address) VALUES(?, ?, ?, ?, ?)";
                PreparedStatement pst;
                pst = conn.prepareStatement(SQL);
                pst.setString(1, contact.getFirstname());
                pst.setString(2, contact.getSurname());
                pst.setString(3, contact.getEmail());
                pst.setString(4, contact.getPhoto());
                pst.setString(5, contact.getAddress());
                int res = 0;
                res = pst.executeUpdate();
                if (res > 0) {
                    state = true;
                }
            } else {
                System.out.println("Error Creating a User");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionPool.getInstance().closeConnection(conn);
        }
        return state;
    }
    
    public boolean update(EE_Contact contact) throws SQLException {
        boolean state = false;
        Connection conn = null;

        try {
            conn = ConnectionPool.getInstance().getConnection();
            if (conn != null) {
                String SQL = "UPDATE contacts SET "
                        + "firstname = ?, surname = ?, "
                        + "email = ?, photo = ?, address = ? "
                        + "where id = ?";
                PreparedStatement pst;
                pst = conn.prepareStatement(SQL);
                pst.setString(1, contact.getFirstname());
                pst.setString(2, contact.getSurname());
                pst.setString(3, contact.getEmail());
                pst.setString(4, contact.getPhoto());
                pst.setString(5, contact.getAddress());
                pst.setInt(6, contact.getId());
                int res = 0;
                res = pst.executeUpdate();
                if (res > 0) {
                    state = true;
                }
            } else {
                System.out.println("Error Updating a Contact");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionPool.getInstance().closeConnection(conn);
        }
        return state;
    }
    
    public boolean delete(EE_Contact contact) throws SQLException {
        boolean state = false;
        Connection conn = null;

        try {
            conn = ConnectionPool.getInstance().getConnection();
            if (conn != null) {
                String SQL = "DELETE FROM contacts WHERE id = ?";
                PreparedStatement pst;
                pst = conn.prepareStatement(SQL);
                pst.setInt(1, contact.getId());
                int res = 0;
                res = pst.executeUpdate();
                if (res > 0) {
                    state = true;
                }
            } else {
                System.out.println("Error Deleting a Contact");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionPool.getInstance().closeConnection(conn);
        }
        return state;
    }
}
