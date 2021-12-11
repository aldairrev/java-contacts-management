<%-- 
    Document   : index
    Created on : Dec 9, 2021, 9:31:10 PM
    Author     : aldairrev
--%>

<%@page import="io.github.cdimascio.dotenv.Dotenv"%>
<%
    Dotenv dotenv = Dotenv.load();
    final String redirectURL = dotenv.get("APP_URL") + "/home";
    response.setStatus(response.SC_MOVED_PERMANENTLY);
    response.sendRedirect(redirectURL);
%>