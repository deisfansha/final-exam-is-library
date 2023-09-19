package com.example.IsLibrary.dto.response;

public class DtoBookListIsbn {
    private String title;
    private String isbn;
    private String author;
    private Boolean status;

    public DtoBookListIsbn(String title, String isbn, String author, Boolean status) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.status = status;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
