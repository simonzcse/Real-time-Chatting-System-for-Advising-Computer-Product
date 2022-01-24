package com.example.fypchatapps.Adapter;

import com.example.fypchatapps.Model.Address;
import com.example.fypchatapps.Model.Customer;
import com.example.fypchatapps.Model.Staff;


public class Customer2StaffAdapter extends Staff {
    private Customer customer;

    public Customer2StaffAdapter(Customer customer){
    this.customer=customer;
    setAddress(customer.getAddress());
    setEmail_id(customer.getEmail_id());
    setFirst_name(customer.getFirst_name());
    setImageURL(customer.getImageURL());
    setLast_name(customer.getLast_name());
    setPassword(customer.getPassword());
    setRole(customer.getRole());
    setStaff_id(customer.getCustomer_id());
    setUsername(customer.getUsername());
    setMobile(customer.getMobile()!=null?customer.getMobile():"1823");
    setStatus(customer.getStatus()!=null?customer.getStatus():"offline");
}

    @Override
    public String getUsername() {
        return "(Customer) " +super.getUsername();
    }

}

