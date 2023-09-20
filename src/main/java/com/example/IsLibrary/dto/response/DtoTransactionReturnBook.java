package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class DtoTransactionReturnBook {
    @JsonProperty("member")
    private String nameMember;
    private String title;
    private String isbn;
    @JsonProperty("due_date")
    private Date dueDate;
    @JsonProperty("return_book")
    private Date returnDate;
    private Boolean pinalty;
    @JsonProperty("pay_fee")
    private Integer payFee;

    public DtoTransactionReturnBook(String nameMember, String title, String isbn, Date dueDate, Date returnDate, Boolean pinalty, Integer payFee) {
        this.nameMember = nameMember;
        this.title = title;
        this.isbn = isbn;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.pinalty = pinalty;
        this.payFee = payFee;
    }

    public String getNameMember() {
        return nameMember;
    }

    public void setNameMember(String nameMember) {
        this.nameMember = nameMember;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Boolean getPinalty() {
        return pinalty;
    }

    public void setPinalty(Boolean pinalty) {
        this.pinalty = pinalty;
    }

    public Integer getPayFee() {
        return payFee;
    }

    public void setPayFee(Integer payFee) {
        this.payFee = payFee;
    }
}
