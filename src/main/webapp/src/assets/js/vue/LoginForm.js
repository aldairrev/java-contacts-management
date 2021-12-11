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

import axios from 'axios';

export default {
    data() {
        return {
            isLogin: true,
            errors: [],
            signin: {
                email: '',
                pass: ''
            },
            signup: {
                firstname: '',
                surname: '',
                email: '',
                pass: '',
                pass_two: ''
            },
            response: {
                checking: false,
                success: false,
                message: ""
            },
            isLoadingRequest: false
        };
    },
    methods: {
        submitForm: function (e) {
            e.preventDefault();
            this.errors = [];
            if (this.isLogin) {
                const isReady = this.checkSignInFields();

                if (isReady) {
                    this.isLoadingRequest = true;
                    
                    const params = new URLSearchParams();
                    params.append('email', this.signin.email);
                    params.append('pass', this.signin.pass);
                    const url = e.target.action;
                    const options = {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    };
                    const self = this;
                    axios.post(url, params, options).then(function (resp) {
                        if (resp.status === 200) {
                            if (resp.data.success) {
                                self.response = {
                                    checking: true,
                                    success: true,
                                    message: resp.data.data.message
                                };
                                setTimeout(function (){
                                    window.location = e.target.attributes.redirect.value;
                                }, 3000);
                            } else {
                                self.errors.push(resp.data.data.message);
                                self.isLoadingRequest = false;
                            }
                        } else {
                            self.isLoadingRequest = false;
                        }
                    }).catch(function (error) {
                        console.log(error);
                    });
                }
            } else {
                const checkFields = this.checkSignupFields();
                const checkPassword = this.checkPassword(this.signup.pass, this.signup.pass_two);
                const isReady = checkFields && checkPassword;

                if (isReady) {
                    this.isLoadingRequest = true;

                    const params = new URLSearchParams();
                    params.append('firstname', this.signup.firstname);
                    params.append('surname', this.signup.surname);
                    params.append('email', this.signup.email);
                    params.append('pass', this.signup.pass);
                    const url = e.target.action;
                    const options = {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    };
                    const self = this;
                    axios.post(url, params, options).then(function (resp) {
                        console.log(resp.data);
                        if (resp.status === 200) {
                            if (resp.data.success) {
                                self.response = {
                                    checking: true,
                                    success: true,
                                    message: resp.data.data.message
                                };
                            } else {
                                self.errors.push(resp.data.data.message);
                                self.isLoadingRequest = false;
                            }
                        } else {
                            self.isLoadingRequest = false;
                        }
                    }).catch(function (error) {
                        console.log(error);
                    });
                }
            }
        },
        toogleIsLogin: function () {
            this.errors = [];
            this.response = {
                checking: false,
                success: false,
                message: ""
            };
            this.signin = {
                email: '',
                pass: ''
            };
            this.signup = {
                firstname: '',
                surname: '',
                email: '',
                pass: '',
                pass_two: ''
            };
            this.isLoadingRequest = false;
            this.isLogin = !this.isLogin;
        },
        checkSignupFields() {
            var isOk = true;
            if (!this.signup.firstname) {
                this.errors.push('Name required.');
                isOk = false;
            }
            if (!this.signup.surname) {
                this.errors.push('Surname required.');
                isOk = false;
            }
            if (!this.signup.email) {
                this.errors.push('Email required.');
                isOk = false;
            }
            if (!this.signup.pass) {
                this.errors.push('Password required.');
                isOk = false;
            }
            if (!this.signup.pass_two) {
                this.errors.push('Confirm Password required.');
                isOk = false;
            }
            return isOk;
        },
        checkSignInFields() {
            var isOk = true;
            if (!this.signin.email && !this.signin.pass) {
                this.errors.push('Email and/or password is missing');
                isOk = false;
            }
            return isOk;
        },
        checkPassword(pass, pass_two) {
            var isEqual = true;
            if (pass !== pass_two) {
                isEqual = false;
                this.errors.push('The password is not the same');
            }
            return isEqual;
        }
    }
};

