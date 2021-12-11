<%-- 
    Document   : login
    Created on : Dec 8, 2021, 2:32:00 PM
    Author     : aldairrev
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:master title="Login">
    <jsp:body>
        <div class="w-100 min-vh-100 mx-auto" style="background-color: #f5f5f5">
            <main class="pt-3" id="app-login-form">
                <form 
                    class="form-signin d-flex justify-content-center align-items-center flex-column"
                    v-if="isLogin"
                    @submit="submitForm"
                    action="${APP_URL}/admin/login"
                    redirect="${APP_URL}/admin/dashboard"
                    >
                    <img class="mb-4" src="${APP_URL}/public/logo.svg" alt="" width="72" height="72">
                    <h1 class="h3 mb-3 fw-bold">Sign in</h1>

                    <div class="form-floating w-100">
                        <input type="email"
                               class="form-control"
                               v-model="signin.email"
                               id="floatingInput"
                               placeholder="name@example.com"
                               :disabled="isLoadingRequest">
                        <label for="floatingInput">Email address</label>
                    </div>
                    <div class="form-floating w-100">
                        <input type="password"
                               class="form-control"
                               v-model="signin.pass"
                               id="floatingPassword"
                               placeholder="Password"
                               :disabled="isLoadingRequest">
                        <label for="floatingPassword">Password</label>
                    </div>
                    <button class="w-100 btn btn-lg btn-primary mb-3" type="submit" :disabled="isLoadingRequest">Sign in</button>
                    <div class="alert alert-success"
                         role="alert"
                         v-if="response.checking">
                        {{ response.message }}
                    </div>
                    <div class="w-100 mb-3" v-if="errors.length">
                        <ul class="list-group">
                            <li
                                v-for="error in errors"
                                class="list-group-item w-100 list-group-item-danger"
                                >
                                {{ error }}
                            </li>
                        </ul>
                    </div>
                    <span>Don't have a account?
                        <a href="#" v-on:click="toogleIsLogin" class="link-primary">Create One</a>
                    </span>
                </form>
                <form 
                    class="form-signup d-flex justify-content-center align-items-center flex-column"
                    v-else
                    @submit="submitForm"
                    action="${APP_URL}/admin/signup"
                    >
                    <img class="mb-4" src="${APP_URL}/public/logo.svg" alt="" width="72" height="72">
                    <h1 class="h3 mb-3 fw-bold">Sign up</h1>

                    <div class="form-floating w-100">
                        <input type="text" v-model="signup.firstname" class="form-control" id="firstname" placeholder="Name" :disabled="isLoadingRequest">
                        <label for="firstname">Name</label>
                    </div>
                    <div class="form-floating w-100">
                        <input type="text" v-model="signup.surname" class="form-control" id="surname" placeholder="Surname" :disabled="isLoadingRequest">
                        <label for="surname">Surname</label>
                    </div>
                    <div class="form-floating w-100">
                        <input type="email" class="form-control" v-model="signup.email" id="email" placeholder="name@example.com" :disabled="isLoadingRequest">
                        <label for="email">Email address</label>
                    </div>
                    <div class="form-floating w-100">
                        <input type="password" class="form-control" v-model="signup.pass" id="pass" placeholder="New Password" :disabled="isLoadingRequest">
                        <label for="pass">New Password</label>
                    </div>
                    <div class="form-floating w-100">
                        <input type="password" class="form-control" v-model="signup.pass_two" id="pass_two" placeholder="Confirm Password" :disabled="isLoadingRequest">
                        <label for="pass_two">Confirm password</label>
                    </div>
                    <button class="w-100 btn btn-lg btn-primary mb-3" type="submit" :disabled="isLoadingRequest">Sign up</button>
                    <div class="alert alert-success"
                         role="alert"
                         v-if="response.checking">
                        {{ response.message }}
                    </div>
                    <div class="w-100 mb-3" v-if="errors.length">
                        <ul class="list-group">
                            <li
                                v-for="error in errors"
                                class="list-group-item w-100 list-group-item-danger"
                                >
                                {{ error }}
                            </li>
                        </ul>
                    </div>
                    <span>
                        Already have an account?
                        <a href="#" v-on:click="toogleIsLogin" class="link-primary">Sign in now!</a>
                    </span>
                </form>
            </main>
        </div>
    </jsp:body>
</t:master>