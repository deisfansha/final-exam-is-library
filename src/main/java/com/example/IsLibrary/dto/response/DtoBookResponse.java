package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public String getCodeBook() {
        return codeBook;
    }

    public void setCodeBook(String codeBook) {
        this.codeBook = codeBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
