<%-- 
    Document   : sidebar
    Created on : Dec 10, 2021, 6:45:14 PM
    Author     : aldairrev
--%>

<%@tag description="Sidebar for admin page" pageEncoding="UTF-8"%>
<%@attribute name="fullname"%>
<%@attribute name="email"%>

<div id="app-admin" class="container-fluid">
    <div class="row flex-nowrap">
        <div class="col-auto col-md-3 col-xl-2 p-sm-3 p-2 bg-light min-vh-100">
            <div class="flex-column align-items-center d-none d-sm-flex">
                <i class="fas fa-user fa-3x mb-3"></i>
                <h4 class="fw-bold">${fullname}</h4>
                <h6 class="text-secondary">(${email})</h6>
            </div>
            <hr class="d-none d-sm-flex"/>
            <ul class="nav nav-pills flex-column mb-auto">
                <li class="mb-2">
                    <a href="#" class="nav-link" 
                       :class="{ active: page === 'dashboard', 'link-dark': page !== 'dashboard'}"
                       v-on:click="changePage('dashboard')"
                       data-page="dashboard">
                        <i class="fas fa-tachometer-alt me-0 me-sm-2"></i>
                        <span class="d-none d-sm-inline">Dashboard</span>
                    </a>
                </li>
                <li>
                    <a href="#" class="nav-link link-dark"
                       :class="{ active: page === 'contacts', 'link-dark': page !== 'contacts' }"
                       v-on:click="changePage('contacts')"
                       data-page="contacts">
                        <i class="fas fa-address-book me-0 me-sm-2"></i>
                        <span class="d-none d-sm-inline">Contacts</span>
                    </a>
                </li>
            </ul>
            <hr>
            <ul class="nav nav-pills flex-column">
                <li>
                    <a href="#" class="nav-link link-dark">
                        <i class="fas fa-sign-out-alt me-0 me-sm-2"></i>
                        <span class="d-none d-sm-inline">Logout</span>
                    </a>
                </li>
            </ul>
        </div>
        <div class="col py-3" style="width: 100px;">
            <div class="page">
                <jsp:doBody/>
            </div>
        </div>
    </div>
</div>
