package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoTransactionResponse {
    private String title;
    private String isbn;
    private String author;
    @JsonProperty("name_member")
    private String nameMember;
    @JsonProperty("due_date")
    private String dueDate;

    public DtoTransactionResponse(String title, String isbn, String author, String nameMember, String dueDate) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.nameMember = nameMember;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNameMember() {
        return nameMember;
    }

    public void setNameMember(String nameMember) {
        this.nameMember = nameMember;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
