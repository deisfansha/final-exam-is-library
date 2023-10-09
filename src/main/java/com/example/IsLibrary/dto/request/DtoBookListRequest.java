package com.example.IsLibrary.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DtoBookListRequest {
    @JsonProperty("code_book")
    private String codeBook;
    private String isbn;

    public DtoBookListRequest() {
    }
}
