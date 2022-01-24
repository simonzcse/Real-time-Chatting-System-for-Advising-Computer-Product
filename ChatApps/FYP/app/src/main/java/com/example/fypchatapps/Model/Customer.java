package com.example.fypchatapps.Model;

public class Customer {
    private String customer_id;
    private String username;
    private String imageURL;
    private String email_id;
    private String first_name;
    private String last_name;
    private String role;
    private String password;
    private Address address;
    private String mobile;
    private String status;
    public Customer(){}

    public Customer(String customer_id, String username, String imageURL, String email_id, String first_name, String last_name, String role, String password, Address address, String mobile, String status) {
        this.customer_id = customer_id;
        this.username = username;
        this.imageURL = imageURL;
        this.email_id = email_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.role = role;
        this.password = password;
        this.address = address;
        this.mobile = mobile;
        this.status = status;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

