package com.example.IsLibrary.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DtoTransactionRequest {
    @JsonProperty("code_member")
    private String codeMember;
    private String isbn;
}
