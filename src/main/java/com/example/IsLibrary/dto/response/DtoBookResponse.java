package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DtoBookResponse {
    @JsonProperty("code")
    private String codeBook;
    private String title;
    private String author;

    public DtoBookResponse(String codeBook, String title, String author) {
        this.codeBook = codeBook;
        this.title = title;
        this.author = author;
    }
}
