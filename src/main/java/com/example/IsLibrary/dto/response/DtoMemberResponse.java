package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
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
}
