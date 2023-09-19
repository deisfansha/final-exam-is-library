package com.example.IsLibrary.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoBookListRequest {
    @JsonProperty("code_book")
    private String codeBook;
    private String isbn;

    public String getCodeBook() {
        return codeBook;
    }

    public void setCodeBook(String codeBook) {
        this.codeBook = codeBook;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
