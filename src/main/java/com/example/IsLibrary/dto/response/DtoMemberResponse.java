package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoMemberResponse {

    @JsonProperty("code")
    private String codeMember;
    private String name;
    private String gender;
    @JsonProperty("phone_number")
    private String phoneNumber;

    public DtoMemberResponse(String codeMember, String name, String gender, String phoneNumber) {
        this.codeMember = codeMember;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public String getCodeMember() {
        return codeMember;
    }

    public void setCodeMember(String codeMember) {
        this.codeMember = codeMember;
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
