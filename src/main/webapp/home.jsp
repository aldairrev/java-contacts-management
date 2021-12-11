<%-- 
    Document   : home
    Created on : Dec 8, 2021, 10:55:17 AM
    Author     : aldairrev
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:master title="Home">
    <jsp:body>
        <div
            class="bg-image w-100 h-100 min-vh-100"
            style="background-image: url('https://assets.aldairrev.pe/bg-office-white.jpg')" 
        >
            <div
                class="w-100 min-vh-100 d-flex justify-content-center align-items-center flex-column"
                style="background-color: #000000AA"
            >
                <div class="text-light text-center">
                    <img class="logo-size mb-4" src="${APP_URL}/public/logo-white.svg" alt="logo" />
                    <h1>Contacts management</h1>
                    <br/>
                    <h3>Hello User!</h3>
                    <p>How are you doing?</p>
                    <p>Start to use this system clicking this button</p>
                </div>
                <div>
                    <a class="d-block btn btn-outline-light" href="${APP_URL}/admin/login">Let's start!</a>
                </div>
            </div>
        </div>
    </jsp:body>
</t:master>
