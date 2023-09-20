package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoReturnBook {
    private String title;
    private String isbn;
    private String author;
    @JsonProperty("name_member")
    private String nameMember;
    @JsonProperty("return_date")
    private String returnDate;

    public DtoReturnBook(String title, String isbn, String author, String nameMember, String returnDate) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.nameMember = nameMember;
        this.returnDate = returnDate;
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

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
