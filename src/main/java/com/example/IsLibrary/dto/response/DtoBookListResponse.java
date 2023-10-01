package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DtoBookListResponse {
    @JsonProperty("code")
    private String codeBook;
    private String title;
    private String author;
    private Long total;

    public DtoBookListResponse(String codeBook, String title, String author, Long total) {
        this.codeBook = codeBook;
        this.title = title;
        this.author = author;
        this.total = total;
    }

}
