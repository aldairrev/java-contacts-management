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

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author aldairrev
 */
public class ConnectionPool {
    Dotenv dotenv = Dotenv.load();
    
    private final String DB = dotenv.get("DB_DATABASE");
    private final String USER = dotenv.get("DB_USERNAME");
    private final String PASS = dotenv.get("DB_PASSWORD");
    private final String HOST = dotenv.get("DB_HOST");
    private final String PORT = dotenv.get("DB_PORT");
    private final String URL = "jdbc:sqlserver://" + HOST + ":" + PORT + ";databaseName=" + DB;    
    private static ConnectionPool dataSource;
    private BasicDataSource basicDataSource = null;

    public ConnectionPool() {
        System.out.println("URL");
        System.out.println(URL);
        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        basicDataSource.setUsername(USER);
        basicDataSource.setPassword(PASS);
        basicDataSource.setUrl(URL);
        
        basicDataSource.setMinIdle(5);
        basicDataSource.setMaxIdle(20);
        basicDataSource.setMaxTotal(50);
        basicDataSource.setMaxWaitMillis(-1);
    }
    
    public static ConnectionPool getInstance()
    {
        if(dataSource == null)
        {
            dataSource = new ConnectionPool();
            return dataSource;
        }
        else
        {
            return dataSource;
        }
    }
    
    public Connection getConnection() throws SQLException
    {
        return basicDataSource.getConnection();
    }
    
    public void closeConnection(Connection conn) throws SQLException
    {
        conn.close();
    }
}
