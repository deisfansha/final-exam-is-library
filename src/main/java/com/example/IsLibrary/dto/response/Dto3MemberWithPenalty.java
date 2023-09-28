package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dto3MemberWithPenalty {
    @JsonProperty("code_member")
    private String code;
    private String name;
    private Long pay;

    public Dto3MemberWithPenalty() {
    }
}
