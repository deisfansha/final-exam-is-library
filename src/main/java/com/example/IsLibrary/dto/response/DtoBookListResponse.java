package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
