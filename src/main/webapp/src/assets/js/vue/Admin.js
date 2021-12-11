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
import VGrid from "@revolist/vue3-datagrid";
import { defineComponent, reactive } from "vue";
export default {
    data() {
        return {
            isLoadingRequest: false,
            page: "contacts",
            contacts: {
                page: "create",
                create: {
                    firstname: '',
                    surname: '',
                    email: '',
                    photo: null,
                    address: ''
                }
            },
            response: {
                checking: false,
                success: false,
                message: ""
            },
            columns: this.getColumns(),
            rows: this.getRows()
        };
    },
    components: {VGrid},
    methods: {
        changePage(page_name) {
            this.page = page_name;
            this.isLoadingRequest = false;
        },
        changeContactsPage(page_name) {
            this.contacts.page = page_name;
            this.isLoadingRequest = false;
            this.response = {
                checking: false,
                success: false,
                message: ""
            };
            this.contacts.create = {
                firstname: '',
                surname: '',
                email: '',
                photo: null,
                address: ''
            }
        },
        submitCreateContact(e) {
            e.preventDefault();
            this.isLoadingRequest = true;
            let formData_photo = new FormData();
            formData_photo.append('photo', this.contacts.create.photo);
            const url = e.target.action;
            let options_photo = {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            };
            const self = this;
            console.log(this.contacts.create.firstname);
            axios.post(url + '/photo', formData_photo, options_photo).then(function (resp) {
                const params = new URLSearchParams();
                params.append('firstname', self.contacts.create.firstname);
                params.append('surname', self.contacts.create.surname);
                params.append('email', self.contacts.create.email);
                params.append('photo', resp.data.data.photo);
                params.append('address', self.contacts.create.address);
                let options = {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                };
                axios.post(url, params, options).then(function (res) {
                    if (res.status === 200) {
                        if (res.data.success) {
                            self.response = {
                                checking: true,
                                success: true,
                                message: res.data.data.message
                            };
                        } else {
//                            self.errors.push(resp.data.data.message);
                            self.isLoadingRequest = false;
                        }
                    }
                }).catch(function (error) {
                    console.log(error);
                });
            }).catch(function (error) {
                console.log(error);
            });
        },
        onChangeFileUpload() {
            this.contacts.create.photo = this.$refs.createPhoto.files[0];
        },
        getColumns() {
            const columns = [
                {
                    prop: "id",
                    name: "Id",
                    columnType: 'numeric',
                    sortable: true,
                    order: 'asc',
                    size: 80
                },
                {
                    prop: "firstname",
                    name: "Firstname",
                    sortable: true,
                    order: 'asc',
                    size: 150
                },
                {
                    prop: "surname",
                    name: "Surname",
                    sortable: true,
                    order: 'asc',
                    size: 150
                },
                {
                    prop: "email",
                    name: "Email",
                    sortable: true,
                    order: 'asc',
                    size: 250
                },
                {
                    prop: "address",
                    name: "Address",
                    autoSize: true
                }
            ];
            return columns;
        },
        getRows() {
            const rows = [
                {
                    id: 1,
                    firstname: "Aldair",
                    surname: "Revilla",
                    email: "aldairreva@gmail.com",
                    address: "Mz i lt 7"
                },
                {
                    id: 2,
                    firstname: "Jimena",
                    surname: "Alzamora",
                    email: "jimenaalzamora@gmail.com",
                    address: "Mz E lt 8"
                }
            ];
            return rows;
        }
    }
};
