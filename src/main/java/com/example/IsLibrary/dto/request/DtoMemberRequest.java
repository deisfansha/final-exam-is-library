package com.example.IsLibrary.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoMemberRequest {
    private String name;
    private String gender;
    @JsonProperty("phone_number")
    private String phoneNumber;

    public DtoMemberRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
