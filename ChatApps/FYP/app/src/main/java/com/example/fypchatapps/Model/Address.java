package com.example.fypchatapps.Model;

public class Address {
    private String city;
    private String country;
    private String road;

    public Address(String city, String country, String road) {
        this.city = city;
        this.country = country;
        this.road = road;
    }
    public Address(){}
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }
}
