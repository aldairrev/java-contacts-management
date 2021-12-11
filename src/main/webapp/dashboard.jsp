<%-- 
    Document   : dashboard
    Created on : Dec 8, 2021, 10:37:54 AM
    Author     : aldairrev
--%>

<%@page import="com.aldairrev.contactsmanagement.beans.EE_User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    EE_User user = null;
    user = (EE_User) session.getAttribute("user");
    String fullname = user.getFirstname() + " " + user.getSurname();
    String email = user.getEmail();
%>
<c:set var="fullname" value="<%=fullname%>"/>
<c:set var="email" value="<%=email%>"/>
<t:master title="Dashboard">
    <jsp:body>
        <t:sidebar fullname="${fullname}" email="${email}">
            <jsp:body>
                <div v-show="page === 'dashboard'">
                    Dashboard
                </div>
                <div v-show="page === 'contacts'">
                    <h1>Contacts</h1>
                    <div class="row g-2 mt-3">
                        <div class="col-12 col-md-3">
                            <button class="w-100 btn btn-primary btn-block" v-on:click="changeContactsPage('view')">
                                <i class="far fa-eye"></i> View
                            </button>
                        </div>
                        <div class="col-12 col-md-3">
                            <button class="w-100 btn btn-success btn-block" v-on:click="changeContactsPage('create')">
                                <i class="fas fa-plus"></i> Create
                            </button>
                        </div>
                        <div class="col-12 col-md-3">
                            <button class="w-100 btn btn-warning btn-block" v-on:click="changeContactsPage('update')">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                        </div>
                        <div class="col-12 col-md-3">
                            <button class="w-100 btn btn-danger btn-block" v-on:click="changeContactsPage('delete')">
                                <i class="fas fa-trash-alt"></i> Delete
                            </button>
                        </div>
                    </div>
                    <div class="mt-4" v-show="contacts.page === 'view'">
                        <h3>View</h3>
                        <v-grid 
                            theme="material"
                            :source="rows"
                            :columns="columns"
                            :resize="true"
                            style="height: 500px"/>
                    </div>
                    <div class="mt-4" v-show="contacts.page === 'create'">
                        <h3>Create</h3>
                        <form class="row g-3"
                              enctype="multipart/form-data"
                              @submit="submitCreateContact"
                              action="${APP_URL}/admin/contacts">
                            <div class="col-md-6">
                                <label for="createFirstname" class="form-label">Firstname</label>
                                <input type="text" class="form-control" id="createFirstname" v-model="contacts.create.firstname" :disabled="isLoadingRequest" required>
                            </div>
                            <div class="col-md-6">
                                <label for="createSurname" class="form-label">Surname</label>
                                <input type="Text" class="form-control" id="createSurname" v-model="contacts.create.surname" :disabled="isLoadingRequest" required>
                            </div>
                            <div class="col-md-4">
                                <label for="createEmail" class="form-label">Email</label>
                                <input type="email" class="form-control" id="createEmail" placeholder="example@noreply.com" v-model="contacts.create.email" :disabled="isLoadingRequest" required>
                            </div>
                            <div class="col-md-5">
                                <label for="createAddress" class="form-label">Address</label>
                                <input type="text" class="form-control" id="createAddress" placeholder="Apartment, studio, or floor" v-model="contacts.create.address" :disabled="isLoadingRequest" required>
                            </div>
                            <div class= "col-md-3">
                                <label for="createImage" class="form-label">Photo</label>
                                <input class="form-control" type="file" id="createPhoto" ref="createPhoto" v-on:change="onChangeCreatePhotoUpload()" :disabled="isLoadingRequest" required>
                            </div>
                            <div class="w-100">
                                <button type="submit" class="w-100 btn btn-primary" :disabled="isLoadingRequest">Create Contact</button>
                            </div>
                            <div class="w-100 alert alert-success"
                                 role="alert"
                                 v-if="response.checking">
                                {{ response.message }}
                            </div>
                        </form>
                    </div>
                    <div class="mt-4" v-show="contacts.page === 'update'">
                        <h3>Edit</h3>
                        <form 
                            class="row g-3 mt-4"
                            enctype="multipart/form-data"
                            @submit="submitUpdateContact"
                            action="${APP_URL}/admin/contacts">
                            <div class="row">
                                <div class="col-12 col-md-4">
                                    <label for="updateId" class="form-label">Id</label>
                                    <input type="number" class="form-control" id="updateId" @blur="getDataUpdateFields" value="0" required>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <label for="updateFirstname" class="form-label">Firstname</label>
                                <input type="text" class="form-control" id="updateFirstname" v-model="contacts.update.firstname" :disabled="isLoadingRequest" required>
                            </div>
                            <div class="col-md-6">
                                <label for="updateSurname" class="form-label">Surname</label>
                                <input type="Text" class="form-control" id="updateSurname" v-model="contacts.update.surname" :disabled="isLoadingRequest" required>
                            </div>
                            <div class="col-md-4">
                                <label for="updateEmail" class="form-label">Email</label>
                                <input type="email" class="form-control" id="updateEmail" placeholder="example@noreply.com" v-model="contacts.update.email" :disabled="isLoadingRequest" required>
                            </div>
                            <div class="col-md-5">
                                <label for="updateAddress" class="form-label">Address</label>
                                <input type="text" class="form-control" id="updateAddress" placeholder="Apartment, studio, or floor" v-model="contacts.update.address" :disabled="isLoadingRequest" required>
                            </div>
                            <div class="col-md-3">
                                <label for="updatePhoto" class="form-label">Photo</label>
                                <input class="form-control" type="file" id="updatePhoto" ref="updatePhoto" v-on:change="onChangeUpdatePhotoUpload()" :disabled="isLoadingRequest">
                            </div>
                            <div class="w-100">
                                <button type="submit" class="w-100 btn btn-primary">Create Contact</button>
                            </div>
                        </form>
                    </div>
                    <div class="mt-4" v-show="contacts.page === 'delete'">
                        Delete
                    </div>
                </div>
            </jsp:body>
        </t:sidebar>
    </jsp:body>
</t:master>
