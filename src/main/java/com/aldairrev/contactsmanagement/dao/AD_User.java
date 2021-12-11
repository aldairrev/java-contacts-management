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

import com.aldairrev.contactsmanagement.beans.EE_User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author aldairrev
 */
public class AD_User {

    public boolean create(EE_User user) throws SQLException {
        boolean state = false;
        Connection conn = null;

        try {
            conn = ConnectionPool.getInstance().getConnection();
            boolean isRegistered = checkEmailIsRegistered(user.getEmail());
            if (conn != null && !isRegistered)
            {
                String SQL = "INSERT INTO Users(firstname, surname, email, pass) VALUES(?, ?, ?, ?)";
                PreparedStatement pst;
                pst = conn.prepareStatement(SQL);
                pst.setString(1, user.getFirstname());
                pst.setString(2, user.getSurname());
                pst.setString(3, user.getEmail());
                pst.setString(4, user.getPass());
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

    public EE_User login(String email) throws SQLException {
        EE_User user = null;
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            String SQL = "SELECT * FROM users WHERE email = ?";
            PreparedStatement pst;
            pst = conn.prepareStatement(SQL);
            pst.setString(1, email);

            ResultSet rst;
            rst = pst.executeQuery();
            System.out.println(rst.toString());
            while (rst.next()) {
                user = new EE_User();
                user.setId(rst.getInt("id"));
                user.setFirstname(rst.getString("firstname"));
                user.setSurname(rst.getString("surname"));
                user.setEmail(rst.getString("email"));
                user.setPass(rst.getString("pass"));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionPool.getInstance().closeConnection(conn);
        }
        return user;
    }

    private boolean checkEmailIsRegistered(String email) throws SQLException {
        boolean isExist = true;

        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            String SQL = "SELECT * FROM users WHERE email = ?";
            PreparedStatement pst;
            pst = conn.prepareStatement(SQL);
            pst.setString(1, email);

            ResultSet rs;
            rs = pst.executeQuery();

            if (rs.next() == false) {
                isExist = false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionPool.getInstance().closeConnection(conn);
        }
        System.out.println("CHECKEMAILISREGISTERED: " + isExist);
        return isExist;
    }
}
