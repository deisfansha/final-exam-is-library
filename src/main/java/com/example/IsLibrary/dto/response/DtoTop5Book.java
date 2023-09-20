package com.example.IsLibrary.dto.response;

import com.example.IsLibrary.models.BookList;

public class DtoTop5Book {
    private BookList bookList;
    private Long total;

    public DtoTop5Book(BookList bookList, Long total) {
        this.bookList = bookList;
        this.total = total;
    }

    public BookList getBookList() {
        return bookList;
    }

    public void setBookList(BookList bookList) {
        this.bookList = bookList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
